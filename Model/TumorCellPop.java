package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by dannichol on 09/11/2016.
 * TODO: These cells need to interact w/ acid and immune cells.
 */
public class TumorCellPop extends CellPop {

    final public double OxygenConsumption = TUMOR_OXYGEN_CONSUMPTION;
    final public double GlucoseConsumption = TUMOR_GLUCOSE_CONSUMPTION;
    double birthRate=TUMOR_PROLIF_RATE;

    final Visualizer visFull;

    public boolean SeedMe = false;

    private SqList VN_Hood = Utils.GenMooreNeighborhood();
    double[] migrantPops = new double[8];

    TumorCellPop(TumorModel model, Visualizer vis) {
        super(model, vis);
        this.visFull = null;
    }

    TumorCellPop(TumorModel model, Visualizer vis, Visualizer visFULL) {
        super(model, vis);
        this.visFull = visFULL;
    }

    void addAcid(int x, int y, double acidProductionRate){};

    static private double DiffusibleDeath(double cellPop, double diffusible,double threshold,double scaleConst){
        return diffusible>threshold?0:cellPop*(1-diffusible/threshold)*scaleConst;
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
            pops[I(xDim/2,yDim/2)] += MAX_POP / 500.;
            this.SeedMe = false;
        }

        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                int i = I(x, y);
                double pop = pops[i];
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += Math.max(pop, 0);
                    continue;
                }

                double hypoxicDeathDelta = 0, acidAmnt=0, oxy=0,gluc=0,acid=0,drugConc=0, hypoxicKillingReduction=1;
                if (OXYGEN_ACTIVE) oxy = myModel.Oxygen.field[I(x, y)];
                if (GLUCOSE_ACTIVE) gluc = myModel.Glucose.field[I(x, y)];
                if (ACID_ACTIVE) acid = myModel.Acid.field[I(x, y)];
                //hypoxicDeathDelta = HypoxicDeath(pop, oxy, gluc, acid);
                acidAmnt=acid*BIN_VOLUME;
                hypoxicDeathDelta=DiffusibleDeath(pops[i],oxy,TUMOR_HYPOXIC_THRESHOLD,NORMAL_HYPOXIC_SCALE_FACTOR);

//                if(OXYGEN_ACTIVE){
//                    hypoxicKillingReduction=oxy*BIN_VOLUME/(1+oxy*BIN_VOLUME);
//                    if(hypoxicKillingReduction<IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION){
//                        hypoxicKillingReduction=IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION;
//                    }
//                }
                if(DRUG_ACTIVE){
                    drugConc=myModel.Drug.field[I(x,y)];
                }

                double birthDelta = Birth(pop, totalPop, birthRate);
                double deathDelta = Death(pop, TUMOR_DEATH_RATE);
                double migrantDelta = Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);

                swap[i] += pop + birthDelta - deathDelta - hypoxicDeathDelta - migrantDelta;
                if(NECRO_CELLS_ACTIVE) {
                    myModel.necroCells.swap[i] += hypoxicDeathDelta;
                }
                if(ACIDIC_CELLS_ACTIVE){
                    addAcid(x,y,ACID_PRODUCTION_RATE); //only effectual for the AcidProducing class
                }
                if (swap[i] < 0.0) {
                    swap[i] = 0.0;
                }
            }
        }
    }

    //called once every tick
   public void Draw() {
       for (int x = 0; x < xDim; x++) {
           for (int y = 0; y < yDim; y++) {
               myVis.SetHeat(x, y, pops[I(x, y)] / MAX_POP); //or *30
           }
       }
   }
}


