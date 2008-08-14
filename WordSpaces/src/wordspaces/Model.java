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
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author alexander
 */
public class Model implements Serializable{
    
    public Map<String,TreeMap> wordDirectory;
    
    //distances caches all computed distances between words.Should be cleared when new words are added to the model
    public TreeMap<String,TreeMap<String,Double>> distances;
    
    //saves the occurences of the words in wordDirectory from all texts
    public Map<String, Integer> wordOccurences;

    //contains the names of the parsed texts
    public Vector<String> parsedSources;
    
    private String name;
    

    public Model(String name) {
        wordDirectory  = Collections.synchronizedSortedMap(new TreeMap<String, TreeMap>());
        wordOccurences = Collections.synchronizedMap(new HashMap<String, Integer>());
        distances      = new TreeMap<String, TreeMap<String,Double>>();
        parsedSources  = new Vector<String>();       
        this.name      = name;
    }
    
    public Double lookupDistance(String w1, String w2){
        if(distances.containsKey(w1)){
            TreeMap t = distances.get(w1);
            if(t.containsKey(w2))
                return (Double) t.get(w2);
        }
        
        return (double) -1;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String toString(){ return name; }
    
    public void eraseDistanceCache(){
        distances = new TreeMap<String, TreeMap<String,Double>>();
    }
}
