package wordspaces.gui.listener;

import wordspaces.gui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class CreateGroupListener implements ActionListener {

    DistancesPanel distPanel;

    public CreateGroupListener(DistancesPanel distPanel) {
        super();
        this.distPanel = distPanel;
    }

    public void actionPerformed(ActionEvent e) {
        JTable table = distPanel.getWordTable();
        int numberofGroups = distPanel.getNumberofGroups();
        TableModel tableModel = table.getModel();
        int[] indices = table.getSelectedRows();
        for(int i=0;i<indices.length;i++){
             String vectorName = (String) tableModel.getValueAt(table.convertRowIndexToModel(indices[i]), 0);
             distPanel.addtoGroup(vectorName,numberofGroups+1);
        }

        if(indices.length >= 1){
            distPanel.groupCounterLabel.setText((numberofGroups+1)+"");
            distPanel.calcGroupGradeButton.setEnabled(true);
            distPanel.clearGroupSettingsButton.setEnabled(true);
        }
    }
}
