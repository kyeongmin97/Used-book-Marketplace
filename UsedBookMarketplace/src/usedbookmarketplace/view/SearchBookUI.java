package usedbookmarketplace.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

import usedbookmarketplace.model.data.Book;

public abstract class SearchBookUI extends TableUI {

	public JButton searchBtn = new JButton("Search");
	public JButton backBtn = new JButton("Back");
	public JButton logoutBtn = new JButton("Logout");

	private JTextField searchTxt = new JTextField(30);
	private JRadioButton titleRBtn = new JRadioButton("Title", true);
	private JRadioButton authorRBtn = new JRadioButton("Author");
	private JRadioButton publisherRBtn = new JRadioButton("Publisher");
	private JRadioButton publicationYearRBtn = new JRadioButton("Publication Year");
	private JRadioButton isbnRBtn = new JRadioButton("ISBN");
	private JRadioButton sellerIdRBtn = new JRadioButton("Seller ID");

	// constructor
	public SearchBookUI() {

	}

	public SearchBookUI(Vector<Book> bookList) {
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
		
		// setting radio buttons
		JPanel radioBtnsPanel = new JPanel();
		ButtonGroup searchFilter = new ButtonGroup();
		searchFilter.add(titleRBtn);
		searchFilter.add(authorRBtn);
		searchFilter.add(publisherRBtn);
		searchFilter.add(publicationYearRBtn);
		searchFilter.add(isbnRBtn);
		searchFilter.add(sellerIdRBtn);
		radioBtnsPanel.add(titleRBtn);
		radioBtnsPanel.add(authorRBtn);
		radioBtnsPanel.add(publisherRBtn);
		radioBtnsPanel.add(publicationYearRBtn);
		radioBtnsPanel.add(isbnRBtn);
		radioBtnsPanel.add(sellerIdRBtn);
		
		// setting button
		add(searchTxt);
		add(searchBtn);
		add(radioBtnsPanel);
		add(btnsPanel);
		add(scroll);
	}

	@Override
	public <T> void updateTable(Vector<T> bookList) {
		model = (DefaultTableModel) table.getModel();	//initialize table
		model.setNumRows(0);

		for (int i = 0; i < bookList.size(); i++) {
			String[] bookInfo = ((Book)bookList.get(i)).getBookInfo();
			model.addRow(new Object[] { bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4],
										bookInfo[5], bookInfo[6], bookInfo[7], bookInfo[8] });
		}

		table.setModel(model);
	}
	
	public void addActionListener_searchBtn(ActionListener action) {
		searchBtn.addActionListener(action);
	}
	public void addActionListener_backBtn(ActionListener action) {
		backBtn.addActionListener(action);
	}
	public void addActionListener_logoutBtn(ActionListener action) {
		logoutBtn.addActionListener(action);
	}
	public void addActionListener_titleRBtn(ActionListener action) {
		titleRBtn.addActionListener(action);
	}
	public void addActionListener_authorRBtn(ActionListener action) {
		authorRBtn.addActionListener(action);
	}
	public void addActionListener_publisherRBtn(ActionListener action) {
		publisherRBtn.addActionListener(action);
	}
	public void addActionListener_publicationYearRBtn(ActionListener action) {
		publicationYearRBtn.addActionListener(action);
	}
	public void addActionListener_isbnRBtn(ActionListener action) {
		isbnRBtn.addActionListener(action);
	}
	public void addActionListener_sellerIdRBtn(ActionListener action) {
		sellerIdRBtn.addActionListener(action);
	}

	// getter, setter
	public String getSearchTxt() 		{	return searchTxt.getText();		}
	public boolean isTitleSelected() 	{	return titleRBtn.isSelected();	}
	public boolean isAuthorSelected() 	{	return authorRBtn.isSelected();	}
	public boolean isISBNSelected() 	{	return isbnRBtn.isSelected();	}
	public boolean isSellerIDSelected() {	return sellerIdRBtn.isSelected();	}
}
