/*
 * DistanceCalculatorWorker.java
 *
 * Created on 14. Juni 2007, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import javax.swing.SwingWorker;
import plugins.ComputesDistance;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import exceptions.DimensionNotEqualException;
import java.util.Map.Entry;
import java.util.SortedMap;
import plugins.EuclideanDistance;
import wordspaces.Fust;

/**
 *
 * @author alexander frey. afrey@uos.de
 */
public class DistanceCalculatorWorker extends SwingWorker<Object,String>{
    
    private TreeMap<String,SortedMap> vectors;
    private Map cachedDistances;
    private ComputesDistance cDist;

    
    public DistanceCalculatorWorker(TreeMap<String,SortedMap> vectors, Map cache, ComputesDistance cDist){
        this.cDist           = cDist;
        this.vectors         = vectors;
        this.cachedDistances = cache; 
    }
    
    
    protected Object doInBackground() throws Exception {
        try {
            getDistances();
        } catch (DimensionNotEqualException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public void getDistances() throws DimensionNotEqualException{
        Entry<String, SortedMap> firstEntry  = null;
        Iterator iter            = null;
        String word              = null;
        String compareWord       = null;
        SortedMap vector         = null;
        SortedMap compareVector  = null;
        double distance;
        
        while(!vectors.isEmpty()){
            /* Get and delete the first entry from vectors */
            firstEntry = vectors.pollFirstEntry();
            word       = (String)firstEntry.getKey();
            vector     = (SortedMap)firstEntry.getValue();
            iter       = vectors.keySet().iterator();

            /* Run through the rest of the entries from vector */
            while(iter.hasNext()){
                compareWord   = (String)iter.next();
                compareVector = vectors.get(compareWord);
                
                //check whether the distance between the two words has already been computed
                if(!cachedDistances.containsKey(word) || !((SortedMap)cachedDistances.get(word)).containsKey(compareWord)  ){
                    Fust.mergeVectors(vector,compareVector);
                    distance = sum(cDist.compute(vector,compareVector));

                    if(cDist instanceof EuclideanDistance)
                        distance = 1 / Math.sqrt(distance);
                    
                    //now save the results to cachedDistances
                    if(cachedDistances.containsKey(word)){
                        ((TreeMap)cachedDistances.get(word)).put(compareWord,distance);                       
                    } else {
                        cachedDistances.put(word, new TreeMap<String, Double>());
                        ((TreeMap)cachedDistances.get(word)).put(compareWord,distance);
                    }
                    if(cachedDistances.containsKey(compareWord)){
                        ((TreeMap)cachedDistances.get(compareWord)).put(word,distance);   
                    } else {
                         cachedDistances.put(compareWord, new TreeMap<String, Double>());
                        ((TreeMap)cachedDistances.get(compareWord)).put(word,distance);
                    }
                }               
            }
            this.firePropertyChange("progress",null,word);
        }
    } 
      
    public static double getDistance(SortedMap vector1, SortedMap vector2, ComputesDistance cDist) throws DimensionNotEqualException{        
        Fust.mergeVectors(vector1, vector2);
        return sum(cDist.compute(vector1, vector2));
    } 
    
    public static double sum(double[] vector){
        double result = 0;
        for(int i=0; i<vector.length;i++){
            result += vector[i];
        }
        
        return result;
    }

}
