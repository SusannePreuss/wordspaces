package wordspaces.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import wordspaces.gui.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import wordspaces.Model;
import wordspaces.Parser;

public class CheckBoxActionListener implements ActionListener {

    private GUI gui;

    public CheckBoxActionListener(GUI g) {
        super();
        this.gui = g;
    }

    
    public void cleanModelwithFocusWords(Model model, Parser parser) {
 //       Map wordDir = model.getWordDirectory();
        Set<String> focusWords = parser.getFocusWords();
        
        for(int i=0;i<model.getDirectorySize();i++){
            String word = model.getVectorNameAt(i);
            
            if(!focusWords.contains(word)){
                model.deleteWordVector(word);
                model.getCachedDistances().remove(word);
                model.getWordVectorFrequency().remove(word);
            }
        }
        gui.setModelhasChanged(model);
        gui.showWordTable();
    }

    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem src = (JCheckBoxMenuItem) e.getSource();        
        boolean checkBoxState = src.isSelected(); 
        Parser parser = gui.getParser();
        Model model = gui.getModel();
        
        if (src.getText().equals("Enable filler for stop words")) {
            if (parser != null) {               
                parser.enableFiller(checkBoxState);
            }
        }

        //das laden von StopWords und FocusWords muss in EINE methode ausgelagert werden !
        //auf die methode sollte auch von aussen zugegriffen werden können...->für GUI.stdParser


        if (src.getText().equals("Enable stop word filtering")) {
            if (parser != null) {
                if (checkBoxState) {
                    parser.enableStopWordsFilter(createSetFromFile("Select stop words file"));
                }
                else{
                    src.setSelected(false);
                }
            }
            else {
                parser.disableStopWordsFilter();
            }
        }           
        if (src.getText().equals("Activate word selection list")){
            if (checkBoxState) {
                parser.enableFocusWords(createSetFromFile("Select focus words file"));
                if(model != null && model.getDirectorySize() != 0) {
                    int decision = JOptionPane.showConfirmDialog(null, "Automatically delete all words in model except the selected focus words ?", "Question", JOptionPane.YES_NO_OPTION);
                    if (decision == 0) {
                        cleanModelwithFocusWords(model, parser);
                    }
                }
            } else {
                src.setSelected(false);
            }
        } else {
            if(parser != null)
                parser.disableFocusWords();
        }
    }
    
    public Set createSetFromFile(String title){
        File file = null;
        Set<String> words = null;
        BufferedReader reader;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        if (file != null && file.exists()) {
            words = new HashSet<String>();
            String word;
            try {
                reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    word = reader.readLine().toLowerCase().trim();
                    if (!word.isEmpty()) {
                        words.add(word);
                        System.out.println(word+" added to SET...");
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
        }

        return words;
    }
}

