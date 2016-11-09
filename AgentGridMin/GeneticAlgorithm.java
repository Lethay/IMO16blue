package AgentGridMin;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rafael on 11/2/16.
 */
class MutantRunner<T extends Mutateable> implements Runnable{
    T mutant;
    double score;
    public MutantRunner(T myMutant){
        mutant=myMutant;
    }
    public void run(){
        score=mutant.Score();
    }
}

public class GeneticAlgorithm  <T extends Mutateable> implements Sortable {
    ArrayList<T> agents;
    ArrayList<Double> scores;
    QuickSorter<GeneticAlgorithm<T>> Sorter;
    Random rn;
    T progenitor;
    String timeStamp;
    int generation;
    public GeneticAlgorithm(T progenitor){
        Sorter=new QuickSorter<GeneticAlgorithm<T>>();
        rn=new Random();
        this.progenitor=progenitor;
        agents=new ArrayList<T>();
        scores=new ArrayList<Double>();
    }
    public double Compare(int iFirst, int iSecond) {
        return scores.get(iFirst)-scores.get(iSecond);
    }

    public void Swap(int iFirst, int iSecond) {
        T temp=agents.get(iFirst);
        double tempScore=scores.get(iFirst);
        agents.set(iFirst,agents.get(iSecond));
        scores.set(iFirst,scores.get(iSecond));
        agents.set(iSecond,temp);
        scores.set(iSecond,tempScore);
    }

    public int Length() {
        return agents.size();
    }
    public void CreateStartingPop(T progenitor,int num){
        agents.clear();
        for(int i=0;i<num;i++){
            T addMe= (T) progenitor.Copy();
            addMe.Randomize();
            agents.add(addMe);
        }
    }
    double RunSortPop(int nThreads,int iStart){
        if(iStart<scores.size()) {
            scores.subList(iStart, scores.size()).clear();
        }
        int len=agents.size();
        ArrayList<MutantRunner<T>> runners=new ArrayList<MutantRunner<T>>();
        ArrayList<Thread> threads=new ArrayList<Thread>();
        ExecutorService executor= Executors.newFixedThreadPool(nThreads);
        for(int i=iStart;i<agents.size();i++){
            MutantRunner<T> runner=new MutantRunner<T>(agents.get(i));
            runners.add(runner);
            threads.add(new Thread(runner));
        }
        //make threads and runners
        for(int i=0;i<threads.size();i++){
            executor.execute(threads.get(i));
        }
        executor.shutdown();
        while(!executor.isTerminated());
        //wait for threads to join
        for(int i=0;i<runners.size();i++){
            scores.add(runners.get(i).score);
        }
        Sorter.QuickSort(this);
        //score agents and sort
        System.out.println("generation: "+generation+"HighScore: "+scores.get(0));
        for(int i=0;i<agents.size();i++){
            agents.get(i).SaveInfo(timeStamp,generation,i,scores.get(i));
        }
        //save info
        generation++;
        return scores.get(0);
    }
    void MutatePop(int bestToKeep,double mutationMag,int nToRecombine,int len){
        agents.subList(bestToKeep,agents.size()).clear();
        for(int i=0;i<nToRecombine;i++){
            //recombine some
            int[] indices=Utils.RandomList(bestToKeep,2,rn);
            T parent1=agents.get(indices[0]);
            T parent2=agents.get(indices[1]);
            T child=(T)parent1.Recombine(parent2);
            child.Mutate(mutationMag);
            agents.add(child);
        }
        for(int i=0;i<len-(bestToKeep+nToRecombine);i++){
            //mutate clones to fill the rest
            int parentToCopy=i%bestToKeep;
            T child=(T)agents.get(parentToCopy).Copy();
            child.Mutate(mutationMag);
            agents.add(child);
        }
    }
    public String[] RunAlgo(int nGenerations,int popSize,int bestToKeep,double mutationMag,int nToRecombine,boolean rerunAll){
        generation=0;
        timeStamp=Utils.TimeStamp();
        if(bestToKeep+nToRecombine>popSize){
            throw new IllegalArgumentException("the population must be big enough to store the next generation!");
        }
        CreateStartingPop(progenitor,popSize);
        RunSortPop(4,0);
        int iStart=rerunAll?0:bestToKeep;
        for(int i=1;i<nGenerations-1;i++){
            RunSortPop(4,iStart);
            MutatePop(bestToKeep,mutationMag,nToRecombine,popSize);
        }
        double finalScore=RunSortPop(4,iStart);
        return new String[]{timeStamp,Double.toString(finalScore)};
    }
}
