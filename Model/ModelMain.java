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
    Visualizer vis;
    GuiWindow win;
    ModelVis(TumorModel model){
        myModel=model;
        int visScale=8;
        vis=new Visualizer(model.xDim,model.yDim,visScale);
        win=new GuiWindow("LungVis",model.xDim*visScale,model.yDim*visScale,1,1);
        win.AddComponent(vis,0,0,1,1);
    }
}


//model of tumor
class TumorModel {
    ArrayList<CellPop> cellPops;
    NormalCells normalCells;
    DiffusionField Oxygen;
    Random rand;
    double[] totalPops;
    int xDim;
    int yDim;
    int tick;



    TumorModel(int x, int y) {
        cellPops=new ArrayList<CellPop>();
        tick=0;
        rand = new Random();
//        Oxygen=new DiffusionField(x,y);
        totalPops = new double[x * y];
        xDim = x;
        yDim = y;
    }

    //adds cell population to the model step rotation
    <T extends CellPop> T AddCellPop(T addMe){
        cellPops.add(addMe);
        return addMe;
    }

    void InitPops(){
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            cellPops.get(iPop).InitPop();
        }
    }

    void RunStep(){
        //clear and fill total pop grid
        Arrays.fill(totalPops,0);
        for(int iPop=0;iPop<cellPops.size();iPop++) {
            CellPop currPop=cellPops.get(iPop);
            for (int i = 0; i < normalCells.pops.length; i++) {
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

public class ModelMain{
        public static void main(String[] args){
            TumorModel firstModel=new TumorModel(100,100);
            ModelVis mainWindow=new ModelVis(firstModel);
            //setting normalCells for access by other populations, adding cellpop for iteration
            firstModel.normalCells=firstModel.AddCellPop(new NormalCells(firstModel,mainWindow.vis));
            firstModel.AddCellPop(new TumorCellPop(firstModel, mainWindow.vis));
            firstModel.InitPops();
            while(true){
                firstModel.RunStep();
            }
        }
        }
