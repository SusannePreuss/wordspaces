/*
 * ScalarProductNorm.java
 *
 * Created on 26. Juni 2007, 22:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package plugins;

import exceptions.DimensionNotEqualException;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author alexander
 */
public class ScalarProductNorm implements ComputesDistance{
    
    /** Creates a new instance of ScalarProductNorm */
    public ScalarProductNorm() {
    }
    
    /** Computes the distance between two vectors
     * that are encapsulated by a treemap each. First it normalizes all entries 
     * and then it proceeds.
     * @param Vector v1 , Vector v2 
     * @return array with entries that divided by the norm of the two vectors
     */
    public double[] compute(TreeMap v1, TreeMap v2) throws DimensionNotEqualException{
        if(v1.size() != v2.size()) throw new DimensionNotEqualException();
        double[] resultArray = new double[v1.size()];
        double x             = 0;
        double y             = 0;
        double normX         = 0;
        double normY         = 0;
        
        Iterator iterV1 = v1.keySet().iterator();
        Iterator iterV2 = v2.keySet().iterator();
         
        while(iterV1.hasNext()) {
            x = (Double) v1.get(iterV1.next());
            y = (Double) v2.get(iterV2.next());
            
            normX += x * x;
            normY += y * y;     
	}
        normX = Math.sqrt(normX);
        normY = Math.sqrt(normY);
        
        iterV1 = v1.keySet().iterator();
        iterV2 = v2.keySet().iterator();
        int counter = 0;  
        while(iterV1.hasNext()) {
            x = (Double) v1.get(iterV1.next());
            y = (Double) v2.get(iterV2.next());
            
            resultArray[counter] = (x / normX) * (y / normY);
            counter++;
        }

        
        return resultArray;
    }
    
}
