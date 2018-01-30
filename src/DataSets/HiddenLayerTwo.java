//package DataSets;

import java.util.*;


/***** <_Hidden-Layer-2_> *****
@Author ScottRobbins        *
****************************/
public class HiddenLayerTwo {

    Map<Integer,Double> batchValues = new HashMap<>(); 
    public HiddenLayerTwo(HistoricDataCollector.HiddenLayerOne layer1,
                          Vector<HistoricDataCollector.Neuron> neurons){
                              
        
    }
    
    public static Map<HistoricDataCollector.Neuron,Vector<Double>> extractBatchValues(Vector<HistoricDataCollector.Neuron>neurons){
        Map<HistoricDataCollector.Neuron,Vector<Double>> result = new HashMap<>();
        
        for(HistoricDataCollector.Neuron n : neurons){
            Vector<Vector<String>> batch = HistoricDataCollector.Neuron.batch;
            Vector<Double> values = new Vector<>();
            for(Vector<String> pts : batch){values.add(Double.parseDouble(pts.get(2)));}
            result.put(n,values);
           
        }
        return result;
    }
    
    /** TODO */
    void batchDump(Map<Integer,Double>batch){}
    
    public static void main(String[]args){ }
    
    
    public static class FourierLayer implements Runnable{
        
        public FourierLayer(){
            
        }
        
        public void run(){
            
        }
    }
}
