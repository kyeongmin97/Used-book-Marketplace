package usedbookmarketplace.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.database.Database;
import usedbookmarketplace.view.MainFrame;
import usedbookmarketplace.view.MenuGeneralUser;
import usedbookmarketplace.view.Search;

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
			// Class Login
			// login view : if "login button" is clicked
			if (event.getSource() == view.login.login_btn) {
				String id = view.login.getIDtxt();
				String pw = view.login.getPWtxt();

				if (id.equals("1") && pw.equals("1")) {
					currentUser = DB.getAccountDB().get(0); // temp
					view.search = new Search(DB.getBookDB());
					view.search.addActionListeners(this);
					view.changeCardToSearch(view.search); // DB를 view에서 안쓰기 위해
				}
				else {
					view.setMenu(new MenuGeneralUser());
					view.getCardLayout().show(view.getContentPane(), "MENU");
				}
			}
			// login view : if "register button" is clicked
			else if (event.getSource() == view.login.register_btn) {
				view.getCardLayout().show(view.getContentPane(), "REGISTER");
			}

			// Class Register
			// register view : if "register button" is clicked
			else if (event.getSource() == view.register.register_btn) {
				checkRegisterException();

				GeneralUser newUser = new GeneralUser(view.register.getAllTxt());
				DB.addUser(newUser); // add a new user

				view.register.setAllTxtEmpty();
				view.getCardLayout().show(view.getContentPane(), "LOGIN");
			}
			// register view : if "back button" is clicked
			else if (event.getSource() == view.register.back_btn) {
				view.register.setAllTxtEmpty();
				view.getCardLayout().show(view.getContentPane(), "LOGIN");
			}

			// Class Search
			// search view : if "search button" is clicked
			else if (event.getSource() == view.search.searchBtn) {
				if (!view.search.getSearchTxt().isEmpty()) {
					// search for a title
					if (view.search.isTitleSelected())
						view.search.updateTable(DB.searchByTitle(view.search.getSearchTxt()));
					// search for an author
					else if (view.search.isAuthorSelected())
						view.search.updateTable(DB.searchByAuthor(view.search.getSearchTxt()));
					// search for an ISBN
					else if (view.search.isISBNSelected())
						view.search.updateTable(DB.searchByISBN(view.search.getSearchTxt()));
					// search for the title
					else if (view.search.isSellerIDSelected())
						view.search.updateTable(DB.searchBySellerID(view.search.getSearchTxt()));
				}
			}
			// search view : if "purchase button" is clicked
			else if (event.getSource() == view.search.purchaseBtn) {
				int searchedIndex = view.search.getTable().getSelectedRow(); 	// the index from the searched table
				int originIndex = DB.getRealIndex(searchedIndex); 				// the actual index stored in the database(file)
				
				if (DB.getBookDB().get(originIndex).isSold()) {
					view.setMessageFrame("Already Sold out");
				}
				else {
					DB.getBookDB().get(originIndex).setSold(true); 					// change the state of a book to "Sold"
					DB.updateBookDB(); 												// update DB
					view.search.updateTable(DB.getBookDB()); 						// update view (table)
					view.setMessageFrame("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser)currentUser).getEmail() + "<br>"
							+ "seller : " + DB.getBookDB().get(originIndex).getSeller().getEmail() + "</html>");
				}
			}
			// search view : if "logout button" is clicked
			else if (event.getSource() == view.search.logoutBtn) {
				view.getCardLayout().show(view.getContentPane(), "LOGIN");
			}
			else if () {
				//해야할 것 : admin추가했음. 로그인 부분 수정
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkRegisterException() throws Exception {
		if (view.register.getIDtxt().isEmpty() || view.register.getPWtxt().isEmpty()
				|| view.register.getNametxt().isEmpty() || view.register.getEmailtxt().isEmpty()
				|| !view.register.getPhoneNumtxt().matches("[0-9|-]+"))
			throw new Exception("Invalid Input : All blanks must be written and enter only numbers and '-' for phone numbers.");

		for (int i = 0; i < DB.getAccountDB().size(); i++) {
			if (view.register.getIDtxt().equals(DB.getAccountDB().get(i).getID()))
				throw new Exception("Invalid Input : The same ID exists");
		}
	}
}
