package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.SwingWorker;
import wordspaces.Fust;
import wordspaces.GroupGradeCalculator;
import wordspaces.Model;

/**
 *
 * @author alexander
 */
public class BatchModelEvaluatorWorker extends SwingWorker<Object,double[]>{

    File[] modelFiles;
    File xmlConfigFile;
    Map<Integer, Vector> groupsMap;
    Model model;
    
    public BatchModelEvaluatorWorker(File[] modelFiles, File xmlFile){
        this.modelFiles = modelFiles;
        this.xmlConfigFile = xmlFile;
    }


    @Override
    protected Object doInBackground() throws Exception {     
        Map<String,Object> tasks;
        Map<Integer,Vector> groups;
        XMLParserWorker parser = new XMLParserWorker(xmlConfigFile);
   //     parser.addPropertyChangeListener(new ParserChangeListener());
        
        parser.execute(); parser.get();
        tasks = parser.getTasks();
        groups = parser.getGroups();
        
        Iterator<String> taskIter = tasks.keySet().iterator();
        String cmd;
        /* For each file, load its model and process it ! */
        for(File file: modelFiles){
            final LoadModelThread modelLoader = new LoadModelThread(file);
            modelLoader.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("modelLoaded".equals(evt.getPropertyName())) {
                        firePropertyChange("progress",null, null);
                    }
                }                
            });  
            modelLoader.execute();
            model = modelLoader.get();
            
            /* Execute all tasks */
            while(taskIter.hasNext()){
                cmd = taskIter.next();
                if(cmd.equals("merge")){
                    executeMergeTasks(model,(Map<String, String>) tasks.get(cmd));
                }
                else if(cmd.equals("remove")){
                    executeRemoveTasks(model, (Vector<String>) tasks.get(cmd));
                }
            }

            /* Calculating GroupGrade */
            if(groups != null){
                firePropertyChange("results",model, GroupGradeCalculator.calcGroupGradeButtonActionPerformed(model, groups));
            }
        }

        return null;
    }

    public void executeMergeTasks(Model model, Map<String, String> tasks){
        Iterator<String> tasksIter = tasks.keySet().iterator();
        String first, second;
        Map firstMap, secMap;
        while(tasksIter.hasNext()){
            first  = tasksIter.next();
            second = tasks.get(first);
            firstMap = model.getContextVector(first);
            secMap   = model.getContextVector(second);
            Fust.mergeContextMaps(firstMap, secMap);
            System.out.println(model+":: Merged "+first+" and "+second);
        }
    }

    public void executeRemoveTasks(Model model, Vector<String> tasks){
        for(String word:tasks){
            model.deleteWordVector(word);
            System.out.println(model+":: Removed "+word);
        }
    }
    
    class ParserChangeListener implements PropertyChangeListener{
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("groups".equals(evt.getPropertyName())) {
            }
            if ("tasks".equals(evt.getPropertyName())) {
            }
            if ("startDoc".equals(evt.getPropertyName())) {
            }
            if ("endDoc".equals(evt.getPropertyName())) {
            }
        }            
    }
}

