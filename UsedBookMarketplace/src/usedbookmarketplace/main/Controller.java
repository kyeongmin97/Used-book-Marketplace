package usedbookmarketplace.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.database.Database;
import usedbookmarketplace.view.*;

public class Controller implements ActionListener {
	Database DB;
	MainFrame view;
	User currentUser;

	public Controller(Database db, MainFrame view) {
		this.DB = db;
		this.view = view;

		view.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			/************************  LOGIN Panel  ************************/
			if (event.getSource() == view.login.login_btn) {						// LOGIN VIEW : if "login button" is clicked
				String id = view.login.getIDtxt();
				String pw = view.login.getPWtxt();

				for (int i = 0; i < DB.getAccountDB().size(); i++) {
					if (id.equals(DB.getAccountDB().get(i).getID())
							&& pw.equals(DB.getAccountDB().get(i).getPW())) {
						// administrator
						if (i == DB.getAccountDB().size() - 1)
							view.setMenu(new Menu_Admin());
						// general user
						else
							view.setMenu(new Menu_GeneralUser());
						view.menu.addActionListeners(this);
						currentUser = DB.getAccountDB().get(i);
						view.getCardLayout().show(view.getContentPane(), "MENU");
						break;
					}
				}
				view.login.setTxtEmpty();
			}
			
			else if (event.getSource() == view.login.register_btn) {				// LOGIN VIEW : if "register button" is clicked
				view.login.setTxtEmpty();
				view.getCardLayout().show(view.getContentPane(), "REGISTER");
			}
			
			
			/************************  MENU Panel  ************************/
			else if (event.getSource() == view.menu.getBtn1()) {					// MENU VIEW : if "button" is clicked
				DB.initSearchIndex();
				if (currentUser instanceof GeneralUser) {							// generalUser mode (buy books button)
					view.search = new Search_GeneralUser(DB.getBookDB());
					view.search.addActionListeners(this);
					view.changeCardToSearch(view.search);	// DB안쓰기 위해서***
				}
				else {																// administrator mode (manage books button)
					view.search_admin = new Search_Admin(DB.getBookDB());
					view.search_admin.addActionListeners(this);
					view.changeCardToSearch(view.search_admin);
				}
			}
			else if (event.getSource() == view.menu.getBtn2()) {
				DB.initSearchIndex();
				if (currentUser instanceof GeneralUser) {
					view.setSale(new Sale(((GeneralUser)currentUser).getBookList()));
					view.getSale().addActionListeners(this);
					view.changeCardToSale(view.getSale());
				}
				else {
					view.setAccountPanel(new AccountPanel(DB.getAccountDB()));
					view.getAccountPanel().addActionListeners(this);
					view.changeCardToAccount(view.getAccountPanel()); // DB를 view에서 안쓰기 위해
				}
			}
			
			
			/************************  REGISTER Panel  ************************/
			else if (event.getSource() == view.register.register_btn) {				// REGISTER VIEW : if "register button" is clicked
				checkRegisterException();

				GeneralUser newUser = new GeneralUser(view.register.getAllTxt());
				DB.addUser(newUser); // add a new user

				view.register.setTxtEmpty();
				view.getCardLayout().show(view.getContentPane(), "LOGIN");
			}
			
			else if (event.getSource() == view.register.back_btn) {					// REGISTER VIEW : if "back button" is clicked
				view.register.setTxtEmpty();
				view.getCardLayout().show(view.getContentPane(), "LOGIN");
			}

			
			/************************  SEARCH Panel (administrator & general user)  ************************/
			else if (event.getSource() == view.search.searchBtn) {					// SEARCH VIEW : if "search button" is clicked
				searchOn(view.search);
			}
			else if (event.getSource() == view.search_admin.searchBtn) {
				searchOn(view.search_admin);
			}
			
			
			else if (event.getSource() == view.search.purchaseBtn) {				// SEARCH VIEW : if "purchase button" is clicked
				int searchedIndex = view.search.getTable().getSelectedRow(); 		// the index from the searched table
				if (searchedIndex != -1) {
					int originIndex = DB.getRealIndex(searchedIndex); 				// the actual index stored in the database(file)
					
					if (DB.getBookDB().get(originIndex).isSold()) {
						view.showMessageFrame("Already Sold out");
					}
					else {
						DB.getBookDB().get(originIndex).setSold(true); 					// change the state of a book to "Sold"
						DB.updateBookDB(); 												// update DB
						view.search.updateTable(DB.getBookDB()); 						// update view (table)
						view.showMessageFrame("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser)currentUser).getEmail() + "<br>"
								+ "seller : " + DB.getBookDB().get(originIndex).getSeller().getEmail() + "</html>");
					}
				}
				else
					view.showMessageFrame("Please select a book");
			}
			
			else if (event.getSource() == view.search_admin.deleteBtn) {			// SEARCH VIEW : if "delete button" is clicked
				int searchedIndex = view.search_admin.getTable().getSelectedRow(); 	// the index from the searched table
				if (searchedIndex == -1)
					view.showMessageFrame("Please select a book");					// if you don't select a book, show you a message
				DB.removeBook(view.search_admin.getTable().getSelectedRow());		// remove the selected book from the database
				view.search_admin.updateTable(DB.getBookDB());						// update view (table)
			}			
			
			else if (event.getSource() == view.search.logoutBtn						// SEARCH VIEW : if "logout button" is clicked
					|| event.getSource() == view.search_admin.logoutBtn) {
				currentUser = null;
				view.getCardLayout().show(view.getContentPane(), "LOGIN");			// change into login card
			}
			
			/************************  SALE Panel (general user)  ************************/
			else if (event.getSource() == view.getSale().getRegisterBtn()) {					// SALE VIEW : if "register button" is clicked
				int searchedIndex = view.getSale().getTable().getSelectedRow(); 		// the index from the searched table
				if (searchedIndex != -1) {
					int originIndex = DB.getRealIndex(searchedIndex); 
					view.getSale().showModifyDialog(DB.getBookDB().get(originIndex));
				}
				else
					view.showMessageFrame("Please select a book");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchOn(Search searchView) {
		if (!searchView.getSearchTxt().isEmpty()) {
			// search for a title
			if (searchView.isTitleSelected())
				searchView.updateTable(DB.searchByTitle(searchView.getSearchTxt()));
			// search for an author
			else if (searchView.isAuthorSelected())
				searchView.updateTable(DB.searchByAuthor(searchView.getSearchTxt()));
			// search for an ISBN
			else if (searchView.isISBNSelected())
				searchView.updateTable(DB.searchByISBN(searchView.getSearchTxt()));
			// search for the title
			else if (searchView.isSellerIDSelected())
				searchView.updateTable(DB.searchBySellerID(searchView.getSearchTxt()));
		}
	}
	
	private void checkRegisterException() throws Exception {
		if (view.register.getIDtxt().isEmpty() || view.register.getPWtxt().isEmpty()
				|| view.register.getNametxt().isEmpty() || view.register.getEmailtxt().isEmpty()
				|| !view.register.getPhoneNumtxt().matches("[0-9|-]+"))
			throw new Exception(
					"Invalid Input : All blanks must be written and enter only numbers and '-' for phone numbers.");

		for (int i = 0; i < DB.getAccountDB().size(); i++) {
			if (view.register.getIDtxt().equals(DB.getAccountDB().get(i).getID()))
				throw new Exception("Invalid Input : The same ID exists");
		}
	}
}
