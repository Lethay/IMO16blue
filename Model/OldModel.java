package Model;

/**
 * Created by rafael on 11/8/16.
 */
public class OldModel {
//    package Model;
//import AgentGridMin.*;
//import org.apache.commons.math3.distribution.AbstractIntegerDistribution.*;
//
//import java.util.Arrays;
//import java.util.Random;
//
//import static Model.CONST.*;
//
//    /**
//     * Created by rafael on 11/8/16.
//     */
//
//    final class CONST{
//        static final double MAX_POP=10000;
//        static final double TIME_STEP=0.1;//in days
//        static final double NORMAL_MAX_RATE=0.02;
//        static final double NORMAL_DEATH_RATE=0.02;
//        static final double NORMAL_HYPOXIC_THRESHOLD=0.2;
//        static final double MAX_HYPOXIC_DEATH_RATE=0.5;
//    }

////class NormalCellPop extends CellPop{
////    double ComputePropRate(){
////        //computes growth rate as a proportion of current cell population
////        return NORMAL_MAX_RATE*(1-GetPopAll()/MAX_POP)*TIME_STEP;
////    }
////    void Prolif(){
////        double rate=ComputePropRate();
////        changePop(rate*pop);
////    }
////
////    double Hypoxia(){
////        double o2=myModel.Oxygen.Get(Xsq(),Ysq());
////        if(o2<NORMAL_HYPOXIC_THRESHOLD){
////            return -pop*MAX_HYPOXIC_DEATH_RATE*(1.0-o2/NORMAL_HYPOXIC_THRESHOLD);
////        }
////        return 0;
////    }
////    void Death(){
////        double hypoxicDeath=Hypoxia();
////        changePop(hypoxicDeath+(-pop*NORMAL_DEATH_RATE*TIME_STEP));
////    }
//
////    double Move(double currPop){
////
////    }
//
////    void Step(Visualizer vis){
////        //what do normal cells do?
////        Prolif();
////        Death();
////    }
////}
//
//    class NormalPopDiff extends PopDiff {
//        int coinFlips(double prob,int trials){
//            for(int i=0;i<trials;i++){
//            }
//        }
//        NormalPopDiff(int x,int y,TumorModel model) {
//            super(x,y,model);
//        }
//        double Prolif(int x,int y,double prevVal) {
//            double rate=NORMAL_MAX_RATE*(1-myModel.totalPops[I(x,y)]/MAX_POP)*TIME_STEP;
//            return rate*prevVal;
//        }
//        double Death(double prevVal) {
//            return -NORMAL_DEATH_RATE*prevVal*TIME_STEP;
//        }
//        //    double Move(double prevVal) {
////    }
//        double Step(int x,int y,double pop) {
//            double birthDelta=Prolif(x,y,pop);
//            double deathDelta=Death(pop);
//            return pop+birthDelta+deathDelta;
//        }
//        void Draw(int x,int y,double pop,Visualizer vis){
//            vis.SetHeat(x,y,pop/MAX_POP);
//        }
//    }
//
//    abstract class PopDiff {
//        TumorModel myModel;
//        double[]pops;
//        double[]swap;
//        final int xDim;
//        final int yDim;
//        int tick;
//        PopDiff(int x,int y,TumorModel myModel) {
//            this.myModel=myModel;
//            pops=new double[x*y];
//            swap=new double[x*y];
//            xDim=x;
//            yDim=y;
//            tick=0;
//        }
//        int I(int x,int y) {
//            return x*yDim+y;
//        }
//        boolean WithinGrid(int x,int y) {
//            if(x>=0&&x<xDim&&y>=0&&y<yDim) {
//                return true;
//            }
//            return false;
//        }
//        abstract double Step(int x,int y,double pop);
//        void RunStep() {
//            for(int x=0;x<xDim;x++) {
//                for(int y=0;y<yDim;y++) {
//                    int i=I(x,y);
//                    swap[i]=Step(x,y,pops[i]);
//                }
//            }
//            for(int i=0;i<xDim*yDim;i++){
//                myModel.totalPops[i]+=pops[i];
//            }
//            double[] temp=pops;
//            pops=swap;
//            swap=temp;
//            tick++;
//        }
//        abstract void Draw(int x,int y,double pop,Visualizer vis);
//        void DrawStep(Visualizer vis) {
//            for(int x=0;x<xDim;x++) {
//                for(int y=0;y<yDim;y++) {
//                    int i=I(x,y);
//                    Draw(x,y,pops[i],vis);
//                }
//            }
//        }
//    }
//
////abstract class CellPopAgents extends DiscAgent {
////    double pop;
////    TumorModel myModel;
////
////    //add new population
////    void Init(double pop, TumorModel myModel,Visualizer vis) {
////        this.pop = 0;
////        this.myModel = myModel;
////        changePop(pop);
////    }
////
////    //change population on square, returns total population
////    double changePop(double delta) {
////        this.pop += delta;
////        if (this.pop <= 0) {
////            delta += this.pop;
////            Remove();
////        }
////        int iPop = myModel.Ipop(Xsq(), Ysq());
////        myModel.pops[iPop] += delta;
////        return myModel.pops[iPop];
////    }
////    double GetPopAll(){
////        int iPop=myModel.Ipop(Xsq(),Ysq());
////        return myModel.pops[iPop];
////    }
////
////    //universal draw function, may want to make abstract and implement for each type
////    private void drawFun(Visualizer vis){
////        if(vis!=null) {
////            vis.SetHeat(Xsq(), Ysq(), this.pop / MAX_POP);
////        }
////    }
////
////    //migrates from one position to another
////    void spreadPop(int newX, int newY, double delta,Visualizer vis) {
////        this.changePop(-delta);
////        CellPop toPop = (CellPop) MyGrid().FirstOnSquare(newX, newY);
////        if (toPop == null) {
////            toPop = (CellPop) MyGrid().AddAgent(newX, newY);
////            toPop.Init(delta, myModel,vis);
////        } else {
////            toPop.changePop(delta);
////        }
////    }
//
//    // pop step function
////    abstract void Step(Visualizer vis);
////}
//
//    //gui and visualizer
//    class ModelVis{
//        TumorModel myModel;
//        Visualizer vis;
//        GuiWindow win;
//        ModelVis(TumorModel model){
//            myModel=model;
//            vis=new Visualizer(model.xDim,model.yDim,4);
//            win=new GuiWindow("LungVis",model.xDim*4,model.yDim*4,1,1);
//            win.AddComponent(vis,0,0,1,1);
//        }
//    }
//
//
//    class TumorModel {
//
//        //    AgentGrid<NormalCellPop> normalCells;
//        PopDiff normalCells;
//        DiffusionField Oxygen;
//        Random rand;
//        double[] totalPops;
//        int xDim;
//        int yDim;
//
//        void InitModel(Visualizer vis) {
//            //how to init?
//            rand = new Random();
//            for (int x = 0; x < xDim; x++) {
//                for (int y = 0; y < xDim; y++) {
//                    normalCells.pops[normalCells.I(x,y)]=rand.nextDouble()*MAX_POP;
////                NormalCellPop firstCells = normalCells.AddAgent(x, y);
////                firstCells.Init(rand.nextDouble() * MAX_POP, this, vis);
//                }
//            }
//            Oxygen.SetAll(0.5);
//        }
//
//        TumorModel(int x, int y) {
//            normalCells=new NormalPopDiff(x,y,this);
////        normalCells = new AgentGrid<NormalCellPop>(x, y, NormalCellPop.class);
//            Oxygen=new DiffusionField(x,y);
//            totalPops = new double[x * y];
//            xDim = x;
//            yDim = y;
//        }
//
//        int I(int x, int y) {
//            return x * yDim + y;
//        }
//
////    void RunStep(Visualizer vis) {
////        CellPop currPop = normalCells.FirstAgent();
////        while (currPop != null) {
////            currPop.Step(vis);
////            currPop = normalCells.NextAgent();
////        }
////        System.out.println("Day:"+normalCells.Tick()*TIME_STEP);
////        normalCells.IncTick();
////        normalCells.Clean();
////        normalCells.ShuffleAgents(rand);
////    }
//    }
//
//    public class Model{
//        public static void main(String[] args){
//            TumorModel firstPop=new TumorModel(100,100);
//            ModelVis mainWindow=new ModelVis(firstPop);
//            firstPop.InitModel(mainWindow.vis);
//            while(true){
//                Arrays.fill(firstPop.totalPops,0);
//                firstPop.normalCells.RunStep();
//                firstPop.normalCells.DrawStep(mainWindow.vis);
//                System.out.println("Day: "+firstPop.normalCells.tick*TIME_STEP);
//            }
//        }
//    }
}
