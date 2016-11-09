package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by dannichol on 09/11/2016.
 */
public class TumorCellPop extends CellPop {

    private SqList VN_Hood = Utils.GenVonNeumannNeighborhood();
    double[] migrantPops = new double[4];

    static final private double TUMOR_PROLIF_RATE = 0.4 * CONST_AND_FUNCTIONS.TIME_STEP;
    static final double TUMOR_DEATH_RATE = 0.02 * CONST_AND_FUNCTIONS.TIME_STEP;


    TumorCellPop(TumorModel model, Visualizer vis) {
        super(model, vis);
    }

    static private double Death(double cellPop, double immunePop, double deathRate, double PDSwitchRate, double killRate) {
        return cellPop * deathRate + PDSwitchRate * cellPop + killRate * cellPop * immunePop;
    }

    static private double HypoxicDeath(double cellPop, double oxygen, double gluc, double acid)
    {
        return 0.0;
    }

    static private double Birth(double cellPop, double resistantPop, double drugConc, double totalPop, double birthRate, double PDSwitchRate, double inhibitionRate) {
        return cellPop * (birthRate * (1 - totalPop / MAX_POP)) + PDSwitchRate * resistantPop + inhibitionRate * resistantPop * drugConc;
    }

    //runs once at the begining of the model to initialize cell pops
    public void InitPop() {
        pops[I(xDim / 2, yDim / 2)] = MAX_POP / 10.;
    }

    //called once every tick
    public void Step() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                int i = I(x, y);
                double pop = pops[i];
                double resistantPop=myModel.cellPops.get(RESIST_TMR_POP_INDEX).pops[i];
                double immunePop=0; //myModel.cellPops.get(IMMUNE_POP_INDEX)[i]; //TODO Correct this variable
                double drugConcentration=0; //TODO Correct this variable
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }

                double birthDelta = Birth(pop, resistantPop, drugConcentration, totalPop, TUMOR_PROLIF_RATE, TUMOUR_SWITCH_RATE, DRUG_INHIBITION_RATE);
                double deathDelta = Death(pop, immunePop, TUMOR_DEATH_RATE, TUMOUR_SWITCH_RATE, IMMUNE_KILL_RATE);
//                double oxyDeathDelta =
                double migrantDelta = Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);
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


