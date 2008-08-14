/*
 * FileFilterThread.java
 *
 * Created on 1. Juni 2007, 01:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;
import wordspaces.*;

/**
 *
 * @author alexander
 */
public class FileFilterWorker extends SwingWorker<Object, Integer> {
    
    private File[] files;
    
    public FileFilterWorker(File[] f) {
        files = f;
    }

    @Override
    protected Object doInBackground() throws Exception {
        for(File f:files)
            firePropertyChange("progress",0,FileRefiner.filterFile(f));
        
        
        return null;
    }
    
}
