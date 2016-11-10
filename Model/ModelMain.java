package Model;
import AgentGridMin.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by rafael on 11/8/16.
 */

abstract class CellPop {
    Visualizer myVis;
    TumorModel myModel;
    double[]pops;
    double[]swap;
    final int xDim;
    final int yDim;
    CellPop(TumorModel myModel,Visualizer myVis) {
        this.myVis=myVis;
        this.myModel=myModel;
        xDim=myModel.xDim;
        yDim=myModel.yDim;
        pops=new double[xDim*yDim];
        swap=new double[xDim*yDim];
    }
    int I(int x,int y) {
        return x*yDim+y;
    }
    //runs once at the begining of the model to initialize cell pops
    abstract void InitPop();
    //called once every tick
    abstract void Step();
    //called once every tick
    abstract void Draw();
}


//gui and visualizer
class ModelVis{
    TumorModel myModel;

    Visualizer visVessels;
    Visualizer visO2;
    Visualizer visNecro;
    Visualizer visNormal;
    Visualizer visTumor;
    Visualizer visPH;
    Visualizer visGL;
    GuiWindow win;

    ModelVis(TumorModel model){
        myModel=model;

        int visScale=3;
        visVessels=new Visualizer(model.xDim,model.yDim,visScale);
        visTumor=new Visualizer(model.xDim,model.yDim,visScale);
        visNormal=new Visualizer(model.xDim,model.yDim,visScale);
        visNecro=new Visualizer(model.xDim,model.yDim,visScale);

        //Diffusible
        visO2 = new Visualizer(model.xDim,model.yDim,visScale);
        visPH = new Visualizer(model.xDim,model.yDim,visScale);
        visGL = new Visualizer(model.xDim,model.yDim,visScale);

        win=new GuiWindow("LungVis",model.xDim*visScale,model.yDim*visScale,3,2);
        win.AddComponent(visNormal,0,0,1,1);
        win.AddComponent(visNecro,1,0,1,1);
        win.AddComponent(visTumor,2,0,1,1);
        win.AddComponent(visVessels,3,0,1,1);
        win.AddComponent(visO2,0,1,1,1);
        win.AddComponent(visPH,1,1,1,1);
        win.AddComponent(visGL,2,1,1,1);

//        win.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                myModel.printCellPops();
//            }
//        });

    }
}


//model of tumor
class TumorModel {
    Random rand;

    ArrayList<CellPop> cellPops;
    ArrayList<DiffusionField> diffuseTypes;

    NormalCells normalCells;
    NecroticCells necroCells;
    TumorCellPop tumorCells;
    resistantTumorCellPop resistantTumorCells;
    Vessels vessels;

    //The fields
    DiffusionField Oxygen;
    DiffusionField Glucose;
    DiffusionField Acid;

    double[] totalPops;
    int xDim;
    int yDim;
    int tick;

    TumorModel(int x, int y) {
        cellPops=new ArrayList<CellPop>();
        diffuseTypes = new ArrayList<DiffusionField>();
        tick=0;
        rand = new Random();

        totalPops = new double[x * y];
        xDim = x;
        yDim = y;
    }

    //adds cell population to the model step rotation
    <T extends CellPop> T AddCellPop(T addMe){
        cellPops.add(addMe);
        return addMe;
    }

     DiffusionField AddDiffusible(DiffusionField addMe) {
         diffuseTypes.add(addMe);
         return addMe;
     }

