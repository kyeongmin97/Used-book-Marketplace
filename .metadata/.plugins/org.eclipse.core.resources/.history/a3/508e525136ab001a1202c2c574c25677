package usedbookmarketplace.view;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;

import usedbookmarketplace.model.data.Book;

public class Search_GeneralUser extends Search {
	
	public JButton purchaseBtn = new JButton("Purchase");
	
	public Search_GeneralUser() {
		
	}
	
	public Search_GeneralUser(Vector<Book> bookList) {
		super(bookList);
		
//		table.getColumn("Publication Year").setPreferredWidth(100);
		
		btnsPanel.add(purchaseBtn);
	}
	
	@Override
	public void addActionListeners(ActionListener action) {
		super.addActionListeners(action);
		purchaseBtn.addActionListener(action);
	}
}
