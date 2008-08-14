/*
 * FileFetcher.java
 *
 * Created on 10. Juni 2007, 00:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class FileFetcher extends SwingWorker<Object, String>{
    
    private String[] urls;
    
    public FileFetcher(String[] urls){
        this.urls = urls;     
    }
    
    public static File fetchFile(URL url){
        File file = null;
        FileWriter writer = null;
        Reader reader = null;
                 
        try {
            reader = new InputStreamReader(url.openStream());
            Html2Text parser = new Html2Text();
            parser.parse(reader);
            
            String fileName = url.getPath().substring(url.getPath().lastIndexOf("/")+1)+".txt";
            file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }else{
                file = new File(JOptionPane.showInputDialog("File exists already. Please enter another name"));
            }
            writer = new FileWriter(file);
            
            writer.write(parser.getText());   
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }   
        finally{
            try {
                writer.close();
                reader.close();   
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return file;
        }
    }

    protected Object doInBackground() throws Exception {
        for(String addr:urls){
            if(!addr.equals("")){
                try{
                    URL url = new URL(addr);
                    firePropertyChange("fileInfo",null,url.getFile());
                    firePropertyChange("progress",null,fetchFile(url));
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(null, addr+" is no valid URL", "Warning", JOptionPane.ERROR_MESSAGE); 
                }        
            }
        }    
        return null;
    }
    
}
