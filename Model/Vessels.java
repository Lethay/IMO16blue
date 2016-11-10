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
               if (rand.nextFloat() < 0.015)
                {
                    double vesDensity = 3*rand.nextGaussian()+3;
                    pops[I(x, y)] = MAX_POP / (vesDensity);
                }

            }
        }

    }

    private static double rhoThresh = 0.6;
    static private double Death(double cellPop, double totalPop, double neighbourTumorPop) {
        if (totalPop > rhoThresh * MAX_POP) {
            return cellPop;//cellPop * (totalPop - (rhoThresh * MAX_POP)) * (1. / (rhoThresh * MAX_POP));
        } else if (neighbourTumorPop > MAX_POP / 10.){
            return cellPop;
        } else {
            return 0.0;
        }

    }

    void Step() {

        SqList VN_Hood = Utils.GenMooreNeighborhood();

        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
//                double press = 0.0;
//                for (int i = 0; i < VN_Hood.length; i++) {
////                    int checkX = VN_Hood.Xsq(i) + x;
////                    int checkY = VN_Hood.Ysq(i) + y;
////                    if (myModel.WithinGrid(checkX, checkY)) {
////
////
////                    }

                double deathDelta = Death(pops[I(x, y)], myModel.totalPops[I(x, y)], myModel.tumorCells.pops[I(x, y)]);
                swap[I(x, y)] = pops[I(x, y)] - deathDelta;

                if (swap[I(x, y)] <= 0) {
                    swap[I(x, y)] = 0;
                }

            }
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
