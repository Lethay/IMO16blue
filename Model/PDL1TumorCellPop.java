package Model;
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


    PDL1TumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
    }

    static private double Death(double cellPop, double immunePop, double drugConc, double acidNumber, double drugEfficacy, double deathRate, double killRate){
        return deathRate*cellPop + cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate  *drugEfficacy*drugConc/(1+drugEfficacy*drugConc) / (1+acidNumber);
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
                double immunePop=0; //myModel.cellPops.get(IMMUNE_POP_INDEX).pops[i]; //TODO correct this variable
                double drugConc=0; //TODO Correct this too
                double acidNumber=0*cellSize; //TODO correct this - the "0" needs to be acidConc
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
                double birthDelta = Birth(pop,totalPop, TUMOR_PROLIF_RATE);
                double deathDelta = Death(pop, immunePop, drugConc, acidNumber, DRUG_EFFICACY, TUMOR_DEATH_RATE, IMMUNE_KILL_RATE);
                double migrantDelta = MigrantPop(totalPop, birthDelta);

                swap[i] += pop + birthDelta - deathDelta - migrantDelta;

            }
        }
    }
    //called once every tick
    public void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                if (pops[I(x,y)] != 0) {
                    //myVis.Set(x, y, 0, 0, 1);
                }
            }
        }
    }


}
