/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;
import wordspaces.IrregularVerbsStemmer;

/**
 *
 * @author alexander
 */
public class IrregularVerbsStemmerWorker extends SwingWorker<Object, Integer>{
    
    private File[] files;
    
    public IrregularVerbsStemmerWorker(File[] f){
        files = f;
    }
    
    @Override
    protected Object doInBackground() throws Exception {   
        for(File f:files)
            firePropertyChange("progress",0,IrregularVerbsStemmer.stemIrregularVerbs(f));
        
        
        return null;
    }

}
