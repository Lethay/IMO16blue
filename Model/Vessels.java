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
                if ((x % 5 == 0) && (y % 5 == 0)) {
                    pops[I(x, y)] = 100;
                }
            }
        }
    }

    void Step() {
        swap = pops;
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
