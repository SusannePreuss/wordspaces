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
import java.util.Iterator;
import java.util.Map;
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
        Map wordDir = model.wordDirectory;
        Set<String> focusWords = parser.getFocusWords();
        Iterator<String> wordDirIter = wordDir.keySet().iterator();
        
        while(wordDirIter.hasNext()){
            String word = wordDirIter.next();
            
            if(!focusWords.contains(word)){
                wordDirIter.remove();
                model.distances.remove(word);
                model.wordOccurences.remove(word);
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
        if (src.getText().equals("Enable stop word filtering")) {
            if (parser != null) {
                if (checkBoxState) {
                    File file = null;
                    BufferedReader reader;
                    JFileChooser chooser = new JFileChooser();                    
                    if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
                        file = chooser.getSelectedFile();
                    }
                    if (file != null && file.exists()) {
                        HashSet<String> stopWords = new HashSet<String>();
                        String word;
                        try {
                            reader = new BufferedReader(new FileReader(file));
                            while (reader.ready()) {
                                word = reader.readLine().toLowerCase().trim();
                                if (!word.isEmpty()) {
                                    stopWords.add(word);
                                    System.out.println(word+" added to stop word list...");
                                }
                            }
                            parser.enableStopWordsFilter(stopWords);
                        }catch (FileNotFoundException ex) {
                            Logger.getLogger("global").log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger("global").log(Level.SEVERE, null, ex);
                        }                       
                    }
                    else{
                        src.setSelected(false);
                    }
                }
                else {
                    parser.disableStopWordsFilter();
                }
            }
        }           
        if (src.getText().equals("Activate word selection list")){
            if (checkBoxState) {
                File file = null;
                BufferedReader reader;
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                }
                if (file != null && file.exists()) {
                    HashSet<String> words = new HashSet<String>();
                    String word;
                    try {
                        reader = new BufferedReader(new FileReader(file));
                        while (reader.ready()) {
                            word = reader.readLine().toLowerCase().trim();
                            if (!word.isEmpty()) {
                                words.add(word);
                                System.out.println(word+" added to focus list...");
                            }
                        }
                        parser.enableFocusWords(words);
                        if(model != null && model.wordDirectory.size() != 0) {
                            int decision = JOptionPane.showConfirmDialog(null, "Automatically delete all words in model except the selected focus words ?", "Question", JOptionPane.YES_NO_OPTION);
                            if (decision == 0) {
                                cleanModelwithFocusWords(model, parser);
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    }
                } else {
                    src.setSelected(false);
                }
            } else {
                if(parser != null)
                    parser.disableFocusWords();
            }
        }
    }
}

