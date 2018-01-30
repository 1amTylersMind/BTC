/** <_MARKET.java_> */
import java.util.*;

/** */
public class Market {
    
    public double price;
    public double high;
    public double low;
    public double volume;
    public Map<Double,Double> orderbook = new HashMap<>();
    
    public Market(Vector<Double>summary,Map<Double,Double>orderbk){
        
        //Initialize the Market Orderbook 
        orderbook = orderbk;
        
        //Get latest price, and the 24hr price points and volume 
        try{
            price  = summary.get(0);
            high   = summary.get(1);
            low    = summary.get(2);
            volume = summary.get(3);
        }catch(IndexOutOfBoundsException e){System.out.print("Malformed Vector Size");}
                
        
        
    }
    
    public static void main(String[]args){
        
    }
    
    
    static class OrderbookInterpreter implements Runnable{
        
        double under;   
        double over;
        double range24;
        double cap24;
        
        boolean bull; 
        boolean bear;
        
        static Map <Double,Double> orders  = new HashMap<>();
        static Map<Integer,Double> weights = new HashMap<>(); 
        
        OrderbookInterpreter(Map<Double,Double>orderbk,
                             double price,
                             double high,
                             double low,
                             double volume){
            if((high - price)>0){under = high - price;}
            if((price - low) >0){over  = price - low;}
            range24 = high - low;
            cap24 = volume * high;//Just an Approximation 
            //^ Could try to calc. the integral of a chart?
            
            //Characterize market to guide flow of run()
            if((under/range24)<(price-low)/range24){
                bear = true; 
                bull = false;
            }else{
                bull = true;
                bear = false;
            }
            OrderbookInterpreter.orders = orderbk;
            run();
            System.out.println(weights.size()+" weights assigned for market orderbook. ");
        }
        
        public void run(){
            
            OrderbookInterpreter.weights = createFieldWeights();

        }
        
        
        /** */
        Map<Integer,Double> createFieldWeights(){
            Vector<Double> weights = new Orderbook(OrderbookInterpreter.orders).weights;
            int index = 0;
            Map<Integer,Double> output = new HashMap<>();
            for(Double val : weights){
                output.put(index,val);
                index +=1;
            }
            
            return output;
        }
        
        
    }
    
}/** <EndOf_Market> */
