package Model;
import AgentGridMin.Visualizer;
import Model.ModelMain.*;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by dannichol on 09/11/2016.
 */
public class TumorCellPop extends CellPop {


    static final private double TUMOR_PROLIF_RATE=0.04*CONST_AND_FUNCTIONS.TIME_STEP;
    static final double TUMOR_DEATH_RATE=0.02*CONST_AND_FUNCTIONS.TIME_STEP;


    TumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
    }

    static private double Death(double cellPop, double immunePop, double deathRate, double PDSwitchRate, double killRate){
        return cellPop*deathRate +  PDSwitchRate*cellPop + killRate*cellPop*immunePop;
    }

    static private double Birth(double cellPop, double resistantPop, double drugConc, double totalPop, double birthRate, double PDSwitchRate, double inhibitionRate){
        return cellPop*(birthRate * (1 - totalPop / MAX_POP)) + PDSwitchRate*resistantPop + inhibitionRate*resistantPop*drugConc;
    }

    static private double MigrantPop(double totalPop, double numBorn){
        return 0.0;
    }

    //runs once at the begining of the model to initialize cell pops
    public void InitPop(){
        for (int x=0; x<50; x++){
            for (int y = 0; y<50; y++) {
                pops[I(x,y)]=1000;
            }

        }
        pops[I(50,50)] = 0;
    }
    //called once every tick
    public void Step(){
        for (int x=0; x<xDim; x++){
            for(int y=0; y<yDim; y++){
                int i = I(x,y);
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
