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
public class FileFilterBalancer extends SwingWorker<File, Integer>{

    private File[] files;
    
    private Vector<FileFilterWorker> queue;
    
    /* Only allow two threads simultaniously to filter text files */
    private final Semaphore semaphore = new Semaphore(2);
    
    public FileFilterBalancer(File[] files){
        this.files = files;
    }
    
    protected File doInBackground() throws Exception {
        for(File file: files){
            semaphore.acquire();
            final FileFilterWorker thread = new FileFilterWorker(file);
            thread.addPropertyChangeListener(new PropertyChangeListener(){
                boolean done = false;
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        firePropertyChange("progress",0,thread.getFile());
                    }
                    if(thread.isDone() && !done){
                        done = true;
                        queue.remove(thread);
                        semaphore.release();
                    }
                }
                
            });
            queue.addElement(thread);
            thread.execute();
        }
        if(queue.size() != 0)
            queue.firstElement().get();     //wait until the last thread has finished
        
        return null;
    }

}
