/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class ModelSaver {
    
    public static File saveModel(Model model){
        if(model != null){
            File file = new File(model+".model");
            ObjectOutputStream oos = null;
            
            if(!file.exists()){
                try {
                    file.createNewFile();
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
            else{           //File already exists
                return null;
            }
            try{
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(model);
                System.out.println("Model has been saved to file "+file.getPath());
                return file;
            }
            catch (IOException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    oos.close();
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
