/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import wordspaces.Model;

/**
 *
 * @author alexander
 */
public class LoadModelThread extends SwingWorker<Model, Integer>{

    /* This is where we load the model from */
    File file;

    public LoadModelThread(File f){
        file = f;
    }
    
    protected Model doInBackground() throws Exception {
        Model model = null;

        if(file != null && file.exists()){
            ObjectInputStream ois = null;
            
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                model = (Model)ois.readObject();
                firePropertyChange("modelLoaded",null, model);  
            } catch (InvalidClassException ex) {
                JOptionPane.showMessageDialog(null, "Model is not compatible.", "Warning", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (IOException ex){
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return model;
    }

}
