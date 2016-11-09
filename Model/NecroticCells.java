package Model;

import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by Nate on 9/11/2016.
 */
public class NecroticCells extends CellPop {

    NecroticCells(TumorModel model, Visualizer vis) {
        super(model,vis);
    }

    //initializes the grid with a starting population, called once
    void InitPop() {}

    //runs one step of the model
    void Step() {
        for(int x=0;x<xDim;x++) {
            for(int y=0;y<yDim;y++) {
                int i = I(x, y);
                double pop = pops[i];
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
                double decayDelta = Death(pop, NECROTIC_DECAY_RATE);
                swap[i] += pop - decayDelta;
            }
        }
    }

    //draws the cells on the screen
    void Draw() {
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                myVis.SetHeat(x,y,pops[I(x,y)]/MAX_POP);
            }
        }
    }
}
