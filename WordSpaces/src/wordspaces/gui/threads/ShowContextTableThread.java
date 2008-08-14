/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import wordspaces.gui.*;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import wordspaces.Model;

/**
 *
 * @author alexander
 */
public class ShowContextTableThread extends Thread{
    
    private DefaultTableModel tableModel;
    private Model model;
    private String name;
    private GUI gui;      //this is to deactivate
    private static ShowContextTableThread instance;
    
    private ShowContextTableThread(){}
    
    private ShowContextTableThread(GUI gui, String name){
        this.model = gui.getModel(); 
        this.name  = name;
        this.gui   = gui;
        this.tableModel = (DefaultTableModel) gui.getContextTable().getModel();        
    }
    
    /**
     * This class is implemented as an singleton. To get an instance call getInstance
     * @param gui Reference to the GUI instance.
     * @param name Name of the selected word in gui.wordTable.
     * @return Instance of ShowContextTableThread or null if there is already an instance.
     */
    public static ShowContextTableThread getInstance(GUI gui, String name){
        if(instance != null){
            return null;
        }
        else{
            return new ShowContextTableThread(gui,name);
        }
    }
    
    public static boolean instanceRunning(){
        return instance != null;
    }
    
    
    public void run(){
        System.out.println("Start building context table!");
        Iterator iter = model.wordDirectory.get(name).keySet().iterator();
        String contextWord;
        while(iter.hasNext()){
            contextWord = (String)iter.next();
            tableModel.addRow(new Object[] {
                contextWord, model.wordDirectory.get(name).get(contextWord)
            });
        }
        System.out.println("Finished table building!");
        instance = null;
    }
}
