package Model;
import AgentGridMin.*;
//import sun.awt.X11.Visual;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.io.FileOutputStream;
import static AgentGridMin.Utils.TimeStamp;
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
    boolean WithinGrid(int x,int y) {
        if(x>=0&&x<xDim&&y>=0&&y<yDim) {
            return true;
        }
        return false;
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
    Visualizer visPDL1;
    Visualizer visPH;
    Visualizer visGL;
    Visualizer visDR;

    Visualizer visFULL;

    GuiWindow win;

    void AddVis(Visualizer vis,int x,int y,String title){
       win.AddComponent(new JLabel(title),x,y*2,1,1);
        win.AddComponent(vis,x,y*2+1,1,1);
    }

    ModelVis(TumorModel model){
        myModel=model;

        int visScale=2;
        visVessels=new Visualizer(model.xDim,model.yDim,visScale);
        visTumor=new Visualizer(model.xDim,model.yDim,visScale);
        visNormal=new Visualizer(model.xDim,model.yDim,visScale);
        visNecro=new Visualizer(model.xDim,model.yDim,visScale);
        visO2=new Visualizer(model.xDim,model.yDim,visScale);
        visTcells=new Visualizer(model.xDim,model.yDim,visScale);
        visPDL1=new Visualizer(model.xDim,model.yDim,visScale);

        //Diffusible
        visO2 = new Visualizer(model.xDim,model.yDim,visScale);
        visPH = new Visualizer(model.xDim,model.yDim,visScale);
        visGL = new Visualizer(model.xDim,model.yDim,visScale);
        visDR = new Visualizer(model.xDim,model.yDim,visScale);

        //The full viz
        visFULL = new Visualizer(model.xDim,model.yDim,visScale);

        win=new GuiWindow("LungVis",model.xDim*visScale,model.yDim*visScale,4,6);
        //first layer
        AddVis(visNormal,0,0,"Normal");
        AddVis(visNecro,1,0,"Necro");
        AddVis(visTumor,2,0,"Tumor");
        AddVis(visVessels,3,0,"Vessel");
        AddVis(visTcells,0,1,"TCells");
        AddVis(visO2,1,1,"O2");
        //third layer
        AddVis(visPH,2,1,"Ph");
        AddVis(visGL,3,1,"Gluc");
        AddVis(visDR,0,2,"Drug");
        AddVis(visPDL1,1,2,"PDL1");

        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String loc="file"; //options: "terminal", "file" or "none"
                myModel.printCellPops(loc);
            }
        });
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
    PDL1TumorCellPop PDL1TumorCells;
    AcidProducingTumorCellPop acidTumorCells;
    TCells tCells;
    Vessels vessels;

    //The fields
    DiffusionField Oxygen;
    DiffusionField Glucose;
    DiffusionField Acid;
    DiffusionField Drug;


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
            if (cellPops.get(iPop)==null) continue;
            cellPops.get(iPop).InitPop();
        }

    }

    void RunCellStep(){
        //clear and fill total pop grid
        Arrays.fill(totalPops,0);
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            if (cellPops.get(iPop)==null) continue;
            CellPop currPop=cellPops.get(iPop);
            double sizeScale=currPop.cellSize;
            for (int i = 0; i < currPop.pops.length; i++) {
                totalPops[i] += currPop.pops[i]*sizeScale;
            }
        }
        //clear cellpop swap grids
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            if (cellPops.get(iPop)==null) continue;
            Arrays.fill(cellPops.get(iPop).swap,0);
        }
        //run steps of each cellpop
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            if (cellPops.get(iPop)==null) continue;
            cellPops.get(iPop).Step();
        }

        //switch pops and swaps in preparation for next time step
        for(int iPop=0;iPop<cellPops.size();iPop++){
            if (cellPops.get(iPop)==null) continue;
            CellPop currPop = cellPops.get(iPop);
            double[] temp=currPop.pops;
            currPop.pops=currPop.swap;
            currPop.swap=temp;
        }
        //draw each cellpop, if they have a visualizer
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            if (cellPops.get(iPop)==null) continue;
            CellPop currPop = cellPops.get(iPop);
            if (currPop.myVis != null) {
                currPop.Draw();
            }
        }

        //print how much time has passed
        tick++;
        if (tick == SEED_TIME)
        {
            tumorCells.SeedMe = true;
        }
        if (tick == IMMUNE_TIME)
        {
            tCells.active = true;
        }
        System.err.println("Day: "+tick*TIME_STEP); //TODO: put this information onto the GUI.
    }

    void RunDiffuseStep(double discreteTimeStep) {
        double t = 0.0;
        double dt = DIFFUSE_DT;

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
            if(OXYGEN_ACTIVE) {
                Oxygen.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * OXYGEN_PRODUCTION_RATE;
            }
            if(GLUCOSE_ACTIVE) {
                Glucose.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * GLUCOSE_PRODUCTION_RATE;
            }
            if(DRUG_ACTIVE) {
                Drug.field[ProdIndices[vi]] = vessels.pops[ProdIndices[vi]] * DRUG_PRODUCTION_RATE;
            }
        }

        while (t < discreteTimeStep) {
            //Cell-type specific consumption
            for (int ci = 0; ci < xDim * yDim; ci++) {
                if(OXYGEN_ACTIVE) {
                    Oxygen.field[ci] -= tumorCells.pops[ci] * tumorCells.OxygenConsumption * dt;
                    Oxygen.field[ci] -= normalCells.pops[ci] * normalCells.OxygenConsumption * dt;
                    if (Oxygen.field[ci] < 0) {
                        Oxygen.field[ci] = 0.0;
                    }
                }
                if(ACID_ACTIVE) {
                    Acid.field[ci] -= normalCells.pops[ci] * 0.0 * normalCells.OxygenConsumption * dt;
                    if (Acid.field[ci] < 0) {
                        Acid.field[ci] = 0.0;
                    }
                }
                if(GLUCOSE_ACTIVE) {
                    Glucose.field[ci] -= tumorCells.pops[ci] * tumorCells.GlucoseConsumption * dt;
                    Glucose.field[ci] -= normalCells.pops[ci] * normalCells.GlucoseConsumption * dt;
                    if (Glucose.field[ci] < 0) {
                        Glucose.field[ci] = 0.0;
                    }
                }
                if(DRUG_ACTIVE) {
                    Drug.field[ci] -= PDL1TumorCells.pops[ci] * PDL1TumorCells.DrugConsumption * dt;
                    //Drug.field[ci] -= normalCells.pops[ci] * normalCells.DrugConsumption * dt;
                    if (Drug.field[ci] < 0) {
                        Drug.field[ci] = 0.0;
                    }
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
    void printCellPops(String loc){
        PrintStream ps;
        if(loc.equals("file")){
            String filename="cellDensities_"+TimeStamp()+".txt";
            try {ps = new PrintStream(new FileOutputStream(filename, true));} catch(IOException e){
                e.printStackTrace();
                return;
            }
        }
        else if(loc.equals("terminal")) {ps= new PrintStream(System.out);}
        else{return;}
        for(int x=0; x<xDim; x++){
            for(int y=0; y<yDim; y++){
                ps.printf("%g ",totalPops[I(x, y)]);
            }
            ps.print("\n");
        }
        if(loc.equals("file")){
            ps.close();
        }
    }
}

public class ModelMain {
    public static void main(String[] args) {


        TumorModel firstModel = new TumorModel(110, 110);
        ModelVis mainWindow = new ModelVis(firstModel);
        //setting normalCells for access by other populations, adding cellpop for iteration

        if(NORMAL_CELLS_ACTIVE) {firstModel.normalCells= firstModel.AddCellPop(new NormalCells(firstModel, mainWindow.visNormal));} //index 0
        else{firstModel.normalCells= null;}
        if(TUMOR_CELLS_ACTIVE) {firstModel.tumorCells= firstModel.AddCellPop(new TumorCellPop(firstModel, mainWindow.visTumor));} //index 1
        else{firstModel.tumorCells= null;}
        if(PDL1_CELLS_ACTIVE) {firstModel.PDL1TumorCells= firstModel.AddCellPop(new PDL1TumorCellPop(firstModel, mainWindow.visPDL1));} //index 2
        else{firstModel.PDL1TumorCells= null;}
        if(ACIDIC_CELLS_ACTIVE) {firstModel.acidTumorCells= firstModel.AddCellPop(new AcidProducingTumorCellPop(firstModel, mainWindow.visTumor));} //index 3
        else{firstModel.acidTumorCells= null;}
        if(NECRO_CELLS_ACTIVE) {firstModel.necroCells= firstModel.AddCellPop(new NecroticCells(firstModel,mainWindow.visNecro));} //index 4
        else{firstModel.necroCells= null;}
        if(T_CELLS_ACTIVE) {firstModel.tCells= firstModel.AddCellPop(new TCells(firstModel,mainWindow.visTcells));} //index 5
        else{firstModel.tCells= null;}

        //The vessels
        if(VESSELS_ACTIVE) {firstModel.vessels= firstModel.AddCellPop(new Vessels(firstModel, mainWindow.visVessels));} //index 6
        else{firstModel.vessels= null;}
        
        //The diffusibles
        if(OXYGEN_ACTIVE) {firstModel.Oxygen= firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, OXYGEN_DIFFUSION_RATE, mainWindow.visO2));}
        else{firstModel.Oxygen= null;}
        if(GLUCOSE_ACTIVE) {firstModel.Glucose= firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, GLUCOSE_DIFFUSION_RATE, mainWindow.visGL));}
        else{firstModel.Glucose= null;}
        if(ACID_ACTIVE) {firstModel.Acid= firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, ACID_DIFFUSION_RATE, mainWindow.visPH));}
        else{firstModel.Acid= null;}
        if(DRUG_ACTIVE) {firstModel.Drug= firstModel.AddDiffusible(new DiffusionField(firstModel.xDim, firstModel.yDim, DRUG_DIFFUSION_RATE, mainWindow.visDR));}
        else{firstModel.Drug= null;}

        firstModel.InitPops();
        while (true) {
            firstModel.RunCellStep();
            firstModel.RunDiffuseStep(DIFFUSE_TIME_LENGTH);
        }
    }
}
