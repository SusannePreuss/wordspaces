/*
 * BuildMatrix.java
 *
 * Created on 29. Juni 2007, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import Jama.Matrix;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import exceptions.DimensionNotEqualException;

/**
 *
 * @author alexander
 */
public class BuildMatrix extends SwingWorker<Matrix, String>{
    
    private TreeMap[] selectedVectors;
    
    /** Creates a new instance of BuildMatrix */
    public BuildMatrix(TreeMap<String, Double>[] selection) {
        this.selectedVectors = selection;
    }

    /**
     * This method builds an double[][] from the TreeMap[] array selectedVectors.
     * It is implemented as a SwingWorker so it is able to give justice about its
     * progress.
     */
    protected Matrix doInBackground() throws Exception {
        TreeMap contextVector = null;
        Iterator freqIter     = null;
        double[][] freqArray  = new double[selectedVectors.length][selectedVectors[0].size()];
        
        for(int i=0; i < selectedVectors.length; i++){           
            contextVector = selectedVectors[i];
            freqIter      = contextVector.values().iterator();
            int j         = 0;
            while(freqIter.hasNext()){
                freqArray[i][j] = (Double) freqIter.next();
                j++;
            }
            firePropertyChange("progress", null,i);            
        }      
        return new Matrix(freqArray);
    }
    
    /**
     * This method fills the frequency values from the Matrix m into a 
     * TreeMap[] array. Each TreeMap in the TreeMap array selection 
     * corresponds to one context vector. This method is especially usefull after
     * SVD.
     */
    public static TreeMap[] decomposeMatrixToTreeMap(Matrix m, TreeMap<String, Double>[] selection){
        TreeMap[] result = new TreeMap[m.getRowDimension()];
        for(int i=0; i<m.getRowDimension(); i++){
            
            result[i] = new java.util.TreeMap<java.lang.String, java.lang.Double>();
            java.util.Iterator stringIter = selection[i].keySet().iterator();
           
            try{    
                if (selection[i].keySet().size() != m.getColumnDimension()) 
                    throw new exceptions.DimensionNotEqualException();                
            } catch (DimensionNotEqualException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            
            for (int j = 0; j < m.getColumnDimension(); j++) {
                result[i].put(stringIter.next(), m.get(i, j));
            }

        }
       
        return result;
    }
    
}
