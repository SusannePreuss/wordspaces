/*
 * BuildWordTable.java
 *
 * Created on 20. Juni 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import javax.swing.SwingWorker;
import wordspaces.Model;

/**
 *
 * @author alexander
 */
public class BuildWordTable extends SwingWorker<Object[][],Object[]>{
    
    private Model model;
    
    /** Creates a new instance of BuildWordTable */
    public BuildWordTable(Model m) {
        model = m;
    }

    protected Object[][] doInBackground() throws Exception {
        //first the new wordTableModel is build and then filled with words from wordDirectory in model
        Map<String, Integer> wordOccurences = model.getWordOccurences();
        Object[][] data = new Object[model.getDirectorySize()][3];
        Object[] preResult = new Object[3];
        String word;
        for(int i=0;i<model.getDirectorySize();i++){
            word = model.getVectorNameAt(i);
            data[i][0] = word;
            data[i][1] = wordOccurences.get(word);
            data[i][2] = model.getContextVector(word).size();
            preResult[0] = word;
            preResult[1] = data[i][1];
            preResult[2] = data[i][2];
            
            firePropertyChange("progress",null, preResult);
        }
        
        return data;
    }
    
}


