package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.SwingWorker;
import wordspaces.Model;

/**
 *
 * @author alexander
 */
public class BatchModelEvaluatorWorker extends SwingWorker<Object,Object>{

    File[] modelFiles;
    File xmlConfigFile;
    Map<Integer, Vector> groupsMap;
    Map<String,Object> tasks;

    Model model;
    
    public BatchModelEvaluatorWorker(File[] modelFiles, File xmlFile){
        this.modelFiles = modelFiles;
        this.xmlConfigFile = xmlFile;
    }


    @Override
    protected Object doInBackground() throws Exception {     
        final XMLParserWorker parser = new XMLParserWorker(xmlConfigFile);
        parser.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                if ("groups".equals(evt.getPropertyName())) {
                    System.out.println("Groups ready");
                    groupsMap = (Map<Integer, Vector>) evt.getNewValue();
                }
                if ("tasks".equals(evt.getPropertyName())) {
                    tasks = (Map<String, Object>) evt.getNewValue();
                    System.out.println("Tasks ready");
                }
            }            
        });

        parser.execute(); parser.get();
        System.out.println("parser finished !"+groupsMap.get(0)+"   tasks:"+tasks.get("delete"));
        Iterator<String> taskIter = tasks.keySet().iterator();
        String cmd;
        /* For each file, load its model and process it ! */
        for(File file: modelFiles){
            final LoadModelThread modelLoader = new LoadModelThread(file);
            modelLoader.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("modelLoaded".equals(evt.getPropertyName())) {
                        model = (Model) evt.getNewValue();                        
                        firePropertyChange("progress",null, null);
                    }
                    if(modelLoader.isDone()){

                    }
                }                
            });  
            modelLoader.execute(); modelLoader.get();
            
            System.out.println("Loaded "+model.toString());
            while(taskIter.hasNext()){
                cmd = taskIter.next();
                if(cmd.equals("merge")){
                    executeMergeTasks(model,(Map<String, String>) tasks.get(cmd));
                }
                else if(cmd.equals("remove")){
                    
                }
            }
        }

        return null;
    }

    public void executeMergeTasks(Model model, Map<String, String> tasks){

    }

}
