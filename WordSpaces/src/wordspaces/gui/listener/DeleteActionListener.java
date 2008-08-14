package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import wordspaces.Model;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class DeleteActionListener implements ActionListener{

    private GUI gui;
    
    public DeleteActionListener(GUI g){
        gui = g;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        JMenuItem menu = (JMenuItem)evt.getSource();
        if(menu.getText().equals("Delete vector")){
            Model m = gui.getModel();
            JTable wordTable = gui.getWordTable();
            JTable contextTable = gui.getContextTable();
            DefaultTableModel wordModel = (DefaultTableModel) wordTable.getModel();
            DefaultTableModel contextModel = (DefaultTableModel) contextTable.getModel();
            int[] indices = wordTable.getSelectedRows();
            int selectedRow;
            String selectedWord;
            for(int i=indices.length-1; i >= 0; i--){
                selectedRow = wordTable.convertRowIndexToModel(indices[i]);
                selectedWord = (String) wordModel.getValueAt(selectedRow,0);
                m.wordDirectory.remove(selectedWord);
                m.wordOccurences.remove(selectedWord);
                wordModel.removeRow(selectedRow);
            }
            contextModel.setRowCount(0);
            gui.getSizeLabel().setText(m.wordDirectory.size()+"");
            gui.getTableModelMap().put(m, true);
        }
        
        else if(menu.getText().equals("Delete") || menu.getText().equals("Delete in all contexts")){
            Model model = gui.getModel();
            JTable wordTable = gui.getWordTable();
            JTable contextTable = gui.getContextTable();   
            DefaultTableModel contextModel = (DefaultTableModel) contextTable.getModel();            
            int[] indices = contextTable.getSelectedRows();
            /* Now we save the selected context words in an String[] */
            String[] selectedContextWords = new String[indices.length];
            for(int i=0; i<indices.length;i++){
                int selectedRow = contextTable.convertRowIndexToModel(indices[i]);
                selectedContextWords[i] = (String) contextModel.getValueAt(selectedRow,0);
            }
            /* Now we have to decide if we go through all wordVectors or if we 
             * only delete the context words from one word vector... */  
            String[] wordVectors;
            DefaultTableModel wordTableModel = (DefaultTableModel) wordTable.getModel();
            if(menu.getText().equals("Delete in all contexts")){
                wordVectors = new String[model.wordDirectory.size()];
                for(int i=0;i<model.wordDirectory.size();i++){
                    wordVectors[i] = (String) wordTableModel.getValueAt(i, 0);
                }
            }
            else{                   //only delete context words from one word vector                     
                wordVectors = new String[1];  
                wordVectors[0] = (String) wordTableModel.getValueAt(wordTable.convertRowIndexToModel(wordTable.getSelectedRow()),0);
            }
            /* Go through all wordVectors, this might be only one... */
            for(String wordVector:wordVectors){
                TreeMap contextMap = model.wordDirectory.get(wordVector);
                /* Now delete all selected context words in contextmap */
                for(String selectedWord:selectedContextWords){
                    System.out.print("deleting "+selectedWord+" ");
                    contextMap.remove(selectedWord);
                }
                System.out.println("from "+wordVector+"...");
                if(contextMap.size() == 0){           //we deleted all context words from context vector
                    model.wordDirectory.remove(wordVector);
                    if(wordVectors.length == 1)       //we can only update the table if we delete from only one word vector
                        wordTableModel.removeRow(wordTable.convertRowIndexToModel(wordTable.getSelectedRow()));
                    
                }else{
                    if(wordVectors.length == 1)       //we can only update the table if we delete from only one word vector
                        wordTableModel.setValueAt(contextMap.size(), wordTable.convertRowIndexToModel(wordTable.getSelectedRow()), 2);
                }
            }
            contextModel.setRowCount(0);    //better set this to zero     
            gui.setModelhasChanged(model);
            if(wordVectors.length != 1)
                gui.showWordTable();
        }
        
        else if(menu.getText().equals("Delete parser")){
            JList list = gui.getParserList();
            DefaultListModel model = (DefaultListModel) list.getModel();
            int[] indices = list.getSelectedIndices();
            for(int i=indices.length-1; i >= 0; i--){
                model.removeElementAt(indices[i]);                   
            }
            gui.setParser(null);
        }
        
        else if(menu.getText().equals("Delete model")){
            JList list = gui.getModelList();
            DefaultListModel listModel = (DefaultListModel) list.getModel();
            int[] indices = list.getSelectedIndices();
            if(indices.length > 0){
                for(int i=indices.length-1; i >= 0; i--){
                    Model model = (Model) listModel.getElementAt(indices[i]);
                    gui.getTableModelMap().remove(model.toString());
                    listModel.removeElementAt(indices[i]);
                    gui.setModel(null);
                    ((DefaultTableModel)gui.getWordTable().getModel()).setRowCount(0);
                    ((DefaultTableModel)gui.getContextTable().getModel()).setRowCount(0);
                    gui.getDistancesPanel().clearWordDirTable();
                    gui.getDistancesPanel().clearWordDistTable();
                    gui.getSizeLabel().setText("0");        
                    /* Resetting distancePanel except the group settings */
                    gui.getDistancesPanel().clearGroupLabels();
                }
                gui.getInfoWindow().setTitle("");
            }
        }
        
        else if(menu.getText().equals("Delete text")){
            JList list = gui.getTextSourcesList();
            DefaultListModel model = (DefaultListModel) list.getModel();
            int[] indices = list.getSelectedIndices();            
            for(int i=indices.length-1; i >= 0; i--){
                model.removeElementAt(indices[i]);
            }
            gui.getTextSizeLabel().setText(model.getSize()+" items"); 
        }   
    }
}
