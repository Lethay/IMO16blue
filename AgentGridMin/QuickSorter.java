package AgentGridMin;

/**
 * Created by rafael on 11/1/16.
 */
public class QuickSorter <T extends Sortable>{
    //sorts greatest to least
    public void QuickSort(T sortMe){
        SortHelper(sortMe,0,sortMe.Length()-1);
    }
    void SortHelper(T sortMe,int lo,int hi){
        if(lo<hi){
            int p=Partition(sortMe,lo,hi);
            SortHelper(sortMe,lo,p-1);
            SortHelper(sortMe,p+1,hi);
        }
    }
    int Partition(T sortMe,int lo,int hi){
        for(int j=lo;j<hi;j++){
            if(sortMe.Compare(hi,j)<=0){
                sortMe.Swap(lo,j);
                lo++;
            }
        }
        sortMe.Swap(lo,hi);
        return lo;
    }
}
