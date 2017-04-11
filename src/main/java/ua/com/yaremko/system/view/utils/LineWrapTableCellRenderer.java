package ua.com.yaremko.system.view.utils;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class LineWrapTableCellRenderer  extends JTextArea implements TableCellRenderer {

	public LineWrapTableCellRenderer() {
	    setLineWrap(true);
	    setWrapStyleWord(true);
	    setOpaque(true);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
	    if (isSelected) {
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	    } else {
	      setForeground(table.getForeground());
	      setBackground(table.getBackground());
	    }
	    setFont(table.getFont());
	    if (hasFocus) {
	      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	      if (table.isCellEditable(row, column)) {
	        setForeground(UIManager.getColor("Table.focusCellForeground"));
	        setBackground(UIManager.getColor("Table.focusCellBackground"));
	      }
	    } else {
	      setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
	    }
	    setText((value == null) ? "" : value.toString());
	    return this;
	  }

}