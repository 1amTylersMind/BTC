//package DataSets;

import java.util.*;
import java.util.Calendar;
import java.nio.file.*;
import java.util.regex.*;
import java.io.*;
/**
 * "that which exists, but is constantly changing"
 */
public class Maya {
    
    //Paths for different OS'
    public static String p1;
    public static String p2;
    
    public static String name;
    public static String subCategory;
    public static File file;
    
    public static Map<String,Vector<Double>> abstraction = new HashMap<>(); 
    public static Vector<String>         tableParameters = new Vector<>();
    Vector<Vector<Double>> DATA = new Vector<>();
    
    public static Vector<String>            date = new Vector<>();
    public static Vector<Integer>           time = new Vector<>();
    
    public Maya(String path,String fname,String type){
        initializeTime();
            Maya.p1 = path;//dockerpath
            //args[0]=p2;//alt
            Maya.name = fname ;//filename
            Maya.subCategory  = type;//type 
            System.out.println(
        "Constructing DataSet Abstraction with:\n"+
        "\t-Path: "     + Maya.p1   + "\n"+
        "\t-FileName: " + Maya.name + "\n"+
        "\t-Type: "     + Maya.subCategory);
        
        //Get whatever dataset it is into Map<String,Vector<Double>>
        Maya.abstraction = Maya.contructs();
        // then isolateComponent() of first row which holds parameters 
        System.out.println(Maya.tableParameters.size()+" Parameters Initialized ");
        List <String> timeSeries = new ArrayList<String>(Maya.abstraction.keySet());
        System.out.println("Created time series vector with "+
                                        timeSeries.size()+" data points");
        interpretTimeSeries(timeSeries);
        
        // iterate through each column, which is a diff mkt or val. 
        for(int i = 1;i<Maya.tableParameters.size();i++){
            Vector <Double> column = isolateComponent(Maya.abstraction,i);
            System.out.println(column.size()+" points for parameter "+Maya.tableParameters.get(i));
            DATA.add(column);
        }
        //Configure the time intervals/range of the data set 
        interpretTimeSeries(timeSeries);
        
    }
    
    /** TODO: Fix AM/PM */
    static void initializeTime(){
        
        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Locale [] places = Calendar.getAvailableLocales();
        Vector <String> date = new Vector<>();
        
        String dayWk  = today.getTime().toString().split(" ")[0];
        String month  = today.getTime().toString().split(" ")[1];
        String day    = today.getTime().toString().split(" ")[2];
        String time   = today.getTime().toString().split(" ")[3];
        String zone   = today.getTime().toString().split(" ")[4];
        String yr     = today.getTime().toString().split(" ")[5];
        
        String hr  = time.split(":")[0];
        int min = Integer.parseInt(time.split(":")[1]);
        int sec = Integer.parseInt(time.split(":")[2]);
        boolean pm ;
        int hour = Integer.parseInt(hr) - 5;
        if(Integer.parseInt(hr)<0){hour = 12 - 5 + Integer.parseInt(hr);pm=true;}
        else{pm=false;}
        int AmPm = -1;
        if(pm=false){AmPm=0;}
        else{AmPm=1;}
        String ampm;
        if(AmPm==0){ampm="AM";}
        else{ampm="PM";}
        
        System.out.println(" - Initialized on "+dayWk+" "+month+" "+day+" "+yr+" at "+
                           hour +":"+min  +":"+sec+" "+ampm);
        //Fill Maya.date field                   
        Maya.date.add(month);
        Maya.date.add(day);
        Maya.date.add(yr);
        //Fill Maya.time field 
        Maya.time.add(hour);
        Maya.time.add(min);
        Maya.time.add(sec);
        Maya.time.add(AmPm);//|0 for AM | 1 for PM|  
        
        
    }
    
    static void interpretTimeSeries(List<String>dates){
        
    }
    
    
    
    static Map<String,Vector<Double>> contructs(){
        Map<String,Vector<Double>> filedata = new HashMap<>();
        Vector <String> fileContents = new Vector<>();
        Vector <String> dates = new Vector<>();
        File f = Paths.get(Maya.p1,Maya.name).toFile();
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
             int index = 0;
             int nUsableDataLns = 0;
             int nUsableDates = 0;
            while((line = br.readLine()) != null){
                line+="\n";
                if(line.contains("Time")){
                    Vector<String> tableParams = new Vector<String>();
                    System.out.println("Defining table parameters:\n");
                    for(String e : line.split(",")){
                        tableParams.add(e);
                        System.out.print("["+e+"] ");
                    }
                    Maya.tableParameters = tableParams;
                }else{
                    Vector<Double> row = new Vector<>();
                    int ref = 0;
                    String date = line.split(",")[0];
                    for(String e : line.split(",")){       
                        row.add(extract(e));
                        filedata.put(date,row);
                    }
                    
    
                }
                
        
               index++;
            }
            
            br.close();
            System.out.println(filedata.size()+" rows vectorized");
            }catch(FileNotFoundException e){System.out.println("File Not Found!\n"+f.getPath());}
            catch(IOException e){e.printStackTrace();}
            return filedata;
    }
    
    static void getDimensions(){}
    
    static Vector<Double> isolateComponent(
                          Map<String,Vector<Double>> dataset,
                          int col){
        Vector<Double> column = new Vector<>();
        for(Map.Entry<String,Vector<Double>>entry:dataset.entrySet()){
           column.add(entry.getValue().get(col));
        }
        return column;
    }
    
    
    public static void main(String[]args){
     
        
        
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
    
    
    
}
