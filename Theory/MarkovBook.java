import java.nio.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.math.*;

/**Orderbooks are saved in this folder, as <Orderbook[i].txt> */
public class MarkovBook {
    
    public int N;
    
    public MarkovBook(String nStates){
        
        this.N = Integer.parseInt(nStates);
        
        createStates();
        
    }
    
    public Vector<State> createStates(){
        int i = 1;
        Vector<State> rawchain = new Vector<>();
        while(i<=this.N){
            rawchain.add(new State(i));
            i++;
        }
        return rawchain;
        
    }
    
    public static void main(String[]args){
        // java MarkovBook [N] 
        if(args.length>1){System.out.println("Incorrect Usage");}
        else{
            MarkovBook m = new MarkovBook(args[0]);
        }
    }
    
    private static class State implements Runnable{
        //Orderbook text file location
        private static String dir;
        private static String fname;
        // Hold orderbook information with following structures
        public static Vector<Order> rawOrders = new Vector<>();
        public static Vector<Double> lastPrices = new Vector<>();
        public static Map<Double,Vector<Order>> operator = new HashMap<>();
        
        public static Vector<Double> PVEC = new Vector<>();
        
        public static boolean upward;
        public static boolean downward;
        
        
        public State(int bookNum){
            State.dir = System.getProperty("user.dir");
            State.fname = "orderbook"+bookNum+".txt";
            run();
            
        }
        
        public void run(){
            //Get the Orderbook text file
            File bk = Paths.get(State.dir,State.fname).toFile(); 
            // Now read it 
            BufferedReader br = null;
            Vector<Order> orders = new Vector<>();            
            try{
                String line;
                br = new BufferedReader(new FileReader(bk));
                while((line = br.readLine()) != null){
                    if(line.contains("$")){
                        String price = line.split(",")[0].split("$")[0];
                        String volume = line.split(",")[1].split(" BTC")[0];
                        orders.add(new Order(price,volume));
                    }
                    if(line.contains("ORDERBOOK")!=true && line.contains("$")!=true &&
                                                    line.contains(",")==true){
                        State.lastPrices.add(Double.parseDouble(line.split(",")[0].split("\\[")[1]));
                     
                    }
                }
            }catch(FileNotFoundException e){System.out.print("Couldnt find file ");}
            catch(IOException e){e.printStackTrace();}
            System.out.println("Found "+orders.size()+" orders to normalize ");
            if(orders.size()>0){State.rawOrders = orders;}
            // Normalize the state, and then create 
            // new vector of Normalized orders  
            MarkovBook.State.operator = createOperator();
            normalizeOperator(MarkovBook.State.operator);
        }
        
