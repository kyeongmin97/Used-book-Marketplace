package usedbookmarketplace.view;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public abstract class Table extends JPanel {
	protected DefaultTableModel model;
	protected JTable table = new JTable(model);
	protected JScrollPane scroll;
	
	protected JPanel btnsPanel = new JPanel();
	
	public Table() {
	}
	
	// update table
	public abstract <T> void updateTable(Vector<T> data);
	public abstract void addActionListeners(ActionListener action);
	
	public JTable getTable() {
		return table;
	}
}
