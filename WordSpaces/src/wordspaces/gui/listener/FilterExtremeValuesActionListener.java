/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import wordspaces.FrequencyFilter;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class FilterExtremeValuesActionListener implements ActionListener{

    private GUI gui;
    
    public FilterExtremeValuesActionListener(GUI g){
        gui = g;
    }
    
    
    public void actionPerformed(ActionEvent arg0) {
        String string = JOptionPane.showInputDialog("Enter the percentage(0-50) of extreme values(min+max) you like to filter");
        if(string != null && !string.isEmpty()){
            Map<String, Map<String,Double>> selectedVectors = new HashMap();
            int[] indices = gui.getWordTable().getSelectedRows();
            int selectedRow;
            String selectedWord;
            for(int i=0;i<indices.length;i++){
                selectedRow = gui.getWordTable().convertRowIndexToModel(indices[i]);
                selectedWord = (String) gui.getWordTable().getModel().getValueAt(selectedRow,0);               
                selectedVectors.put(selectedWord, gui.getModel().getContextVector(selectedWord));
            }
            
            FrequencyFilter.filterExtremeValues(selectedVectors, Integer.parseInt(string));

            gui.getTableModelMap().remove(gui.getModel().toString());
            gui.showWordTable();
        }    
    }        
}

