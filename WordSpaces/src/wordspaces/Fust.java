/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeSet;

/**
 *
 * @author alexander
 */
public class Fust {
    
    /**
     * Computes the k greatest key-value pairs from map and returns them in descending
     * order in a TreeSet.
     * @param map Map from which the k greatest key-values pairs are computed.
     * @param k Number of greatest key-value pairs in map where values are used for ordering.
     * @return TreeSet<Entry>. Entries are sorted by values in descending order. If two 
     * equal, then keys are used for ordering.
     */     
    public static TreeSet< Entry<String,Double> > getKsmallestEdges(SortedMap<String, Double> map, int k){
         TreeSet< Entry<String,Double> > result = new TreeSet(new Comparator() {
            public int compare(Object obj, Object obj1) {
                int vcomp = ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
                if (vcomp != 0) return vcomp;
                else return ((Comparable) ((Map.Entry) obj1).getKey()).compareTo(((Map.Entry) obj).getKey());
            }           
         });           
         result.addAll(map.entrySet());
         Iterator< Entry<String,Double> > result_iter = result.descendingIterator(); //descending is wrong but its the only its running

         int delete_counter = result.size() - k;
         while(result_iter.hasNext() && delete_counter != 0){
            result_iter.next();
            result_iter.remove();
            delete_counter--;
         }

         return result;
    }
    
    
    /** Merges two contexts together. The two contexts are given as Maps  
     * and the stemContext represents the treemap to which we merge.
     * @param stemContext The TreeMap to which we merge
     * @param similarWordContext is merged into stemContext
     */
    public static void mergeContextMaps(Map v1, Map v2) throws NullPointerException{
        if(v1 == null || v2 == null){
            throw new NullPointerException("empty map in mergeContextMaps !");          
        }
        else {
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
    }

}
