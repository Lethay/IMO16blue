package AgentGridMin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by rafael on 10/11/16.
 */
public final class Utils {

    public static int CoinFlip(double prob,int trials,Random rn){
        int heads=0;
        for(int i=0;i<trials;i++){
            if(rn.nextDouble()<prob){
                heads++;
            }
        }
        return heads;
    }

    public static double Gaussian(double mean,double stdDev,Random rn){
        return rn.nextGaussian()*stdDev+mean;
    }
    public static SqList GenMooreNeighborhood(){
        return new SqList(new int[]{1,1,1,0,1,-1,0,-1,-1,-1,-1,0,-1,1,0,1});
    }
    public static SqList GenMooreNeighborhoodWithOrigin() {
        return new SqList(new int[]{0, 0, 1, 1, 1, 0, 1, -1, 0, -1, -1, -1, -1, 0, -1, 1, 0, 1});
    }
    public static int RandomVariable(double[]probs,Random rn){
        double rand=rn.nextDouble();
        for(int i=0;i<probs.length;i++){
            rand-=probs[i];
            if(rand<0){
                return i;
            }
        }
        return -1;
    }
    public static void SumTo1(double[] vals){
        double tot=0;
        for(int i=0;i<vals.length;i++){
            tot+=vals[i];
        }
        for(int i=0;i<vals.length;i++){
            vals[i]=vals[i]/tot;
        }
    }

    public static SqList GenVonNeumannNeighborhood(){
        return new SqList(new int[]{1,0,-1,0,0,1,0,-1});
    }
    public static SqList3 GenVonNeumannNeighborhood3(){
        SqList3 ret=new SqList3(6);
        ret.Set(0,1,0,0);
        ret.Set(1,-1,0,0);
        ret.Set(2,0,1,0);
        ret.Set(3,0,-1,0);
        ret.Set(4,0,0,1);
        ret.Set(5,0,0,-1);
        return ret;
    }
    public static SqList GenVonNeumannNeighborhoodWithOrigin(){
        return new SqList(new int[]{0,0,1,0,-1,0,0,1,0,-1});
    }

    public static double MichaelisMenten(final double conc,final double maxRate,final double halfRateConc){
        if(conc>0) {
            return -(maxRate * conc) / (halfRateConc + conc);
        }
        return 0;
    }
    public static void ShuffleInts(int[] arr,int lenToShuffle,int Count,Random rn){
        for(int i=0;i<Count;i++){
            int iSwap=rn.nextInt(lenToShuffle-i)+i;
            int swap=arr[iSwap];
            arr[iSwap]=arr[i];
            arr[i]=swap;
        }
    }
    public static void SquaresAlongLine(SqList writeHere,double x0,double y0,double x1,double y1){
        double dx = Math.abs(x1 - x0);
        double dy = Math.abs(y1 - y0);

        int x = (int)(Math.floor(x0));
        int y = (int)(Math.floor(y0));

        int n = 1;
        int x_inc, y_inc;
        double error;

        if (dx == 0)
        {
            x_inc = 0;
            error = Double.MAX_VALUE;
        }
        else if (x1 > x0)
        {
            x_inc = 1;
            n += (int)(Math.floor(x1)) - x;
            error = (Math.floor(x0) + 1 - x0) * dy;
        }
        else
        {
            x_inc = -1;
            n += x - (int)(Math.floor(x1));
            error = (x0 - Math.floor(x0)) * dy;
        }

        if (dy == 0)
        {
            y_inc = 0;
            error -= Double.MAX_VALUE;
        }
        else if (y1 > y0)
        {
            y_inc = 1;
            n += (int)(Math.floor(y1)) - y;
            error -= (Math.floor(y0) + 1 - y0) * dx;
        }
        else
        {
            y_inc = -1;
            n += y - (int)(Math.floor(y1));
            error -= (y0 - Math.floor(y0)) * dx;
        }

        int Count=0;
        if(n>writeHere.capacity){
            System.out.println(n);
        }
        for (; n > 0; --n)
        {
            writeHere.Set(Count,(int)Math.floor(x),(int)Math.floor(y));
            Count++;

            if (error > 0)
            {
                y += y_inc;
                error -= dx;
            }
            else
            {
                x += x_inc;
                error += dy;
            }
        }
        writeHere.length=Count;
    }
    public static SqList SquaresAlongLine(double x0,double y0, double x1,double y1){
            double dx = Math.abs(x1 - x0);
            double dy = Math.abs(y1 - y0);

            int x = (int)(Math.floor(x0));
            int y = (int)(Math.floor(y0));

            int n = 1;
            int x_inc, y_inc;
            double error;

            if (dx == 0)
            {
                x_inc = 0;
                error = Double.MAX_VALUE;
            }
            else if (x1 > x0)
            {
                x_inc = 1;
                n += (int)(Math.floor(x1)) - x;
                error = (Math.floor(x0) + 1 - x0) * dy;
            }
            else
            {
                x_inc = -1;
                n += x - (int)(Math.floor(x1));
                error = (x0 - Math.floor(x0)) * dy;
            }

            if (dy == 0)
            {
                y_inc = 0;
                error -= Double.MAX_VALUE;
            }
            else if (y1 > y0)
            {
                y_inc = 1;
                n += (int)(Math.floor(y1)) - y;
                error -= (Math.floor(y0) + 1 - y0) * dx;
            }
            else
            {
                y_inc = -1;
                n += y - (int)(Math.floor(y1));
                error -= (y0 - Math.floor(y0)) * dx;
            }

            int Count=0;
        SqList ret=new SqList(n);
            for (; n > 0; --n)
            {
                ret.Set(Count,(int)Math.floor(x),(int)Math.floor(y));
                Count++;

                if (error > 0)
                {
                    y += y_inc;
                    error -= dx;
                }
                else
                {
                    x += x_inc;
                    error += dy;
                }
            }
        return ret;
        }

