package AgentGridMin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by bravorr on 10/27/16.
 */
public class AgentGrid3 <T extends DiscAgent3>{
        Constructor<?> builder;
        static Random rn=new Random();
        ArrayList<T> agents;
        ArrayList<T> deads;
        int iLastAlive;
        public final int xDim;
        public final int yDim;
        public final int zDim;
        public final int length;
        T[] grid;
        int[] counts;
        int pop;
        int iAgent;
        int tick;

        public AgentGrid3(int x,int y,int z, Class<T> type){
            this.builder=type.getDeclaredConstructors()[0];
            this.builder.setAccessible(true);
//        try {
//            this.builder = type.getConstructor();
//        }
//        catch(Exception e){
//            throw new RuntimeException("no constructor!");
//
//        }
            //creates a new grid with given dimensions
            xDim=x;
            yDim=y;
            zDim=z;
            length=x*y*z;
            agents=new ArrayList<T>();
            grid=(T[])new DiscAgent3[length];
            counts= new int[length];
            iLastAlive =-1;
            deads=new ArrayList<T>();
        }
        public int IGrid(int x, int y,int z){
            //gets grid index from location
            return x*yDim*zDim+y*zDim+z;
        }
        public int ItoX(int i){
            return i/yDim*zDim;
        }
        public int ItoY(int i){
            return (i/zDim)%xDim;
        }
        public int ItoZ(int i){
            return i%yDim;
        }
        public int IGrid(double x, double y,double z){
            //gets grid index from location
            return (int)Math.floor(x)*yDim*zDim+(int)Math.floor(y)*yDim+(int)Math.floor(z);
        }
        public boolean WithinGrid(int x, int y,int z){
            if(x>=0&&x<xDim&&y>=0&&y<yDim&&z>=0&&z<zDim){
                return true;
            }
            return false;
        }
        public boolean WithinGrid(double x, double y,double z){
            int xInt=(int)Math.floor(x);
            int yInt=(int)Math.floor(y);
            int zInt=(int)Math.floor(z);
            return WithinGrid(xInt,yInt,zInt);
        }
        public int Tick(){return tick;}
        public void IncTick(){tick++;}
        void AddAgentToSquare(T agent,int iGrid){
            //internal function, adds agent to grid square
            if(grid[iGrid]==null) {
                grid[iGrid]=agent;
            }
            else{
                grid[iGrid].prevSq=agent;
                agent.nextSq=grid[iGrid];
                grid[iGrid]=agent;
            }
            counts[iGrid]++;
        }
        void RemAgentFromSquare(T agent,int iGrid){
            //internal function, removes agent from grid square
            if(grid[iGrid]==agent){
                grid[iGrid]=(T)agent.nextSq;
            }
            if(agent.nextSq!=null){
                agent.nextSq.prevSq=agent.prevSq;
            }
            if(agent.prevSq!=null){
                agent.prevSq.nextSq=agent.nextSq;
            }
            agent.prevSq=null;
            agent.nextSq=null;
            counts[iGrid]--;
        }
        T GetNewAgent(){
            T newAgent;
            //internal function, inserts agent into AgentGridMin.AgentGrid
            if(deads.size()>0){
                newAgent=deads.remove(deads.size()-1);
            }
            else if(agents.size()<iLastAlive+1){
                iLastAlive++;
                newAgent=agents.get(iLastAlive);
            }
            else {
                try {
                    newAgent = (T)builder.newInstance();
                }
                catch (Exception e){
                    throw new RuntimeException("Could not instantiate");
                }
                agents.add(newAgent);
                newAgent.myGrid=this;
                iLastAlive++;
                newAgent.iList=iLastAlive;
                //agent.iList= iLastAlive;
            }
            newAgent.alive=true;
            newAgent.birthTick=this.tick;
            pop++;
            return newAgent;
        }
        public T AddAgent(int x,int y,int z){
            T newAgent=GetNewAgent();
            newAgent.Setup(x,y,z);
            return newAgent;
        }
        public T AddAgent(double x,double y,double z){
            T newAgent=GetNewAgent();
            newAgent.Setup(x,y,z);
            return newAgent;
        }
        void RemoveAgent(T agent,int iGrid){
            //internal function, removes agent from world
            agent.alive=false;
            RemAgentFromSquare(agent, iGrid);
            deads.add(agent);
            pop--;
        }
        //    public void TransplantAgent(T agent){
//        //moves agent from one grid to another
//        if(agent.myGrid==this){
//            return;
//        }
//        agent.myGrid.RemoveAgent(agent);
//        AddAgent(agent);
//    }
        public void ShuffleAgents(){
            //shuffles the AgentGridMin.AgentGrid agents list (Don't run during agent iteration)
            for(int iSwap1 = iLastAlive; iSwap1>0; iSwap1--){
                int iSwap2=rn.nextInt(iSwap1+1);
                T swap1=agents.get(iSwap1);
                T swap2=agents.get(iSwap2);
                swap1.iList = iSwap2;
                swap2.iList = iSwap1;
                agents.set(iSwap2,swap1);
                agents.set(iSwap1,swap2);
            }
        }
        public void Clean(){
            //cleans the grid by removing dead agents (Don't run during agent iteration)
            //may need to double check implementation!!
            int iNext=0;
            while(iNext< iLastAlive){
                T nextDead=agents.get(iNext);
                if(nextDead.alive==false){
                    while(iLastAlive >=iNext) {
                        T subAgent=agents.get(iLastAlive);
                        if(subAgent.alive==true){
                            subAgent.iList=iNext;
                            agents.set(iNext,subAgent);
                            agents.set(iLastAlive,nextDead);
                            iLastAlive--;
                            iNext++;
                            break;
                        }
                        iLastAlive--;
                    }
                }
                else{
                    iNext++;
                }
            }
            if(iLastAlive +1!=agents.size()) {
                agents.subList(iLastAlive + 1, agents.size()).clear();
            }
            deads.clear();
        }
        public int PopOnSquare(int x, int y,int z){
            //gets population count at location
            return counts[IGrid(x,y,z)];
        }
        public void SquaresWithPop(SqList3 coordsToSearch,int minAgents,int maxAgents){
            //modifies list in place to contain coords that have a number of agents between min and max
            //returns the number of matching coords found.
            int nFound=0;
            int searchLen=coordsToSearch.length;
            for(int i=0;i<searchLen;i++){
                int lookX=coordsToSearch.Xsq(i);
                int lookY=coordsToSearch.Ysq(i);
                int lookZ=coordsToSearch.Zsq(i);
                int popFound= PopOnSquare(lookX,lookY,lookZ);
                if(popFound>=minAgents&&popFound<=maxAgents){
                    coordsToSearch.Set(nFound,lookX,lookY,lookZ);
                    nFound++;
                }
            }
            coordsToSearch.length=nFound;
        }
        public T FirstAgent(){
            //use before a while loop to begin iterating over all agents (advances age of agents)
            iAgent=0;
            return NextAgent();
        }
        public T NextAgent(){
            //use within a while loop that exits when the returned agent is null to iterate over all agents (advances age of agents)
            while(iAgent<=iLastAlive) {
                T possibleRet=agents.get(iAgent);
                iAgent += 1;
                if (possibleRet != null && possibleRet.alive && possibleRet.birthTick != tick) {
                    return possibleRet;
                }
            }
            return null;
        }
        public List<T> AllAgents(){
            //returns an unmodifiable list of all agents, for use in iterating over all agents
            return Collections.unmodifiableList(agents);
        }
        public T FirstOnSquare(int x, int y,int z){
            return grid[IGrid(x,y,z)];
        }

        public void OnSquare(ArrayList<T>putHere,int x,int y,int z){
            T agent= FirstOnSquare(x,y,z);
            while(agent!=null){
                putHere.add(agent);
                agent=(T)agent.nextSq;
            }
        }
        public int PopOnSquares(SqList3 squares,int xOffset,int yOffset,int zOffset){
            int count=0;
            for(int i=0;i<squares.length;i++){
                int x=squares.Xsq(i)+xOffset;
                int y=squares.Ysq(i)+yOffset;
                int z=squares.Zsq(i)+zOffset;
                if(WithinGrid(x,y,z)){
                    count+=PopOnSquare(x,y,z);
                }
            }
            return count;
        }
        public int Pop(){
            //gets population
            return pop;
        }
}
