package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by dannichol on 09/11/2016.
 * TODO: These cells need to interact w/ acid and immune cells.
 */
public class TumorCellPop extends CellPop {

    final public double OxygenConsumption = 0.0025;
    final public double GlucoseConsumption = 0.003;
    double birthRate=0.22*TIME_STEP;

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

    double Death(double cellPop, double immunePop, double drugConc, double acidNumber, double hypoxicKillingReduction, double drugEfficacy, double deathRate, double killRate){
        double baseDeathRate=deathRate*cellPop; //base death rate
        double tCellKillRate=cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate / (1+acidNumber)*hypoxicKillingReduction;
        return baseDeathRate+tCellKillRate;
    }

    void addAcid(int x, int y, double acidProductionRate){};

    static private double HypoxicDeath(double cellPop, double oxygen, double gluc, double acid)
    {
        double hypDeath = 0.0;
        if (oxygen < TUMOUR_LOW_OXYGEN_DEATH_THRESHOLD)
        {
            hypDeath += 0.1;
        }
        if (acid > TUMOUR_HIGH_ACID_DEATH_THRESHOLD)
        {
            hypDeath += 0.0;
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
            pops[I(xDim/2,yDim/2)] += MAX_POP / 500.;
            this.SeedMe = false;
        }

        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                int i = I(x, y);
                double pop = pops[i];
                double immunePop = myModel.tCells.pops[i];
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += Math.max(pop, 0);
                    continue;
                }

                double hypoxicDeathDelta = 0, acidAmnt=0, oxy=0,gluc=0,acid=0,drugConc=0, hypoxicKillingReduction=1;
                if (OXYGEN_ACTIVE && GLUCOSE_ACTIVE && ACID_ACTIVE) {
                    oxy = myModel.Oxygen.field[I(x, y)];
                    gluc = myModel.Glucose.field[I(x, y)];
                    acid = myModel.Acid.field[I(x, y)];

                    hypoxicDeathDelta = HypoxicDeath(pop, oxy, gluc, acid);
                    acidAmnt=acid*BIN_VOLUME;
                }
                if(OXYGEN_ACTIVE){
                    hypoxicKillingReduction=oxy*BIN_VOLUME/(1+oxy*BIN_VOLUME);
                    if(hypoxicKillingReduction<IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION){
                        hypoxicKillingReduction=IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION;
                    }
                }
                if(DRUG_ACTIVE){
                    drugConc=myModel.Drug.field[I(x,y)];
                }

                double birthDelta = Birth(pop, totalPop, birthRate);
                double deathDelta = Death(pop, immunePop, drugConc, acidAmnt, hypoxicKillingReduction, DRUG_EFFICACY, TUMOR_DEATH_RATE, IMMUNE_KILL_RATE);
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
       double nrmRho;
       double resRho;
       double necRho;
       for (int x = 0; x < xDim; x++) {
           for (int y = 0; y < yDim; y++) {
               if(myVis!=null&&pops[I(x,y)]>1) {
                   myVis.SetHeat(x, y, pops[I(x, y)] / MAX_POP);
               }
               myVis.SetHeat(x, y, 30*pops[I(x, y)] / MAX_POP);

               if (visFull != null)
               {
                   nrmRho = pops[I(x,y)];
                   resRho = myModel.PDL1TumorCells.pops[I(x,y)];
                   necRho = myModel.necroCells.pops[I(x,y)];
                   visFull.MultipleDensitiesSet(x,y,nrmRho, resRho, necRho);
               }

           }
       }
   }
     // public void Draw() {
     //     for (int x = 0; x < xDim; x++) {
     //         for (int y = 0; y < yDim; y++) {
     //             if (pops[I(x,y)] != 0) {
     //                 myVis.SetHeat(x, y,pops[I(x,y)]);
     //             }
     //         }
     //     }
     // }
}


