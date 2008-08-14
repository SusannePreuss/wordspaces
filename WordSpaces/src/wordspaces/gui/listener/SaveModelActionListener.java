/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JOptionPane;
import wordspaces.Model;
import wordspaces.gui.GUI;
import wordspaces.gui.threads.SaveModelWorker;

/**
 *
 * @author alexander
 */
public class SaveModelActionListener implements ActionListener{
    
    private GUI gui;
    
    public SaveModelActionListener(GUI g){
        gui = g;
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        final Model model = gui.getModel();
        File file = new File(model+".model");
        int decision = 0;
        if(file.exists()){
            decision = JOptionPane.showConfirmDialog(null,"Overwrite Model ?", "Question", JOptionPane.YES_NO_OPTION);
        }
        if(decision == 0){  
            file.delete();
        }
        if(model != null){
            final SaveModelWorker thread = new SaveModelWorker(model);
            gui.getProgressBar().setString("Saving...");
            gui.getProgressBar().setStringPainted(true);
            gui.getProgressBar().setIndeterminate(true);
            thread.addPropertyChangeListener(new PropertyChangeListener() {
                boolean done = false;
                public  void propertyChange(PropertyChangeEvent evt) {
                    if(thread.isDone() && !done){
                        done = true;
                        System.out.println("Finished saving "+model+"...");
                        gui.getProgressBar().setIndeterminate(false);
                        gui.getProgressBar().setString("");
                        gui.getProgressBar().setValue(0);
                        gui.getProgressBar().setStringPainted(false);
                    }
                }
            });
            thread.execute();
        }
        else
            JOptionPane.showMessageDialog(null, "Please select a model", "Warning", JOptionPane.ERROR_MESSAGE);
    }
}
