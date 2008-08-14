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
import java.util.TreeMap;
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
        Map<String, TreeMap> wordDirectory  = model.wordDirectory;
        Map<String, Integer> wordOccurences = model.wordOccurences;
        Object[][] data = new Object[wordDirectory.size()][3];
        Object[] preResult = new Object[3];
        Iterator iter = wordDirectory.keySet().iterator();
        String word;
        int counter = 0;
        while(iter.hasNext()){
            word = (String)iter.next();
            data[counter][0] = word;
            data[counter][1] = wordOccurences.get(word);
            data[counter][2] = wordDirectory.get(word).size();
            preResult[0]     = word;
            preResult[1]     = data[counter][1];
            preResult[2]     = data[counter][2];
            counter++; 
            
            firePropertyChange("progress",null, preResult);
        }
        
        return data;
    }
    
}


