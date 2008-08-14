/*
 * SimpleStemmer.java
 *
 * Created on 6. Juni 2007, 10:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class IrregularVerbsStemmer {
    
    /** Creates a new instance of SimpleStemmer */
    public IrregularVerbsStemmer() {
    }
    
    public static File stemIrregularVerbs(File file){
        IrregularVerbs verbs = new IrregularVerbs();
        File outputFile = new File(file.getAbsoluteFile()+".stemmed.txt");
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String[] inputLine = null;
        String verbBase = null;
        String outputLine = new String();
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            
            while(reader.ready()){
                inputLine = reader.readLine().toLowerCase().split("\\s");
                
                for(String s:inputLine){
                    verbBase = verbs.getBaseForm(s);
                    if(verbBase != null){
                        s = verbBase;
                    }
                    /*
                    else if(s.endsWith("s")){
                        s = s.substring(0,s.length()-1);
                    }
                    if(s.endsWith("ing")){
                        s = s.substring(0,s.length()-3);
                    }
                    else if(s.endsWith("ed")){
                        s = s.substring(0,s.length()-2);
                    }
                    **/
                    outputLine = outputLine.concat(" "+s); 
                }
                writer.write(outputLine);
                writer.newLine();
                outputLine = "";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } 
        }
        
        return outputFile; 
    }
    
}
