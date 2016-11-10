package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by liam on 09/11/2016.
 * //TODO: These cells produce acid. They need to be initialised on the grid somewhere. They need to interact w/ acid and immune cells.
 */
public class acidProducingTumorCellPop extends CellPop {

    final public double OxygenConsumption = 0.0025;
    final public double GlucoseConsumption = 0.003;
    final public double DrugConsumption = 0.03;

    public boolean SeedMe = false;

    private SqList VN_Hood = Utils.GenMooreNeighborhood();
    double[] migrantPops = new double[8];

    acidProducingTumorCellPop(TumorModel model, Visualizer vis) {
        super(model, vis);
    }


    static private double Death(double cellPop, double immunePop, double acidNumber, double deathRate, double killRate){
        return deathRate*cellPop + cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate / (1+acidNumber);
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

    static private double Birth(double cellPop, double totalPop, double birthRate){
        return birthRate*cellPop*(1 - totalPop/MAX_POP);
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
                double immunePop=myModel.tCells.pops[i];
                double acidNumber=0*cellSize; //TODO correct this - the "0" needs to be acidConc
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += Math.max(pop,0);
                    continue;
                }

                double oxy = myModel.Oxygen.field[I(x,y)];
                double gluc = myModel.Glucose.field[I(x,y)];
                double acid = myModel.Acid.field[I(x,y)];

                double hypoxicDeathDelta = HypoxicDeath(pop, oxy, gluc, acid);
                double birthDelta = Birth(pop,totalPop, TUMOR_PROLIF_RATE);
                double deathDelta = Death(pop, immunePop, acidNumber, TUMOR_DEATH_RATE, IMMUNE_KILL_RATE);
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
                if(myVis!=null&&pops[I(x,y)]>1) {
                    myVis.SetHeat(x, y, pops[I(x, y)] / MAX_POP);
                }
                myVis.SetHeat(x, y, 30*pops[I(x, y)] / MAX_POP);
            }
        }
    }
}


