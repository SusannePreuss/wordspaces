/*
 * ComputesDistance.java
 *
 * Created on 12. Juni 2007, 00:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package plugins;

import java.util.TreeMap;

/**
 *
 * @author alexander
 */
public interface ComputesDistance {
    public double[] compute(TreeMap v1,TreeMap v2)throws DimensionNotEqualException;
}
