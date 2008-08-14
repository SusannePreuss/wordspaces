/*
 * Graph.java
 * 
 * Created on 08.08.2007, 23:22:27
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordspaces.gui.graph;


import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;



class GraphPanel extends Panel
    implements Runnable, MouseListener, MouseMotionListener {
    Graph graph;
    boolean showDist;
    boolean showBiDEdges;       //bidirectional edges
    boolean showRepEdges;       //repulsive edges
    boolean showUniEdges;       //standard edges
    boolean pause;
    boolean scuff;
    boolean amplify;
    
    int nnodes;
    public Node nodes[];

    int nedges;
    public Edge edges[];

    Thread relaxer;

    GraphPanel(Graph graph, int nodesCount) {
	this.graph = graph;
        nodes = new Node[nodesCount];
        edges = new Edge[2*nodesCount*(nodesCount-1)];
	addMouseListener(this);
    }

    int findNode(String lbl) {
	for (int i = 0 ; i < nnodes ; i++) {
	    if (nodes[i].lbl.equals(lbl)) {
		return i;
	    }
	}
	return addNode(lbl);
    }
    int addNode(String lbl) {
	Node n = new Node();
	n.x = 10 + 380*Math.random();
	n.y = 10 + 380*Math.random();
	n.lbl = lbl;
	nodes[nnodes] = n;
	return nnodes++;
    }
    void addEdge(String from, String to, double len, double dist, boolean isBiDirectional) {
        int fromNodeNR = findNode(from);
        int toNodeNR   = findNode(to);
        if(nodes[fromNodeNR].edges.containsKey(toNodeNR)){          //there exist already an edge from 'fromNode' to 'toNode'
            if(nodes[fromNodeNR].edges.get(toNodeNR).dist < dist){  //old labelDistance value is smaller, so it is going
                nodes[fromNodeNR].edges.get(toNodeNR).dist = dist;  //to be replaced by new value
                nodes[toNodeNR].edges.get(fromNodeNR).dist = dist;
            }              
        }else{                                                      //new edge pair must be constructed
            Edge forth = new Edge();
            Edge back  = new Edge();

            forth.from = fromNodeNR;        
            forth.to   = toNodeNR;
            forth.len  = len;
            forth.dist = dist;
            back.from  = toNodeNR;
            back.to    = fromNodeNR;
            back.len   = len;
            back.dist  = dist;
            
            nodes[fromNodeNR].edges.put(forth.to, forth);              //then we add to the node 'from' edge e
            nodes[toNodeNR].edges.put(back.to, back);
            edges[nedges++] = forth;
            edges[nedges++] = back;
            
            if(isBiDirectional){
                forth.isBiDirectEdge = true;
                back.isBiDirectEdge  = true;
            }
        }
    }

    public void run() {
        Thread me = Thread.currentThread();
	while (relaxer == me) {
	    if(!pause)
                relax();
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		break;
	    }
	}
    }

    synchronized void relax() {
        double vx,vy,len,
               dx,dy,f;
        Edge e;
	for (int i = 0 ; i < nedges ; i++) {
	    e = edges[i];
	    vx = nodes[e.to].x - nodes[e.from].x;
	    vy = nodes[e.to].y - nodes[e.from].y;
            
            if(amplify && e.isBiDirectEdge){
                len = Math.sqrt(vx * vx + vy * vy)*10;
            }else{
                len = Math.sqrt(vx * vx + vy * vy);
            }
            
            len = (len == 0) ? .0001 : len;
	    f   = (edges[i].len - len) / (len * 10);
	    dx  = f * vx;
	    dy  = f * vy;

	    nodes[e.to].dx += dx;
	    nodes[e.to].dy += dy;
	    nodes[e.from].dx += -dx;
	    nodes[e.from].dy += -dy;
	}

        Node n1,n2;
        double bestdist, dist;
        int nearestNode;
        Dimension dim = getSize();
	for (int i = 0 ; i < nnodes ; i++) {
	    n1 = nodes[i];
	    dx = 0;
	    dy = 0;
            bestdist = Double.MAX_VALUE;
            nearestNode = 0;

	    for (int j = 0 ; j < nnodes ; j++) {
                if(i == j) continue;
		n2 = nodes[j];
		vx = n1.x - n2.x;
		vy = n1.y - n2.y;
		len = vx * vx + vy * vy;        //distance between two nodes
		if (len == 0) {
		    dx += Math.random();
		    dy += Math.random();
		} else if (len < 100*100) {
		    dx += vx / len;
		    dy += vy / len;
		}
                if (scuff){                  
                    dist = len;   //einfach len verwenden !!!!!!!
                    if (dist < bestdist) {
                        nearestNode = j;                               
                        bestdist = dist;                        
                    }
                }
            }
            
            if(scuff){               
                if(!nodes[i].edges.containsKey(nearestNode)){                
                    addEdge(nodes[i].lbl, nodes[nearestNode].lbl, 200, 0, false);
                    System.out.println("New edge between: "+nodes[i].lbl+"--->"+nodes[nearestNode].lbl+"  Abstand: 100!");
       //         }else{
       //             Edge eForth = nodes[i].edges.get(nearestNode);
       //             Edge eBack  = nodes[nearestNode].edges.get(i);
       //             if(eForth.labelDistance == 0){
       //                 eForth.distance += 10;
       //                 eBack.distance  += 10;
       //                 System.out.println("Longer between: "+nodes[i].lbl+"--->"+nodes[nearestNode].lbl);
       //             }
                    
                }
            }
	
            double dlen = dx * dx + dy * dy;
            if (dlen > 0) {
                dlen = Math.sqrt(dlen) / 2;
                n1.dx += dx / dlen;
                n1.dy += dy / dlen;
            }
        }
        Node n;
	for (int i = 0 ; i < nnodes ; i++) {
	    n = nodes[i];
	    if (!n.fixed) {
		n.x += Math.max(-5, Math.min(5, n.dx)/2);
		n.y += Math.max(-5, Math.min(5, n.dy)/2);
            }
 //sollen die raender beruecksichtigt werden ?
 //           if (n.x < 0) {
 //               n.x = 0;
 //           } 
 //           else if (n.x > dim.width) {
 //               n.x = dim.width;
 //           }
 //           if (n.y < 0) {
 //               n.y = 0;
 //           } 
 //           else if (n.y > dim.height) {
 //               n.y = dim.height;
 //           }
	    n.dx /= 2;
	    n.dy /= 2;
	}
	repaint();
    }

    Node pick;
    boolean pickfixed;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    final Color fixedColor = Color.red;
    final Color selectColor = Color.pink;
    final Color nodeColor = new Color(250, 220, 100);
    final Color stressColor = Color.darkGray;
    final Color arcColorUniDirect = Color.blue;
    final Color arcColorBiDirect  = Color.green;
    final Color arcColorRepulsive = Color.black;

    public void paintNode(Graphics g, Node n, FontMetrics fm) {
	int x = (int)n.x;
	int y = (int)n.y;
	g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
	int w = fm.stringWidth(n.lbl) + 10;
	int h = fm.getHeight() + 4;
	g.fillRect(x - w/2, y - h / 2, w, h);
	g.setColor(Color.black);
	g.drawRect(x - w/2, y - h / 2, w-1, h-1);
	g.drawString(n.lbl, x - (w-10)/2, (y - (h-4)/2) + fm.getAscent());
    }

    @Override
    public synchronized void update(Graphics g) {
	Dimension d = getSize();
	if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
	    offscreen = createImage(d.width, d.height);
	    offscreensize = d;
	    if (offgraphics != null) {
	        offgraphics.dispose();
	    }
	    offgraphics = offscreen.getGraphics();
	    offgraphics.setFont(getFont());
	}

	offgraphics.setColor(getBackground());
	offgraphics.fillRect(0, 0, d.width, d.height);

        if(showBiDEdges || showRepEdges || showUniEdges){
            for (int i = 0 ; i < nedges ; i++) {
                if(edges[i].isBiDirectEdge && showBiDEdges){     //now the bidirectional edges get painted
                    edges[i].paint(offgraphics);
                }
                if(edges[i].dist == 0.0 && showRepEdges){                   //now the repulsive (0.0) edges get painted
                    edges[i].paint(offgraphics);
                }
                if(showUniEdges && edges[i].dist != 0.0 && !edges[i].isBiDirectEdge )    {                                       //standard edges get painted
                    edges[i].paint(offgraphics);
                }
            }
        }
	        
        else if(pick != null){       //now the distances on the edges from the selected node gets painted
            Iterator iter = pick.edges.keySet().iterator();       
            while(iter.hasNext()){
                Integer i = (Integer) iter.next();
                pick.edges.get(i).paint(offgraphics);
            }
        }
        
	FontMetrics fm = offgraphics.getFontMetrics();
	for (int i = 0 ; i < nnodes ; i++) {
	    paintNode(offgraphics, nodes[i], fm);
	}
	g.drawImage(offscreen, 0, 0, null);
    }

    public void mouseClicked(MouseEvent e) {        
        if(e.getButton() == e.BUTTON3){
            double bestdist = Double.MAX_VALUE;
            int x = e.getX();
            int y = e.getY();
            for (int i = 0 ; i < nnodes ; i++) {
                Node n = nodes[i];
                double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
                if (dist < bestdist) {      //determines the closest node to the position where the click has occured
                    pick = n;
                    bestdist = dist;
                }
            }
            
            if(!pick.fixed) {
                pick.fixed = true;
                showDist = true;
            }
            else pick.fixed = false;
        }    
    }

    public void mousePressed(MouseEvent e) {
        if(nnodes >= 1){
            addMouseMotionListener(this);
            double bestdist = Double.MAX_VALUE;
            int x = e.getX();
            int y = e.getY();
            for (int i = 0 ; i < nnodes ; i++) {
                Node n = nodes[i];
                double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
                if (dist < bestdist) {
                    pick = n;                               //pick is the node which is closest to the place
                    bestdist = dist;                        //where the mouseEvent occured
                }
            }
            pickfixed = pick.fixed;
            pick.fixed = true;
            pick.x = x;
            pick.y = y;
            showDist = true;
            repaint();
            e.consume();
        }
    }

    public void mouseReleased(MouseEvent e) {
        removeMouseMotionListener(this);
        if (pick != null) {
            pick.x = e.getX();
            pick.y = e.getY();
            pick.fixed = pickfixed;
            pick = null;
            showDist = graph.showDistances.isSelected();
            repaint();
            e.consume();
        }	
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        pick.x = e.getX();
	pick.y = e.getY();
	repaint();
	e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void start() {
	relaxer = new Thread(this);
	relaxer.start();
    }
    
    
    public void drawThickLine(
        Graphics g, int x1, int y1, int x2, int y2, float thickness, Color c) {
        // The thick line is in fact a filled polygon
        g.setColor(c);
        int dX = x2 - x1;
        int dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double)(thickness) / (2 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * (double)dY;
        double ddy = scale * (double)dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
  }

    public class Edge {
        int from;
        int to;
    
        //isBiDirect makes painting of the edges much faster
        boolean isBiDirectEdge = false;

        double len;
        double dist;
    
        public void paint(Graphics offgraphics){
            int x1 = (int) nodes[from].x;
            int y1 = (int) nodes[from].y;
            int x2 = (int) nodes[to].x;                //we need the coordinates from the other node
            int y2 = (int) nodes[to].y;                //in order to paint the string at the correct position
            float thickness = (float) ((dist / 20));
            
            if(thickness > 5)
                thickness = 5;
                
            if(thickness <= 1.5){
                if(isBiDirectEdge)
                    offgraphics.setColor(arcColorBiDirect);
                else if(dist != 0.0)
                    offgraphics.setColor(arcColorUniDirect);
                else
                    offgraphics.setColor(arcColorRepulsive);
                offgraphics.drawLine(x1, y1, x2, y2);
            }else{
                if(isBiDirectEdge)
                    drawThickLine(offgraphics,x1,y1,x2,y2,thickness,arcColorBiDirect);
                else if(dist != 0.0)
                    drawThickLine(offgraphics,x1,y1,x2,y2,thickness,arcColorUniDirect);
                else
                    offgraphics.setColor(arcColorRepulsive);
            }
            
            if(showDist){
                offgraphics.setColor(stressColor);
                offgraphics.drawString(dist+"", x1 + (x2-x1)/2, y1 + (y2-y1)/2);
            }
        }
    }

    
    public class Node {
        double x;
        double y;

        double dx;
        double dy;

        boolean fixed;

        String lbl;
        HashMap<Integer,Edge> edges;

    
        public Node(){
            edges = new HashMap<Integer,Edge>();
        
        }
    }

}


public class Graph extends JFrame implements ItemListener{

    GraphPanel panel;
    Panel controlPanel;
    JCheckBox pause;
    JCheckBox scuff;
    JCheckBox amplify;
    JButton display_edges;
    JPopupMenu edgesPopup;
    JCheckBox showAllEdges;
    JCheckBox showBiDEdges;
    JCheckBox showRepEdges;
    JCheckBox showUniEdges;
    JCheckBox showDistances;
    
    public Graph(String s, String name, int nodesCount) {
        System.out.println("NodesCount:"+nodesCount);
        setLayout(new BorderLayout());
        setSize(600, 400);
        this.setTitle(name);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        showAllEdges  = new JCheckBox("Show all edges");
        showUniEdges  = new JCheckBox("Show unidirectional edges");
        showBiDEdges  = new JCheckBox("Show bidirectional edges");
        showRepEdges  = new JCheckBox("Show repulsive edges");
        showDistances = new JCheckBox("Show distances");

        display_edges = new JButton("Display Modes");

        display_edges.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                edgesPopup.show(e.getComponent(),e.getX(), e.getY());              
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {edgesPopup.setVisible(false);}
        });

        pause      = new JCheckBox("Pause");
        scuff      = new JCheckBox("Selective scuff");
        amplify    = new JCheckBox("Amplify bidirectional edges");
        edgesPopup = new JPopupMenu();

        edgesPopup.add(showAllEdges);
        edgesPopup.add(showUniEdges);
        edgesPopup.add(showBiDEdges);
        edgesPopup.add(showRepEdges);
        edgesPopup.add(showDistances);

	panel = new GraphPanel(this, nodesCount);
	add("Center", panel);      
        controlPanel = new Panel();
	add("South", controlPanel);
        
        controlPanel.add(display_edges);
        controlPanel.add(amplify); 
        controlPanel.add(scuff); 
        controlPanel.add(pause); 

        amplify.addItemListener(this);
        scuff.addItemListener(this);
        pause.addItemListener(this);
        showAllEdges.addItemListener(this);
        showUniEdges.addItemListener(this);
        showBiDEdges.addItemListener(this);
        showRepEdges.addItemListener(this);
        showDistances.addItemListener(this);

        Dimension d = getSize();
        String firstNode, str, tmpDist,
               secondNode;
        //  node1-node2/labelDistance //
	for (StringTokenizer t = new StringTokenizer(s, ",") ; t.hasMoreTokens() ; ) {
            double distance = (d.height+d.width) / 4;               //standard distance if no distValue is given
	    str = t.nextToken();
            int i = str.indexOf('-');
	    if (i > 0) {                                            //checks if only one node is given
                double labelDistance = 0.0;                         //labelDistance is only going to be written on the edges
		int j = str.indexOf('/');
		if (j > 0) {                                        //checks whether a distance is given
                    labelDistance = Double.valueOf(str.substring(j+1).trim()).doubleValue();
                    str = str.substring(0, j); 
                    firstNode = str.substring(0,i);
                    secondNode = str.substring(i+1);
                    if(labelDistance > 0.01){
                        labelDistance = labelDistance * 100;                          //now we have a real percentage value
                        distance = 100 / labelDistance;                               //the larger labelDistance the smaller distance and vice versa
                        labelDistance = Math.round( labelDistance * 100. ) / 100.;
                        distance = (4 * distance * distance);
                        if(distance > (d.height+d.width) / 2)
                            distance = (d.height+d.width) / 4;
                                             
                        //search in s for the contrary connection, if it's present --> bidirectional
                        int k = s.indexOf(secondNode+"-"+firstNode+"/");
                        
                        if(k >=0){  //bidirectional information might be existing
                            //now we have to check if the labelDistance is > 0 for the other direction
                            //then we have a bidirectional edge
                            int l = s.indexOf(",", k);          //index of ',' starting at k
                            tmpDist = s.substring(k+secondNode.concat(firstNode).length()+2, l);
                            if(Double.valueOf(tmpDist).doubleValue() > 0){
                                panel.addEdge(firstNode, secondNode, distance, labelDistance, true);
                            }
                        }
                        else{       //this edge is unidirectional
                            panel.addEdge(firstNode, secondNode, distance, labelDistance, false);
                        }
                    }
                    else{       //distance is too small
                        System.out.println("distance "+labelDistance+" between "+firstNode+" and "+secondNode+" too small to visualize...");
                    }
		}               
	    }
	}
	
        //Make the first node mentioned in String s the center node and fixate it
//        String center = s.substring(0, s.indexOf("-"));
//        Node n = panel.nodes[panel.findNode(center)];
//	n.x = d.width / 2;
//	n.y = d.height / 2;
//	n.fixed = true;
        setVisible(true);
        panel.start();
    }
    
    public void itemStateChanged(ItemEvent e) {
	Object src = e.getSource();
	boolean on = e.getStateChange() == ItemEvent.SELECTED;
        if (src == showAllEdges) {
            panel.showRepEdges = on;
            panel.showBiDEdges = on;
            showRepEdges.setSelected(on);
            showBiDEdges.setSelected(on);
            showUniEdges.setSelected(on);
            /* Disable the other buttons */
            showRepEdges.setEnabled(!on);
            showBiDEdges.setEnabled(!on);
            showUniEdges.setEnabled(!on);
        }
        if (src == showRepEdges) {
            panel.showRepEdges = on;
        }
        if (src == showBiDEdges) {
            panel.showBiDEdges = on;
        }
        if (src == showUniEdges) {
            panel.showUniEdges = on;
        }
        if (src == showDistances) {
            panel.showDist = on;
        }
        if (src == pause) panel.pause = on;
        if (src == scuff) panel.scuff = on;
        if (src == amplify) panel.amplify = on;
    }
}
