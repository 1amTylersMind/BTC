//package Network;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/***************************************************************
 * <DataTrainingLayer>                                         *
 *
@Author ScottRobbins
 **************************************************************/
public class DataTrainingLayer implements Runnable {
    
    public String opMode;
    public String resourcePath;
    public String resourceName;
    public Vector<Double> localxrema = new Vector<>();
    public Vector<Double> keypts     = new Vector<>();
    public Vector<Double> deriv      = new Vector<>();
    public Vector<Double> deriv2nd   = new Vector<>();
    
    /** <_DATA_TRAINING_>*/
    public DataTrainingLayer(String mode, 
                             String fpath, 
                             String fname){
        
        resourcePath = fpath;
        resourceName = fname;
        
        switch(mode){
            case "orderbook":
                opMode = mode;
                System.out.println("DATATRAINING_LAYER_: <orderbook> "+fpath+" "+fname);
                run();
                break;
            case "functionData":
                System.out.println("DATATRAINING_LAYER_: <functionData> "+fpath+" "+fname);
                opMode = mode;
                run();
                break;
            case "historicData":
                System.out.println("DATATRAINING_LAYER.FunctionGenerator: <historicData>" );
                opMode = mode;
                run();
                break;
       }
      
       
    
    }
    
    public void run(){
        
        switch(opMode){
            /** <DIVIDE_functionality> */
            case "orderbook":
                Vector<String> fileCont = getFileContents(resourcePath,resourceName);
                new BookKeeper(fileCont);
                //applyFunctionWeights(fileCont);
                
            case "functionData":
                Vector<String> filedat = getFileContents(resourcePath,resourceName);
                Vector<Double> dataTrain = new Vector<>();
                
                if(resourceName.compareTo("./cb30d.txt")==0||
                   resourceName.compareTo("./nAddr.txt")==0||
                   resourceName.compareTo("./mktcap.txt")==0||
                   resourceName.compareTo("./usdvol.txt")==0){
                       
                    for(String dat : filedat){dataTrain.add(extract(dat.split(" ")[1]));}
                    pointbypoint(dataTrain);
                }
            case "historicData":
                
                
                break;
                
        }
        
     }
    
    
    
    void pointbypoint(Vector<Double>fdat){
        
        
        int i=0;
        Double max   = 0.0;
        Double min   = 0.0;
        
        for(Double pt : fdat){
            if(i>0){
                deriv.add(pt - fdat.get(i-1));
                if(deriv.get(i-1)==0||(deriv.get(i-1)<1 && deriv.get(i-1)>-1)){
                    localxrema.add(pt);
                    }
                }
            i++;
            if(pt>max){max = pt;}  
            
            if(deriv.size()>2){
                deriv2nd.add(deriv.get(deriv.size()-1) - deriv.get(deriv.size()-2));
            }
        }
        Collections.sort(fdat);
        min = fdat.get(1);
        System.out.println("\t - "+deriv.size()+" derivative values calculated");
        System.out.println("\t - "+deriv2nd.size()+" 2nd derivatives calculated");
        System.out.println("\t - "+localxrema.size()+" local maxima/minima detected. [Derivative = 0]");
        System.out.println("\t - global maximum at "+max);
        System.out.println("\t - global minimum at "+min);
        for(Double d : localxrema){System.out.println("\t\to $"+d);}
        
    }
    
    /** */
    Vector<String> getFileContents(String path,String fname){
        //Initialize variables
        Vector <String> fileContents = new Vector<>();
        File f = Paths.get(path,fname).toFile();
        BufferedReader br = null;
        //
        try{
            br = new BufferedReader(new FileReader(f));
            String ln;
            while((ln = br.readLine()) != null){
                Log(ln);
                fileContents.add(ln);}
            br.close();
        }catch(FileNotFoundException e){Log("File Not Found");}
        catch(IOException e){e.printStackTrace();}
         
        return fileContents;
    }
    
  
    
    
      /** <Extract> double from input String */
    public static double extract(String str) {
            Double dig = 0.0;
            Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
            while (m.find()) {
                return Double.parseDouble(m.group(1));
            }
            return dig;
        }
    
       /* Log method */
    public static void Log(String in) {
        BufferedWriter writer = null;
        try {
            Path p = Paths.get("/projects/BTC/src/Network", "./results.txt");
            File log = p.toFile();
            writer = new BufferedWriter(new FileWriter(log, true));
            writer.write(in + "\n");// <--logs it here
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
    public static class Neuron  implements Runnable{
        
        public static Vector<Vector<String>> batch;
        public static Vector<String> self;
        public static String date;
        public static String mkt;
        public static String value;
        public static int batchSize;
        public static String units;
        
        public Neuron(Vector<Vector<String>> pointCollection){
            Neuron.batch = pointCollection;
            Neuron.batchSize = pointCollection.size();
            
        }
        
        /** TODO*/
        public void run(){}
        
        public Vector<Vector<String>> findPoint(Vector<Vector<String>> chunk,String value){
            int location = -1;
            Vector<Vector<String>> result = new Vector<Vector<String>>();
             
            for(Vector<String> vec : chunk){
                if(vec.get(2).compareTo(value)==0){result.add(vec);}
            }   
                
            return result;
        }
        
        /** TODO */
        void compareToLocalMaxima(){}
        
        
        
    }
    
    
    public static void main(String[]args){
        if(args.length>2){new DataTrainingLayer(args[0],args[1],args[2]);}
        else{Log("Incorrect Arguments");}  
        
    }
    
}/** <EndOf_DataTrainingLayer> */
