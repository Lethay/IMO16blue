package Model;
import AgentGridMin.Visualizer;
import Model.ModelMain.*;

import static Model.CONST_AND_FUNCTIONS.MAX_POP;

/**
 * Created by dannichol on 09/11/2016.
 */
public class TumorCellPop extends CellPop {


    static final private double TUMOR_PROLIF_RATE = 0.04 * CONST_AND_FUNCTIONS.TIME_STEP;
    static final double TUMOR_DEATH_RATE = 0.02 * CONST_AND_FUNCTIONS.TIME_STEP;


    TumorCellPop(TumorModel model, Visualizer vis) {
        super(model, vis);
    }

    static private double Death(double cellPop, double totalPop, double deathRate) {
        return cellPop * deathRate;
    }

    static private double Birth(double cellPop, double totalPop, double birthRate) {
        return cellPop * (birthRate * (1 - totalPop / MAX_POP));
    }

    static private double MigrantPop(double totalPop, double numBorn) {
        return 0.0;
    }

    //runs once at the begining of the model to initialize cell pops
    public void InitPop() {
//        pops[I(xDim / 2, yDim / 2)] = MAX_POP / 10.;
    }

    //called once every tick
    public void Step() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                int i = I(x, y);
                double pop = pops[i];
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
                double birthDelta = Birth(pop, totalPop, TUMOR_PROLIF_RATE);
                double deathDelta = Death(pop, totalPop, TUMOR_DEATH_RATE);
                double migrantDelta = MigrantPop(totalPop, birthDelta);

                swap[i] += pop + birthDelta - deathDelta - migrantDelta;

            }
        }
    }

    //called once every tick
    public void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                myVis.SetHeat(x, y, pops[I(x, y)] / MAX_POP);
            }
        }
    }
}


