/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;
import wordspaces.Model;
import wordspaces.ModelSaver;

/**
 *
 * @author alexander
 */
public class SaveModelWorker extends SwingWorker<Object, Integer>{
    
    private Model model;
    
    public SaveModelWorker(Model m){
        model = m;
    }
    
    
    protected Object doInBackground() throws Exception {
        File file = null;
        if(model != null){
            file = ModelSaver.saveModel(model);
            if(file == null){
                firePropertyChange("error",0,"ModelSaver returned null!");                
            }               
        }        
        
        return file;
    }
}
