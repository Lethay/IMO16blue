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
               if (rand.nextFloat() < 0.001)
                {
                    pops[I(x, y)] = MAX_POP / 5.;
                }

            }
        }

    }

    void Step() {
        for (int i=0; i < xDim*yDim; i++) {
            swap[i] = pops[i];
        }
    }

    //draws the cells on the screen
    void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                if (pops[I(x,y)] > 0) {
                    myVis.Set(x, y, 1, 0, 0);
                }
            }
        }
    }

}
