package Model;
import AgentGridMin.Visualizer;
import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by liam on 09/11/2016.
 * //TODO: These cells produce acid. Currently no difference between these files.
 */
public class acidProducingTumorCellPop extends TumorCellPop {
    acidProducingTumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
        birthRate=0.10*TIME_STEP;
    }

}


