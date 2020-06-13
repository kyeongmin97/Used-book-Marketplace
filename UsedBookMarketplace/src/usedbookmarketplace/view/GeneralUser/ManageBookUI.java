package usedbookmarketplace.view.GeneralUser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.view.TableUI;

public class ManageBookUI extends TableUI{

	//private InputBookInfoUI manageDialog; 
	private JButton registerBtn = new JButton("Register");
	private JButton modifyBtn = new JButton("Modify");
	private JButton deleteBtn = new JButton("Delete");
	private JButton backBtn = new JButton("Back");
	private JButton logoutBtn = new JButton("Logout");
	
	public ManageBookUI (Vector<Book> bookList) {
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
		scroll.setPreferredSize(new Dimension(640, 430));
		
		// setting button
		btnsPanel.add(registerBtn);
		btnsPanel.add(modifyBtn);
		btnsPanel.add(deleteBtn);
		btnsPanel.add(backBtn);
		btnsPanel.add(logoutBtn);
		
		// add components
		add(btnsPanel);
		add(scroll);
	}
	
	@Override
	public <T> void updateTable(Vector<T> bookList) {
		model = (DefaultTableModel) table.getModel();
		model.setNumRows(0);

		for (int i = 0; i < bookList.size(); i++) {
			String[] bookInfo = ((Book)bookList.get(i)).getBookInfo();
			model.addRow(new Object[] { bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4],
										bookInfo[5], bookInfo[6], bookInfo[7], bookInfo[8] });
		}

		table.setModel(model);		
	}
	
	public void addActionListener_registerBtn(ActionListener action) {
		registerBtn.addActionListener(action);
	}
	public void addActionListener_modifyBtn(ActionListener action) {
		modifyBtn.addActionListener(action);
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
