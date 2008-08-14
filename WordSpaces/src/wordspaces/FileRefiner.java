/*
 * FileRefiner.java
 * 
 * Created on 21.08.2007, 12:47:54
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander frey afrey@uos.de
 */
public class FileRefiner {

    /** Removes the special characters and brakes the file
     * into the single sentences.
     * @param file 
     * @return 
     */
    public static File filterFile(File file){
        if(file != null){
            File outputFile = new File(file.getAbsoluteFile()+".filtered.txt");
            BufferedReader reader = null;
            BufferedWriter writer = null;
            int charValue = 0; 
            boolean lastCharWasWhiteSpace = true;
                                
            try {
                reader = new java.io.BufferedReader(new java.io.FileReader(file));
                writer = new BufferedWriter(new FileWriter(outputFile));

                while((charValue = reader.read()) != -1){
                    //in this case we detected a letter
                    if(Character.isLetter(charValue)){
                        writer.write(charValue);
                        lastCharWasWhiteSpace = false;
                    }
                    //in this case we detected the end of a sentence
                    else if(SpecialCharacters.getEndofSentenceChars().contains((char)charValue)){ 
                        writer.newLine();
                        lastCharWasWhiteSpace = true;
                    } 
                    //in this case we detected a white space
                    else{ 
                        if(!lastCharWasWhiteSpace){
                            writer.write(32);
                            lastCharWasWhiteSpace = true;
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
            return outputFile;
        }
        return null;
    }
}
