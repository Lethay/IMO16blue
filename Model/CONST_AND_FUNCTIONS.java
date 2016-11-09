package Model;

import AgentGridMin.SqList;

import java.util.Arrays;

/**
 * Created by rafael on 11/9/16.
 */
public class CONST_AND_FUNCTIONS {

    //cell constants
    static final double MAX_POP=10000;
    static final double TIME_STEP=0.2; //days
    static final double NECROTIC_DECAY_RATE=0.07*TIME_STEP;
    static final double NORMAL_PROLIF_RATE=0.04*TIME_STEP;
    static final double NORMAL_DEATH_RATE=0.02*TIME_STEP;
    static final double NORMAL_HYPOXIC_THRESHOLD=0.2;
    static final double MAX_HYPOXIC_DEATH_RATE=0.5;


    //diffusible constants
    static final double OXYGEN_PRODUCTION_RATE=0.03;

    //returns pop to be born
    static double Birth(double cellPop,double totalPop,double maxProlifRate) {
        return cellPop*(maxProlifRate * (1 - totalPop / MAX_POP));
    }
    //returns pop to be killed
    static double Death(double cellPop,double deathRate){
        return cellPop*deathRate;
    }

    static void NecroDeath(double[] NecroSwap,int i,double deadCellPop){
        NecroSwap[i]+=deadCellPop;
    }
    //gets the population that will migrate, based on the number of cells born
    static double MigrantPop(double totalPop,double numBorn){
        return numBorn * (totalPop / MAX_POP);
    }

    //executes migration, returns number of migrants
    static double Migrate( TumorModel myModel, double[] swap,int x, int y, double popToMigrate, SqList neighborhood, double[] storePops){
        Arrays.fill(storePops,0);
        double myTotal = myModel.totalPops[myModel.I(x, y)];
        double surroundingDiff = 0;
        for (int i = 0; i < neighborhood.length; i++) {
            int checkX = neighborhood.Xsq(i) + x;
            int checkY = neighborhood.Ysq(i) + y;
            if (myModel.WithinGrid(checkX, checkY)) {
                double neighborDelta = myTotal-myModel.totalPops[myModel.I(checkX, checkY)];
                if(neighborDelta>0) {
                    storePops[i] = neighborDelta;
                    surroundingDiff += neighborDelta;
                }
            }
        }
        if(surroundingDiff>0) {
            for (int i = 0; i < storePops.length; i++) {
                if (storePops[i] > 0) {
                    swap[myModel.I(neighborhood.Xsq(i) + x, neighborhood.Ysq(i) + y)] += popToMigrate * (storePops[i] / surroundingDiff);
                }
            }
        }
        return surroundingDiff>0?popToMigrate:0;
    }
}
