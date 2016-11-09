package AgentGridMin;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created by rafael on 10/10/16.
 */
public class AgentGrid <T extends DiscAgent>{
    Constructor<?> builder;
    ArrayList<T> agents;
    ArrayList<T> deads;
    int iLastAlive;
    public final int xDim;
    public final int yDim;
    public final int length;
    T[] grid;
    int[] counts;
    int pop;
    int iAgent;
    int tick;

    public AgentGrid(int x,int y, Class<T> type){
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
        length=x*y;
        agents=new ArrayList<T>();
        grid=(T[])new DiscAgent[length];
        counts= new int[length];
        iLastAlive =-1;
        deads=new ArrayList<T>();
    }
    public int IGrid(int x, int y){
        //gets grid index from location
                return x*yDim+y;
    }
    public int ItoX(int i){
        return i/yDim;
    }
    public int ItoY(int i){
        return i%xDim;
    }
    public int IGrid(double x, double y){
        //gets grid index from location
                return (int)Math.floor(x)*yDim+(int)Math.floor(y);
    }
    public boolean WithinGrid(int x, int y){
        if(x>=0&&x<xDim&&y>=0&&y<yDim){
            return true;
        }
        return false;
    }
    public boolean WithinGrid(double x, double y){
        int xInt=(int)Math.floor(x);
        int yInt=(int)Math.floor(y);
        return WithinGrid(xInt,yInt);
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
    void RemAgentFromSquareFast(T agent,int iGrid){
        grid[iGrid]=null;
        counts[iGrid]--;
    }
    void AddAgentToSquareFast(T agent,int iGrid){
        if(counts[iGrid]!=0){
            throw new RuntimeException("Adding multiple agents on the same square!");
        }
       grid[iGrid]=agent;
        counts[iGrid]++;
    }
    T GetNewAgent(){
        T newAgent;
        //internal function, inserts agent into AgentGridMin.AgentGrid
        if(deads.size()>0){
            newAgent=deads.remove(deads.size()-1);
        }
        else if(agents.size()>iLastAlive+1){
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
    public T AddAgent(int x,int y){
        T newAgent=GetNewAgent();
        newAgent.Setup(x,y);
        return newAgent;
    }
    public T AddAgentFast(int x,int y){
        T newAgent=GetNewAgent();
        newAgent.SetupFast(x,y);
        return newAgent;

    }
    public T AddAgent(double x,double y){
        T newAgent=GetNewAgent();
        newAgent.Setup(x,y);
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
    public void ShuffleAgents(Random rn){
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
                        nextDead.iList=iLastAlive;
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
        //if(iLastAlive +1!=agents.size()) {
        //    agents.subList(iLastAlive + 1, agents.size()).clear();
        //}
        deads.clear();
    }
    public int PopOnSquare(int x, int y){
        //gets population count at location
        return counts[IGrid(x,y)];
    }
    public void SquaresWithPop(SqList coordsToSearch,int minAgents,int maxAgents){
        //modifies list in place to contain coords that have a number of agents between min and max
        //returns the number of matching coords found.
        int nFound=0;
        int searchLen=coordsToSearch.length;
        for(int i=0;i<searchLen;i++){
            int lookX=coordsToSearch.Xsq(i);
            int lookY=coordsToSearch.Ysq(i);
            int popFound= PopOnSquare(lookX,lookY);
            if(popFound>=minAgents&&popFound<=maxAgents){
                coordsToSearch.Set(nFound,lookX,lookY);
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
    public T FirstOnSquare(int x, int y){
        return grid[IGrid(x,y)];
    }

    public void OnSquare(ArrayList<T>putHere,int x,int y){
        T agent= FirstOnSquare(x,y);
        while(agent!=null){
            putHere.add(agent);
            agent=(T)agent.nextSq;
        }
    }
    public int PopOnSquares(SqList squares,int xOffset,int yOffset){
        int count=0;
        for(int i=0;i<squares.length;i++){
            int x=squares.Xsq(i)+xOffset;
            int y=squares.Ysq(i)+yOffset;
            if(WithinGrid(x,y)){
                count+=PopOnSquare(x,y);
            }
        }
        return count;
    }
    public void Clear(){
        for(int i=0;i<=iLastAlive;i++){
            T curr=agents.get(i);
            if(curr.alive){
                curr.Remove();
            }
        }
    }
    public int Pop(){
        //gets population
        return pop;
    }
}