package Model;

import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;
import java.util.Random;

import static Model.CONST_AND_FUNCTIONS.MAX_POP;

/**
 * Created by rafael on 11/8/16.
 */
public class Vessels extends CellPop {
    Random rand = new Random();
    Vessels(TumorModel myModel, Visualizer vis) {
        super(myModel, vis);

    }

    void InitPop() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
               if (rand.nextFloat() < 0.005)
                {
                    double nVessels = Math.max(Math.min(2500*rand.nextGaussian(),5000),0)+1000;
                    pops[I(x, y)] = nVessels;
                }

            }
        }

    }

    private static double rhoThresh = 0.6;
    static private double Death(double cellPop, double totalPop, double neighbourTumorPop) {
        if (totalPop > rhoThresh * MAX_POP) {
            return cellPop; //* (totalPop - (rhoThresh * MAX_POP)) * (1. / (rhoThresh * MAX_POP));
        } else if (neighbourTumorPop > MAX_POP / 10.){
            return cellPop;
        } else {
            return 0.0;
        }

    }

    void Step() {

        SqList VN_Hood = Utils.GenMooreNeighborhood();

        for(int i=0;i<xDim*yDim;i++){
            //double deathDelta = Death(pops[I(x, y)], myModel.totalPops[I(x, y)], myModel.tumorCells.pops[I(x, y)]);
            swap[i]+=pops[i];
        }
    }
    //draws the cells on the screen
    void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                myVis.SetHeat(x,y, pops[I(x,y)]/MAX_POP);
            }
        }
    }

}
