package usedbookmarketplace.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.data.user.User;

public class AccountPanel extends Table {
	private JButton changeStateBtn = new JButton("Change User State");
	private JButton deleteBtn = new JButton("Delete User");
	private JButton logoutBtn = new JButton("Logout");
	
	public AccountPanel() {
		
	}	
	
	public AccountPanel (Vector<User> bookList) {
		super();

		// setting table
		String[] colName = { "Title", "Author", "Publisher", "Publication Year", "ISBN", "Price", "Book State", "Seller ID", "Is Sold" };
		model = new DefaultTableModel(null, colName) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(model);
		table.getColumn("Publication Year").setPreferredWidth(100);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		updateTable(bookList);
		
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(640, 480));
		
		// setting button
		btnsPanel.add(changeStateBtn);
		btnsPanel.add(deleteBtn);
		btnsPanel.add(logoutBtn);
		
		// add components
		add(btnsPanel);
		add(scroll);
	}
	
	@Override
	public <T> void updateTable(Vector<T> data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addActionListeners(ActionListener action) {
		changeStateBtn.addActionListener(action);
		deleteBtn.addActionListener(action);
		logoutBtn.addActionListener(action);
	}
	
	public JButton getChangeStateBtn() {	return changeStateBtn;	}
	public JButton getDeleteBtn() {		return deleteBtn;	}
	public JButton getLogoutBtn() {		return logoutBtn;	}
}
