package usedbookmarketplace.view.Admin;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.view.SearchBookUI;

public class SearchBookUI_Admin extends SearchBookUI {
	
	public JButton deleteBtn = new JButton("Delete");
	
	public SearchBookUI_Admin(Vector<Book> bookList) {
		super(bookList);
		
		btnsPanel.add(deleteBtn);
		btnsPanel.add(backBtn);
		btnsPanel.add(logoutBtn);
	}
	
	public void addActionListener_delete(ActionListener action) {
		deleteBtn.addActionListener(action);
	}
}
