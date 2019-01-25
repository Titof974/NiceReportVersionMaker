package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class validityCellRender extends DefaultTableCellRenderer {
    JLabel lbl = new JLabel();

    //ImageIcon icon = new ImageIcon(getClass().getResource("sample.png"));

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
//        lbl.setText((String) value);
//        lbl.setForeground(Color.red);
//        this.setHorizontalAlignment(JLabel.CENTER);
//        this.setEnabled(false);
//        //lbl.setIcon(icon);
//        return lbl;
        JPanel jPanel = new JPanel();
        jPanel.setSize(10,10);
        jPanel.setBackground((boolean) value ? Color.green : Color.red);
        return jPanel;
    }
}
