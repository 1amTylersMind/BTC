import java.util.*;
import java.net.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import javax.net.ssl.*;

public class DataCollector {

    //Trades Links uri + resource
    private static String resource    = "https://api.cryptowat.ch/markets/";
    private static String BfxOrderbk  = "bitfinex/btcusd/trades";
    private static String BstampOrdBk = "bitstamp/btcusd/trades";
    private static String CbaseOrdBk  = "coinbase/btcusd/price";
    private static String summaryBfnx = "bitfinex/btcusd/summary";
    private static String summaryCb   = "coinbase/btcusd/summary";
    private static String summaryStmp = "bitstamp/btcusd/summary";
    
    public Vector<Map<Integer,Vector<Double>>>        btcData   = new Vector<>();
    public Vector<Map<Vector<Double>,Vector<Double>>> btcMktDat = new Vector<>();
    public static Vector<Vector<Double>> multiMarketSummary = new Vector<>();

    public DataCollector() {

        /**Mkt Summary Resources - <[Reactive]> Creates Bounds and limits  
        will <return> a <summary> ->  <[last,high,low,volume]> */
        Vector<String> summaries = new Vector<>();
        summaries.add(resource + summaryBfnx);
        summaries.add(resource + summaryCb);
        summaries.add(resource + summaryStmp);

        /**Order Book Resources - <[Pro-active]> Recurrent NN data 
        Will <return> an <Orderbook> -> <[Price($),Volume(BTC)]> */
        Vector<String> resources = new Vector<>();
        resources.add(resource + BfxOrderbk);
        resources.add(resource + BstampOrdBk);
        // resources.add(resource + CbaseOrdBk); CB doesnt tell Volume

        /** Use resources to <Collect_Data> **/
        GatherDataPoints gdp = new GatherDataPoints(summaries);
    
        /** Begin <Collecting_Live_Data> */
        btcData = gdp.liveDataCapt(resources);
    }

    public static void main(String[] args) {
        //Get Live Data
        DataCollector btcDC = new DataCollector();
        //Organize it
        DataCleaner dc = new DataCleaner(btcDC);
      
      //BackPropagate Results Through the netowrk
      //DataTrainingLayer(btcDC,...) or something 
        
    }

    static class GatherDataPoints implements Runnable {

        String link;
        Vector<Double> mktSum = new Vector<>();
        
        public GatherDataPoints(Vector<String> links) {
            for (String lnk : links) {
                link = lnk;
                run();
                
            }
        }

        public void run() {
            Vector<String> data = connect(link);
            mktSum = parse(data);
            //System.out.println("MKT. SUMMARY: " + link);
            System.out.println(mktSum);
            DataCollector.multiMarketSummary.add(mktSum);
        }

        Vector<Map<Integer,Vector<Double>>> liveDataCapt(Vector<String> lnks) {
            Vector<Map<Integer,Vector<Double>>> btcOrders = new Vector<>();
            for (String lnk : lnks) {
                System.out.println("ORDERBOOK " + lnk);
                Map<Integer,Vector<Double>> orderbk = parseData(connect(lnk));
                btcOrders.add(orderbk);
                
            }
            return btcOrders;
        }

        /** <Connect> */
        Vector<String> connect(String link) {
            URL url;
            BufferedReader br = null;
            Vector<String> code = new Vector<>();

            try {
                url = new URL(link);
                HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
                String line;
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) != null) {
                    code.add(line);
                }
            } catch (UnknownHostException e) {
                System.out.println("UnknownHostError! ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return code;
        }

        /** <PARSE> */
        Vector<Double> parse(Vector<String> code) {
            Vector<Double> answer = new Vector<>();
            String[] arr = code.get(0).split(",");
            int index = 0;
            double last = extract(code.get(0).split(",")[0].split("last")[1]);
            double high = 0;
            double low = 0;
            double vol = 0;
            for (String e : arr) {
                double num = extract(e.split(":")[1]);
                if (index == 1) {
                    high = num;
                }
                if (index == 2) {
                    low = num;
                }
                if (index == 5) {
                    vol = num;
                }
                index += 1;
            }
            answer.add(last);
            answer.add(high);
            answer.add(low);
            answer.add(vol);
            return answer;
        }

        /** parse gathered html */
        public Map<Integer,Vector<Double>> parseData(Vector<String> code) {
            String answer = "";
            for (String s : code) {
                String[] arr = s.split(",");
                int x = 0;
                Map<Double, Double> orders = new HashMap<>();
                double[] tees = new double[arr.length];
                for (String e : arr) {
                    tees[x] = extract(e);
                    if (x < arr.length - 4){}
                    x += 1;
                    
                    if (x % 4 == 0) {
                        orders.put(tees[x - 1], tees[x - 2]);
                    }
                }

                //Check if orderbook was constructed properly
                for (Map.Entry<Double, Double> entry : orders.entrySet()) {
                    System.out.print(entry.getValue()+"," + entry.getKey()+"\n");
                    answer += (entry.getValue() + "," + entry.getKey() + '\n');
                }
             }
                //Return a Map<TradeNumber,Vector<Price,Volume>> instead
                Map<Integer,Vector<Double>> orderbook = new HashMap<>();
                int tradeNumber = 0;
                String [] dat = answer.split("\n");
                for(String line : dat){
                    double price = extract(line.split(",")[0]);
                    double vol = extract(line.split(",")[1]);
                    Vector<Double> trade = new Vector<>();
                    trade.add(price); trade.add(vol);
                    tradeNumber+=1;
                    orderbook.put(tradeNumber,trade);
                    
                }
            
            return orderbook;
        }

        /** <Extract> double from input String */
        static double extract(String str) {
            Double dig = 0.0;
            Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
            while (m.find()) {
                return Double.parseDouble(m.group(1));
            }
            return dig;
        }

    }
    /**<EndOf_GatherDataPoints> */

}
/**<EndOf_DataCollector> */
