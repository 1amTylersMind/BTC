
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/** */
public class CSVtoTXT {
    //For Codenvy Build 
    public final String cpath = "/projects/BTC/src/DataSets";
    //For Linux Build
    public final String altpath = "/root/Desktop/BTC/src/DataSets";

    public CSVtoTXT(String fname,String os) {
	File f = null;
        if(os.compareTo("kali")==0){f = Paths.get(altpath,fname).toFile();}
	else{f = Paths.get(cpath,fname).toFile();}
        run(f);    
    
    }
    
    void run(File f){
        Vector<String>fileContents = new Vector<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
             int index = 0;
            while((line = br.readLine()) != null){
                String ln  = index+ "";
                for(String element : line.split(",")){
                    if(element.contains("-")==false){
                        fileContents.add(element);
                        ln+= " "+element;
                    }
               }
               index++;
               System.out.println(ln);
            }
            br.close();
        
        }
        catch(FileNotFoundException e){System.out.println("File Not Found!\n"+f.getPath());}
        catch(IOException e){e.printStackTrace();}
        
    }

    public static void main(String[] args) {
        new CSVtoTXT(args[0],args[1]);
    }

}