    void InitPops(){
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            cellPops.get(iPop).InitPop();
        }

    }

    void RunCellStep(){
        //clear and fill total pop grid
        Arrays.fill(totalPops,0);
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            CellPop currPop=cellPops.get(iPop);
            for (int i = 0; i < currPop.pops.length; i++) {
                totalPops[i] += currPop.pops[i];
            }
        }
        //clear cellpop swap grids
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            Arrays.fill(cellPops.get(iPop).swap,0);
        }
        //run steps of each cellpop
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            cellPops.get(iPop).Step();
        }
        //(PDE step here)
        //switch pops and swaps in preparation for next time step
        for(int iPop=0;iPop<cellPops.size();iPop++){
            CellPop currPop = cellPops.get(iPop);
            double[] temp=currPop.pops;
            currPop.pops=currPop.swap;
            currPop.swap=temp;
        }
        //draw each cellpop, if they have a visualizer
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            CellPop currPop = cellPops.get(iPop);
            if (currPop.myVis != null) {
                currPop.Draw();
            }
        }

        //print how much time has passed
        tick++;
        System.err.println("Day: "+tick*TIME_STEP); //TODO: put this information onto the GUI.
    }

    void RunDiffuseStep(double discreteTimeStep) {
        double t = 0.0;
        double dt = 0.01;

        int[] ProdIndices = new int[xDim * yDim];
        int k = 0;
        for (int i = 0; i < xDim * yDim; i++) {
            if (vessels != null) {
                if (vessels.pops[i] != 0) {
                    ProdIndices[k] = i;
                }
            }
            k += 1;
        }

        //NOTE: A proper 'framework' centric way to implement this would be split
        //DTypes into vessel-produced and cell-produced
        for (int vi = 0; vi < k; vi++) {
            //Vessel production (fixed conc)
            Oxygen.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * OXYGEN_PRODUCTION_RATE * dt;
            Glucose.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * GLUCOSE_PRODUCTION_RATE * dt;
        }

        while (t < discreteTimeStep) {
            //Cell-type specific consumption
            for (int ci = 0; ci < xDim * yDim; ci++) {
                Oxygen.field[ci] -= tumorCells.pops[ci] * tumorCells.OxygenConsumption * dt;
                Oxygen.field[ci] -= normalCells.pops[ci] * normalCells.OxygenConsumption * dt;
                if (Oxygen.field[ci] < 0) {
                    Oxygen.field[ci] = 0.0;
                }
                Acid.field[ci] -= normalCells.pops[ci] * 0.0 * normalCells.OxygenConsumption * dt;
                if (Oxygen.field[ci] < 0) {
                    Oxygen.field[ci] = 0.0;
                }
                Glucose.field[ci] -= tumorCells.pops[ci] * tumorCells.GlucoseConsumption * dt;
                Glucose.field[ci] -= normalCells.pops[ci] * normalCells.GlucoseConsumption * dt;
                if (Oxygen.field[ci] < 0) {
                    Oxygen.field[ci] = 0.0;
                }
            }
            for (DiffusionField DType : diffuseTypes){
                DType.Diffuse(false, 0.0, false);
            }

            t = t + dt;
        }

        for (DiffusionField DType : diffuseTypes){
            DType.DrawField();
        }
    }

    //gets index from x and y coords
    int I(int x, int y) {
        return x * yDim + y;
    }
    boolean WithinGrid(int x,int y) {
        if(x>=0&&x<xDim&&y>=0&&y<yDim) {
            return true;
        }
        return false;
    }

    //Print data out
    void printCellPops(){
        for(int x=0; x<xDim; x++){
            for(int y=0; y<yDim; y++){
                System.out.printf("%g ",totalPops[I(x, y)]);
            }
            System.out.print("\n");
        }
    }
}

public class ModelMain {
    public static void main(String[] args) {
        TumorModel firstModel = new TumorModel(100, 100);
        ModelVis mainWindow = new ModelVis(firstModel);
        //setting normalCells for access by other populations, adding cellpop for iteration
        firstModel.normalCells = firstModel.AddCellPop(new NormalCells(firstModel, mainWindow.visNormal)); //0
        firstModel.necroCells=firstModel.AddCellPop(new NecroticCells(firstModel,mainWindow.visNecro));//1
        firstModel.tumorCells=firstModel.AddCellPop(new TumorCellPop(firstModel, mainWindow.visTumor));//2
        firstModel.resistantTumorCells=firstModel.AddCellPop(new resistantTumorCellPop(firstModel, mainWindow.visTumor));//3

        //The vessels
        firstModel.vessels = firstModel.AddCellPop(new Vessels(firstModel, mainWindow.visVessels));//4

        //The diffusibles
        firstModel.Oxygen = firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, OXYGEN_DIFFUSION_RATE, mainWindow.visO2));
        firstModel.Glucose = firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, GLUCOSE_DIFFUSION_RATE, mainWindow.visGL));
        firstModel.Acid= firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, ACID_DIFFUSION_RATE, mainWindow.visPH));

        firstModel.InitPops();
        while (true) {
            firstModel.RunCellStep();
            firstModel.RunDiffuseStep(DIFFUSE_TIME_LENGTH);
        }
    }
}
