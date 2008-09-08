package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class FileFilterBalancerWorker extends SwingWorker<File, Integer>{

    private File[] files;
    
    private Vector<FileFilterWorker> queue;
    
    /* Only allow two threads simultaniously to filter text files */
    private final Semaphore semaphore = new Semaphore(2);
    
    public FileFilterBalancerWorker(File[] files){
        this.files = files;
        queue = new Vector();
    }
    
    protected File doInBackground() throws Exception {
        for(File file: files){
            semaphore.acquire();
            final FileFilterWorker thread = new FileFilterWorker(file);
            thread.addPropertyChangeListener(new PropertyChangeListener(){
                boolean done = false;
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        firePropertyChange("progress",0,evt.getNewValue());
                    }
                    if(thread.isDone() && !done){
                        done = true;
                        queue.remove(thread);
                        semaphore.release();
                    }
                }
                
            });

            queue.add(thread);            
            thread.execute();   
        }
        if(queue.size() != 0)
            queue.firstElement().get();     //wait until the last thread has finished
        
        return null;
    }

}
