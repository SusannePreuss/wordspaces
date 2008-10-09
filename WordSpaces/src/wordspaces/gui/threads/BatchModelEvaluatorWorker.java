package wordspaces.gui.threads;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.SwingWorker;
import plugins.ComputesDistance;
import wordspaces.FrequencyFilter;
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
    ComputesDistance cDist;
    Map<Integer, Vector> groupsMap;
    Model model;
    
    public BatchModelEvaluatorWorker(File[] modelFiles, File xmlFile, ComputesDistance cDist){
        this.modelFiles = modelFiles;
        this.xmlConfigFile = xmlFile;
        this.cDist      = cDist;
    }


    @Override
    protected Object doInBackground() throws Exception {     
        Map<String,Object> tasks;
        Map<Integer,Vector> groups;
        XMLParserWorker parser = new XMLParserWorker(xmlConfigFile);
        
        parser.execute(); parser.get();
        tasks = parser.getTasks();
        groups = parser.getGroups();
        
        Iterator<String> taskIter;
        String cmd;
        double[] result;
        TreeMap<String, SortedMap> wordVectorMap;
        String vectorName;
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
            double time = System.currentTimeMillis();
            taskIter = tasks.keySet().iterator();
            /* Execute all tasks */
            while(taskIter.hasNext()){
                cmd = taskIter.next();
                if(cmd.equals("merge")){
                    executeMergeTasks(model,(Map<String, String>) tasks.get(cmd));
                }
                else if(cmd.equals("remove")){
                    executeRemoveTasks(model, (Vector<String>) tasks.get(cmd));
                }
                else if(cmd.equals("filterfrequencies")){
                    executeFilterFrequencies(model, (Integer) tasks.get(cmd));
                }
                else if(cmd.equals("filterextremevalues")){
                    executeFilterExtremeValues(model,(Integer) tasks.get(cmd));
                }
            }
            System.out.println("There are "+model.getDirectorySize()+" word vectors left in model "+model);
            /* Calculating GroupGrade */
            if(groups != null){
                wordVectorMap = new TreeMap();
                /* First we have to build wordVectorMap<String, SortedMap<String, Double>> by iterating
                 * through all words in the model and putting them with their context vectors into
                 * wordVectorMap. */               
                for(int i=0; i<model.getDirectorySize();i++){
                    vectorName = model.getVectorNameAt(i);
                    wordVectorMap.put(vectorName, model.getContextVector(vectorName));
                }
                DistanceCalculatorWorker task = new DistanceCalculatorWorker(wordVectorMap, model.getCachedDistances(), cDist);
                task.execute(); task.get();
                result = GroupGradeCalculator.calcGroupGradeButtonActionPerformed(model, groups);
                firePropertyChange("results",model, result);
            }
            System.out.println("Time needed:"+(System.currentTimeMillis()-time));
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
            try{
                Fust.mergeContextMaps(firstMap, secMap);
                model.deleteWordVector(second);
                System.out.println(model+":: Merged "+first+" and "+second);
            }
            catch(NullPointerException e){
                System.out.println(e.getMessage());
            }            
        }
    }

    public void executeRemoveTasks(Model model, Vector<String> tasks){
        for(String word:tasks){
            model.deleteWordVector(word);
            System.out.println(model+":: Removed "+word);
        }
    }

    public void executeFilterExtremeValues(Model model, int percentage){
        Map<String, Map<String,Double>> selectedVectors = new HashMap();
        String selectedWord;
        for(int i=0;i<model.getDirectorySize();i++){
            selectedWord = model.getVectorNameAt(i);             
            selectedVectors.put(selectedWord, model.getContextVector(selectedWord));
        }
        
        FrequencyFilter.filterExtremeValues(selectedVectors, percentage);
    }
    
    public void executeFilterFrequencies(Model model, int upto){
        FrequencyFilter.filterFrequenciesInWordMap(model, upto);
    }
}

