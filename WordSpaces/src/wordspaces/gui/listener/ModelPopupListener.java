package wordspaces.gui.listener;

import wordspaces.gui.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import wordspaces.Model;

public class ModelPopupListener extends MouseAdapter {

    private GUI gui;
    private JPopupMenu menu;

    public ModelPopupListener(GUI g, JPopupMenu menu) {
        super();
        this.gui = g;
        this.menu = menu;
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger() && e.getButton() == MouseEvent.BUTTON3) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            JList modelList = (JList) e.getComponent();
            Model model = gui.getModel();        
            DefaultListModel modelListModel = (DefaultListModel) gui.getModelList().getModel();
            HashMap<String, Boolean> modelChangedMap = gui.getModelChangedMap();
            int index = modelList.getSelectedIndex();
            if (index != -1) {
                /* Check if the selected model was already selected before,if so
                 * then only build the wordTable if the model has changed. */
                if (model != null && model.toString().equals(((Model) modelListModel.getElementAt(index)).toString())) {
                    /* Only build the word table if model has changed */
                    if (modelChangedMap.containsKey(model.toString()) && modelChangedMap.get(model.toString())) {
                        gui.showWordTable();
                    }
                } 
                /* Build the wordTable if there wasn't a model selected before, or
                 * if a new model has been selected. */
                else {
                    model = (Model) modelListModel.getElementAt(index);
                    System.out.println(model.getParsedSources().size());
                    gui.setModel(model);
                    gui.showWordTable();
                    gui.getDistancesPanel().showDistances(model.getCachedDistances());
                    gui.distWindowButton.setText("Hide DistWindow");
                    gui.getInfoWindow().setTitle(model.toString());
                }
                gui.showHistoryTable();
            }
        }
    }
}
