package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import wordspaces.Model;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class RenameModelActionListener implements ActionListener{

    private GUI gui;
    
    public RenameModelActionListener(GUI g){
        gui = g;
    }
    
    public void actionPerformed(ActionEvent evt) {
        String name = JOptionPane.showInputDialog("New name");

        if(name != null){
            JList list = gui.getModelList();
            DefaultListModel listModel = (DefaultListModel) list.getModel();
            int index = list.getSelectedIndex();
            if(index != -1){
                Model model = (Model) listModel.getElementAt(index);
                File old_file = new File(model+".model");
                if(old_file.exists()){
                    old_file.renameTo(new File(name+".model"));
                    System.out.println("renamed!");
                }
                gui.getTableModelMap().remove(model.toString());
                listModel.removeElementAt(index);
                model.setName(name);
                listModel.addElement(model);
                gui.setModel(model);                
            }
        }
    }
}
