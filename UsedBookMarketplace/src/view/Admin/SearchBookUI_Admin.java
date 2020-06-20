package view.Admin;

import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;

import model.data.Book;
import view.SearchBookUI;

public class SearchBookUI_Admin extends SearchBookUI {
	
	private JButton deleteBtn = new JButton("Delete");
	
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
