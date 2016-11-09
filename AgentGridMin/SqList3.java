package AgentGridMin;

/**
 * Created by bravorr on 10/27/16.
 */
public class SqList3 {
    public int[] data; // coordinate values
    public final int capacity; //finite capacity that it can hold
    public int length; // number of coordinates you intend to score (change this to change number of coordinates stored)

    // passing value that it can hold, constructor if given a number
    public SqList3(int capacity){
        this.data=new int[capacity*3];
        this.capacity=capacity;
        this.length=capacity;
    }
    // constructor if you give it an array of values x,y,x,y
    public SqList3(int[] points){
        this.data=points;
        this.length=points.length/3;
        this.capacity=points.length/3;
    }
    // pulls x and y values;
    public int Xsq(int i){
        return data[i*3];
    }
    public int Ysq(int i){
        return data[i*3+1];
    }
    public int Zsq(int i){
        return data[i*3+2];
    }
    // sets the values;
    public void SetX(int i,int xSq){
        data[i*3]=xSq;
    }
    public void SetY(int i,int ySq){
        data[i*3+1]=ySq;
    }
    public void SetZ(int i,int zSq){
        data[i*3+2]=zSq;
    }
    public void Set(int i,int xSq,int ySq,int zSq){
        data[i*3]=xSq;
        data[i*3+1]=ySq;
        data[i*3+2]=zSq;
    }
}
