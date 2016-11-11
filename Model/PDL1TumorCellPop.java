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
    final public double DrugConsumption = 0.03;

    PDL1TumorCellPop(TumorModel model, Visualizer vis) {
        super(model,vis);
        birthRate=0.20*TIME_STEP;
    }
    //cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate  *drugEfficacy*drugConc/(1+drugEfficacy*drugConc) / (1+acidNumber) *hypoxicKillingReduction;

    @Override
    double Death(double cellPop, double immunePop, double drugConc, double acidNumber, double hypoxicKillingReduction, double drugEfficacy, double deathRate, double killRate){
        double baseDeathRate=deathRate*cellPop; //base death rate
        double tCellKillRate=cellPop*immunePop/(IMMUNE_KILL_RATE_SHAPE_FACTOR+cellPop)*killRate / (1+acidNumber) *hypoxicKillingReduction;
        tCellKillRate *=drugEfficacy*drugConc/(1+drugEfficacy*drugConc); //Reduction term specific for PD-L1 presenting cells
        return baseDeathRate+tCellKillRate;
    }
}
