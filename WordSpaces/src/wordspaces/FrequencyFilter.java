/*
 * FrequencyFilter.java
 *
 * Created on 18. Juni 2007, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alexander
 */
public class FrequencyFilter {
    
    /** Creates a new instance of FrequencyFilter */
    public FrequencyFilter() {
    }
    
    public static void filterFrequenciesInContext(Map vector, int freq){
        Iterator iter = vector.keySet().iterator();
        Iterator contextIter = null;
        TreeMap contextMap = null;
        while(iter.hasNext()){
            String vectorName = (String) iter.next();
            contextMap  = (TreeMap) vector.get(vectorName);
            contextIter = contextMap.keySet().iterator();
            
            while(contextIter.hasNext()){
                String contextWord  = (String) contextIter.next();
                if(contextWord == null || contextMap == null || contextMap.get(contextWord) == null)    
                    System.out.println("ContextWord:"+contextWord+"  contextMap:"+contextMap+"  vectorName"+vectorName);
                double wordFreq = (Double)contextMap.get(contextWord);
                if(wordFreq <= freq)
                    contextIter.remove();
                
            }
            
            if(contextMap.size() == 0)
                iter.remove();
            
        }
    }
    
    public static void filterFrequenciesInWordMap(Model m, int freq){
        Iterator iter = m.wordOccurences.keySet().iterator();
        
        while(iter.hasNext()){
            String word = (String) iter.next();
            int f = m.wordOccurences.get(word);
            if(f <= freq){
                m.wordDirectory.remove(word);
            }
        }
    }
    
    public static void filterFrequenciesInWordMap(DefaultTableModel tableModel, Model m, int freq){
        Vector rowVector = tableModel.getDataVector();
        String vectorName;
        Vector colVector;
        for(int i=rowVector.size()-1; i>=0; i--){
            colVector = (Vector) rowVector.elementAt(i);
            vectorName = (String) colVector.elementAt(0);
            int f = (Integer)colVector.elementAt(1);
            if(f <= freq){
 //               System.out.println(vectorName+" geloescht"+"  freq is"+f);
                m.wordDirectory.remove(vectorName);
                tableModel.removeRow(i);
            }
        }
    }
    
}
