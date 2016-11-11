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
public class PDL1TumorCellPop extends TumorCellPop {
    final public double DrugConsumption = PDL1_DRUG_CONSUMPTION;

    PDL1TumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
        birthRate=PDL1_PROLIF_RATE;
    }
}
