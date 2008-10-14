/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author alexander
 */
public class GroupGradeCalculator {
    
    
    public static double[] calcGroupGradeButtonActionPerformed(Model model, Map<Integer, Vector> groups) {
        /* double array contains the result values.
         * result[0] = grade
         * result[1] = errors 
         * result[2] = possible_max_error 
         */
        double[] result = new double[3];
        Iterator<Integer> groupIter = groups.keySet().iterator();
        Iterator<Entry<String,Double>> k_smallest_edges_iter;
        String word, neighborWord;
        Vector<String> members;
        TreeSet<Entry<String, Double>> k_smallest_edges;
        Entry entry;
        float grade = 0, 
              error = 0, 
              group_error = 0, 
              possible_maximal_error = 0;
        
        int grp, grp_SIZE, errors = 0,
            pos_in_k_smallest_edges = 0;     //pos of the word of k_smallest_edges, needed to calculate the error

        while(groupIter.hasNext()){          //go through all groups
            System.out.println("!!!! NEW GROUP !!!!");
            grp = groupIter.next();
            members = groups.get(grp);
            //first identify the number of words in the same group as vectorName
            grp_SIZE = members.size();
            group_error = 0;
            possible_maximal_error += calcMaxGroupError(grp_SIZE);
            
            /* go through all words in the group */
            for( int i=0 ; i < grp_SIZE ; i++ ){        
                word = members.elementAt(i);            //get the next word name
                k_smallest_edges = Fust.getKsmallestEdges(model.getCachedDistances().get(word), grp_SIZE-1);   //now we have the k smallest edges to word
                k_smallest_edges_iter = k_smallest_edges.iterator();
                pos_in_k_smallest_edges = 0;                    //is reseted for the next word
                
                /* now check for word how many errors have been made */
                while(k_smallest_edges_iter.hasNext()){         
                    entry = k_smallest_edges_iter.next();
                    neighborWord = (String) entry.getKey();
                    pos_in_k_smallest_edges++;                  //first word is at pos 1 and so on...
                    /* now check if this neighborWord is in the same group as word, if so give points. */
                    if(!members.contains(neighborWord)){
                        /* Calculate the error by dividing 1 by pos_in_k_smallest_edges */
                        error = (float) 1 / (float) pos_in_k_smallest_edges;
                        group_error += error / possible_maximal_error;
                        errors++;
                        System.out.println("Error! "+word.toUpperCase()+" neighbor is "+neighborWord.toUpperCase()+" pos is "+pos_in_k_smallest_edges+" error "+error);
                    }
                }
            }
            /* Calculate the arithmetic average of the grp_error by dividing grp_error
             * by grp_SIZE. We get the average error for a word in the group. */
            group_error = group_error / (float) grp_SIZE;
            grade += group_error;
        }
        result[0] = 1 - ( grade / groups.size() );
        result[1] = errors;
        result[2] = possible_maximal_error;
        return result;
    }
    
    private static float calcMaxGroupError(int grp_SIZE){
        float result = 0;
        for(int i=1;i<grp_SIZE;i++){
            result += (float) 1 / (float) i;
        }
        
        return result;
    }

}
