package Model;
import AgentGridMin.Visualizer;
import Model.ModelMain.*;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by liam on 09/11/2016.
 */
public class resistantTumorCellPop extends CellPop {


    static final private double TUMOR_PROLIF_RATE=0.04*CONST_AND_FUNCTIONS.TIME_STEP;
    static final double TUMOR_DEATH_RATE=0.02*CONST_AND_FUNCTIONS.TIME_STEP;


    resistantTumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
    }

    static private double Death(double cellPop, double drugConc, double deathRate, double PDSwitchRate, double inhibitionRate){
        return cellPop*deathRate + inhibitionRate*cellPop*drugConc +  PDSwitchRate*cellPop;
    }

    static private double Birth(double cellPop, double nonResistantPop, double totalPop, double birthRate, double PDSwitchRate){
        return cellPop*(birthRate * (1 - totalPop / MAX_POP)) + PDSwitchRate*nonResistantPop;
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
                double nonResistantPop=myModel.cellPops.get(TMR_POP_INDEX).pops[i];
                double drugConcentration=0; //TODO Correct this variable
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
                double birthDelta = Birth(pop, nonResistantPop,totalPop, TUMOR_PROLIF_RATE, TUMOUR_SWITCH_RATE);
                double deathDelta = Death(pop, drugConcentration, TUMOR_DEATH_RATE, TUMOUR_SWITCH_RATE, DRUG_INHIBITION_RATE);
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
