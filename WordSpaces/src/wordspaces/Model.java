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
    
    //saves the occurences of the vectors in wordDirectory
    private Map<String, Integer> wordVectorFrequency;

    //contains the names of the parsed texts
    private Vector<String> parsedSources;

    /* avoids thousands of dublicates of Strings in wordDirectory and in
     * each wordContextVector */
    private Map<String,String> stringCache;
    
    private Map<String, Double> stringFreq;
    
    private String name;
    

    public Model(String name) {
        wordDirectory  = new TreeMap<String, SortedMap<String,Double>>();
        wordVectorFrequency = Collections.synchronizedMap(new HashMap<String, Integer>());
        distances      = new TreeMap<String, SortedMap<String,Double>>();
        parsedSources  = new Vector<String>();
        stringCache      = new HashMap();
        stringFreq       = new HashMap();
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
    
    public Map<String, Integer> getWordVectorFrequency(){
 //       for(int i=0;i<1;i++){
 //          System.out.println(((Object[])wordVectorFrequency.keySet().toArray())[i]);
 //        }
        return wordVectorFrequency;
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

    public Map<String,String> getStringCache(){
        return stringCache;
    }

    public Map<String,Double> getStringFrequency(){
        return stringFreq;
    }

    public synchronized SortedMap<String,Double> addWordVector(String vectorName){
        if(!wordDirectory.containsKey(vectorName)){
            addWordtoWordCache(vectorName);
            wordDirectory.put(stringCache.get(vectorName), Collections.synchronizedSortedMap(new TreeMap<String,Double>()));
        }

        return wordDirectory.get(vectorName);
    }
    
    public synchronized double addContextWord(String vectorName, String contextWord){
        double freq = 0;
        addWordtoWordCache(contextWord);
        if(!wordDirectory.containsKey(vectorName)){
            addWordVector(vectorName).put(stringCache.get(contextWord), 1.0);
        }
        else if(!wordDirectory.get(vectorName).containsKey(contextWord)){
            wordDirectory.get(vectorName).put(stringCache.get(contextWord), 1.0);
        }
        else{
            freq = wordDirectory.get(vectorName).get(contextWord);         
            wordDirectory.get(vectorName).put(stringCache.get(contextWord), ++freq);
        }  
   
        return freq;
    }
    
    public synchronized void deleteWordVector(String word){       
        /* first check if the word is existing in wordDirectory... */
        if(wordDirectory.get(word) != null){
            /* run through all contextWords and remove them from the stringCache */
            Iterator<String> contextIter = wordDirectory.get(word).keySet().iterator();
            while(contextIter.hasNext()){
                removeWordfromStringCache(contextIter.next());
                contextIter.remove();
            }

            /* finally we remove also the vectorName */
            wordDirectory.remove(word);
            removeWordfromStringCache(word);
            wordVectorFrequency.remove(word);
        }    
    }

    public synchronized void deleteContextWord(String vectorName, String contextWord){
        wordDirectory.get(vectorName).remove(contextWord);
        removeWordfromStringCache(contextWord);

        /* no context word left in context vector, so we remove it too...*/
        if(wordDirectory.get(vectorName).size() == 0){
            deleteWordVector(vectorName);
        }
    }
    
    
    public void eraseDistanceCache(){
        distances = new TreeMap<String, SortedMap<String,Double>>();
    }
    
    private void addWordtoWordCache(String word){
        if(!stringCache.containsKey(word)){
            stringCache.put(word, word);
            stringFreq.put(stringCache.get(word), 1.0);
        }
        else{
            double freq = stringFreq.get(word);
            stringFreq.put(stringCache.get(word), ++freq);
        }
    }
    
    private void removeWordfromStringCache(String word){       
        if(stringCache.containsKey(word)){
            double freq = stringFreq.get(word);
            freq--;
            if(freq > 0)
                stringFreq.put(stringCache.get(word), freq);
            else{
                stringCache.remove(word);
                stringFreq.remove(word);
            }
        }
    }
    
    public String toString(){ return name; }
}
