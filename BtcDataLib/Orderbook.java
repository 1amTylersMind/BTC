import java.util.*;

/** <Orderbook> */
public class Orderbook {
    
    public static Vector<Trade> orderbook = new Vector<>();
    public static Vector<Double> weights  = new Vector<>();
    
    public Orderbook(Map<Double,Double>orders){
        
        
        for(Map.Entry<Double,Double>entry:orders.entrySet()){
            Trade t = new Trade(entry.getKey(),entry.getValue());
            Orderbook.orderbook.add(t);
            weights.add(Orderbook.Trade.weight);
        } 
        
        int N = orders.size();
        
        
    }
    
    public static void main(String[]args){
        
    }
    
    static class Trade {
         
         static double value;
         static double size;
         static double weight;
        
        
        Trade(Double price, Double volume){
            Trade.value = price;
            Trade.size = volume;
            Trade.weight = (price*volume);
        }
        
    }
    
    
}/** <EndOf_Orderbook>*/
