package wordspaces.gui;

import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author alexander
 */
public class GroupRenderer extends DefaultTableCellRenderer {

    private DistancesPanel distPanel;

    private Color[] colors;

    public GroupRenderer(DistancesPanel panel){
        this.distPanel = panel;
        colors = new Color[9];

        colors[0] = Color.BLUE;
        colors[1] = Color.ORANGE;
        colors[2] = Color.GREEN;
        colors[3] = Color.CYAN;
        colors[4] = Color.RED;
        colors[5] = Color.PINK;
        colors[6] = Color.YELLOW;
        colors[7] = Color.GRAY;
        colors[8] = Color.MAGENTA;
    }

    @Override
    public void setValue( Object value )
    {
        String vector = value.toString();
        int grp = distPanel.getGroupNumber(vector);
        if ( grp != 0 ){                                //vector is member of group grp
            setForeground  ( colors[grp-1] );
            setText  ( vector );
        }
        else{
            setForeground  ( Color.BLACK );
            setText  ( vector );
        }
    }
}
