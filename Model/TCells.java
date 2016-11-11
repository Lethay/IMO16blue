package Model;

import AgentGridMin.SqList;
import AgentGridMin.Visualizer;

import java.util.Arrays;

import static AgentGridMin.Utils.*;
import static Model.CONST_AND_FUNCTIONS.*;

/**
 * Created by liamb on 09/11/2016.
 */
public class TCells extends CellPop{
    SqList MooreHood=GenMooreNeighborhood();
    SqList VNHoodwOrigin=GenVonNeumannNeighborhoodWithOrigin();
    SqList VNHood=GenVonNeumannNeighborhood();
    double[] popList=new double[8];
    double[] MovePops;
    double[] diffConsts;
    public boolean active = false;
    TCells(TumorModel myModel, Visualizer myVis){
        super(myModel,myVis);
        this.cellSize=0.25;
        this.MovePops=new double[xDim*yDim];
        this.diffConsts=new double[xDim*yDim];
    }
    double Interact(int i,double interactPop){
        double tumorNormal=myModel.tumorCells.pops[i];
        double tumorPDL1=myModel.PDL1TumorCells.pops[i];
        double totalTumor=tumorNormal+tumorPDL1;
        double interactPopNormal=tumorNormal/(totalTumor);
        double interactPopPDL1=tumorPDL1/(totalTumor);
        //normal cells
        myModel.tumorCells.swap[i]-=tumorNormal*interactPopNormal/(IMMUNE_KILL_RATE_SHAPE_FACTOR+tumorNormal)*IMMUNE_KILL_RATE; ///(1+acidNumber)*hypoxicKillingReduction;
        //pdl1 cells
        myModel.PDL1TumorCells.swap[i]-=tumorPDL1*interactPopPDL1/(IMMUNE_KILL_RATE_SHAPE_FACTOR+tumorPDL1)*IMMUNE_KILL_RATE  *DRUG_EFFICACY*myModel.Drug.field[i]/(1+DRUG_EFFICACY*myModel.Drug.field[i]);// / (1+acidNumber) *hypoxicKillingReduction;
        double leftoverCells=interactPop-totalTumor;
        return leftoverCells>0?leftoverCells:0;
    }
    void InitPop() {
    }
    void Step(){
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                int i=I(x,y);
                //IMMUNE CELLS ENTER THROUGH VESSELS
                double VesselPop = myModel.vessels.pops[i];
                if (active&&VesselPop > 1) {
                    swap[i] += Birth(VesselPop,myModel.totalPops[i],VESSELS_TO_TCELLS);
                }
                diffConsts[i]=Math.min(Math.max(1-myModel.totalPops[i]/MAX_POP,0),1);
                MovePops[i]=0;
                //Skip pops less than 1
                if(pops[i]<1){
                    swap[i]+=pops[i];
                    continue;
                }
                swap[i]+=pops[i];
                swap[i]-=Death(pops[i],TCELL_DEATH_RATE);
                //interaction with tumor cells on same square
                double popsToInteract=pops[i];
                if(myModel.tumorCells.pops[i]>1){
                    //popsToInteract-=
                            Interact(i,popsToInteract);
                }
                //double totalPop=0;
                //Arrays.fill(popList,0);
                //for(int j=0;j<MooreHood.length;j++){
                //    int lookX=MooreHood.Xsq(j)+x;
                //    int lookY=MooreHood.Ysq(j)+y;
                //    if(myModel.WithinGrid(lookX,lookY)&&myModel.tumorCells.pops[I(lookX,lookY)]>1){
                //        popList[j]=myModel.tumorCells.pops[j];
                //        totalPop+=myModel.totalPops[j];
                //    }
                //}
                ////interaction with tumor cells on other squares
                //double interacted=0;
                //for(int j=0;j<MooreHood.length;j++){
                //    if(popList[j]>0){
                //        interacted+=Interact(I(MooreHood.Xsq(j)+x,MooreHood.Ysq(j)+y),popsToInteract*(popList[j]/totalPop));
                //    }
                //}
                //popsToInteract-=interacted;
                MovePops[i]=popsToInteract;
                //cell migration
            }
        }
        //Movement
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                int midI=I(x,y);
                double diffMiddle=diffConsts[midI];
                double middleMovers=MovePops[midI];
                double diffSum=0;
                int countSq=0;
                double influxSum=0;
                if(WithinGrid(x+1,y)) {
                    int i = I(x + 1, y);
                    double influxPop = MovePops[i];
                    double diffRate = diffConsts[i]+diffMiddle;
                    diffSum += influxPop * diffRate;
                    countSq += 1;
                    influxSum += diffRate;
                }
                if(WithinGrid(x-1,y)) {
                    int i = I(x - 1, y);
                    double influxPop = MovePops[i];
                    double diffRate = diffConsts[i]+diffMiddle;
                    diffSum += influxPop * diffRate;
                    countSq += 1;
                    influxSum += diffRate;
                }
                if(WithinGrid(x,y+1)) {
                    int i = I(x, y+1);
                    double influxPop = MovePops[i];
                    double diffRate = diffConsts[i]+diffMiddle;
                    diffSum += influxPop * diffRate;
                    countSq += 1;
                    influxSum += diffRate;
                }
                if(WithinGrid(x,y-1)) {
                    int i = I(x, y-1);
                    double influxPop = MovePops[i];
                    double diffRate = diffConsts[i]+diffMiddle;
                    diffSum += influxPop * diffRate;
                    countSq += 1;
                    influxSum += diffRate;
                }
                swap[midI]+=TCELL_MOVE_RATE*(diffSum-(influxSum*MovePops[midI]));
            }
        }
    }
    void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                myVis.SetHeat(x, y, (pops[I(x, y)]*10) / MAX_POP);
            }
        }
    }
}
