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
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author alexander frey afrey@uos.de
 */
public class Parser {

    private static final String FILLER = "xxxxxx";
    
    private boolean filler;
    
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
        String[] splitLine,context;
        TreeMap<String,Double> contextVector;
        Vector<String> lineTokens = new Vector();
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            while (reader.ready()) {                                                   
                splitLine = reader.readLine().toLowerCase().split("\\s");               //read line from reader and split it...
                for(String w: splitLine){
                    if(        !(w.length() <= 1)                                       //ignore words that consists only of one character
                            && !(w.charAt(0) == w.charAt(1))                            //first two chars must not be equal
                            && !(((Character)w.charAt(0)).compareTo('a') < 0)                        //ignore words that are lexicographic smaller than "a"
                            && !(((Character)w.charAt(0)).compareTo('z') > 0)){         //and lex. bigger than a word that begins with a 'z'
                        if(stopWords != null && !stopWords.contains(w)){                //ignore all stop-words, filler disabled
                            lineTokens.addElement(w);
                        }else if(stopWords != null && filler){                              //exchange stop-words with filler
                            lineTokens.addElement("xxxxxx");
                        }else if(stopWords == null){                                    //stop-words disabled
                            lineTokens.addElement(w);
                        }
                    }
                }
                for(int i=0; i<lineTokens.size(); i++){         //go through all words...
                    word = lineTokens.elementAt(i);
                    if(filler && !word.equals(FILLER)){
                        //now the word gets counted in the treemap wordOccurences in the model                    
                        if(model.wordOccurences.get(word) != null){     //word has already been seen
                            model.wordOccurences.put(word, model.wordOccurences.get(word) + 1);
                        }else{                                          //it's the first occurence of word
                            model.wordOccurences.put(word, 1);
                        }
                    
                    
                        if(focusWords == null || focusWords.contains(word)){
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
                            //if word is new then add it to the 'words' treemap with an
                            //fresh initialized context treemap'
                            if(!model.wordDirectory.containsKey(word)){
                                model.wordDirectory.put(word,new TreeMap<String,Double>());
                            }                       
                            //contextVector is the corresponding vector to 'word', containing
                            //the 'context words' as keys and their frequency as values
                            contextVector = model.wordDirectory.get(word);
                            for(int j=0; j < context.length; j++){
                                if(!context[j].equals(FILLER)){       //dont add the filler to the context
                                    Double freq = contextVector.get(context[j]);
                                    if(freq == null){
                                        contextVector.put(context[j], new Double(1));
                                    }else{
                                        contextVector.put(context[j], new Double(++freq));
                                    }                
                                }
                            }
                        }
                    }
                }
                lineTokens.removeAllElements();
            }
        }
        catch (Exception ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
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
 
    public String toString(){
        if(!name.isEmpty())
            return name+"-L"+leftContextWidth+"R"+rightContextWidth; 
        else
            return "L"+leftContextWidth+"R"+rightContextWidth; 
    }
}
