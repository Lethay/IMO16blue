package AgentGridMin;

/**
 * Created by rafael on 11/2/16.
 */
public interface Sortable {
    double Compare(int iFirst,int iSecond);//pos if iFirst > iSecond
    void Swap(int iFirst,int iSecond);
    int Length();
}
