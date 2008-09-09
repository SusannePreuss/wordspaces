/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import javax.swing.SwingWorker;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author alexander frey  afrey@uos.de
 */
public class XMLParserWorker extends SwingWorker<Object,Object>{
    
    private File source;
    private BufferedReader fileReader;
    private SAXParser parser;
    
    public XMLParserWorker(File groupFile){
        source = groupFile;
        parser = new SAXParser();
        parser.setContentHandler(new GroupHandler());
        try {
            fileReader = new BufferedReader(new FileReader(source));
        }catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } 

    }

    @Override
    protected Object doInBackground() throws Exception {
        try{
            parser.parse(new InputSource(fileReader));

        } catch (SAXException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());       
        }
        
        return null;
    }
    
    class GroupHandler extends DefaultHandler{
        
        Map<Integer, Vector> groups;

        /* Contains the tasks. In case of "remove" Object could be a set
         * of strings in a vector. In case of "merge" Object could be a
         * hashmap<String,String> */
        Map<String, Object> tasks;

        /* contains the currently group_nr which is created */
        int grpNr;

        public void startDocument(){
            firePropertyChange("startDoc",null, "Start reading from xml...");
        }
        
        public void endDocument(){
            firePropertyChange("endDoc",null, "Finished reading from xml...");
        }
        
        public void startElement(String uri, String localName, String name, Attributes atts){
            /* Begin of a group setting */
            if(name.equals("groupsetting")){
                groups = new HashMap();
            }
            /* Begin of a group */
            else if(name.equals("group")){
                grpNr = Integer.parseInt(atts.getValue("nr"));
            }
            /* words in a group */
            else if(name.equals("word")){
                String word = atts.getValue("value");               
                if(!groups.containsKey(grpNr)){      
                    groups.put(grpNr, new Vector());
                }
                groups.get(grpNr).addElement(word);
            }
            /* Now tasks get processed */
            else if(name.equals("tasks")){
                if(tasks == null)
                    tasks = new HashMap();
            }
            else if(name.equals("remove")){
                String word = atts.getValue("word");
                if(!tasks.containsKey("remove")){
                    tasks.put("remove", new Vector());
                }
                /* add the word to the remove vector */
                ((Vector)tasks.get("remove")).addElement(word);
            }
            else if(name.equals("merge")){
                if(!tasks.containsKey("merge")){
                    Map<String, String> mergeMap = new HashMap();
                    tasks.put("merge", mergeMap);
                }
                /* the first word in the list is the word we merge_TO */
                ((Map)tasks.get("merge")).put(atts.getValue("mergeinto"), atts.getValue("vector"));
            }
        }
        
        public void endElement(String uri, String localName, String name){
            if(name.equals("group")){
                grpNr = -1;     //not needed
            }
            else if(name.equals("groupsetting")){
                /* return the created group hashmap */
                firePropertyChange("groups",null, groups);
            }
            else if(name.equals("tasks")){
                /* return the created task hashmap */
                firePropertyChange("tasks",null, tasks);
            }

        }
        
    }
    
}
