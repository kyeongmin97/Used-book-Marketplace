package view.GeneralUser;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;

import model.data.Book;
import view.SearchBookUI;

public class SearchBookUI_General extends SearchBookUI {
	
	private JButton purchaseBtn = new JButton("Purchase");
	
	public SearchBookUI_General(Vector<Book> bookList) {
		super(bookList);
		
		btnsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		btnsPanel.add(purchaseBtn);
		btnsPanel.add(backBtn);
		btnsPanel.add(logoutBtn);
	}
	
	public void addActionListener_purchase(ActionListener action) {
		purchaseBtn.addActionListener(action);
	}
}
