package AgentGridMin;
//
///**
// * Created by bravorr on 10/19/16.
// */
public class ContAgent extends DiscAgent{
    private double xPos;
    private double yPos;
//    public ContAgent(AgentGrid inGrid,double x,double y){
//        xPos=x;
//        yPos=y;
//        this.xSq=(int)Math.floor(x);
//        this.ySq=(int)Math.floor(y);
//    }
    public void Setup(double xPos,double yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xPos,yPos));
    }
    public void Setup(int xPos,int yPos){
        this.xPos=xPos+0.5;
        this.yPos=yPos+0.5;
        myGrid.AddAgentToSquare(this,myGrid.IGrid(xPos,yPos));
    }
    public void MoveSq(int newX, int newY){
        int oldX=(int)xPos;
        int oldY=(int)yPos;
        if(oldX!=newX||oldY!=newY) {
            myGrid.RemAgentFromSquare(this, myGrid.IGrid(oldX,oldY));
            myGrid.AddAgentToSquare(this, myGrid.IGrid(newX, newY));
        }
        this.xPos=newX+0.5;
        this.yPos=newY+0.5;
    }
    public void MoveCont(double newX, double newY){
        int xIntNew=(int)newX;
        int yIntNew=(int)newY;
        int xIntOld=(int)xPos;
        int yIntOld=(int)yPos;
        if(xIntNew!=xIntOld||yIntNew!=yIntOld) {
            myGrid.RemAgentFromSquare(this, myGrid.IGrid(xIntOld,yIntOld));
            myGrid.AddAgentToSquare(this, myGrid.IGrid(xIntNew,yIntNew));
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
    public int Xsq(){
        return (int)xPos;
    }
    public int Ysq(){
        return (int)yPos;
    }
}
