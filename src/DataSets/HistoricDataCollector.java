// package DataSets;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;

/** <HistoricDataCollector>.java
 * Dependencies: Maya.java 
 * @Author ScottRobbins 

 * * * HistoricDataCollector * * *
 */
public class HistoricDataCollector {

    public static Map<String, String>                 files     = new HashMap<>();
    public static Vector<Map<String, Vector<Double>>> abstracts = new Vector<>();
    public static Vector<Vector<String>>              params    = new Vector<>();
    public static Vector<Vector<Double>>              data      = new Vector<>(); 
    public static final String[]                      modes     = {"price", "volume", "volatility"};

    public HistoricDataCollector(String fpath, Vector<String> fileNames) {
        int i = 0;
        for (String fileName : fileNames) {
            HistoricDataCollector.files.put(fpath, fileName);
            new Maya(fpath, fileName, modes[i]);
            HistoricDataCollector.abstracts.add(Maya.abstraction);
            HistoricDataCollector.params.add(Maya.tableParameters);
            i++;
        }
        int nCols = HistoricDataCollector.params.get(0).size();
        int nRows = HistoricDataCollector.abstracts.size() * HistoricDataCollector.abstracts.get(0).size();
        System.out.println(HistoricDataCollector.params.size()+" Parameters");
        System.out.println("Abstraction Size: " + nRows + " Rows and " + nCols + " Cols = " + nRows * nCols + " Total Data Pts");
        // Maya(Sring path, String fname, String type)
        // Save the Maya.abstraction, table params, 
        // basically all of the data structures maya makes 
        // is collected here. Then make a runnable to cross reference
        // markets

    }


    public static void main(String[] args) {
        Vector<String> fnames = new Vector<>();
        String dir = args[0];
        for (String e : args) {
            fnames.add(e);
        }
        fnames.remove(0);

        HistoricDataCollector HDC = new HistoricDataCollector(dir, fnames);
        HiddenLayerOne hl1 = new HiddenLayerOne(dir, fnames, HDC.modes, HistoricDataCollector.params, HistoricDataCollector.abstracts);


    }

    public static class HiddenLayerOne implements Runnable {


        static String                             path;
        static String                             name;
        static List<String>                      dates;
       


        public static Vector<Vector<Double>>      pricedata    = new Vector<>();
        public static Vector<Vector<Double>>      volumedata   = new Vector<>();
        public static Vector<Vector<Double>>      volatiledata = new Vector<>();

