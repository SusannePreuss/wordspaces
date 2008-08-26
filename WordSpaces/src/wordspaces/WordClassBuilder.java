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
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class WordClassBuilder extends SwingWorker<Object, Integer>{
    
    public static final float THRESHOLD = (float)0.8;
    
    private Model model;
    
    /* occurences is a reference to model.wordOccurences which saves the freq */
    private Map<String, Integer> occurences;
    
    /**
     * Creates a new instance of WordClassBuilder
     */
    public WordClassBuilder(Model m) {
        model      = m;
        occurences = (Map<String, Integer>) m.getWordVectorFrequency();
    }
    
    public void buildWordClasses(){
        String word = null;
        String stem = null;
        int wordPos = 0;
                
        for(int i=0;i<model.getDirectorySize();i++){             
            stem = model.getVectorNameAt(i);                    //take a word that might be a stem for others      
            wordPos = i + 1;                                 //from wordPos start the while loop to look for sim. words
            word = model.getVectorNameAt(wordPos);  
            /* before we look for similar words, we find word classes in
             * the context vector of stem. This is done first when we see stem
             * and then after it has been merged with another context vector. */
            shrinkContextWords(stem, model.getContextVector(stem));


            /* stop searching for similar words when the first letter changes 
             * or the end of the array is reached */
            while(model.getDirectorySize() > wordPos && stem.charAt(0) == word.charAt(0)){
                //System.out.println("Sim:"+stem+"   "+word+" is"+computeSimilarity(stem,word));
                
                
                if(computeSimilarity(stem,word) >= THRESHOLD){                        //this indicates that the two words are similar               
                    firePropertyChange("merge", null, stem+" and "+word+" got merged...");
                    firePropertyChange("remove",null,wordPos);
                    mergeContextMaps(model.getContextVector(stem), model.getContextVector(word));
                    occurences.put(stem, occurences.get(stem)+occurences.get(word));
                    model.deleteWordVector(word);           //'word' and its context must be deleted
                    
                    /* now as the maps got merged we need to look for wordClasses
                     * in the contextVector of stem again. */
                    shrinkContextWords(stem, model.getContextVector(stem));
                }
                /* when sim was big enough there is no need for wordPos++
                 * since 'word' was deleted and we are already at the next wordPos ! */
                else wordPos++;     
                                    
                /* get the next context Vector at position wordPos */
                if(wordPos < model.getDirectorySize()){
                    word = model.getVectorNameAt(wordPos);
                }
                
            }
            firePropertyChange("progress",0,null);          
        }   
    }
    
    public void shrinkContextWords(String vectorName, SortedMap<String,Double> contextMap){
        double stemFreq  = 0;     //frequency of the word that is the stem
        double wordFreq  = 0;     //frequency of the word that is going to be reduced to the stem
        String stem      = null;
        String word      = null;
        String[] context = contextMap.keySet().toArray(new String[contextMap.size()]);
        /* for each word in contextMap */
        for(int i=0;i<context.length;i++){
            /* take a word that might be a stem for others */
            stem = context[i];
            /* run through all following words as long as the first letters are
             * similar or the context_array_end hasn't been reached */
            for( int j=i+1 ; j<context.length && stem.charAt(0) == context[j].charAt(0) ; j++){
                /* get the next word */
                word = context[j];
                
                if(computeSimilarity(stem,word) >= THRESHOLD){
                    System.out.println(stem+" "+contextMap.get(stem)+ " is stem for "+word+" "+contextMap.get(word));
                    stemFreq = contextMap.get(stem);
                    wordFreq = contextMap.get(word);
                    contextMap.put(stem,stemFreq+wordFreq);
                    /* word must be deleted now */
                    model.deleteContextWord(vectorName, word);
                    /* array has changed so we need to obtain it again */
                    context = contextMap.keySet().toArray(new String[contextMap.size()]);
                }
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
    
    private float computeSimilarity(String w1, String w2){
        int simChars = 0;
        int restChar = 0;
            
        /* compute number of similar characters */
        while(w1.length() > simChars && w2.length() > simChars && w1.charAt(simChars) == w2.charAt(simChars)) simChars++;
            
        /* compute number of different characters
         * for 'theories' and 'theory' this is 4 */
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
            }else if(model.getContextVector(attachement) != null){    // -->check whether attachement is in wordMap
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
