/*
 * FileShowThread.java
 *
 * Created on 1. Juni 2007, 01:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.swing.JTextArea;

/**
 *
 * @author alexander
 */
public class FileShowThread extends Thread{
    
    private File file;
    private JTextArea textArea;
    
    public FileShowThread(File file,JTextArea textArea) {
        this.file = file;
        this.textArea = textArea;
    }
    
    public void run(){
        BufferedReader reader = null;
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            textArea.append("BEGIN-------"+"\n");
            while(reader.ready()){
                textArea.append(reader.readLine());
                textArea.append("\n");
            }
            textArea.append("END-------"+"\n");
        }
        catch (IOException ex){}
        
    }
    
}
