/*
 * WordClassBuilder.java
 *
 * Created on 3. Juni 2007, 22:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class WordClassBuilder extends SwingWorker<Object, Integer>{
    
    public static final float THRESHOLD = (float)0.8;
    
    private static SortedMap<String, SortedMap> wordMap;
    
    //occurences is a reference to model.wordOccurences which saves the freq
    private static Map<String, Integer> occurences;
    /**
     * Creates a new instance of WordClassBuilder
     */
    public WordClassBuilder(Model m) {
        wordMap    = m.wordDirectory;
        occurences = (SortedMap<String, Integer>) m.wordOccurences;       
    }
    
    public void buildWordClasses(){
        String[] wordArray = (String[]) wordMap.keySet().toArray();
        String word        = null;
        String stem        = null;
        int wordPos      = 0;
                
        for(int i=0;i<wordArray.length;i++){             
            stem = wordArray[i];                    //take a word that might be a stem for others      
            wordPos = i + 1;                                 //from wordPos start the while loop to look for sim. words
            word = wordArray[wordPos];         
            while(wordArray.length > wordPos && stem.charAt(0) == word.charAt(0)){
                //System.out.println("Sim:"+stem+"   "+word+" is"+computeSimilarity(stem,word));
                //stop searching for similar words when the first letter changes or the end of the array is reached...                
                if(computeSimilarity(stem,word) >= THRESHOLD){                        //this indicates that the two words are similar               
                    firePropertyChange("merge", null, stem+" and "+word+" got merged...");
                    firePropertyChange("remove",null,wordPos);
                    mergeContextMaps(wordMap.get(stem), wordMap.get(word));
                    occurences.put(stem, occurences.get(stem)+occurences.get(word));
                    wordMap.remove(word);                       //'word' and its context must be deleted
                    wordArray = (String[]) wordMap.keySet().toArray();     //the array must also be updated due to the remove
                }
                else wordPos++;     //when sim was big enough there is no need for wordPos++ since 'word' was deleted and we are 
                                    //already at the next wordPos !
                if(wordPos < wordArray.length){      
                    word = wordArray[wordPos];
                }
                
            }
            firePropertyChange("progress",0,null);          
   //       shrinkContextWords((TreeMap) wordMap.get(stem));
        }   
    }
    
    public void shrinkContextWords(SortedMap contextMap){
        Iterator iter    = contextMap.keySet().iterator();
        int stemFreq     = 0;     //frequency of the word that is the stem
        int wordFreq     = 0;     //frequency of the word that is going to be reduced to the stem
        String stem      = null;
        String word      = null;
        
        if(iter.hasNext())
            stem = (String) iter.next();     //take a word that might be a stem for others
        
        while(iter.hasNext()){
            //then take the next word and compare both
            word = (String) iter.next();
            
            if(computeSimilarity(stem,word) >= THRESHOLD){
                stemFreq = (Integer) contextMap.get(stem);
                wordFreq = (Integer) contextMap.get(word);
                contextMap.put(stem,stemFreq+wordFreq);
                iter.remove();
            }else{
                stem = word;
            }
        }
    }
    
    
    /** Merges two contexts together. The two contexts are given as Treemaps  
     * and the stemContext represents the treemap to which we merge.
     * @param stemContext The TreeMap to which we merge
     * @param similarWordContext is merged into stemContext
     */
    public static void mergeContextMaps(Map v1, Map v2){
        Iterator iter = v2.keySet().iterator();
        String contextWord = null;
        double freq;
        
        while(iter.hasNext()){
            contextWord = (String)iter.next();
                   
            if(v1.containsKey(contextWord)){
                freq = (Double) v1.get(contextWord);
                freq = freq + (Double) v2.get(contextWord);
            } else{
                freq = (Double) v2.get(contextWord);    
            }
            v1.put(contextWord,freq);
        }   
    }
    
    private static float computeSimilarity(String w1, String w2){
        int simChars = 0;
        int restChar = 0;
            
        //compute number of similar characters
        while(w1.length() > simChars && w2.length() > simChars && w1.charAt(simChars) == w2.charAt(simChars)) simChars++;
            
        //compute number of different characters
        //for 'theories' and 'theory' this is 4
        restChar += w1.length() - simChars;
        restChar += w2.length() - simChars;
        
        //differ the words only in an attachement or due to different chars ?
        if(simChars < w1.length()){ //they differ due to different chars !
            //w1 could be e.g. 'theories' and w2 'theory'
            if( !(    (w1.charAt(simChars) == 'e' || w1.charAt(simChars) == 'i')   &&    (w2.charAt(simChars) == 'i' || (w2.charAt(simChars) == 'y'))    ))                
                restChar = 2*restChar;    //make it less likely when above condition is not given e.g. not the case that w1 'theories' and w2 'theory'         
            
        }else{                      //they differ due to an attachement on w2
            String attachement = w2.substring(simChars);
            if(attachement.equals("ed") || attachement.equals("d")){
                return 1;
            }else if(attachement.equals("ing")){
                return 1;
            }else if (attachement.equals("al")){
                return 1;
            }else if(attachement.equals("able")){
                return 1;
            }else if(attachement.equals("ance")){
                return 1;
            }else if(attachement.equals("ors")){
                return 1;
            }else if(attachement.equals("es") || attachement.equals("s")){
                return 1;
            }else if(attachement.equals("ly")){
                return 1;
            }else if(wordMap.containsKey(attachement)){    // -->check whether attachement is in wordMap
                return 0;
            }
        }
        return (float)simChars/(float)restChar;

  //      return (float)n/(float)w1.length();
  //      return w2.indexOf(w1)+1;
        
        
    }

    protected Object doInBackground() throws Exception {
        buildWordClasses();
        return null;
    }
    
}
