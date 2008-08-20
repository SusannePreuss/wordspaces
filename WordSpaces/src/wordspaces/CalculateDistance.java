/*
 * CalculateDistance.java
 *
 * Created on 14. Juni 2007, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import javax.swing.SwingWorker;
import plugins.ComputesDistance;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import exceptions.DimensionNotEqualException;
import java.util.Map.Entry;
import java.util.SortedMap;
import plugins.EuclideanDistance;

/**
 *
 * @author alexander
 */
public class CalculateDistance extends SwingWorker<Object,String>{
    
    private TreeMap<String,SortedMap> vectors;
    private Map cachedDistances;
    private ComputesDistance cDist;

    
    public CalculateDistance(TreeMap<String,SortedMap> vectors, Map distances, ComputesDistance cDist){
        this.cDist           = cDist;
        this.vectors         = vectors;
        this.cachedDistances = distances; 
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
            firstEntry = vectors.pollFirstEntry();
            word       = (String)firstEntry.getKey();
            vector     = (SortedMap)firstEntry.getValue();
            iter       = vectors.keySet().iterator();
            
            while(iter.hasNext()){
                compareWord   = (String)iter.next();
                compareVector = vectors.get(compareWord);
                
                //check whether the distance between the two words has already been computed
                if(!cachedDistances.containsKey(word) || !((SortedMap)cachedDistances.get(word)).containsKey(compareWord)  ){
                    mergeVectors(vector,compareVector);
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
    
    
    public static void mergeVectors(Map v1, Map v2){
        Iterator iterV1 = v1.keySet().iterator();
        String entryV1 = null;
        String entryV2 = null;
        
        while(iterV1.hasNext()){
            entryV1 = (String) iterV1.next();  
            if(!v2.containsKey(entryV1)){
                v2.put(entryV1,new Double(0));
            }
        }
        
        Iterator iterV2 = v2.keySet().iterator();
        while (iterV2.hasNext()){
            entryV2 = (String) iterV2.next();  
            if(!v1.containsKey(entryV2)){
                v1.put(entryV2,new Double(0));
            }
        } 
    }
      
    public static double getDistance(SortedMap vector1, SortedMap vector2, ComputesDistance cDist) throws DimensionNotEqualException{        
        mergeVectors(vector1, vector2);
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
