/*
 * EuclideanDistance.java
 *
 * Created on 12. Juni 2007, 00:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package plugins;

import exceptions.DimensionNotEqualException;
import java.util.Iterator;
import java.util.SortedMap;

/**
 *
 * @author alexander
 */
public class EuclideanDistance implements ComputesDistance{
    
    /** Creates a new instance of EuclideanDistance */
    public EuclideanDistance() {}

    /** Computes the distance between two vectors
     * that are encapsulated by a treemap each. First it normalizes all entries
     * and then it proceeds.
     * @param Vector v1 , Vector v2
     * @return double array with normalized entries, sum up all entries 
     * take the square root and you get the eucl. dist.
     */
    public double[] compute(SortedMap v1, SortedMap v2) throws DimensionNotEqualException{
        if(v1.size() != v2.size()) throw new DimensionNotEqualException();
        double[] resultArray = new double[v1.size()];
        double normX = 0;
        double normY = 0;
        double x,y;
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
        
        //if we divide every entry in the vectors with the corresponding norm we end up with normalized vectors
        iterV1 = v1.keySet().iterator();
        iterV2 = v2.keySet().iterator();
        int counter = 0;
        while(iterV1.hasNext()){
            x = (Double) v1.get(iterV1.next());
            y = (Double) v2.get(iterV2.next());
            
            x = x / normX;
            y = y / normY;
            
            resultArray[counter] = Math.pow(x-y, 2);
            counter++;
        }
   
        
        return resultArray;
    }
    
}
