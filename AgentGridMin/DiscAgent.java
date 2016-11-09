package AgentGridMin;

/**
 * Created by rafael on 10/10/16.
 */


public class DiscAgent {
    int xSq;
    int ySq;
    int birthTick;
    int iList;
    boolean alive;
    DiscAgent nextSq;
    DiscAgent prevSq;
    AgentGrid myGrid;
    void Setup(int xSq,int ySq){
        this.xSq=xSq;
        this.ySq=ySq;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xSq,ySq));
    }
    void SetupFast(int xSq,int ySq){
        this.xSq=xSq;
        this.ySq=ySq;
        myGrid.AddAgentToSquareFast(this,myGrid.IGrid(xSq,ySq));
    }
    void Setup(double xPos,double yPos){
        Setup((int)xPos,(int)yPos);
    }
    public AgentGrid MyGrid(){return myGrid;}
    public boolean Alive(){
        return alive;
    }
    public int Age(){
        return myGrid.tick-birthTick;
    }
    public void MoveSq(int x, int y){
        //moves agent discretely
        int iPrevPos=myGrid.IGrid(xSq,ySq);
        int iNewPos=myGrid.IGrid(x,y);
        myGrid.RemAgentFromSquare(this,iPrevPos);
        myGrid.AddAgentToSquare(this,iNewPos);
        this.xSq=x;
        this.ySq=y;
    }
    public void MoveSqFast(int x,int y){
        int iPrevPos=myGrid.IGrid(xSq,ySq);
        int iNewPos=myGrid.IGrid(x,y);
        myGrid.RemAgentFromSquareFast(this,iPrevPos);
        myGrid.AddAgentToSquareFast(this,iNewPos);
        this.xSq=x;
        this.ySq=y;
    }
    public void MoveCont(double x, double y){
       MoveSq((int)x,(int)y);
    }
    public int Xsq(){
        return xSq;
    }
    public int Ysq(){
        return ySq;
    }
    public double Xpos(){
        return xSq+0.5;
    }
    public double Ypos(){
        return ySq+0.5;
    }
    public void Remove(){
        //kills agent
        myGrid.RemoveAgent(this,myGrid.IGrid(xSq,ySq));
    }
}
