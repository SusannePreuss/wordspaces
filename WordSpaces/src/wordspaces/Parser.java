/*
 * Parser.java
 *
 * Created on 21. April 2007, 18:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Vector;



/**
 *
 * @author alexander frey afrey@uos.de
 */
public class Parser {

    private static final String FILLER = "xxxxxx";
    
    private boolean filler;

    private boolean cancelled;
    
    private String name;
    
    private int leftContextWidth;
    
    private int rightContextWidth;
    
    private Set<String> focusWords;
    
    private Set<String> stopWords;
    
    
    /** Creates a new instance of Parser. 
     * @param leftContextWidth Number of considered words left to the focus word
     * @param rightContextWidth Number of considered words right to the focus word
     */
    public Parser(int leftContextWidth, int rightContextWidth) {
        this.leftContextWidth  = leftContextWidth;
        this.rightContextWidth = rightContextWidth;
        this.filler            = false;
        this.cancelled         = false;
        name                   = new String();
    }    
    
    /** Parses a given file into a given Model. When finished
     * the parsed words are saved in the model.
     * @param file existing plain text file
     * @param model existing model
     */
    public void parseFile(File file, Model model) throws FileNotFoundException{
        BufferedReader reader = null;
        String word;
        String[] splitLine, context;
        Vector<String> lineTokens = new Vector();
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            while (reader.ready() && !cancelled) {
                splitLine = reader.readLine().toLowerCase().split("\\s");               //read line from reader and split it...
                for(String w: splitLine){
                    if(        !(w.length() <= 1)                                       //ignore words that consists only of one character
                            && !(w.charAt(0) == w.charAt(1))                            //first two chars must not be equal
                            && !(((Character)w.charAt(0)).compareTo('a') < 0)                        //ignore words that are lexicographic smaller than "a"
                            && !(((Character)w.charAt(0)).compareTo('z') > 0)){         //and lex. bigger than a word that begins with a 'z'
                        if(stopWords != null && !stopWords.contains(w)){                //ignore all stop-words, filler disabled
                            lineTokens.addElement(w);
                        }else if(stopWords != null && filler){                              //exchange stop-words with filler
                            lineTokens.addElement(FILLER);
                        }else if(stopWords == null){                                    //stop-words disabled
                            lineTokens.addElement(w);
                        }
                    }
                }
                for(int i=0; i<lineTokens.size(); i++){         //go through all words...
                    word = lineTokens.elementAt(i);
                    if(!filler || (filler && !word.equals(FILLER))){
                        if(focusWords == null || focusWords.contains(word)){
                            /* now the word gets counted in the treemap wordVectorFreq
                             * only if its a focusWord */
                            if(model.getWordVectorFrequencies().get(word) != null){     //word has already been seen
                                model.getWordVectorFrequencies().put(word, model.getWordVectorFrequencies().get(word) + 1);
                            }else{                                          //it's the first occurence of word
                                model.getWordVectorFrequencies().put(word, 1);
                            }
                              
                            //boundaries for context are determined
                            int leftBound  = i-leftContextWidth;
                            int rightBound = i+rightContextWidth;
                            if(leftBound  < 0) leftBound = 0;
                            if(rightBound >= lineTokens.size()) rightBound = lineTokens.size()-1;
                    
                            int k = 0;
                            context = new String[rightBound - leftBound];
                            //this for-loop is for the context initialization of
                            //the coresponding word.It goes from leftBound to rigthBound
                            //and neglects the word at pos i.
                            for(int j=leftBound; j <= rightBound; j++){
                                if(j != i){
                                    context[k++] = lineTokens.elementAt(j);
                                }
                            }                      
                            //contextVector is the corresponding vector to 'word', containing
                            //the 'context words' as keys and their frequency as values
                            model.addWordVector(word);
                            for(int j=0; j < context.length; j++){
                                if(!context[j].equals(FILLER)){       //dont add the filler to the context
                                    model.addContextWord(word, context[j]);              
                                }
                            }
                        }
                    }
                }
                lineTokens.removeAllElements();
            }
            /* parseFile might have been cancelled, but before we stop the parsing
             * process we have to reset the boolean value */
            cancelled = false;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void enableFiller(Boolean b){
        filler = b;
    }    
    
    public void enableStopWordsFilter(Set<String> stopWords){
        this.stopWords = stopWords;
    }
    
    public void disableStopWordsFilter(){
        this.stopWords = null;
    }
    
    public void enableFocusWords(Set<String> words){
        focusWords = words;
    }
    
    public void disableFocusWords(){
        focusWords = null;
    }
    
    public Set getFocusWords(){
        return focusWords;
    } 
    
    public Set getStopWords(){
        return stopWords;
    }
    
    public boolean isFocusWordsEnabled(){
        if(focusWords != null)
            return true;
        
        return false;
    }
    
    public boolean isFillerEnabled(){
        return filler;
    }
    
    public boolean isStopWordsEnabled(){
        if(stopWords != null)
            return true;
        
        return false;
    }
    
    public void setName(String n){
        name = n;
    }
    
    public String getName(){
        return name;
    }

    public void cancelParsing(){
        cancelled = true;
    }
 
    public String toString(){
        if(!name.isEmpty())
            return name+"-L"+leftContextWidth+"R"+rightContextWidth; 
        else
            return "L"+leftContextWidth+"R"+rightContextWidth; 
    }
}
