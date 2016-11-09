package AgentGridMin;

/**
 * Created by bravorr on 10/27/16.
 */
public class ContAgent3 extends DiscAgent3{
    private double xPos;
    private double yPos;
    private double zPos;
    //    public ContAgent(AgentGrid inGrid,double x,double y){
//        xPos=x;
//        yPos=y;
//        this.xSq=(int)Math.floor(x);
//        this.ySq=(int)Math.floor(y);
//    }
    public void Setup(double xPos,double yPos,double zPos){
        this.xPos=xPos;
        this.yPos=yPos;
        this.zPos=zPos;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xPos,yPos,zPos));
    }
    public void Setup(int xPos,int yPos,int zPos){
        this.xPos=xPos+0.5;
        this.yPos=yPos+0.5;
        this.zPos=zPos+0.5;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xPos,yPos,zPos));
    }
    public void MoveSq(int newX, int newY,int newZ){
        int oldX=(int)xPos;
        int oldY=(int)yPos;
        int oldZ=(int)zPos;
        if(oldX!=newX||oldY!=newY||oldZ!=newZ) {
            myGrid.RemAgentFromSquare(this, myGrid.IGrid(oldX,oldY,oldZ));
            myGrid.AddAgentToSquare(this, myGrid.IGrid(newX, newY,newZ));
        }
        this.xPos=newX+0.5;
        this.yPos=newY+0.5;
        this.zPos=newZ+0.5;
    }
    public void MoveCont(double newX, double newY,double newZ){
        int xIntNew=(int)newX;
        int yIntNew=(int)newY;
        int zIntNew=(int)newZ;
        int xIntOld=(int)xPos;
        int yIntOld=(int)yPos;
        int zIntOld=(int)zPos;
        if(xIntNew!=xIntOld||yIntNew!=yIntOld||zIntNew!=zIntOld) {
            myGrid.RemAgentFromSquare(this, myGrid.IGrid(xIntOld,yIntOld,zIntOld));
            myGrid.AddAgentToSquare(this, myGrid.IGrid(xIntNew,yIntNew,zIntNew));
        }
        xPos=newX;
        yPos=newY;
    }
    public double Xpos(){
        return xPos;
    }
    public double Ypos(){
        return yPos;
    }
    public double Zpos(){
        return zPos;
    }
    public int Xsq(){
        return (int)xPos;
    }
    public int Ysq(){
        return (int)yPos;
    }
    public int Zsq(){
        return (int)zPos;
    }
}
