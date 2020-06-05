package usedbookmarketplace.view;

import javax.swing.*;

public class Search extends JPanel {
	private JLabel search;
	
	public Search() {
		search = new JLabel("search view");

		setLayout(null);

		search.setBounds(230, 150, 100, 20);
		
		add(search);
	}
}
