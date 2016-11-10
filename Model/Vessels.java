package Model;

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
               if (rand.nextFloat() < 0.01)
                {
                    pops[I(x, y)] = MAX_POP / 5.;
                }

            }
        }

    }

    private static double rhoThresh = 0.9;
    static private double Death(double cellPop, double totalPop) {
        if (totalPop > 0.55 * MAX_POP) {
            return cellPop;//cellPop * (totalPop - (rhoThresh * MAX_POP)) * (1. / (rhoThresh * MAX_POP));
        } else {
            return 0.0;
        }

    }

    void Step() {
        for (int x=0; x < xDim; x++) {
            for(int y=0; y<yDim; y++) {
                double deathDelta = Death(pops[I(x,y)],  myModel.totalPops[I(x,y)]);
                swap[I(x,y)] = pops[I(x,y)] - deathDelta;
                if (swap[I(x,y)] < 1)
                {
                    swap[I(x,y)] = 0;
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
