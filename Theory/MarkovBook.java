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
            normalizeState();
            
        }
        
        public void normalizeState(){
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
            System.out.println("Total: "+vtot+" BTC");
            System.out.println("Largest order: "+max+" BTC");
            System.out.println("Average Price: $"+pavg+" [+/-] $"+depth);
            
            //Scale data and add weights  
            double norm = (1/Math.pow(State.rawOrders.size(),2));
            //Create Volume weights 
            Vector <Double> vWeights = new Vector<>();
            Map <Order,Double> statemap = new HashMap<>();
            double w = 0;
            int i = 0;
            //Create weights and map them to their associated Orders 
            for(Order ord : State.rawOrders){w += (ord.volume*ord.volume);}
            for(Order o : State.rawOrders){vWeights.add(o.volume/norm);}
            for(Order order : State.rawOrders){statemap.put(order,vWeights.get(i));i++;}
            
            double diff = 0; double nowavg = 0;
            for(Double p : State.lastPrices){nowavg += p; diff += (p-pavg);}
            diff/=lastPrices.size();
            double now = nowavg/State.lastPrices.size();
            System.out.println("$"+now+" [+/-] $"+diff);
            //Identify how average of the orderbooks compares to avg prices across 3 markets. 
            if(diff>0){State.upward = true;}
            else{State.downward = true;}
            
            //Start using the weights to create a normalized orderbook 
            //of probabilities connected to their prices. Bin by ~$5
            
            
            
            
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