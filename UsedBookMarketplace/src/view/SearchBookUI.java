package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

import model.data.Book;

public abstract class SearchBookUI extends TableUI {

	protected JButton searchBtn = new JButton("Search");
	protected JButton backBtn = new JButton("Back");
	protected JButton logoutBtn = new JButton("Logout");

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
		table.getColumn("Publication Year").setPreferredWidth(110);
		table.getColumn("ISBN").setPreferredWidth(110);
		table.getTableHeader().setReorderingAllowed(false);
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

		if (bookList != null) {
			for (int i = 0; i < bookList.size(); i++) {
				String[] bookInfo = ((Book) bookList.get(i)).getBookInfo();
				model.addRow(new Object[] { bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4],
						bookInfo[5], bookInfo[6], bookInfo[7], bookInfo[8] });
			}
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

	public void setSearchTxt(String searchWord) {
		searchTxt.setText(searchWord);
	}
	public String getSearchTxt() {
		return searchTxt.getText();
	}
	

	public JButton getSearchBtn() {
		return searchBtn;
	}
	public JRadioButton getSellerIdRBtn() {
		return sellerIdRBtn;
	}
}
