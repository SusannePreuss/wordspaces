/*
 * ParserCreateDialog.java
 *
 * Created on 31. Mai 2007, 15:46
 */

package wordspaces.gui;

import javax.swing.JDialog;
import javax.swing.SpinnerNumberModel;
import wordspaces.*;

/**
 *
 * @author  alexander frey afrey@uos.de
 */
public class ParserCreateDialog extends javax.swing.JPanel {

    /**
     * Creates new form ParserCreateDialog
     */
    public ParserCreateDialog(GUI g) {
        initComponents();
        leftSpinner.setValue(4);        
        rightSpinner.setValue(4);
        gui = g;
        parserNameTextField.setText(gui.lastParserNameCache);
        dialog = new JDialog(gui,"Specify the parsers context boundaries",true);
        dialog.add(this);
        dialog.setSize(310,200);
        dialog.setResizable(false);
        
        dialog.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        leftSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(4,0,50,1));
        rightSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(4,0,50,1));
        createButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        parserNameLabel = new javax.swing.JLabel();
        parserNameTextField = new javax.swing.JTextField();

        setMaximumSize(new java.awt.Dimension(310, 150));
        setMinimumSize(new java.awt.Dimension(310, 150));
        setName("Create Parser"); // NOI18N
        setPreferredSize(new java.awt.Dimension(310, 150));

        jLabel1.setText("Left context width");

        jLabel2.setText("Right context width");

        createButton.setText("Create Parser");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        parserNameLabel.setText("Name");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(leftSpinner)
                                            .add(createButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                        .add(5, 5, 5)))
                                .add(33, 33, 33)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(cancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel2))
                                    .add(rightSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(parserNameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                        .add(14, 14, 14))
                    .add(layout.createSequentialGroup()
                        .add(parserNameLabel)
                        .addContainerGap(259, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(parserNameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(parserNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(leftSpinner)
                    .add(rightSpinner))
                .add(21, 21, 21)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(cancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(createButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        parser = new Parser((Integer)leftSpinner.getValue(),(Integer)rightSpinner.getValue());
        String name = parserNameTextField.getText();
        if(!name.isEmpty()){
            parser.setName(name);
            /* name equals the last created parser, thus we can apply all 
             * properties of the last parser */
            if(name.equals(gui.lastParserNameCache)){
                /* get the last created parser, its name must equal to name */
                Parser last_parser = gui.getParser();
                if(last_parser.getName().equals(name)){
                    /* now we apply all settings to the new parser */
                    parser.enableFocusWords(last_parser.getFocusWords());
                    parser.enableStopWordsFilter(last_parser.getStopWords());
                    parser.enableFiller(last_parser.isFillerEnabled());
                }
            }
            else        //if names differ, then set the new name into gui 
                gui.lastParserNameCache = name;  //save the name
        }
        dialog.dispose();       
        gui.addParser(parser);
        System.out.println("New parser "+name+" created. Left context size is "+leftSpinner.getValue()+" and the right is "+rightSpinner.getValue()+".");
    }//GEN-LAST:event_createButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSpinner leftSpinner;
    private javax.swing.JLabel parserNameLabel;
    private javax.swing.JTextField parserNameTextField;
    private javax.swing.JSpinner rightSpinner;
    // End of variables declaration//GEN-END:variables
    private JDialog dialog;
    public Parser parser;
    private GUI gui;
}
