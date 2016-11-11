package Model;

import AgentGridMin.SqList;
import AgentGridMin.Utils;
import AgentGridMin.Visualizer;

import java.util.Random;

import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by rafael on 11/8/16.
 */
public class NormalCells extends CellPop {
    private SqList VN_Hood = Utils.GenVonNeumannNeighborhood();
    double[] migrantPops = new double[4];
    Random rand = new Random();

    final public double OxygenConsumption= NORMAL_OXYGEN_CONSUMPTION;
    final public double GlucoseConsumption=NORMAL_GLUCOSE_CONSUMPTION;
    final public double DrugConsumption = NORMAL_DRUG_CONSUMPTION ;


    NormalCells(TumorModel model,Visualizer vis) {
        super(model,vis);
    }

    //initializes the grid with a starting population, called once
    void InitPop() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                double tisDensity = 10. + (rand.nextGaussian());
                if (tisDensity < 7. || tisDensity > 13.)
                {
                    tisDensity = 10.;
                }
//                double tisDensity = 10.;
                pops[I(x, y)] = MAX_POP / (tisDensity);
            }
        }
    }

    //runs one step of the model
    void Step() {
        for(int x=0;x<xDim;x++) {
            for(int y=0;y<yDim;y++) {
                int i = I(x, y);
                double pop = pops[i];
                double totalPop = myModel.totalPops[i];
                if (pop < 1) {
                    swap[i] += pop;
                    continue;
                }
                double birthDelta = Birth(pop, totalPop, NORMAL_PROLIF_RATE);
//                double birthDelta = MetabolicBirth(pop, totalPop, NORMAL_PROLIF_RATE, myModel.Oxygen.Get(x,y),myModel.Glucose.Get(x,y),GLUCOSE_USAGE_NORMAL,OXYGEN_USAGE_NORMAL, ACID_RATE_NORMAL);
                double deathDelta = Death(pop, NORMAL_DEATH_RATE);
                //NecroDeath(myModel.necroCells.swap,i,deathDelta);
                double migrantDelta =  Migrate(myModel, swap, x, y, MigrantPop(totalPop, birthDelta), VN_Hood, migrantPops);
                swap[i] += pop + birthDelta - deathDelta - migrantDelta;
                //NecroDeath(myModel.necroCells.swap,i,deathDelta);
            }

        }
    }

    //draws the cells on the screen
    void Draw() {
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                myVis.SetHeat(x,y,(pops[I(x,y)])/MAX_POP);
            }
        }
    }
}
