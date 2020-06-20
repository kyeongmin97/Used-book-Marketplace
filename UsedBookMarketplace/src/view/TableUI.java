package view;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class TableUI extends JPanel {
	protected DefaultTableModel model;
	protected JTable table = new JTable(model);
	
	protected JPanel btnsPanel = new JPanel();
	
	public TableUI() {
		
	}
	
	// update table
	public abstract <T> void updateTable(Vector<T> data);
	
	public JTable getTable() {
		return table;
	}
}
