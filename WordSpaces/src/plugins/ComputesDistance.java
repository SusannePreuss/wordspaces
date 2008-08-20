/*
 * ComputesDistance.java
 *
 * Created on 12. Juni 2007, 00:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package plugins;

import exceptions.DimensionNotEqualException;
import java.util.SortedMap;

/**
 *
 * @author alexander
 */
public interface ComputesDistance {
    public double[] compute(SortedMap v1, SortedMap v2)throws DimensionNotEqualException;
}
