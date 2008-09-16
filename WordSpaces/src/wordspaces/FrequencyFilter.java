/*
 * FrequencyFilter.java
 *
 * Created on 18. Juni 2007, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
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
    
    public static void filterExtremeValues(Map<String, Map<String, Double>> selectedVectors, int percentage){
        if(percentage > 0 && percentage <= 50){
            /* this treeset is necessary to sort the TreeMap by values... */
            TreeSet<Entry> set = new TreeSet(new Comparator() {
                public int compare(Object obj, Object obj1) {
                    int vcomp = ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
                    if (vcomp != 0) return vcomp;
                    else return ((Comparable) ((Map.Entry) obj1).getKey()).compareTo(((Map.Entry) obj).getKey());
                }
            });
                
            Iterator<String> selectedVectorsIter = selectedVectors.keySet().iterator();
            String vectorName;
            Map<String, Double> contextMap;
            int removeable_count;
            Entry<String, Double> entry;
            
            while(selectedVectorsIter.hasNext()){
                vectorName = selectedVectorsIter.next();
                System.out.println("filtering in "+vectorName);
                contextMap = selectedVectors.get(vectorName);
                removeable_count = (int)(((float)contextMap.size() / (float)100)*percentage);
                set.addAll(contextMap.entrySet());
                for(int j=0;j<removeable_count;j++){
                    entry = set.pollFirst();
                    contextMap.remove(entry.getKey());
                    entry = set.pollLast();
                    contextMap.remove(entry.getKey());
                }
            }
        }
        else{
            System.out.println("error: percentage is out of range -> "+percentage);
        }
    }
    
}
