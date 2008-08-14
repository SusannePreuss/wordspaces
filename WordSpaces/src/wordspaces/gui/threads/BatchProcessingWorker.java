/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import wordspaces.Model;
import wordspaces.Parser;

/**
 *
 * @author alexander
 */
public class BatchProcessingWorker extends SwingWorker<Object, Integer>{

    private Vector<File> textFiles;
    private Parser[] parserVector;  


    public BatchProcessingWorker(Vector textFiles, Parser[] parser) {
        this.textFiles    = textFiles;
        this.parserVector = parser;
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        /* For each parser create a model,parse the text and save it 
         * all in a file. */
        for(Parser parser:parserVector){
            final Model model = new Model(parser+"-SW"+parser.isStopWordsEnabled()+"-FL"+parser.isFillerEnabled()+"-FO"+parser.isFocusWordsEnabled());
            final FileParserBalancerWorker thread = new FileParserBalancerWorker(parser,textFiles,model);
            thread.addPropertyChangeListener(new PropertyChangeListener() {
                boolean done = false;
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        /* The progress reported by the fileParserThread is just
                         * passed forward... */
                        firePropertyChange("progress",0,evt.getNewValue());
                    }              
                    if(thread.isDone() && !done){
                        done = true;
                        try {
                            SaveModelWorker task = new SaveModelWorker(model);
                            task.execute();task.get();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BatchProcessingWorker.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(BatchProcessingWorker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        firePropertyChange("finished",0,model);            
                    }
                }              
            });
            thread.execute(); thread.get();
        }        
                           
        return null;
    }
}
