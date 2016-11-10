package Model;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;
import Model.ModelMain.*;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by liam on 09/11/2016.
 * TODO: These cells need to interact w/ acid and immune cells.

 */
public class PDL1TumorCellPop extends CellPop {

    static final private double TUMOR_PROLIF_RATE=0.04*CONST_AND_FUNCTIONS.TIME_STEP;
    static final double TUMOR_DEATH_RATE=0.02*CONST_AND_FUNCTIONS.TIME_STEP;
    final public double DrugConsumption = 0.03;

    private SqList VN_Hood = Utils.GenMooreNeighborhood();
    double[] migrantPops = new double[8];

    PDL1TumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
    }

    static private double Death(double cellPop, double immunePop, double drugConc, double acidNumber, double hypoxicKillingReduction, double drugEfficacy, double deathRate, double killRate){
        return deathRate*cellPop + cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate  *drugEfficacy*drugConc/(1+drugEfficacy*drugConc) / (1+acidNumber) *hypoxicKillingReduction;
    }

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

    static private double MigrantPop(double totalPop, double numBorn){
        return 0.0;
    }

    //runs once at the begining of the model to initialize cell pops -- but there is no initial population in this case.
    public void InitPop(){}
    //called once every tick
    public void Step(){
        for (int x=0; x<xDim; x++){
            for(int y=0; y<yDim; y++){
                int i = I(x,y);
                double pop = pops[i];
                double immunePop=myModel.tCells.pops[i];
                double drugConc=0; //TODO Correct this too
                double acidNumber=0*cellSize; //TODO correct this - the "0" needs to be acidConc
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
               
                double hypoxicDeathDelta = 0, acidAmnt=0, oxy=0,gluc=0,acid=0;
                if (OXYGEN_ACTIVE && GLUCOSE_ACTIVE && ACID_ACTIVE) {
                    oxy = myModel.Oxygen.field[I(x, y)];
                    gluc = myModel.Glucose.field[I(x, y)];
                    acid = myModel.Acid.field[I(x, y)];

                    hypoxicDeathDelta = HypoxicDeath(pop, oxy, gluc, acid);
                    acidAmnt=acid*BIN_VOLUME;
                }
                double hypoxicKillingReduction=oxy*BIN_VOLUME/(1+oxy*BIN_VOLUME);
                if(hypoxicKillingReduction<IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION){
                    hypoxicKillingReduction=IMMUNE_CELL_MAX_HYPOXIC_KILL_RATE_REDUCTION;
                }
                
                double birthDelta = Birth(pop,totalPop, TUMOR_PROLIF_RATE);
                double deathDelta = Death(pop, immunePop, drugConc, acidAmnt, hypoxicKillingReduction, DRUG_EFFICACY, TUMOR_DEATH_RATE, IMMUNE_KILL_RATE);
                double migrantDelta = Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);
                
                swap[i] += pop + birthDelta - deathDelta - hypoxicDeathDelta - migrantDelta;
                if(NECRO_CELLS_ACTIVE) {
                    myModel.necroCells.swap[i] += hypoxicDeathDelta;
                }
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
                if (pops[I(x,y)] != 0) {
                    myVis.SetHeat(x, y,pops[I(x,y)]);
                }
            }
        }
    }


}
