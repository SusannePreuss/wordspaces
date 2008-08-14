/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;
import wordspaces.Model;
import wordspaces.Parser;

/**
 *
 * @author alexander
 */
public class FileParserWorker extends SwingWorker<Model, Integer>{
    
    private Parser parser;
    
    private Model model;
    
    private File file;

    public FileParserWorker(Parser p, Model m, File f){
        parser = p;
        model = m;
        file = f;
    }
    
    
    protected Model doInBackground() throws Exception {
        parser.parseFile(file,model);
        model.parsedSources.addElement(file.getName().toLowerCase());
              
        return model;
    }

}
