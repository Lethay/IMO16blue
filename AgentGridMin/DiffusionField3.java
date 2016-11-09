package AgentGridMin;

import java.util.Arrays;

/**
 * Created by bravorr on 10/27/16.
 */
public class DiffusionField3 {
    final public int xDim;
    final public int yDim;
    final public int zDim;
    public double[] field;
    public double[] swap;
    public DiffusionField3(int x,int y,int z){
        xDim=x;
        yDim=y;
        zDim=z;
        field=new double[xDim*yDim*zDim];
        swap=new double[xDim*yDim*zDim];
    }
    public double Get(int x,int y,int z) {
        return field[x*yDim*zDim+y*zDim+z];
    }
    public void Set(int x,int y,int z,double val){
        field[x*yDim*zDim+y*zDim+z]=val;
    }
    public double GetSwap(int x,int y,int z){
        return swap[x*yDim*zDim+y*zDim+z];
    }
    public void SetSwap(int x,int y,int z,double val){
        swap[x*yDim*zDim+y*zDim+z]=val;
    }
    public void Diffuse(double diffRate,boolean boundaryCond,double boundaryValue,boolean WrapXZ){
        Utils.Diffusion3(field,swap,xDim,yDim,zDim,diffRate,boundaryCond,boundaryValue,WrapXZ);
        double[] temp=field;
        field=swap;
        swap=temp;
    }
    public double MaxDiff(boolean scaled){
        double maxDiff=0;
        if(!scaled){
            for(int i=0;i<field.length;i++){
                maxDiff=Math.max(maxDiff,Math.abs(field[i]-swap[i]));
            }
        }else{
            for(int i=0;i<field.length;i++){
                maxDiff=Math.max(maxDiff,Math.abs((field[i]-swap[i])/field[i]));
            }
        }
        return maxDiff;
    }
    public void SetAll(double val){
        Arrays.fill(field,val);
    }
    public double Avg(){
        double tot=0;
        for(int i=0;i<field.length;i++){
            tot+=field[i];
        }
        return tot/field.length;
    }
}
