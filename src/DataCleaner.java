/** */
import java.util.*;

/**DataCleaner 
 * @Author ScottRobbins 
 */
public class DataCleaner {
    
    Vector<Map<Integer,Vector<Double>>> dataCollected = new Vector<>();
    Vector<Vector<Double>>  summary = new Vector<>();
    
    public DataCleaner(DataCollector dc){
        dataCollected = dc.btcData;
        summary = dc.multiMarketSummary;
        Map<Integer,Market> now = unpackDataCollected();
        
    }
   Map<Integer,Market> unpackDataCollected(){
        int nDataPoints = 0;
        int i=0;
        //
        Map<Integer,Market> snapshot = new HashMap<>();
    
        for(Map<Integer,Vector<Double>> orderbk : dataCollected){
            Map<Double,Double> orderbook = new HashMap<>();
            for(Map.Entry<Integer,Vector<Double>>entry:orderbk.entrySet()){
                orderbook.put(entry.getValue().get(0),entry.getValue().get(1));
                nDataPoints+=1; 
            }
            
            Market thisMkt = new Market(summary.get(i),orderbook);
            snapshot.put(i,thisMkt);
            i++;
        }
        System.out.println("DataCleaner.unpackDataCollected found "+nDataPoints +" Data Points ");
        return snapshot;
    }
    
    public static void main(String[]args){
        
    }
    
}/**<EndOf_DataCleaner>*/