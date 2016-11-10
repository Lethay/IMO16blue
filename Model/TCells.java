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
    SqList VNHoodwOrigin=GenVonNeumannNeighborhoodWithOrigin();
    SqList VNHood=GenVonNeumannNeighborhood();
    double[] popList=new double[5];
    TCells(TumorModel myModel, Visualizer myVis){
        super(myModel,myVis);
        this.cellSize=0.25;
    }
    double Interact(int i,double interactPop){
        double tVsC=interactPop-myModel.tumorCells.pops[i];
        if(tVsC>0){
            myModel.tumorCells.swap[i]-=interactPop*TCELLS_VS_TUMORCELLS;
            return interactPop-interactPop;
        }
        myModel.tumorCells.swap[i]-=myModel.tumorCells.pops[i]*TCELLS_VS_TUMORCELLS;
        return 0;
    }
    void InitPop() {
    }
    void Step(){
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++) {
                int i=I(x,y);
                //IMMUNE CELLS ENTER THROUGH VESSELS
                double VesselPop = myModel.vessels.pops[i];
                if (VesselPop > 1) {
                    swap[i] += Birth(VesselPop,myModel.totalPops[i],VESSELS_TO_TCELLS);
                }
                //Skip pops less than 1
                if(pops[i]<1){
                    swap[i]+=pops[i];
                    continue;
                }
                swap[i]-=Death(pops[i],TCELL_DEATH_RATE);
                //interaction with tumor cells on same square
                double popsToInteract=pops[i];
                if(myModel.tumorCells.pops[i]>1){
                    popsToInteract-=Interact(i,popsToInteract);
                }
                double totalPop=0;
                Arrays.fill(popList,0);
                for(int j=0;j<VNHood.length;j++){
                    int lookX=VNHood.Xsq(j)+x;
                    int lookY=VNHood.Ysq(j)+y;
                    if(myModel.WithinGrid(lookX,lookY)&&myModel.tumorCells.pops[I(lookX,lookY)]>1){
                        popList[j]=myModel.tumorCells.pops[j];
                        totalPop+=myModel.totalPops[j];
                    }
                }
                //interaction with tumor cells on other squares
                double interacted=0;
                for(int j=0;j<VNHood.length;j++){
                    if(popList[j]>0){
                        interacted+=Interact(I(VNHood.Xsq(j)+x,VNHood.Ysq(j)+y),popsToInteract*(popList[j]/totalPop));
                    }
                }
                popsToInteract-=interacted;
                //cell migration
                MigrateTCells(myModel,swap,x,y,popsToInteract*TCELL_MOVE_RATE,VNHoodwOrigin,popList,cellSize);
            }
        }
    }
    void Draw() {
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                myVis.SetHeat(x, y, (pops[I(x, y)]*cellSize) / MAX_POP);
            }
        }
    }
}
