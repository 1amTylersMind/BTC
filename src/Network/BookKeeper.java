//package Network;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class BookKeeper {

    //Resource information 
    public static String                      resName;
    public static String                      resPath;

    //Log permanently logged into: 
    //BTC/src./log.txt 
    public static Vector<String>              mktNames = new Vector<>();
    public static Map<String, Vector<Double>> mkt24pts = new HashMap<>();
    
    public static String memfile = "./memory.txt";
    public static String mempath = "/projects/BTC/src";
    public static File memory;

    public BookKeeper(Vector<String> f) {
        Map<Double, Double> orders = seperateOrders(f);
        memory = Paths.get(mempath,memfile).toFile();        
        //Log this to memory 
        try{rememberOrders(orders);}
        catch(Exception e){e.printStackTrace();}
        
        System.out.println("BookKeeper Logged: " + orders.size() + " orders");
        //Also have the 24hr price point & vol data for each market saved
        //in the Map mkt24pts, categorized by market name
        OrderbookHiddenLayer odbhl = new OrderbookHiddenLayer(mkt24pts, orders);
    }
    
    void rememberOrders(Map<Double,Double>orders) throws Exception{
        FileWriter fw = null;
        fw = new FileWriter(BookKeeper.memory,true);
        for(Map.Entry<Double,Double>entry:orders.entrySet()){
            String ln = entry.getKey()+","+entry.getValue()+"\n" ;
            fw.write(ln);
        }
        fw.close();
    }

    Map<Double, Double> seperateOrders(Vector<String> input) {
        Map<Double, Double> ans = new HashMap<>();
        Vector<Double> prices = new Vector<>();
        Vector<Double> vol = new Vector<>();
        Vector<String> markets = new Vector<>();
        Map<Integer, Vector<Double>> mktPricePts = new HashMap<>();

        int nMkt = 0;

        for (String ln : input) {
            if (ln.contains("$")) {
                ln = ln.split("$")[0];
                prices.add(DataTrainingLayer.extract(ln.split(",")[0]));
                vol.add(DataTrainingLayer.extract(ln.split(",")[1]));
            } else if (ln.contains("ORDERBOOK")) {
                markets.add(ln.split(" https://api.cryptowat.ch/markets/")[1]);
            } else if (ln.contains(",")) {
                Vector<Double> pts24 = new Vector<>();
                for (String e : ln.split(",")) {
                    pts24.add(DataTrainingLayer.extract(e));
                }
                mktPricePts.put(nMkt, pts24);
                nMkt++;
            }
        }
        if (prices.size() == vol.size()) {
            int index = 0;
            for (Double price : prices) {
                ans.put(price, vol.get(index));
                index++;
            }
        }

        int element = 0;
        for (String mkt : markets) {
            mkt24pts.put(mkt.split("/btcusd/trades")[0], mktPricePts.get(element));
            element++;
        }


        return ans;
    }

    static class OrderbookHiddenLayer implements Runnable {

        static Map<String, Vector<Double>> mktbounds = new HashMap<>();
        static Map<Double, Double>         trades    = new HashMap<>();

        static Vector<Double>              bounds    = new Vector<>();
        static Vector<Double>              weights   = new Vector<>();

        OrderbookHiddenLayer(Map<String, Vector<Double>> bounds, Map<Double, Double> points) {
            OrderbookHiddenLayer.mktbounds = bounds;
            OrderbookHiddenLayer.trades = points;
            run();

        }

        public void run() {
            /** <ALGORITHM> */
            OrderbookHiddenLayer.bounds = defineBounds();
            OrderbookHiddenLayer.weights = genWeightsByBounds();
            OrderbookHiddenLayer.weights = backPropagateWeights();
            System.out.println("backpropagated "+OrderbookHiddenLayer.weights.size());
        }

        /** <GET.boundaries> from <OrderbookHiddenLayer.mktbounds>*/
        Vector<Double> defineBounds() {
            double avgPrice = 0;
            double avgHigh = 0;
            double avgLow = 0;
            double avgVol = 0;
            double totalVol = 0;
            for (Map.Entry<String, Vector<Double>> entry : OrderbookHiddenLayer.mktbounds.entrySet()) {

                avgPrice += entry.getValue().get(0);
                avgHigh  += entry.getValue().get(1);
                avgLow   += entry.getValue().get(2);
                avgVol   += entry.getValue().get(3);
                
            }
            
            avgPrice /= OrderbookHiddenLayer.mktbounds.values().size();
            avgHigh /= OrderbookHiddenLayer.mktbounds.values().size();
            avgLow /= OrderbookHiddenLayer.mktbounds.values().size();
            totalVol = avgVol; //Before being scaled by [N] of markets 
            avgVol /= OrderbookHiddenLayer.mktbounds.values().size();

            Vector<Double> bounds = new Vector<>();
            bounds.add(avgPrice);
            bounds.add(avgHigh);
            bounds.add(avgLow);
            bounds.add(avgVol);
            bounds.add(totalVol);

            return bounds;
        }
        
        /** */
        Vector<Double> genWeightsByBounds() {
            Vector<Double> result = new Vector<>(); //Hold the weights
            
            /** <BOUNDS> */
            double avg24   = OrderbookHiddenLayer.bounds.get(0); double offset;
            double high24  = OrderbookHiddenLayer.bounds.get(1); double distup;
            double low24   = OrderbookHiddenLayer.bounds.get(2); double distlow;
            double vol24   = OrderbookHiddenLayer.bounds.get(3); double volwt;
            double totVol  = OrderbookHiddenLayer.bounds.get(4); double tiny;
            
            double bkvol   = 0; 
            List<Double>vols = new ArrayList<>(OrderbookHiddenLayer.trades.keySet());
            for(Double v :vols){bkvol+=v;}
            
            for(Map.Entry<Double,Double>entry:OrderbookHiddenLayer.trades.entrySet()){
                
                double price = entry.getKey();
                double vol   = entry.getValue();
                offset  = price - avg24;
                distup  = high24 - price;
                distlow = price - low24; 
                volwt   = vol * (bkvol/vol24);
                tiny    = vol/totVol;
                
                /*
                result.add(offset);
                result.add(distup);
                result.add(distlow);
                result.add(volwt);
                result.add(tiny);*/
                
                double priceWeight = (entry.getKey() - avg24)/high24; 
                double volWeight   = entry.getValue()*(vol24/totVol);
                result.add(priceWeight*volWeight);
                
            }
            
            double volw = bkvol/vol24;

            return result;
        }
        
        /** */
        Vector<Double> backPropagateWeights() {
            Vector<Double> finalweights = new Vector<>();
            List <Double>  askPrices    = new ArrayList<>(OrderbookHiddenLayer.trades.keySet()); 
            
            if(askPrices.size()!=OrderbookHiddenLayer.weights.size()){
                System.out.println(
                "Dimensions of weights and orderbook prices do NOT match!"
                );
            }
            int index = 0;
            for(Double d : OrderbookHiddenLayer.weights){
                finalweights.add(askPrices.get(index)*d);
                index++;
            }
            
            return finalweights;
        }

    }

    public static void main(String[] args) {}


}
