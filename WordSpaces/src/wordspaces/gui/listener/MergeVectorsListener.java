/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import wordspaces.Fust;
import wordspaces.Model;
import wordspaces.WordClassBuilder;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class MergeVectorsListener implements MenuListener{
    
    private GUI gui;
    private JMenu mergeVectors;

    public MergeVectorsListener(GUI g) {
        this.gui = g;
    }

    @Override
    public void menuSelected(MenuEvent evt) {
        mergeVectors = (JMenu) evt.getSource();
        mergeVectors.add(new JMenuItem("Merge TO"));
        mergeVectors.addSeparator();
        final JTable wordTable = gui.getWordTable();
        final DefaultTableModel wordTableModel = (DefaultTableModel) wordTable.getModel();
        final int[] indices = wordTable.getSelectedRows();
        final Model model = gui.getModel();
        if(indices.length >= 2){
            for(int i=0;i<indices.length;i++){
                //zuerst wird ein neues MenuItem mit dem Namen des Kontextvec. erstellt
                JMenuItem wordMenuItem = new JMenuItem((String) wordTableModel.getValueAt( wordTable.convertRowIndexToModel(indices[i]), 0));
                        
                //dann wird dem MenuItem der ActionListener hinzugefuegt
                wordMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        //this is the name of the context vector to which we merge the other vectors
                        String word = ((JMenuItem)evt.getSource()).getText();
                        //this is the index of the vector name.It will be initialized later in the the first for-loop
                        int index_mergeVector = 0;
                        //in this for-loop we just merge but do not delete,this is because of updating the entries
                        for(int i=indices.length-1;i>=0;i--){
                            String otherWord = (String) wordTableModel.getValueAt( wordTable.convertRowIndexToModel(indices[i]), 0);
                            if(!word.equals(otherWord)){
                                //now the vectors get merged.
                                Fust.mergeContextMaps(model.getWordVector(word), model.getWordVector(otherWord));
                                //now the occurences have to be updated
                                model.getWordVectorFrequencies().put(model.getStringCache().get(word), model.getWordVectorFrequencies().get(word)+model.getWordVectorFrequencies().get(otherWord));
                                System.out.println(otherWord+" was merged with "+word);
                            }
                            else{
                                index_mergeVector = indices[i];
                            }
                        }
                        //now the entries of the merge Vector get updated
                        wordTableModel.setValueAt(model.getWordVectorFrequencies().get(word),wordTable.convertRowIndexToModel(index_mergeVector), 1);
                        wordTableModel.setValueAt(model.getWordVector(word).size(),wordTable.convertRowIndexToModel(index_mergeVector), 2);
                        //now all vectors except the vector to which we merged get deleted
                        for(int i=indices.length-1;i>=0;i--){
                            int selectedRow = wordTable.convertRowIndexToModel(indices[i]);
                            String otherVecName = (String) wordTableModel.getValueAt( selectedRow, 0);
                            if(!word.equals(otherVecName)){
                                model.deleteWordVector(otherVecName);
                                model.getWordVectorFrequencies().remove(otherVecName);
                                wordTableModel.removeRow(selectedRow);
                            }
                        }
                        ((DefaultTableModel)gui.getContextTable().getModel()).setRowCount(0);
                        gui.getSizeLabel().setText(model.getDirectorySize()+"");
                        gui.setModelhasChanged(model);
                    }
                });
                mergeVectors.add(wordMenuItem);
                
            }
        }
    }

    @Override
    public void menuDeselected(MenuEvent arg0) {
        mergeVectors.removeAll();
    }

    @Override
    public void menuCanceled(MenuEvent arg0) {
    }

}
