package wordspaces.gui.listener;

import wordspaces.gui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import exceptions.DimensionNotEqualException;
import java.util.SortedMap;
import plugins.EuclideanDistance;
import plugins.ScalarProductNorm;
import wordspaces.Model;

public class CompareWordsListener implements ActionListener {

    private final GUI gui;
    JTable table;

    public CompareWordsListener(GUI gui, JTable table) {
        super();
        this.table = table;
        this.gui = gui;
    }

    public void actionPerformed(ActionEvent arg0) {
        TableModel tableModel = table.getModel();
        DistancesPanel distPanel = gui.getDistancesPanel();
        JTable comparisonTable = distPanel.getComparisonTable();
        DefaultTableModel comparisonTableModel = (DefaultTableModel) comparisonTable.getModel();
        Model model = gui.getModel();
        int[] indices = table.getSelectedRows();
        if (indices.length == 2) {
            distPanel.clearComparisonTable();
            String vectorName1 = (String) tableModel.getValueAt(table.convertRowIndexToModel(indices[0]), 0);
            String vectorName2 = (String) tableModel.getValueAt(table.convertRowIndexToModel(indices[1]), 0);
            SortedMap vector1 = model.getWordVector(vectorName1);
            SortedMap vector2 = model.getWordVector(vectorName2);
            try {
                double[] simArray = (new ScalarProductNorm()).compute(vector1, vector2);
                double[] divArray = (new EuclideanDistance()).compute(vector1, vector2);
                double scalarSum = 0;
                double euclidSum = 0;
                Iterator contextWords = vector1.keySet().iterator();
                Iterator vec1EntryIter = vector1.values().iterator();
                Iterator vec2EntryIter = vector2.values().iterator();
                comparisonTableModel.setColumnIdentifiers(new Object[]{"Context words", vectorName1 + " Freqencies", vectorName2 + " Freqencies", "Contribution Sim.", "Contribution Div."});
                TableRowSorter<TableModel> comparisonSorter = new TableRowSorter<TableModel>(comparisonTableModel);
                comparisonSorter.setComparator(1, distPanel.doubleComparator);
                comparisonSorter.setComparator(2, distPanel.doubleComparator);
                comparisonSorter.setComparator(3, distPanel.doubleComparator);
                comparisonSorter.setComparator(4, distPanel.doubleComparator);
                comparisonTable.setRowSorter(comparisonSorter);
                for (int i = 0; i < simArray.length; i++) {
                    comparisonTableModel.addRow(new Object[]{contextWords.next(), vec1EntryIter.next(), vec2EntryIter.next(), simArray[i], divArray[i]});
                    scalarSum += simArray[i];
                    euclidSum += divArray[i];
                }
                distPanel.scalarLabel.setText("Scalarproduct is " + scalarSum);
                distPanel.euclideanLabel.setText("Euclidean distance is " + 1 / Math.sqrt(euclidSum));
            } catch (DimensionNotEqualException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select two word vectors.", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }
}
