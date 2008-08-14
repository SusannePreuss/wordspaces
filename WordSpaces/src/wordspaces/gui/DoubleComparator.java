/*
 * DoubleComparator.java
 * 
 * Created on 16.08.2007, 17:04:52
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui;

import java.util.Comparator;

/**
 *
 * @author alexander
 */
public class DoubleComparator implements Comparator<Double>{

    public DoubleComparator() {}

    public int compare(Double o1, Double o2) {
        double result = o1 - o2;
        if( result < 0 )
            return -1;
        else if( result > 0 )
            return 1;
        else
            return 0;
    }

}
