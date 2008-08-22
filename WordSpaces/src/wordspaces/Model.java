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
import java.util.Iterator;
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

    /* avoids thousands of dublicates of Strings in wordDirectory and in
     * each wordContextVector */
    public Map<String,String> wordCache;
    
    public Map<String, Double> wordFreq;
    
    private String name;
    

    public Model(String name) {
        wordDirectory  = new TreeMap<String, SortedMap<String,Double>>();
        wordOccurences = new HashMap<String, Integer>();
        distances      = new TreeMap<String, SortedMap<String,Double>>();
        parsedSources  = new Vector<String>();
        wordCache      = new HashMap();
        wordFreq       = new HashMap();
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
     
    public SortedMap<String,SortedMap<String,Double>> getCachedDistances(){
        return distances;
    }
    
    public Map<String, Integer> getWordOccurences(){
        return wordOccurences;
    }
    
    public Vector<String> getParsedSources(){
        return parsedSources;
    }

    public SortedMap<String,Double> getContextVector(String vectorName){
        if(wordDirectory.containsKey(vectorName))
            return wordDirectory.get(vectorName);

        else
            return null;
    }

    public String getVectorNameAt(int pos){
        if(pos < 0 || pos >= wordDirectory.size())      //out of bounds !
            return null;

        return (String) wordDirectory.keySet().toArray()[pos];
    }

    public int getDirectorySize(){
        return wordDirectory.size();
    }

    public synchronized SortedMap<String,Double> addWordVector(String vectorName){
        if(!wordDirectory.containsKey(vectorName)){
            addWordtoWordCache(vectorName);
            wordDirectory.put(wordCache.get(vectorName), Collections.synchronizedSortedMap(new TreeMap<String,Double>()));           
        }

        return wordDirectory.get(vectorName);
    }
    
    public synchronized double addContextWord(String vectorName, String contextWord){
        double freq = 0;
        addWordtoWordCache(contextWord);
        if(!wordDirectory.containsKey(vectorName)){
            addWordVector(vectorName).put(wordCache.get(contextWord), 0.0);     //wordCache.get
        }
        else if(!wordDirectory.get(vectorName).containsKey(contextWord)){
            wordDirectory.get(vectorName).put(wordCache.get(contextWord), 0.0);    //wordCache.get
        }
        else{
            freq = wordDirectory.get(vectorName).get(contextWord);         
            wordDirectory.get(vectorName).put(wordCache.get(contextWord), ++freq);     //
        }  
   
        return freq;
    }
    
    public synchronized void deleteWordVector(String word){       
        /* run through all contextWords and remove them from the wordCache */
        Iterator<String> contextIter = wordDirectory.get(word).keySet().iterator();
        while(contextIter.hasNext()){
            removeWordfromWordCache(contextIter.next());
            contextIter.remove();
        }

        /* finally we remove also the vectorName */
        wordDirectory.remove(word);
        removeWordfromWordCache(word);
    }

    public synchronized void deleteContextWord(String vectorName, String contextWord){
        wordDirectory.get(vectorName).remove(contextWord);
        removeWordfromWordCache(contextWord);
    }
    
    
    public void eraseDistanceCache(){
        distances = new TreeMap<String, SortedMap<String,Double>>();
    }
    
    private void addWordtoWordCache(String word){
        if(!wordCache.containsKey(word)){
            wordCache.put(word,word);
            wordFreq.put(wordCache.get(word), 0.0);
        }
        else{
            double freq = wordFreq.get(word);
            wordFreq.put(wordCache.get(word), ++freq); //wordCache.get
        }
    }
    
    private void removeWordfromWordCache(String word){
        if(wordCache.containsKey(word)){
            double freq = wordFreq.get(word);
            freq--;
            if(freq > 0)
                wordFreq.put(wordCache.get(word), freq);
            else{
                wordCache.remove(word);
                wordFreq.remove(word);
            }
        }
    }
    
    public String toString(){ return name; }
}
