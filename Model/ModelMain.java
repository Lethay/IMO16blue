package Model;
import AgentGridMin.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by rafael on 11/8/16.
 */

abstract class CellPop {
    final public double OxygenConsumption = 0.0000003;
    Visualizer myVis;
    TumorModel myModel;
    double[]pops;
    double[]swap;
    final int xDim;
    final int yDim;
    double cellSize;
    CellPop(TumorModel myModel,Visualizer myVis) {
        this.myVis=myVis;
        this.myModel=myModel;
        xDim=myModel.xDim;
        yDim=myModel.yDim;
        this.cellSize=1;
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
    Visualizer visTcells;
    GuiWindow win;
    ModelVis(TumorModel model){
        myModel=model;
        int visScale=2;
        visVessels=new Visualizer(model.xDim,model.yDim,visScale);
        visTumor=new Visualizer(model.xDim,model.yDim,visScale);
        visNormal=new Visualizer(model.xDim,model.yDim,visScale);
        visNecro=new Visualizer(model.xDim,model.yDim,visScale);
        visO2=new Visualizer(model.xDim,model.yDim,visScale);
        visTcells=new Visualizer(model.xDim,model.yDim,visScale);
        win=new GuiWindow("LungVis",model.xDim*visScale,model.yDim*visScale,3,2);
        win.AddComponent(visNormal,0,0,1,1);
        win.AddComponent(visNecro,1,0,1,1);
        win.AddComponent(visTumor,2,0,1,1);
        win.AddComponent(visVessels,0,1,1,1);
        win.AddComponent(visO2,1,1,1,1);
        win.AddComponent(visTcells,2,1,1,1);
    }
}


//model of tumor
class TumorModel {
    ArrayList<CellPop> cellPops;
    ArrayList<DiffusionField> diffuseTypes;
    NormalCells normalCells;
    NecroticCells necroCells;
    TumorCellPop tumorCells;
    TCells tCells;
    resistantTumorCellPop resistantTumorCells;
    Vessels vessels;
    DiffusionField Oxygen;
    Random rand;
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
            double sizeScale=currPop.cellSize;
            for (int i = 0; i < currPop.pops.length; i++) {
                totalPops[i] += currPop.pops[i]*sizeScale;
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
        System.out.println("Day: "+tick*TIME_STEP);
    }

    void RunDiffuseStep(double discreteTimeStep) {
        double t = 0.0;
        double dt = 0.001;

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
        for (int di = 0; di < diffuseTypes.size(); di++) {
            DiffusionField DType = diffuseTypes.get(di);
            while (t < discreteTimeStep) {
                //Cell-type specific consumption
                for (int ci = 0; ci < xDim * yDim; ci++) {
                    DType.field[ci] -= normalCells.pops[ci] * normalCells.OxygenConsumption * dt;
                    if (DType.field[ci] < 0) {
                        DType.field[ci] = 0.0;
                    }
                }

                for (int vi = 0; vi < k; vi++) {
                    //Vessel production (fixed conc)
                    DType.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * 10*OXYGEN_PRODUCTION_RATE * dt;
                }

                DType.Diffuse(0.0001, false, 0.0, false);
                t = t + dt;
            }
        }
        for (int di = 0; di < diffuseTypes.size(); di++) {
            diffuseTypes.get(di).DrawField();
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
        firstModel.vessels = firstModel.AddCellPop(new Vessels(firstModel, mainWindow.visVessels));//4
        firstModel.Oxygen = firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, mainWindow.visO2));
        firstModel.tCells = firstModel.AddCellPop(new TCells(firstModel,mainWindow.visTcells));
        firstModel.InitPops();
        while (true) {
            firstModel.RunCellStep();
            firstModel.RunDiffuseStep(0.1);

        }
    }
}
