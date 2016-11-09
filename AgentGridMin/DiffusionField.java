package AgentGridMin;

import java.util.Arrays;

/**
 * Created by bravorr on 10/21/16.
 */
public class DiffusionField {

    final public int xDim;
    final public int yDim;
    final public Visualizer vis;
    public double[] field;
    public double[] swap;
    public DiffusionField(int x,int y, Visualizer vis){
        this.vis = vis;
        xDim=x;
        yDim=y;
        field=new double[xDim*yDim];
        swap=new double[xDim*yDim];
    }
    public double Get(int x,int y) {
        return field[x*yDim+y];
    }
    public void Set(int x,int y,double val){
        field[x*yDim+y]=val;
    }
    public double GetSwap(int x,int y){
        return swap[x*yDim+y];
    }
    public void SetSwap(int x,int y,double val){
        swap[x*yDim+y]=val;
    }
    public void Diffuse(double diffRate,boolean boundaryCond,double boundaryValue,boolean wrapX){
        Utils.Diffusion(field,swap,xDim,yDim,diffRate,boundaryCond,boundaryValue,wrapX);
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

    public void DrawField(){
        //Now draw the fields
        for (int x = 0; x<xDim; x++)
        {
            for (int y=0; y<yDim; y++)
            {
                //vis.SetHeat(x, y, 10*Get(x,y));
                float f = (float) Get(x,y);
                f *= 100.;
                if (f > 1.0)
                {
                    f = 1;
                }
                if(vis!=null) {
                    vis.Set(x, y, f, 0, 0);
                }
            }
        }
    }
}