        /** <PROCESS+NORMALIZE_ORDERBOOK_DATA>*/
        public Map<Double,Vector<Order>> createOperator(){
            double max  = 0;
            double vtot = 0;
            double maxp = 0;
            double ptot = 0;
            double minp = State.rawOrders.get(0).price;
            for(Order o : State.rawOrders){
                vtot += o.volume;
                ptot += o.price;
                if(o.volume>max){max = o.volume;}
                if(o.price>maxp){maxp = o.price;} 
                if(o.price<minp){minp = o.price;}
            }
            double pavg = ptot/State.rawOrders.size();
            double depth = maxp - minp;
            
            /** <Basic_Info> Average, Max, Min, etc.  */
            //System.out.println("Total: "+vtot+" BTC");
            //System.out.println("Largest order: "+max+" BTC");
            //System.out.println("Average Price: $"+pavg+" [+/-] $"+depth);
            /** Scale data and add weights  */
            double norm = (1/Math.pow(State.rawOrders.size(),2));
            /** Create Volume weights */ 
            Vector <Double> vWeights = new Vector<>();
            Map <Order,Double> statemap = new HashMap<>();
            double w = 0;
            int i = 0;
            /** <create_weights> and map them to their associated Orders */
            for(Order ord : State.rawOrders){w += (ord.volume*ord.volume);}
            for(Order o : State.rawOrders){vWeights.add(o.volume/norm);}
            for(Order order : State.rawOrders){statemap.put(order,vWeights.get(i));i++;}
            
            double diff = 0; double nowavg = 0;
            for(Double p : State.lastPrices){nowavg += p; diff += Math.abs(p-pavg);}
            diff/=lastPrices.size();
            double now = nowavg/State.lastPrices.size();
            //System.out.println("$"+now+" [+/-] $"+diff);
            /** Identify how average of the orderbooks compares to avg prices across 3 markets. */
            if(diff>0){State.upward = true;}
            else{State.downward = true;}
            
             double p0 = (minp +(minp + diff/2))/2;  
             double p1 = minp + diff/2;
             double p2 = pavg;
             double p3 = maxp-(diff/2);
             State.PVEC.add(p0);
             State.PVEC.add(p1);
             State.PVEC.add(p2);
             State.PVEC.add(p3);
            /**Start using the weights to create a normalized orderbook 
               of probabilities connected to their prices. <Bin_by_1/4s> */
            Map<Double,Vector<Double>> histOrders = new HashMap<>();
            double minbin = 0;   Vector<Double> mbin = new Vector<>();
            double midmin = 0; Vector<Double> mdmnbn = new Vector<>();
            double midbin = 0;  Vector<Double> mdbin = new Vector<>();
            double upmidbn = 0; Vector<Double> umdbn = new Vector<>();
            double upbn = 0;    Vector<Double> upbin = new Vector<>();
            
            /** <Eigenstate> placeholders */
            Vector<Order> e0 = new Vector<>();
            Vector<Order> e1 = new Vector<>();
            Vector<Order> e2 = new Vector<>();
            Vector<Order> e3 = new Vector<>();
            Map<Double,Vector<Order>> orderbookOperator = new HashMap<>();
            
            //Iterate through orders! 
            for(Order or : State.rawOrders){
                /** <LOW_END_BIN>*/         
                if(or.price>=minp && or.price<(minp+diff/2)){
                    minbin = or.volume;
                    if(or.volume>minbin){minbin = or.price;}
                    mbin.add(or.price);   
                    e0.add(or);
                }/** <MID_LOW_BIN>*/
                if(or.price>(minp+diff/2) && or.price<=(pavg)){
                    midmin = or.volume;
                    if(or.volume>midmin){minbin = or.price;}
                    mdmnbn.add(or.price);
                    e1.add(or);
                }/** <UPPER_MID_BIN> */
                if(or.price>pavg && or.price<=(maxp - diff/2)){
                    midbin = or.volume;
                    if(or.volume>midbin){midbin = or.price;}
                    mdbin.add(or.price);
                    e2.add(or);
                }/** <UPPER_BIN> */
                if(or.price>(pavg+diff/2) && or.price<=maxp){
                    upbn = or.volume;
                    if(or.volume>upbn){upbn = or.price;}
                    upbin.add(or.price);
                    e3.add(or);
                }
                   
            }
            /** put the vector of points picked up in these bounds/bins*/
            histOrders.put(p0,mbin);
            histOrders.put(p1,mdmnbn);
            histOrders.put(p2,mdbin);
            histOrders.put(p3,upbin);
            orderbookOperator.put(p0,e0);
            orderbookOperator.put(p1,e1);
            orderbookOperator.put(p2,e2);
            orderbookOperator.put(p3,e3);
            
            //System.out.println("____________________________STATES__________________________________");
            System.out.println(p0+"|"+p1+"|"+p2 +"|"+ p3+"|");
            int tot = mbin.size() + mdmnbn.size()+mdbin.size()+upbin.size();
            System.out.println(mbin.size()+" + "+mdmnbn.size()+" + "+mdbin.size()+
                                           " + "+upbin.size()+" = "+tot);
           //System.out.println("____________________________________________");
           
         return orderbookOperator;   
        }
        
        /***/
       public void normalizeOperator(Map<Double,Vector<Order>>orderbookOperator){
             /** Now find which bin has the most <VOLUME> in its orders */
           Vector<Double> bincounts = new Vector<>();
           for(Map.Entry<Double,Vector<Order>>entry:orderbookOperator.entrySet()){
               double sum = 0;
               for( Order trade : entry.getValue()){sum += trade.volume;}
               bincounts.add(sum);
           }
           //Pass in the Price Ranges that orderbook was divided into 
           double p0 = State.PVEC.get(0);
           double p1 = State.PVEC.get(1);
           double p2 = State.PVEC.get(2);
           double p3 = State.PVEC.get(3);
           //Compare sizes of bins 
           if(bincounts.get(0)>bincounts.get(1) && bincounts.get(0)>bincounts.get(2) 
              && bincounts.get(0)>bincounts.get(3)){System.out.println("Leaning towards $"+p0);}
           if(bincounts.get(1)>bincounts.get(0) && bincounts.get(1)>bincounts.get(2)
              && bincounts.get(1)>bincounts.get(3)){System.out.println("Leaning Towards $"+p1);}
           if(bincounts.get(2)>bincounts.get(0) && bincounts.get(2)>bincounts.get(1)
              && bincounts.get(2)>bincounts.get(3)){System.out.println("Leaning Towards $"+p2);}
           if(bincounts.get(3)>bincounts.get(0) && bincounts.get(3)>bincounts.get(2)
              && bincounts.get(3)>bincounts.get(1)){System.out.println("Leaning Towards $"+p3);}
       }
        
        
    }/** END of <MarkovBook.STATE>*/
    
    public static class Order{
        
        public double price; 
        public double volume;
        public double weight;
        
        public Order(String Price, String Volume){
            this.price  = MarkovBook.extract(Price);
            this.volume = MarkovBook.extract(Volume);
        }
        
                
        //Set weight method for an Order 
        public void setWeight(double w){this.weight = w;}
        
    }/** END of <MarkovBook.ORDER>*/
   
   /** <Extract> double from input String */
        static double extract(String str) {
            Double dig = 0.0;
            Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
            while (m.find()) {
                return Double.parseDouble(m.group(1));
            }
            return dig;
        }
    
}/**END of <MarkovBook.java> */