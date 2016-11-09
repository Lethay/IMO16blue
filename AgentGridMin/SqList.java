package AgentGridMin;

/**
 * Created by rafael on 10/23/16.
 */
public class SqList {
    public int[] data; // coordinate values
    public final int capacity; //finite capacity that it can hold
    public int length; // number of coordinates you intend to score (change this to change number of coordinates stored)

    // passing value that it can hold, constructor if given a number
    public SqList(int capacity){
        this.data=new int[capacity*2];
        this.capacity=capacity;
        this.length=capacity;
    }
    // constructor if you give it an array of values x,y,x,y
    public SqList(int[] points){
        this.data=points;
        this.length=points.length/2;
        this.capacity=points.length/2;
    }
    // pulls x and y values;
    public int Xsq(int i){
        return data[i*2];
    }
    public int Ysq(int i){
        return data[i*2+1];
    }
    // sets the values;
    public void SetX(int i,int xSq){
        data[i*2]=xSq;
    }
    public void SetY(int i,int ySq){
        data[i*2+1]=ySq;
    }
    public void Set(int i,int xSq,int ySq){
        data[i*2]=xSq;
        data[i*2+1]=ySq;
    }
}