    public static int[] RandomList(int nEntries,int CountRandom,Random rn){
        int indices[]=new int[nEntries];
        for(int i=0;i<nEntries;i++){
            indices[i]=i;
        }
        ShuffleInts(indices,indices.length,CountRandom,rn);
        return indices;
    }
    public static double DistSq(double x1,double y1,double x2,double y2){
        double xDist=x2-x1,yDist=y2-y1;
        return xDist*xDist+yDist*yDist;
    }
    static public SqList CoordsWithinRad(double radius){
        double distSq=radius*radius;
        int min=(int)Math.floor(-radius);
        int max=(int)Math.ceil(radius);
        SqList ret=new SqList(((max+1-min)*(max+1-min)));
        int len=0;
        for(int x=min;x<=max;x++){
            for(int y=min;y<=max;y++){
                if(AgentGridMin.Utils.DistSq(0,0,x,y)<=distSq){
                    ret.Set(len,x,y);
                    len++;
                }
            }
        }
        ret.length=len;
        return ret;
    }

    static public double Avg(double[] stuff){
        double tot=0;
        for(int i=0;i<stuff.length;i++){
            tot+=stuff[i];
        }
        return tot/stuff.length;
    }

    public static double BoundVal(double val,double min,double max){
        return Math.min(Math.max(val,min),max);
    }
    public static float BoundVal(float val,double min,double max){
        return (float)Math.min(Math.max((double)val,min),max);
    }
    public static double ScaleVal(double val,double min,double max){
        return (val-min)/(max-min);
    }
    public static int ModWrap(int val,int max) {
        if (val < 0) {
            return max + val % max;
        } else {
            return val % max;
        }
    }
    static public String TimeStamp(){
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

    }
    static public SqList SquareAroundZero(int radius){
        //returns a square with a center location at 0,0
        SqList dataIn=new SqList((radius*2+1)*(radius*2+1));
        int nCoord=0;
        for(int x=-radius;x<=radius;x++){
            for(int y=-radius;y<=radius;y++){
                dataIn.Set(nCoord,x,y);
                nCoord++;
            }
        }
        return dataIn;
    }
    public static double ConvertPhToH(double ph){
        return Math.pow(10,3.0-ph);
    }
    public static double ConvertHToPh(double h){
        return -Math.log10(h)+3;
    }
    public static double ProbScale(double prob, double timeFraction){
        return 1.0f-(Math.pow(1.0-prob,1.0/timeFraction));

    }
    public static void DiffuseNoBoundary(double []inGrid,double[]outGrid,int xDim, int yDim){

    }
    public static void Diffusion(double []inGrid,double[]outGrid,int xDim, int yDim,double DiffRate, boolean BoundaryCond,double BoundaryValue,boolean wrapX){
        //finite differences twice
        //This code is ugly and repetitive to improve performance by getting around bounds checking
        int x,y;
        //first we do the corners
        if(BoundaryCond){
            outGrid[0]=inGrid[0]+DiffRate*(-inGrid[0]*4+inGrid[1]+inGrid[yDim]+2*BoundaryValue);
            outGrid[(xDim-1)*yDim]=inGrid[(xDim-1)*yDim]+DiffRate*(-inGrid[(xDim-1)*yDim]*4+inGrid[(xDim-2)*yDim]+inGrid[(xDim-1)*yDim+1]+2*BoundaryValue);
            outGrid[(xDim-1)*yDim+yDim-1]=inGrid[(xDim-1)*yDim+yDim-1]+DiffRate*(-inGrid[(xDim-1)*yDim+yDim-1]*4+inGrid[(xDim-2)*yDim+yDim-1]+inGrid[(xDim-1)*yDim+yDim-2]+2*BoundaryValue);
            outGrid[yDim-1]=inGrid[yDim-1]+DiffRate*(-inGrid[yDim-1]*4+inGrid[yDim+yDim-1]+inGrid[yDim-2]+2*BoundaryValue);
        }
        else if(wrapX){
            outGrid[0]=inGrid[0]+DiffRate*(-inGrid[0]*3+inGrid[1]+inGrid[yDim]+inGrid[(xDim-1)*yDim]);
            outGrid[(xDim-1)*yDim]=inGrid[(xDim-1)*yDim]+DiffRate*(-inGrid[(xDim-1)*yDim+0]*3+inGrid[(xDim-2)*yDim]+inGrid[(xDim-1)*yDim+1]+inGrid[0]);
            outGrid[(xDim-1)*yDim+yDim-1]=inGrid[(xDim-1)*yDim+yDim-1]+DiffRate*(-inGrid[(xDim-1)*yDim+yDim-1]*3+inGrid[(xDim-2)*yDim+yDim-1]+inGrid[(xDim-1)*yDim+yDim-2]+outGrid[yDim-1]);
            outGrid[yDim-1]=inGrid[yDim-1]+DiffRate*(-inGrid[yDim-1]*3+inGrid[yDim+yDim-1]+inGrid[yDim-2]+outGrid[(xDim-1)*yDim+yDim-1]);
        }
        else{
            outGrid[0]=inGrid[0]+DiffRate*(-inGrid[0]*2+inGrid[1]+inGrid[yDim]);
            outGrid[(xDim-1)*yDim]=inGrid[(xDim-1)*yDim]+DiffRate*(-inGrid[(xDim-1)*yDim+0]*2+inGrid[(xDim-2)*yDim]+inGrid[(xDim-1)*yDim+1]);
            outGrid[(xDim-1)*yDim+yDim-1]=inGrid[(xDim-1)*yDim+yDim-1]+DiffRate*(-inGrid[(xDim-1)*yDim+yDim-1]*2+inGrid[(xDim-2)*yDim+yDim-1]+inGrid[(xDim-1)*yDim+yDim-2]);
            outGrid[yDim-1]=inGrid[yDim-1]+DiffRate*(-inGrid[yDim-1]*2+inGrid[yDim+yDim-1]+inGrid[yDim-2]);
        }
        //then we do the sides
        if(BoundaryCond){
            x=0; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[(x+1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]+BoundaryValue);
            }
            x=xDim-1; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[(x-1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]+BoundaryValue);
            }
            y=0; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[x*yDim+y+1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]+BoundaryValue);
            }
            y=yDim-1; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[x*yDim+y-1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]+BoundaryValue);
            }
        }
        else if(wrapX) {
            x=0; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[(x+1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]+inGrid[(xDim-1)*yDim+y]);
            }
            x=xDim-1; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*4+inGrid[(x-1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]+outGrid[0*yDim+y]);
            }
            y=0; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[x*yDim+y+1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]);
            }
            y=yDim-1; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[x*yDim+y-1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]);
            }
        }
        else {
            x=0; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[(x+1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]);
            }
            x=xDim-1; for(y=1;y<yDim-1;y++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[(x-1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]);
            }
            y=0; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[x*yDim+y+1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]);
            }
            y=yDim-1; for(x=1;x<xDim-1;x++) {
                outGrid[x*yDim+y]=inGrid[x*yDim+y]+DiffRate*(-inGrid[x*yDim+y]*3+inGrid[x*yDim+y-1]+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]);
            }
        }
        //then we do the middle
        for (x = 1; x < xDim-1; x++) {
            for (y = 1; y < yDim-1; y++) {
                int i=x*yDim+y;
                outGrid[i]=inGrid[i]+DiffRate*(-inGrid[i]*4+inGrid[(x+1)*yDim+y]+inGrid[(x-1)*yDim+y]+inGrid[x*yDim+y+1]+inGrid[x*yDim+y-1]);
            }
        }
    }
    //boolean in3(int xDim,int yDim,int zDim,int x,int y,int z){
    //    return x>=0&&x<xDim&&y>=0&&y<yDim&&z>=0&&z<zDim;
    //}
    static boolean inDim(int Dim,int Val){
        return Val>=0&&Val<Dim;
    }
    public static void Diffusion3(double []inGrid,double[]outGrid,int xDim, int yDim,int zDim,double DiffRate, boolean BoundaryCond,double BoundaryValue,boolean WrapXZ) {
        int x,y,z,count;
        double valSum;
        for (x = 0; x < xDim; x++) {
            for (y = 0; y < yDim; y++) {
                for (z = 0; z < zDim; z++) {
                    //6 squares to check
                    count=0;
                    valSum=0;
                    if(inDim(xDim,x+1)){
                        valSum+=inGrid[(x+1)*yDim*zDim+(y)*zDim+(z)];
                        count++;
                    }
                    else if(WrapXZ){
                        valSum+=inGrid[(0)*yDim*zDim+(y)*zDim+(z)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    if(inDim(xDim,x-1)){
                        valSum+=inGrid[(x-1)*yDim*zDim+(y)*zDim+(z)];
                        count++;
                    }
                    else if(WrapXZ){
                        valSum+=inGrid[(xDim-1)*yDim*zDim+(y)*zDim+(z)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    if(inDim(yDim,y+1)){
                        valSum+=inGrid[(x)*yDim*zDim+(y+1)*zDim+(z)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    if(inDim(yDim,y-1)){
                        valSum+=inGrid[(x)*yDim*zDim+(y-1)*zDim+(z)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    if(inDim(zDim,z+1)){
                        valSum+=inGrid[(x)*yDim*zDim+(y)*zDim+(z+1)];
                        count++;
                    }
                    else if(WrapXZ){
                        valSum+=inGrid[(x)*yDim*zDim+(y)*zDim+(0)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    if(inDim(zDim,z-1)){
                        valSum+=inGrid[(x)*yDim*zDim+(y)*zDim+(z-1)];
                        count++;
                    }
                    else if(WrapXZ){
                        valSum+=inGrid[(xDim-1)*yDim*zDim+(y)*zDim+(zDim-1)];
                        count++;
                    }
                    else if(BoundaryCond){
                        valSum+=BoundaryValue;
                        count++;
                    }

                    int i=x*yDim*zDim+y*zDim+z;
                    outGrid[i]=inGrid[i]+DiffRate*(-inGrid[i]*count+valSum);
                }
            }
        }
    }
}

