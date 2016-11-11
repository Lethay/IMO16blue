package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 *  * Created by liam on 09/11/2016.
 *   * //TODO: Add function to produce acid. All other functions are extended from TumorCellPop.
 *    */
public class AcidProducingTumorCellPop extends TumorCellPop {

    AcidProducingTumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
        birthRate=0.20*TIME_STEP;
    }
    
    @Override
    public void addAcid(int x, int y, double acidProductionRate){
    	myModel.Acid.field[I(x,y)] += acidProductionRate;
    }
}


