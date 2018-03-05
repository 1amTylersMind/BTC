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

    public HistoricDataCollector(Vector<String> fileNames) {
        int i = 0;
        for (String fileName : fileNames) {
            HistoricDataCollector.files.put(System.getProperty("user.dir"), fileName);
            new Maya(fileName, modes[i]);
            HistoricDataCollector.abstracts.add(Maya.abstraction);
            HistoricDataCollector.params.add(Maya.tableParameters);
            i++;
        }
        int nCols = HistoricDataCollector.params.get(0).size();
        int nRows = HistoricDataCollector.abstracts.size() * HistoricDataCollector.abstracts.get(0).size();
        System.out.println(HistoricDataCollector.params.size()+" Parameters");
        System.out.println("Abstraction Size: " + nRows + " Rows and " + nCols + " Cols = " + nRows * nCols + " Total Data Pts"); 
    }


    public static void main(String[] args) {
        Vector<String> fnames = new Vector<>();
        for (String e : args) {
            fnames.add(e);
        }
        HistoricDataCollector HDC = new HistoricDataCollector(fnames);
        HiddenLayerOne hl1 = new HiddenLayerOne(fnames, HDC.modes, HistoricDataCollector.params, HistoricDataCollector.abstracts);


    }

    public static class HiddenLayerOne implements Runnable {


        static String                             path;
        static String                             name;
        static List<String>                      dates;
       
        public static Vector<Vector<Double>>      pricedata    = new Vector<>();
        public static Vector<Vector<Double>>      volumedata   = new Vector<>();
        public static Vector<Vector<Double>>      volatiledata = new Vector<>();

        public HiddenLayerOne(Vector<String> fileNames,
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
            System.out.println("Hidden Layer Data Point Samples for "+parameters.size()+" Types: ");
            
            /**<[INFO_DUMP_TO_CONSOLE]>*/
            System.out.println("o Organized " + HiddenLayerOne.pricedata.size() * HiddenLayerOne.pricedata.get(0).size()
                               + " Price Data Points by Date and Exchange");
            System.out.println("o Organized " + HiddenLayerOne.volumedata.size() * HiddenLayerOne.volumedata.get(0).size()
                               + " Volume Data Points by Date and Exchange");
            System.out.println("o Organized " + HiddenLayerOne.volatiledata.size() * HiddenLayerOne.volatiledata.get(0).size()
                               + " Volatility Data Points by Date and Exchange");
                               
            //Start with trying this on first column
            Vector<Vector<Double>> pricepoints = findLocalExtrema(differential(HiddenLayerOne.pricedata,1));
            Vector<Vector<Double>> volpoints   = findLocalExtrema(differential(HiddenLayerOne.volumedata,1));
            Vector<Vector<Double>> tradepoints = findLocalExtrema(differential(HiddenLayerOne.volatiledata,1));
            
        }
        
        /** <ForwardFeeder>*/
        Vector<Vector<Double>> forwardFeedRows(Map<String, Vector<Double>> data, Vector<String> params) {
            dates = new ArrayList<>(data.keySet());
            // Synchronize with specialized 3vector:
            /** <[Date][MarketName][Double]> */
            params.remove(0);// Just [Time], but I have actual time in List<> dates 
            Vector<Vector<Double>> points = new Vector<>();
            for (Vector<Double> value : data.values()){points.add(value);}
            return points;
        }
        
        public void run() {
            /** 
             *                <:_The_Algorithm_:>
             * <[1]> Find mins and maxes across respective mkts
             * <[2]> From a min or max draw Fibonacci lines
             * <[3]> If prices flatten near the intersection of these points, 
             *       or really any point where the derivative = 0, add it as a 
             *       possible support or resistance level 
             * <[4]> Compare support and resistance levels across markets, and if 
             *       any correlation can be drawn b/w when those levels are tested
             *       Fibbonacci Lvls:  <[23.6%_28.2%_50.0%,_and_61.8%]>
             */
            //Vector<Vector<Double>> pricederivs = differential(HiddenLayerOne.pricedata);
            //Vector<Double> volderiv   = differential(HiddenLayerOne.volumedata);
            //Vector<Double> tradederiv = differential(HiddenLayerOne.volatiledata);
            
           
        }
        
        public Vector<Double> differential(Vector<Vector<Double>>dataset,int col){
            Vector<Double> deriv = new Vector<>();
            Vector<Double> val = new Vector<>();
            int index = 0;
            for(Vector<Double> row : dataset){val.add(row.get(col));}
            for(Double pt : val){
                if(index==0){deriv.add((val.get(1) - val.get(0))/60);}
                else{deriv.add((val.get(index) - val.get(index - 1))/60);}
                index +=1;
            }
            
            return deriv;
        }
        
        /** TODO: Remember the way dataset is organized as above (row/row not row/col)*/
        public Vector<Vector<Double>> findLocalExtrema(Vector<Double> dataset){
            Vector<Vector<Double>> extrema = new Vector<>();
            int index = 0;
            for(Double point : dataset){
                Vector<Double> pt = new Vector<>();
                if(point==0 || (point<0.5 && point>-.5)){
                    pt.add(point);
                    pt.add((double)(index));
                    extrema.add(pt);
                }else{continue;}
                index ++;
            }
            System.out.println("Found "+extrema.size()+" local extrema.");
            return extrema;
        }
        
        private static class POI {
            
            private double equals;
            private int location;
            private static final String [] types = {"p","v","t"}; 
            private static String is;
            
            Vector<Double> fibonacciGrowth = new Vector<>();
            Vector<Double> fibonacciDecay  = new Vector<>();
            
            /** <[P]oint_[O]f_[I]nterest>*/
            POI(double value,int index,String kind){
                boolean valid = false;
                for(String t : types){
                    if(Arrays.binarySearch(types,t)!=-1){is = t;valid = true;}
                    else{System.out.println("Unknown Data Type. Cannot Build a Point of Interest.");}
                }  
                if(valid){
                    this.equals = value;
                    this.location = index;
                }
            }
            
            void genFibNums(){
                //Add fibonacci growth values of this POI
                fibonacciGrowth.add(this.equals+(this.equals*0.236));
                fibonacciGrowth.add(this.equals+(this.equals*0.282));
                fibonacciGrowth.add(this.equals+(this.equals*0.5));
                fibonacciGrowth.add(this.equals+(this.equals*0.618));
                //Now the fibonacci decay numbers 
                fibonacciDecay.add(this.equals-(this.equals*0.236));
                fibonacciDecay.add(this.equals-(this.equals*0.282));
                fibonacciDecay.add(this.equals-(this.equals*0.5));
                fibonacciDecay.add(this.equals-(this.equals*0.618));
            }
            
            
            
        }
        
        
    }/**<ENDof:_[HiddenLayerOne]>*/
}
/** <ENDof:_[HistoricDataCollector]>*/