        public HiddenLayerOne(String pathToData,
                              Vector<String> fileNames,
                              String[] modes,
                              Vector<Vector<String>> parameters,
                              Vector<Map<String, Vector<Double>>> ds) {
            /** <_HiddenLayerOne_:DataStructureDesign>
             * dataSet = <File<Date,<Data>>> 
             * parameters = <Vector<Cols>>
             * 
             * First imformation is gathered by looking at functions
             * of their respective parameters alone 
             */
            if (ds.size() != parameters.size()) {
                System.out.println(" *** Dim Error!! ***");
            } else {
                System.out.println("*******************************************************"
                                   + "\n - Correct Dimensions. Constructing Hidden Layer\n"
                                   + "*******************************************************");
            }
            // Feed all 3 DataSets through [Price, Volume, Volatility] 
            HiddenLayerOne.pricedata = forwardFeedRows(ds.get(0), parameters.get(0));
            HiddenLayerOne.volumedata = forwardFeedRows(ds.get(1), parameters.get(1));
            HiddenLayerOne.volatiledata = forwardFeedRows(ds.get(2), parameters.get(2));
            System.out.println("Hidden Layer Data Point Samples for all 3 Types: ");
            
            System.out.println(HiddenLayerOne.pricedata.get(0).get(0) + " " +
                               HiddenLayerOne.pricedata.get(0).get(1) + ", " +
                               HiddenLayerOne.pricedata.get(1).get(0) + " " + 
                               HiddenLayerOne.pricedata.get(1).get(1)+ ", "  +
                               HiddenLayerOne.pricedata.get(2).get(0)+ " " +
                               HiddenLayerOne.pricedata.get(2).get(1));
            System.out.println(HiddenLayerOne.volumedata.get(0).get(0) + " " +
                               HiddenLayerOne.volumedata.get(0).get(1) + ", " +
                               HiddenLayerOne.volumedata.get(1).get(0) + " " + 
                               HiddenLayerOne.volumedata.get(1).get(1)+ ", "  +
                               HiddenLayerOne.volumedata.get(2).get(0)+ " " +
                               HiddenLayerOne.volumedata.get(2).get(1));
            System.out.println(HiddenLayerOne.volatiledata.get(0).get(0) + " " +
                               HiddenLayerOne.volatiledata.get(0).get(1) + ", " +
                               HiddenLayerOne.volatiledata.get(1).get(0) + " " + 
                               HiddenLayerOne.volatiledata.get(1).get(1)+ ", "  +
                               HiddenLayerOne.volatiledata.get(2).get(0)+ " " +
                               HiddenLayerOne.volatiledata.get(2).get(1));

            System.out.println("o Organized " + HiddenLayerOne.pricedata.size() * HiddenLayerOne.pricedata.get(0).size()
                               + " Price Data Points by Date and Exchange");
            System.out.println("o Organized " + HiddenLayerOne.volumedata.size() * HiddenLayerOne.volumedata.get(0).size()
                               + " Volume Data Points by Date and Exchange");
            System.out.println("o Organized " + HiddenLayerOne.volatiledata.size() * HiddenLayerOne.volatiledata.get(0).size()
                               + " Volatility Data Points by Date and Exchange");
                               
            /**TODO: 
             * (1) - I need to de-dimensionalize  
             * (2) - Sort Categorical Info 
             * (3) - Scale all data between -1 and 1 
             * (4) - Begin calculations/comparisons 
             */
                               
            
                               
            /*HiddenLayerOne.n1 = HiddenLayerOne.singleAttributeFeed(HiddenLayerOne.pricedata);            
            HiddenLayerOne.n2 = HiddenLayerOne.singleAttributeFeed(HiddenLayerOne.volumedata);
            HiddenLayerOne.n3 = HiddenLayerOne.singleAttributeFeed(HiddenLayerOne.volatiledata);
            //Save these three neurons 
            HiddenLayerOne.output.add(HiddenLayerOne.n1);
            HiddenLayerOne.output.add(HiddenLayerOne.n2);
            HiddenLayerOne.output.add(HiddenLayerOne.n3);*/
            
        }
        
      

        /** <ForwardFeeder>*/
        Vector<Vector<Double>> forwardFeedRows(Map<String, Vector<Double>> data, Vector<String> params) {
            dates = new ArrayList<>(data.keySet());
            // Synchronize with specialized 3vector:
            /** <[Date][MarketName][Double]> */
            params.remove(0);// Just [Time], but I have actual time in List<> dates 
            
            Vector<Vector<Double>> points = new Vector<>();
            for (Vector<Double> value : data.values()) {  
                points.add(value);
            }

            return points;
        }
        
        public void run() {
           
        }
        
        public static class PriceSentimentAnalysis implements Runnable {
            
            public PriceSentimentAnalysis(Map<String, Vector<Double>> data, Vector<String> params){
                /** 
                 * Max price over the course of entire data set is = 1. 
                 * Absolute minimum price in data set will = -1.
                 * Have: Map<Date,<RowData>> and <Mkt.Names>
                 * Should also start keeping track of where values occur
                 * and their corresponding dates/mkt. names 
                 * and maybe have mode where only considering single mkt (cols)
                 * vs going date by date (rows). Will have different results 
                 * 
                 * So......    <:_The_Algorithm_:>
                 * <[1]> Grab Max and mins of each Market over time range
                 * keeping track of which dates respective extrema occur. 
                 * <[2]> Run comparisons, keeping track of spread across mkts. 
                 * The difference between each market, and try and measure the
                 * degree to which any two markets group together. 
                 * <[3]> Revisit critical dates, and examine activity of other
                 * markets on that date, and the times directly before/after point. 
                 */
            }
            
            public void run(){
                
            }
        }
        

    }

    
}
/** <:_HistoricDataCollector_:>*/
