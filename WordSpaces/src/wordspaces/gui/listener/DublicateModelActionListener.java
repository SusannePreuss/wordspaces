/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import wordspaces.Model;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class DublicateModelActionListener implements ActionListener{

    private GUI gui;
    
    public DublicateModelActionListener(GUI g){
        gui = g;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        int index = gui.getModelList().getSelectedIndex();
        if(index != -1){
            Model originalModel = (Model) ((DefaultListModel)gui.getModelList().getModel()).get(index);
            String oldName      = originalModel.toString();
            String newName      = JOptionPane.showInputDialog("Please type in the models name.");

            File file = new File(newName+".model");
            ObjectOutputStream oos = null;
            ObjectInputStream ois  = null;
            int decision = 0;

            if(!file.exists()){
                try {
                    file.createNewFile();
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
            else
                decision = JOptionPane.showConfirmDialog(null,"Overwrite Model ?", "Question", JOptionPane.YES_NO_OPTION);

            if(decision == 0){
                try{
                    oos = new ObjectOutputStream(new FileOutputStream(file));
                    originalModel.setName(newName);          //set the new Name of the dublicate Model
                    oos.writeObject(originalModel);        //and save it to file
                    originalModel.setName(oldName);          //restore the old Name
                    oos.close();
                    System.out.println("Model has been saved to file "+file.getAbsolutePath()+"\n");

                    //now the dublicate Model needs to be loaded into the model list
                    ois = new ObjectInputStream(new FileInputStream(file));
                    gui.addModel((Model)ois.readObject());
                } 
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                } 
                catch (ClassNotFoundException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
                finally {
                    try {
                        oos.close();
                        ois.close();
                    } catch (IOException ex) {
                        Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}


