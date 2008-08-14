/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui;

import java.util.Comparator;

/**
 *
 * @author alexander
 */
public class IntegerComparator implements Comparator<Integer>{

    public IntegerComparator() {}

    public int compare(Integer o1, Integer o2) {
        int result = o1 - o2;
        if( result < 0 )
            return -1;
        else if( result > 0 )
            return 1;
        else
            return 0;
    }

}
