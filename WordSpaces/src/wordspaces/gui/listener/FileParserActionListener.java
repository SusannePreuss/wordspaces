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
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;
import wordspaces.Model;
import wordspaces.Parser;
import wordspaces.gui.GUI;
import wordspaces.gui.threads.FileParserBalancerWorker;

/**
 *
 * @author alexander
 */
public class FileParserActionListener implements ActionListener{

    private GUI gui;
 
    private int progress = 0;
    
    public FileParserActionListener(GUI g){
        gui = g;
    }
    
    
    public void actionPerformed(ActionEvent arg0) {
        Parser parser = gui.getParser();
        final Model model   = gui.getModel();
        if(parser != null && model != null){
            Boolean fileIsPresent = false;
            Boolean alwaysAdd = false;
            File file = null;
            
            /* Get the selected Files */
            int[] indices = gui.getTextSourcesList().getSelectedIndices();
            final Vector<File> files = new Vector<File>();            
            for(int i=0; i < indices.length; i++){
                file = (File)((DefaultListModel)gui.getTextSourcesList().getModel()).getElementAt(indices[i]);
                if(!alwaysAdd){  //depends upon the decision that is made in the JOptionpane
                    for(String s:model.getParsedSources()){
                        if(s.equals(file.getName().toLowerCase())){
                            Object[] options = { "Never Add", "Don't add", "Always add", "Add" };
                            int decision = JOptionPane.showOptionDialog(null, file.getName()+" has already been added to model.", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                            if(decision == 3)
                                fileIsPresent = false;
                            if(decision == 2){
                                alwaysAdd     = true;
                                fileIsPresent = false;
                            }
                            if(decision == 1)
                                fileIsPresent = true;
                            if(decision == 0){
                                alwaysAdd     = false;
                                fileIsPresent = true;
                            }
                        }
                    }
                }
                if(!fileIsPresent){
                    files.addElement(file);
                }
            }
            
            final long zeit = System.currentTimeMillis();
            final JProgressBar progressBar = gui.getProgressBar();
            progressBar.setMaximum(files.size());
            progressBar.setStringPainted(true);
            progressBar.setString("(0/"+files.size()+") files finished");
            progressBar.setIndeterminate(true);              
            final FileParserBalancerWorker task = new FileParserBalancerWorker(parser,files,model);
            task.addPropertyChangeListener(new PropertyChangeListener() {
                boolean done = false;
                public  void propertyChange(PropertyChangeEvent evt) {
                    if("progress".equals(evt.getPropertyName())){
                        gui.setModelhasChanged(model);
   //                     gui.showWordTable();          //on small files this produces concurrent exceptions
                        progress++;
                        progressBar.setValue(progress);
                        progressBar.setString("("+progress+"/"+files.size()+") files finished");   
                        System.out.println("Finished parsing of "+((File)evt.getNewValue()).getName());
                    }
                    else if("parsing".equals(evt.getPropertyName())){
                        System.out.println("Parsing "+evt.getNewValue());
                    }
                    if(task.isDone() && !done){
                        done = true;
                        gui.getDistancesPanel().clearWordDirTable();
                        gui.getDistancesPanel().clearWordDistTable();  
                        model.eraseDistanceCache(); 
                        ((DefaultTableModel)gui.getContextTable().getModel()).setRowCount(0);                           
                        progressBar.setStringPainted(false);
                        progressBar.setString("");
                        progressBar.setValue(0);
                        progressBar.setIndeterminate(false);   
                        System.out.println("Parsing is done...");
                        System.out.println("Time needed for parsing into "+model+" took "+(System.currentTimeMillis()-zeit)+" (millis)");
                    }
                }
            });
            task.execute();               
        }           
        else
            JOptionPane.showMessageDialog(null, "Please select a Parser and a Model", "Warning", JOptionPane.ERROR_MESSAGE);

    }
}


