/*
 * FileParserThread.java
 *
 * Created on 3. Juni 2007, 19:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import javax.swing.SwingWorker;
import wordspaces.*;


/**
 *
 * @author alexander
 */
public class FileParserBalancerWorker extends SwingWorker<Model, Integer>{
    
    private Vector<File> files;
    private Parser parser;
    private Model model;
    
    /* Only allow two threads simultaniously to parse text to a model */
    private final Semaphore semaphore = new Semaphore(2);
    
    private Vector<FileParserWorker> queue;

    
    public FileParserBalancerWorker(Parser p, Vector<File> files, Model m) {
        this.files  = files;
        this.parser = p;
        this.model  = m;
        this.queue = new Vector();
    }
    
    public FileParserBalancerWorker(Parser p, File file, Model m) {
        this(p,new Vector(),m);
        files.addElement(file);
    }


    protected Model doInBackground()throws Exception {       
        /* For each file start a fileParserWorker. This way we can control
         * the number of threads that parse a file into a model... */
        for(final File file: files){
            semaphore.acquire();
            
            final FileParserWorker thread = new FileParserWorker(parser,model,file);
            thread.addPropertyChangeListener(new PropertyChangeListener() {
                boolean done = false;
                public  void propertyChange(PropertyChangeEvent evt) {
                    if(thread.isDone() && !done){
                        done = true;
                        semaphore.release();
                        queue.remove(thread);
                        firePropertyChange("progress",0,file);
                    }
                    
                }
            });
            queue.addElement(thread);
            thread.execute();          
        }
        while(queue.size() != 0)
         //   queue.firstElement().get();
                      Thread.sleep(150);
        
        System.out.println("Balancer finished... queue is "+queue.size());            
        
        return model;
    }
    

}
