package Model;

import AgentGridMin.SqList;

import java.util.Arrays;

/**
 * Created by rafael on 11/9/16.
 */
public class CONST_AND_FUNCTIONS {

    static final double DIFFUSE_TIME_LENGTH = 1.0;
    static final double DIFFUSE_DT = 0.01;

    //cell constants
    static final double MAX_POP=10000;
    static final double TIME_STEP=0.2; //days
    static final double NECROTIC_DECAY_RATE=0.01*TIME_STEP;
    static final double NORMAL_PROLIF_RATE=0.02*TIME_STEP;
    static final double NORMAL_DEATH_RATE=0.02*TIME_STEP;

    static final double TUMOR_PROLIF_RATE = 0.4 * TIME_STEP;
    static final double TUMOR_DEATH_RATE = 0.02*TIME_STEP;

    static final double TUMOUR_SWITCH_RATE=0.01*TIME_STEP;
    static final double DRUG_INHIBITION_RATE=0.03*TIME_STEP;
    static final double IMMUNE_KILL_RATE=0.08*TIME_STEP;
    static final int TMR_POP_INDEX=2;
    static final int RESIST_TMR_POP_INDEX=3;
    static final int IMMUNE_POP_INDEX=-1; //TODO give a value for this
    static final double NORMAL_HYPOXIC_THRESHOLD=0.2;
    static final double MAX_HYPOXIC_DEATH_RATE=0.5;


    //diffusible constants
    static final double OXYGEN_DIFFUSION_RATE = 0.1;
    static final double GLUCOSE_DIFFUSION_RATE = 0.1;
    static final double ACID_DIFFUSION_RATE = 0.1;

    static final double OXYGEN_PRODUCTION_RATE=0.003; //per dt, per unit density
    static final double GLUCOSE_PRODUCTION_RATE=0.003; //per dt, per unit density
    static final double ACID_PRODUCTION_RATE=1.0; //per dt, per unit density

    //Govern gluc/oxy on
    static final double GLUCOSE_THRESHOLD = 0.05;
    static final double OXYGEN_THRESHOLD = 0.05;

    static  double modifiedBirthRate(double birthRate, double oxy, double gluc) {
        double oxyPenalty = 1.0;
        double glucPenalty = 1.0;
        if (gluc < GLUCOSE_THRESHOLD)
        {
            glucPenalty = 0.1;
        }
        if (oxy < OXYGEN_THRESHOLD)
        {
            oxyPenalty = 0.1;
        }
        return birthRate * glucPenalty * oxyPenalty;
    }

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
