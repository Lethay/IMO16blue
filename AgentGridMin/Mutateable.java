package AgentGridMin;

/**
 * Created by rafael on 11/2/16.
 */
public interface Mutateable <T>{
    public void Mutate(double magnitude);//create mutated offspring
    public T Copy();//create direct clone of parent
    public void Randomize();//generate randomized agent, for starting population
    public T Recombine(T other);//generate child by combining genomes of 2 parents
    public double Score();//score mutant
    public void SaveInfo(String timeStamp,int generation,int index,double score);
}
