/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;
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
            int percentage = Integer.parseInt(string);
            if(percentage > 0 && percentage <= 50){
                int[] indices = gui.getWordTable().getSelectedRows();
                int selectedRow, removeable_count;
                String selectedWord = new String();
                Entry<String, Double> entry;
                SortedMap<String, Double> selectedMap;
                //this treeset is necessary to sort the TreeMap by values...
                TreeSet<Entry> set = new TreeSet(new Comparator() {
                    public int compare(Object obj, Object obj1) {
                        int vcomp = ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
                        if (vcomp != 0) return vcomp;
                        else return ((Comparable) ((Map.Entry) obj1).getKey()).compareTo(((Map.Entry) obj).getKey());
                    }
                });
                
                for(int i=0;i<indices.length;i++){
                    selectedRow = gui.getWordTable().convertRowIndexToModel(indices[i]);
                    selectedWord = (String) gui.getWordTable().getModel().getValueAt(selectedRow,0);
                    System.out.println("filtering in "+selectedWord);
                    selectedMap = gui.getModel().getContextVector(selectedWord);
                    removeable_count = (int)(((float)selectedMap.size() / (float)100)*percentage);
                    set.addAll(selectedMap.entrySet());
                    for(int j=0;j<removeable_count;j++){
                        entry = set.pollFirst();
                        selectedMap.remove(entry.getKey());
                        entry = set.pollLast();
                        selectedMap.remove(entry.getKey());
                    }
                }
                gui.getTableModelMap().remove(gui.getModel().toString());
                gui.showWordTable();
            }
        }
    }        
}

