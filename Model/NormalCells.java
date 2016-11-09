package Model;

import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by rafael on 11/8/16.
 */
public class NormalCells extends CellPop {
    private SqList VN_Hood = Utils.GenVonNeumannNeighborhood();
    double[] migrantPops = new double[4];

    NormalCells(TumorModel model,Visualizer vis) {
        super(model,vis);
    }

    //initializes the grid with a starting population, called once
    void InitPop() {
        for (int x = (int)(xDim*3.0/8); x < xDim*5.0/8; x++) {
            for (int y = (int)(yDim*3.0/8); y < xDim*5.0/8; y++) {
                double start= myModel.rand.nextDouble()*MAX_POP;
                pops[I(x,y)]=start;
            }
        }
    }

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
                double birthDelta = Birth(pop, totalPop, NORMAL_PROLIF_RATE);
                double deathDelta = Death(pop, NORMAL_DEATH_RATE);
                double migrantDelta = Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);
                swap[i] += pop + birthDelta - deathDelta - migrantDelta;
            }

        }
    }

    //draws the cells on the screen
    void Draw() {
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                //myVis.SetHeat(x,y,pops[I(x,y)]/MAX_POP);
            }
        }
    }
}
