/*
 * DistancesPanel.java
 *
 * Created on 15. Juni 2007, 10:44
 */

package wordspaces.gui;

import wordspaces.gui.listener.CreateGroupListener;
import wordspaces.gui.listener.CompareWordsListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import wordspaces.Model;
import wordspaces.gui.graph.Graph;

/**
 *
 * @author  alexander
 */
public class DistancesPanel extends javax.swing.JPanel {
    
    private GUI gui;
    private HashMap<Integer,Vector> groups;
    
    /** Creates new form DistancesPanel */
    public DistancesPanel(final GUI gui) {
        initComponents();
        visualizeButton.setEnabled(false);
        this.gui = gui;
        jSplitPane1.setDividerLocation(140);

        groups           = new HashMap();
        doubleComparator = new DoubleComparator();
        distPopup        = new JPopupMenu();
        JMenuItem compareVectors = new JMenuItem("Compare");
        JMenuItem createGroup    = new JMenuItem("Create group");

        distPopup.add(compareVectors);
        distPopup.add(createGroup);

        comparisonTableModel = new javax.swing.table.DefaultTableModel(
            new String [] { "Context words", "Frequency", "Frequency", "Contribution2Similarity", "Contribution2Diversity" }, 0
        );
        comparisonTable.setModel(comparisonTableModel);

        wordDirTableModel = new javax.swing.table.DefaultTableModel(
            new String [] {
                "Word vectors"
            }, 0
        );
        wordDirTable.setModel(wordDirTableModel);
        wordDirTable.setAutoCreateRowSorter(true);
        
        wordDistTableModel = new javax.swing.table.DefaultTableModel(
            new String [] {
                "Word", "Similarity"
            }, 0
        );
        wordDistTable.setModel(wordDistTableModel);
        TableRowSorter<TableModel> wordDistSorter = new TableRowSorter<TableModel>(wordDistTableModel);
        wordDistSorter.setComparator(1, new DoubleComparator());
        wordDistTable.setRowSorter(wordDistSorter);

        compareVectors.addActionListener(new CompareWordsListener(gui,wordDirTable));
        
        createGroup.addActionListener(new CreateGroupListener(this));

        wordDirTable.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == e.BUTTON1){
                    int index = wordDirTable.getSelectedRow();
                    if(index != -1){
                        showWordDistTable((String) wordDirTableModel.getValueAt(wordDirTable.convertRowIndexToModel(index),0));
                    }
                }else if(e.getButton() == e.BUTTON3){          //this code is a dublicate from GUI and that's not so nice !
                    distPopup.show(e.getComponent(),e.getX(), e.getY());
                }
            }
            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }      
        });

        wordDirTable.setDefaultRenderer(Object.class, new GroupRenderer(this));
        
        dialog = new JDialog();
        dialog.add(this);
        dialog.setSize(860,400);
        dialog.setLocation(60,350);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        distanceUpdateLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        distancesPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        wordDirTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        wordDistTable = new javax.swing.JTable();
        wordsCountLabel = new javax.swing.JLabel();
        contextWordsCountLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        groupCounterLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        groupGradeLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comparisonPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        comparisonTable = new javax.swing.JTable();
        scalarLabel = new javax.swing.JLabel();
        euclideanLabel = new javax.swing.JLabel();
        visualizeButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        calcGroupGradeButton = new javax.swing.JButton();
        clearGroupSettingsButton = new javax.swing.JButton();

        wordDirTable.setModel(new javax.swing.table.DefaultTableModel(
            new String [] {
                "Words"
            }, 0
        ));
        jScrollPane2.setViewportView(wordDirTable);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jScrollPane3.setViewportView(wordDistTable);

        jSplitPane1.setRightComponent(jScrollPane3);

        wordsCountLabel.setText("0"); // NOI18N

        contextWordsCountLabel.setText("0"); // NOI18N

        jLabel1.setText("Number of groups ");

        groupCounterLabel.setText("0");

        jLabel2.setText("Calculated group grade");

        groupGradeLabel.setText("<not available>");

        jLabel3.setText("Items");

        jLabel4.setText("Items");

        javax.swing.GroupLayout distancesPanelLayout = new javax.swing.GroupLayout(distancesPanel);
        distancesPanel.setLayout(distancesPanelLayout);
        distancesPanelLayout.setHorizontalGroup(
            distancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distancesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(distancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(distancesPanelLayout.createSequentialGroup()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(distancesPanelLayout.createSequentialGroup()
                        .addComponent(wordsCountLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupCounterLabel)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupGradeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                        .addComponent(contextWordsCountLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(23, 23, 23))))
        );
        distancesPanelLayout.setVerticalGroup(
            distancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distancesPanelLayout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addGroup(distancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wordsCountLabel)
                    .addComponent(jLabel1)
                    .addComponent(groupCounterLabel)
                    .addComponent(jLabel2)
                    .addComponent(groupGradeLabel)
                    .addComponent(jLabel3)
                    .addComponent(contextWordsCountLabel)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Distances", distancesPanel);

        comparisonTable.setModel(new javax.swing.table.DefaultTableModel(
            new String [] {
                "Context", "Vector 1", "Vector 2", "Similarity", "Difference"
            },0
        ));
        jScrollPane1.setViewportView(comparisonTable);

        scalarLabel.setText("Scalarproduct");

        euclideanLabel.setText("Euclidean distance");

        javax.swing.GroupLayout comparisonPanelLayout = new javax.swing.GroupLayout(comparisonPanel);
        comparisonPanel.setLayout(comparisonPanelLayout);
        comparisonPanelLayout.setHorizontalGroup(
            comparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(comparisonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(comparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                    .addGroup(comparisonPanelLayout.createSequentialGroup()
                        .addComponent(euclideanLabel)
                        .addGap(187, 187, 187)
                        .addComponent(scalarLabel)))
                .addContainerGap())
        );
        comparisonPanelLayout.setVerticalGroup(
            comparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(comparisonPanelLayout.createSequentialGroup()
                .addGroup(comparisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(euclideanLabel)
                    .addComponent(scalarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Comparison", comparisonPanel);

        visualizeButton.setText("Visualize in Graph"); // NOI18N
        visualizeButton.setToolTipText("Visualize distances in a graph");
        visualizeButton.setEnabled(false);
        visualizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizeButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear all Distances"); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        calcGroupGradeButton.setText("Calculate Group Grade");
        calcGroupGradeButton.setToolTipText("Calculate the grade (0-1) of the groups");
        calcGroupGradeButton.setEnabled(false);
        calcGroupGradeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcGroupGradeButtonActionPerformed(evt);
            }
        });

        clearGroupSettingsButton.setText("Clear Groups");
        clearGroupSettingsButton.setToolTipText("Clear all group settings");
        clearGroupSettingsButton.setEnabled(false);
        clearGroupSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearGroupSettingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                    .addComponent(distanceUpdateLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearGroupSettingsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(visualizeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calcGroupGradeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(distanceUpdateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearGroupSettingsButton)
                    .addComponent(visualizeButton)
                    .addComponent(calcGroupGradeButton)
                    .addComponent(clearButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearAll();
}//GEN-LAST:event_clearButtonActionPerformed

    private void visualizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeButtonActionPerformed
        String num = JOptionPane.showInputDialog(this, "Enter number of k-largest edges between nodes that should be considered");
        if(num != null){
            int k_smallest_edges = Integer.parseInt(num);
            String s = new String(), wordVector, contextWord;
            Model model = gui.getModel();
            Iterator wordsIter = model.getCachedDistances().keySet().iterator();

            TreeSet<Entry<String,Double>> sortedSet;
            Iterator<Entry<String,Double>> sortedSetIter;
            Entry<String,Double> entry;

            while(wordsIter.hasNext()){             //run through all words in wordDirTable
                wordVector = (String) wordsIter.next();           
                sortedSet = getKsmallestEdges( model.getCachedDistances().get(wordVector) , k_smallest_edges );

                sortedSetIter = sortedSet.iterator();

                //now the remaining last entries from distancesIter get written to String s
                while(sortedSetIter.hasNext()){
                    entry = sortedSetIter.next();
                    contextWord = entry.getKey();
                    double d = entry.getValue();  //contextWord is the corresponding context word to d
                    s = s.concat(wordVector+"-"+contextWord+"/"+d+",");               
                }     
            }
            System.out.println("Transmitting : "+s);
            Graph graph = new Graph(s,k_smallest_edges+" largest edges considered",model.getCachedDistances().size());
        }
}//GEN-LAST:event_visualizeButtonActionPerformed

    private void calcGroupGradeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcGroupGradeButtonActionPerformed
        float grade = 0, error = 0, group_error = 0, possible_maximal_error = 0;
        Model model = gui.getModel();
        Iterator<Integer> groupIter = groups.keySet().iterator();
        Iterator<Entry<String,Double>> k_smallest_edges_iter;
        String word, neighborWord;
        Vector<String> members;
        TreeSet<Entry<String, Double>> k_smallest_edges;
        Entry entry;
        int grp, grp_SIZE, errors = 0,
            word_counter = 0, 
            pos_in_k_smallest_edges = 0;     //pos of the word of k_smallest_edges, needed to calculate the error

        while(groupIter.hasNext()){          //go through all groups
            System.out.println("!!!!!!NEW GROUP!!!!");
            grp = groupIter.next();
            members = groups.get(grp);
            //first identify the number of words in the same group as vectorName
            grp_SIZE = members.size();
            word_counter += (members.size()-1) * members.size();
            group_error = 0;
            possible_maximal_error += calcMaxGroupError(grp_SIZE);
            System.out.println(possible_maximal_error);
            
            /* go through all words in the group */
            for( int i=0 ; i < grp_SIZE ; i++ ){        
                word = members.elementAt(i);            //get the next word name
                k_smallest_edges = getKsmallestEdges(model.getCachedDistances().get(word), grp_SIZE-1);   //now we have the k smallest edges to word                                
                k_smallest_edges_iter = k_smallest_edges.iterator();
                pos_in_k_smallest_edges = 0;                    //is reseted for the next word
                
                /* now check for word how many errors have been made */
                while(k_smallest_edges_iter.hasNext()){         
                    entry = k_smallest_edges_iter.next();
                    neighborWord = (String) entry.getKey();
                    pos_in_k_smallest_edges++;                  //first word is at pos 1 and so on...
                    /* now check if this neighborWord is in the same group as word, if so give points. */
                    if(!members.contains(neighborWord)){
                        /* Calculate the error by dividing 1 by pos_in_k_smallest_edges and add it to grade */
                        error = (float) 1 / (float) pos_in_k_smallest_edges;
                        group_error += error;
                        errors++;
                        System.out.println("Error! "+word.toUpperCase()+" neighbor is "+neighborWord.toUpperCase()+" pos is "+pos_in_k_smallest_edges+" error "+error);
                     }
                }
            }
            /* Normalize the group_error by dividing it by the size of the group */
            group_error = group_error / (float) grp_SIZE;
            System.out.println("Group error is "+group_error);
            grade += group_error;
        }
        grade = (possible_maximal_error-grade) / possible_maximal_error;
        groupGradeLabel.setText(grade+" ("+(possible_maximal_error-grade)+"/"+possible_maximal_error+") Errors:"+errors);
    }//GEN-LAST:event_calcGroupGradeButtonActionPerformed

    private void clearGroupSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearGroupSettingsButtonActionPerformed
        clearGroups();
        clearGroupLabels();
}//GEN-LAST:event_clearGroupSettingsButtonActionPerformed
    /**
     * This method clears everything that has to do with the group settings 
     * and later invokes the clearGroupLabels() method...
     */
    public void clearGroups(){
        groups.clear();
        groupCounterLabel.setText("0");
        clearGroupLabels();
        clearGroupSettingsButton.setEnabled(false);
        calcGroupGradeButton.setEnabled(false);
    }
    
    public void clearGroupLabels(){
        groupGradeLabel.setText("<not available>");
        wordDirTable.repaint();         //to remove the colored words...
    }
    
    /**
     * Computes the k greatest key-values pairs from map and returns them in descending
     * order in a TreeSet.
     * @param map Map from which the k greatest key-values pairs are computed.
     * @param k Number of greatest key-value pairs in map where values are used for ordering.
     * @return TreeSet<Entry>. Entries are sorted by values in descending order. If two 
     * equal, then keys are used for ordering.
     */     
    public TreeSet< Entry<String,Double> > getKsmallestEdges(SortedMap<String, Double> map, int k){
         TreeSet< Entry<String,Double> > result = new TreeSet(new Comparator() {
            public int compare(Object obj, Object obj1) {
                int vcomp = ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
                if (vcomp != 0) return vcomp;
                else return ((Comparable) ((Map.Entry) obj1).getKey()).compareTo(((Map.Entry) obj).getKey());
            }           
         });           
         result.addAll(map.entrySet());
         Iterator< Entry<String,Double> > result_iter = result.descendingIterator(); //descending is wrong but its the only its running

         int delete_counter = result.size() - k;
         while(result_iter.hasNext() && delete_counter != 0){
            result_iter.next();
            result_iter.remove();
            delete_counter--;
         }

         return result;
    }
    
    public float calcMaxGroupError(int grp_SIZE){
        float result = 0;
        for(int i=1;i<grp_SIZE;i++){
            result += (float) 1 / (float) i;
        }
        
        return result;
    }

    public void addtoGroup(String v, int grp){
        Vector vector;
        if(groups.containsKey(grp)){            //Group is already present
            vector = groups.get(grp);
            vector.addElement(v);
        }
        else{
            vector = new Vector();
            vector.addElement(v);
            groups.put(grp, vector);
        }
    }

    public HashMap<Integer,Vector> getGroups(){
        return groups;
    }

    public void showDistances(SortedMap distances){
        this.distances = distances;
        clearWordDirTable();
        clearWordDistTable();
        clearComparisonTable();
        Iterator iter = distances.keySet().iterator(); 
        while(iter.hasNext()){
            wordDirTableModel.addRow(new Object[] {iter.next()});
        }
        if(distances.size() > 0){
            visualizeButton.setEnabled(true);
            clearButton.setEnabled(true);
        }
        wordsCountLabel.setText(distances.size()+"");
        setVisible(true);
    }
    
    public void showWordDistTable(String selectedWord){
        clearWordDistTable();
        TreeMap distMap = (TreeMap)gui.getModel().getCachedDistances().get(selectedWord);
        Iterator iter = distMap.keySet().iterator();
        while(iter.hasNext()){
            String word = (String) iter.next();
            double distance = (Double) distMap.get(word);
            wordDistTableModel.addRow(new Object[] {word, distance});
        }
        contextWordsCountLabel.setText(distMap.size()+"");      
    }
    
    /**
     * Clear everything from the tables to the groups and labels.
     */
    public void clearAll(){
        gui.getModel().eraseDistanceCache();
        clearWordDirTable();
        clearWordDistTable();
        //now everything in comparison gets deleted
        clearComparisonTable();
        euclideanLabel.setText("Euclidean distances");
        scalarLabel.setText("Scalarproduct");     
        visualizeButton.setEnabled(false);        
        clearButton.setEnabled(false);
        //now clear all group settings
        clearGroups();
        clearGroupLabels();
    }
    
    public void clearWordDirTable(){
        wordDirTableModel.setRowCount(0); 
        wordsCountLabel.setText("0");
    }
    
    public void clearWordDistTable(){
        wordDistTableModel.setRowCount(0);   
        contextWordsCountLabel.setText("0");
    }

    public void clearComparisonTable(){
        comparisonTableModel.setRowCount(0);
    }
    
    public void setVisible(boolean vis){
        dialog.setVisible(vis);
    }

    public JTable getWordTable(){
        return wordDirTable;
    }
    
    public JTable getDistTable(){
        return wordDistTable;
    }
    
    public JTable getComparisonTable(){
        return comparisonTable;
    }

    public int getNumberofGroups(){
        return groups.size();
    }

 
    
    /**
     * Returns the corresponding group number of
     * the given vectorName.
     *
     * @param vectorName the given vector name
     * @return the group to which the vector belongs. 0 if it
     * is not a member of any group.
     */
    public int getGroupNumber(String vectorName){
        int grp = 0;
        Iterator<Integer> iter = groups.keySet().iterator();
        while(iter.hasNext()){
            grp = iter.next();
            if(groups.get(grp).contains(vectorName))
                return grp;
        }

        return 0;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton calcGroupGradeButton;
    public javax.swing.JButton clearButton;
    public javax.swing.JButton clearGroupSettingsButton;
    private javax.swing.JPanel comparisonPanel;
    protected javax.swing.JTable comparisonTable;
    private javax.swing.JLabel contextWordsCountLabel;
    protected javax.swing.JLabel distanceUpdateLabel;
    private javax.swing.JPanel distancesPanel;
    public javax.swing.JLabel euclideanLabel;
    public javax.swing.JLabel groupCounterLabel;
    private javax.swing.JLabel groupGradeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel scalarLabel;
    public javax.swing.JButton visualizeButton;
    private javax.swing.JTable wordDirTable;
    private javax.swing.JTable wordDistTable;
    protected javax.swing.JLabel wordsCountLabel;
    // End of variables declaration//GEN-END:variables
    private JDialog dialog;
    protected javax.swing.table.DefaultTableModel wordDirTableModel;
    protected javax.swing.table.DefaultTableModel wordDistTableModel;
    protected javax.swing.table.DefaultTableModel comparisonTableModel;
    private SortedMap distances;
    public DoubleComparator doubleComparator;
    private JPopupMenu distPopup;
}
