package view.Admin;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.data.Book;
import model.data.User;
import view.TableUI;

public class ManageAccountUI extends TableUI {
	
	private JButton changeStateBtn = new JButton("Change User State");
	private JButton deleteBtn = new JButton("Delete User");
	private JButton backBtn = new JButton("Back");
	private JButton logoutBtn = new JButton("Logout");
	
	public ManageAccountUI (Vector<User> userList) {
		super();

		// setting table
		String[] colName = { "ID", "PW", "Name", "Phone Number", "Email", "Is Activated" };
		model = new DefaultTableModel(null, colName) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(model);
		//table.getColumn("Phone Number Year").setPreferredWidth(100);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		updateTable(userList);
		
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(640, 430));
		
		// setting button
		btnsPanel.add(changeStateBtn);
		btnsPanel.add(deleteBtn);
		btnsPanel.add(backBtn);
		btnsPanel.add(logoutBtn);
		
		// add components
		add(btnsPanel);
		add(scroll);
	}
	
	@Override
	public <T> void updateTable(Vector<T> userList) {
		model = (DefaultTableModel) table.getModel();
		model.setNumRows(0);

		for (int i = 0; i < userList.size(); i++) {
			String[] bookInfo = ((User)userList.get(i)).getUserInfo();
			if (bookInfo.length == 6)
				model.addRow(new Object[] {bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4], bookInfo[5]});
		}

		table.setModel(model);		
	}
	
	public void addActionListener_changeStateBtn(ActionListener action) {
		changeStateBtn.addActionListener(action);
	}
	public void addActionListener_deleteBtn(ActionListener action) {
		deleteBtn.addActionListener(action);
	}
	public void addActionListener_backBtn(ActionListener action) {
		backBtn.addActionListener(action);
	}
	public void addActionListener_logoutBtn(ActionListener action) {
		logoutBtn.addActionListener(action);
	}
}
