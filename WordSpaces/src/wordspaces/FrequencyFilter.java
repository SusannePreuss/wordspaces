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
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alexander
 */
public class FrequencyFilter {
    
    public static void filterFrequenciesInContext(Map<String, Double> vector, int freq){
        Iterator<String> iter = vector.keySet().iterator();
        while(iter.hasNext()){
            String contextWord = iter.next();
            if(vector.get(contextWord) <= freq)
                vector.remove(contextWord);              
        }
    }
    
    public static void filterFrequenciesInWordMap(Model m, int freq){
        Iterator iter = m.getWordVectorFrequency().keySet().iterator();
        
        while(iter.hasNext()){
            String word = (String) iter.next();
            int f = m.getWordVectorFrequency().get(word);
            if(f <= freq){
                m.deleteWordVector(word);
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
                m.deleteWordVector(vectorName);
                tableModel.removeRow(i);
            }
        }
    }
    
}
