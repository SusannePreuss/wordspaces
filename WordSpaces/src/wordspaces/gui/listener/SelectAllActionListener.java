/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class SelectAllActionListener implements ActionListener{

    private GUI gui;
    
    public SelectAllActionListener(GUI g){
        gui = g;
    }
    
    
    public void actionPerformed(ActionEvent evt) {
        JPopupMenu menu = (JPopupMenu) ((JMenuItem)evt.getSource()).getParent();
        if(menu.getName().equals("wordPopup")){
            gui.getWordTable().selectAll();
        }
        else if(menu.getName().equals("contextPopup")){
            gui.getContextTable().selectAll();
        }
        else if(menu.getName().equals("sourcePopup")){
            JList list = gui.getTextSourcesList();
            list.setSelectionInterval(0,(list.getModel().getSize()-1));
        }
    }

}
