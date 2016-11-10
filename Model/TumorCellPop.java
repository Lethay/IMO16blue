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

    final public double OxygenConsumption = 0.0025;
    final public double GlucoseConsumption = 0.003;
    final public double DrugConsumption = 0.03;

    public boolean SeedMe = false;

    private SqList VN_Hood = Utils.GenMooreNeighborhood();
    double[] migrantPops = new double[8];

    TumorCellPop(TumorModel model, Visualizer vis) {
        super(model, vis);
    }


    static private double Death(double cellPop, double immunePop, double deathRate, double PDSwitchRate, double killRate) {
        return cellPop * deathRate + PDSwitchRate * cellPop + killRate * cellPop * immunePop;
    }

    static private double HypoxicDeath(double cellPop, double oxygen, double gluc, double acid)
    {
        double hypDeath = 0.0;
        if (oxygen < TUMOUR_LOW_OXYGEN_DEATH_THRESHOLD)
        {
            hypDeath += 0.3;
        }
        if (acid > TUMOUR_HIGH_ACID_DEATH_THRESHOLD)
        {
            hypDeath += 0.2;
        }
        return hypDeath * cellPop;
    }

    static private double Birth(double cellPop, double resistantPop, double drugConc, double totalPop, double birthRate, double PDSwitchRate, double inhibitionRate, double gluc, double oxy)
    {
        double modifiedBirthRate = modifiedBirthRate(birthRate, gluc, oxy);
        return cellPop * (modifiedBirthRate * (1 - totalPop / MAX_POP)) + PDSwitchRate * resistantPop + inhibitionRate * resistantPop * drugConc;
    }

    //runs once at the begining of the model to initialize cell pops
    public void InitPop() {
        return ;
    }

    //called once every tick
    public void Step() {

        if (SeedMe) {
            pops[I(xDim / 2, yDim / 2)] += MAX_POP / 500.;
            this.SeedMe = false;
        }

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

                double deathDelta = Death(pop, immunePop, TUMOR_DEATH_RATE, TUMOUR_SWITCH_RATE, IMMUNE_KILL_RATE);

                double oxy = myModel.Oxygen.field[I(x,y)];
                double gluc = myModel.Glucose.field[I(x,y)];
                double acid = myModel.Acid.field[I(x,y)];

                double hypoxicDeathDelta = HypoxicDeath(pop, oxy, gluc, acid);
                double birthDelta = Birth(pop, resistantPop, drugConcentration, totalPop, TUMOR_PROLIF_RATE, TUMOUR_SWITCH_RATE, DRUG_INHIBITION_RATE, gluc, oxy);
                double migrantDelta = Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);
                swap[i] += pop + birthDelta - deathDelta - hypoxicDeathDelta - migrantDelta;
                myModel.necroCells.swap[i] += hypoxicDeathDelta;
                if (swap[i] < 0.0){
                    swap[i]=0.0;
                }

            }
        }
    }

    //called once every tick
    public void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                myVis.SetHeat(x, y, 30*pops[I(x, y)] / MAX_POP);
            }
        }
    }
}


