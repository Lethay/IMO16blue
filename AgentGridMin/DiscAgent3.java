package AgentGridMin;

/**
 * Created by bravorr on 10/27/16.
 */
public class DiscAgent3{
    int xSq;
    int ySq;
    int zSq;
    int birthTick;
    int iList;
    boolean alive;
    DiscAgent3 nextSq;
    DiscAgent3 prevSq;
    AgentGrid3 myGrid;
    public void Setup(int xSq,int ySq,int zSq){
        this.xSq=xSq;
        this.ySq=ySq;
        this.zSq=zSq;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xSq,ySq,zSq));
    }
    public void Setup(double xPos,double yPos,double zPos){
        Setup((int)xPos,(int)yPos,(int)zPos);
    }
    public AgentGrid3 MyGrid(){return myGrid;}
    public boolean Alive(){
        return alive;
    }
    public int Age(){
        return myGrid.tick-birthTick;
    }
    public void MoveSq(int x, int y,int z){
        //moves agent discretely
        int iPrevPos=myGrid.IGrid(xSq,ySq,zSq);
        int iNewPos=myGrid.IGrid(x,y,z);
        myGrid.RemAgentFromSquare(this,iPrevPos);
        myGrid.AddAgentToSquare(this,iNewPos);
        this.xSq=x;
        this.ySq=y;
        this.zSq=z;
    }
    public void MoveCont(double x, double y,double z){
        MoveSq((int)x,(int)y,(int)z);
    }
    public int Xsq(){
        return xSq;
    }
    public int Ysq(){
        return ySq;
    }
    public int Zsq(){
        return zSq;
    }
    public double Xpos(){
        return xSq+0.5;
    }
    public double Ypos(){
        return ySq+0.5;
    }
    public void Remove(){
        //kills agent
        myGrid.RemoveAgent(this,myGrid.IGrid(xSq,ySq,zSq));
    }
}
