/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import javax.swing.SwingWorker;
import wordspaces.Model;
import wordspaces.ModelSaver;
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
                        /* Now the model is being saved ! */
                        firePropertyChange("finished",0,ModelSaver.saveModel(model));       
                    }
                }              
            });
            if(!this.isCancelled()){
                thread.execute(); thread.get();
            }
            else{
                /* FileParserBalancerWorker invokes the cancel() on the parser */
                thread.cancel(false);
                firePropertyChange("cancelled",0,null);
                System.out.println("BatchProcessingWorker cancelled !");
                return null;
            }          
        }
                           
        return null;
    }
}
