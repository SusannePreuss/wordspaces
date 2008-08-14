package wordspaces.gui.listener;

import wordspaces.gui.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import wordspaces.Parser;

public class ParserPopupListener extends MouseAdapter {
    
    private GUI gui;
    private JPopupMenu menu;
    
    public ParserPopupListener(GUI g, JPopupMenu menu){
        gui = g;
        this.menu = menu;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger() && e.getButton() == e.BUTTON3) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getButton() == e.BUTTON1) {
            Parser parser = gui.getParser();
            int index = gui.getParserList().getSelectedIndex();
            if (index != -1) {
                parser = (Parser) gui.getParserList().getModel().getElementAt(index); 
                gui.setParser(parser);
            }
        }
    }
}
