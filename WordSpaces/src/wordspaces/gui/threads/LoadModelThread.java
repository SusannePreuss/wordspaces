/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import wordspaces.Model;
import wordspaces.gui.GUI;

/**
 *
 * @author alexander
 */
public class LoadModelThread extends SwingWorker<Object, Integer>{
    
    private GUI gui;
    
    public LoadModelThread(GUI g){
        gui = g;
    }
    
    protected Object doInBackground() throws Exception {
        File file = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if(f.getName().toLowerCase().endsWith(".model") || f.isDirectory()){
                    return true;
                }
                else 
                    return false;
            }
            public String getDescription() {
                return "Model files";
            }
        });
        if(chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION)
            file = chooser.getSelectedFile();

        if(file != null && file.exists()){
            ObjectInputStream ois = null;
            gui.getProgressBar().setMaximum(1);
            gui.getProgressBar().setString("Loading...");
            gui.getProgressBar().setStringPainted(true);
            gui.getProgressBar().setIndeterminate(true);
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                Model model = (Model)ois.readObject();
                ((DefaultListModel)gui.getModelList().getModel()).addElement(model);
                
                //automatically selects the new model if no model was selected before
                if(gui.getModel() == null){
                    gui.setModel(model);
                    gui.getModelList().setSelectedIndex(((DefaultListModel)gui.getModelList().getModel()).getSize()-1);     //select the last added model                    
                    gui.showWordTable();
                    gui.showHistoryTable();
                }
                System.out.println("Model '"+model+"' with "+model.wordDirectory.size()+" words loaded...");
            } catch (InvalidClassException ex) {
                JOptionPane.showMessageDialog(null, "Model is not compatible.", "Warning", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (IOException ex){
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                    gui.getProgressBar().setIndeterminate(false);
                    gui.getProgressBar().setString("");
                    gui.getProgressBar().setValue(0);
                    gui.getProgressBar().setStringPainted(false);
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return null;
    }

}
