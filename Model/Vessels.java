package Model;

import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.MAX_POP;

/**
 * Created by rafael on 11/8/16.
 */
public class Vessels extends CellPop {
    Vessels(TumorModel myModel, Visualizer vis) {
        super(myModel, vis);

    }

    void InitPop() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                if ((x % 5 == 0) && (y % 5 == 0) && (y > 10) && (y < 70) && (x > 10) && (x < 70)) {
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
