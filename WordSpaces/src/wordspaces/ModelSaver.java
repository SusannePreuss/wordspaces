/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author alexander
 */
public class ModelSaver {
    
    public static File saveModel(Model model){
        int dublicate_counter = 1;
        if(model != null){
            File file = new File(model+".model");
            
            while(file.exists()){
                dublicate_counter++;
                file = new File(model+"("+dublicate_counter+")"+".model");
            }
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                System.out.println(ex);
            }
            
            ObjectOutputStream oos = null;
            try{
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(model);
                System.out.println("Model has been saved to file "+file.getPath());
                return file;
            }
            catch (IOException ex) {
                System.out.println(ex);
            }
            finally {
                try {
                    oos.close();
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
        return null;
    }
}
