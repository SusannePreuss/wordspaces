/*
 * Model.java
 * 
 * Created on 28.05.2007, 12:50:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author alexander
 */
public class Model implements Serializable{
    
    private SortedMap<String,SortedMap<String,Double>> wordDirectory;
    
    //distances caches all computed distances between words.Should be cleared when new words are added to the model
    private SortedMap<String,SortedMap<String,Double>> distances;
    
    //saves the occurences of the words in wordDirectory from all texts
    private Map<String, Integer> wordOccurences;

    //contains the names of the parsed texts
    private Vector<String> parsedSources;
    
    private String name;
    

    public Model(String name) {
        wordDirectory  = Collections.synchronizedSortedMap(new TreeMap<String, SortedMap<String,Double>>());
        wordOccurences = Collections.synchronizedMap(new HashMap<String, Integer>());
        distances      = new TreeMap<String, SortedMap<String,Double>>();
        parsedSources  = new Vector<String>();       
        this.name      = name;
    }
    
    public Double lookupDistance(String w1, String w2){
        if(distances.containsKey(w1)){
            Map map = distances.get(w1);
            if(map.containsKey(w2))
                return (Double) map.get(w2);
        }
        
        return (double) -1;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public SortedMap<String,SortedMap<String,Double>> getWordDirectory(){
        return wordDirectory;
    }
    
    public SortedMap<String,SortedMap<String,Double>> getCachedDistances(){
        return distances;
    }
    
    public Map<String, Integer> getWordOccurences(){
        return wordOccurences;
    }
    
    public Vector<String> getParsedSources(){
        return parsedSources;
    }
    
    
    public String toString(){ return name; }
    
    public void eraseDistanceCache(){
        distances = new TreeMap<String, SortedMap<String,Double>>();
    }
}
