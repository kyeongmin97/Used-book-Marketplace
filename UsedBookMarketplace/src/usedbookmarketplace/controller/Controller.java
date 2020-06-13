package usedbookmarketplace.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.database.Database;
import usedbookmarketplace.view.*;
import usedbookmarketplace.view.Admin.SearchBookUI_Admin;
import usedbookmarketplace.view.GeneralUser.SearchBookUI_General;

public class Controller {
	private Database DB = new Database();
	private View view = new View();
	private SearchStrategy searchFilter = new searchByTitle();

	public Controller() {
		DB.addObserver(view);
		
		view.getLoginUI().addActionListener_login(new LoginUI_LoginBtn_Listener());
		view.getLoginUI().addActionListener_register(new LoginUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_register(new RegisterAccountUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_back(new RegisterAccountUI_BackBtn_Listener());
	}
	
	
	/**
	 * LOGIN UI, (1) if "login button" is clicked
	 */
	class LoginUI_LoginBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.authenticate(view.getLoginUI().getIDtxt(), view.getLoginUI().getPWtxt());
				
				view.setMode(DB);
				setActionListener();
				view.getLoginUI().setTxtEmpty();
				view.changeScene("MENU");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * LOGIN UI, (2) if "register button" is clicked
	 */
	class LoginUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.getLoginUI().setTxtEmpty();
				view.changeScene("REGISTER");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * REGISTER UI (1) if "register button" is clicked
	 */
	class RegisterAccountUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String[] userInfo = view.getRegisterAccountUI().getAllTxt();
				checkRegisterUser(userInfo);
				
				DB.registerUser(userInfo);
				view.successRegister();
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * REGISTER UI (2) if "back button" is clicked
	 */
	class RegisterAccountUI_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.getRegisterAccountUI().setTxtEmpty();
				view.changeScene("LOGIN");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * MENU UI (1) if "left button" is clicked - (in generalUser mode) buy books or (in administrator mode) delete books 
	 */
	class MenuUI_Btn1_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.initSearchIndex();
				view.changeScene("SEARCH", view.getSearchBookUI(), DB);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MENU UI (2) if "right button" is clicked - (in generalUser mode) manage books or (in administrator mode) manage accounts 
	 */
	class MenuUI_Btn2_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				if (DB.getCurrentUser() instanceof GeneralUser)
					view.changeScene("MANAGE_BOOK", view.getManageBookUI(), DB);
				else
					view.changeScene("MANAGE_ACCOUNT", view.getManageAccountUI(), DB);
			} catch (Exception e) {
				e.printStackTrace();
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * SEARCH UI, (1) if "search button" is clicked
	 */
	class SearchBookUI_SearchBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.searchBook(view.getSearchBookUI().getSearchTxt(), searchFilter);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * 2. SEARCH UI, (2) if "purchase button" is clicked (general user)
	 */
	class SearchBookUI_PurchaseBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getSearchBookUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				int originIndex = DB.getRealIndex(selectedIndex); 				// the actual index stored in the database(file)
				checkInvalidPurchase(originIndex);
				
				DB.purchaseBook(originIndex);
				signalToEmailServer(originIndex);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}

		private void checkInvalidPurchase(int originIndex) throws Exception {
			Book selectedBook = DB.getBookDB().get(originIndex);
			if (selectedBook.isSold())
				throw new Exception("Already Sold out!");
			if (!selectedBook.getSeller().isActivated())
				throw new Exception("The seller of the book you want to purchase is deactivated!");
			if (selectedBook.getSeller().getID().equals(DB.getCurrentUser().getID()))
				throw new Exception("You can't buy your own book!");
		}
	}
	/**
	 * 3. SEARCH UI, (3) if "delete button" is clicked (administrator)
	 */
	class SearchBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getSearchBookUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				
				DB.deleteBook_admin(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * MANAGE BOOK UI, (1) if "register button" is clicked
	 */
	class ManageBookUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.setInputMode();
				view.getInputBookUI().addActionListener_btn(new InputBookInfoUI_RegisterBtn_Listener());
				view.changeScene("INPUT");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MANAGE BOOK UI, (2) if "modify button" is clicked
	 */
	class ManageBookUI_ModifyBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				
				view.setInputMode(DB.getBookDB().get(DB.getRealIndex(selectedIndex)));
				view.getInputBookUI().addActionListener_btn(new InputBookInfoUI_ModifyBtn_Listener());
				view.changeScene("INPUT");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MANAGE BOOK VIEW : (3) if "delete button" is clicked
	 */
	class ManageBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				
				DB.deleteBook_user(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * INPUT BOOK VIEW : if "register button" is clicked
	 */
	class InputBookInfoUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String[] bookInfo = view.getInputBookUI().getAllTxt();
				//checkRegisterBook(bookInfo);
				
				DB.registerBook(bookInfo);
				view.showMessageScreen("complete registration!");
				view.changeScene("MANAGE_BOOK"); //view.getManageBookUI() 넣어야되는가? 책 추가하고 테이블 업데이트 잘되는가? @@ TODO
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * INPUT BOOK UI : if "modify button" is clicked
	 */
	class InputBookInfoUI_ModifyBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				String[] bookInfo = view.getInputBookUI().getAllTxt();
				//checkRegisterBook(bookInfo);	//길이 6인지, 각 값이 올바른지
				
				DB.modifyBook(bookInfo, selectedIndex);
				view.showMessageScreen("complete modification!");
				view.changeScene("MANAGE_BOOK"); //view.getManageBookUI() 넣어야되는가? 테이블 업데이트 잘되는가? @@ TODO
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	class ManageAccountUI_ChangeStateBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageAccountUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				
				DB.changeUserState(selectedIndex);
			} catch (Exception e) {
				e.printStackTrace();
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	class ManageAccountUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageAccountUI().getTable().getSelectedRow();
				checkIsSelected(selectedIndex);
				checkIsDeactivated(selectedIndex);
				
				DB.deleteAccount(selectedIndex);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
		
		private void checkIsDeactivated(int selectedIndex) throws Exception {
			if (((GeneralUser)DB.getAccountDB().get(selectedIndex)).isActivated())
				throw new Exception("Cannot delete the user (activated)");
		}
	}
	
	/**
	 * User : Logout
	 */
	class User_LogoutBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.setCurrentUser(null);
				view.changeScene("LOGIN", null, DB);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * User : Back
	 */
	class User_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.changeScene("MENU");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	// Set the search filter as the title.
	class SearchBookUI_TitleRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchByTitle();
		}
	}
	class SearchBookUI_AuthorRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchByAuthor();
		}
	}
	class SearchBookUI_PublisherRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchByPublisher();
		}
	}
	class SearchBookUI_PublicationYearRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchByPublicationYear();
		}
	}
	class SearchBookUI_IsbnRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchByISBN();
		}
	}
	class SearchBookUI_SellerIdRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new searchBySellerID();
		}
	}

	private void signalToEmailServer(int index) throws Exception {
		// what email sever does
		view.showMessageScreen("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser)DB.getCurrentUser()).getEmail() + "<br>"
				+ "seller : " + DB.getBookDB().get(index).getSeller().getEmail() + "</html>");
	}
	
	
	private void checkRegisterUser(String[] userInfo) throws Exception {
		if (userInfo[0].isEmpty() || userInfo[1].isEmpty() || userInfo[2].isEmpty() || !userInfo[3].matches("[0-9|-]+") || userInfo[4].isEmpty())
			throw new Exception("Invalid Input : All blanks must be written and enter only numbers and '-' for phone numbers.");

		for (int i = 0; i < DB.getAccountDB().size(); i++) {
			if (userInfo[0].equals(DB.getAccountDB().get(i).getID()))
				throw new Exception("Invalid Input : The same ID exists");
		}
	}
	
	// if you don't select a book or account, show you a message
	private void checkIsSelected(int searchedIndex) throws Exception { 
		if (searchedIndex == -1)	
			throw new Exception("Please select the row from the table!");
	}
	
	private void setActionListener(){
		// Add listeners only to common parts of both modes.
		view.getMenuUI().addActionListener_btn1(new MenuUI_Btn1_Listener());
		view.getMenuUI().addActionListener_btn2(new MenuUI_Btn2_Listener());
		view.getSearchBookUI().addActionListener_searchBtn(new SearchBookUI_SearchBtn_Listener());
		view.getSearchBookUI().addActionListener_backBtn(new User_BackBtn_Listener());
		view.getSearchBookUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		
		// search Filter
		view.getSearchBookUI().addActionListener_titleRBtn(new SearchBookUI_TitleRbtn_Listener());
		view.getSearchBookUI().addActionListener_authorRBtn(new SearchBookUI_AuthorRbtn_Listener());
		view.getSearchBookUI().addActionListener_publisherRBtn(new SearchBookUI_PublisherRbtn_Listener());
		view.getSearchBookUI().addActionListener_publicationYearRBtn(new SearchBookUI_PublicationYearRbtn_Listener());
		view.getSearchBookUI().addActionListener_isbnRBtn(new SearchBookUI_IsbnRbtn_Listener());
		view.getSearchBookUI().addActionListener_sellerIdRBtn(new SearchBookUI_SellerIdRbtn_Listener());
		
		// depend on the mode (administrator or general user)
		if (DB.getCurrentUser() instanceof GeneralUser) {
			((SearchBookUI_General)view.getSearchBookUI()).addActionListener_purchase(new SearchBookUI_PurchaseBtn_Listener());
			view.getManageBookUI().addActionListener_registerBtn(new ManageBookUI_RegisterBtn_Listener());
			view.getManageBookUI().addActionListener_modifyBtn(new ManageBookUI_ModifyBtn_Listener());
			view.getManageBookUI().addActionListener_deleteBtn(new ManageBookUI_DeleteBtn_Listener());
			view.getManageBookUI().addActionListener_backBtn(new User_BackBtn_Listener());
			view.getManageBookUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		}
		else {
			((SearchBookUI_Admin)view.getSearchBookUI()).addActionListener_delete(new SearchBookUI_DeleteBtn_Listener());
			view.getManageAccountUI().addActionListener_changeStateBtn(new ManageAccountUI_ChangeStateBtn_Listener());
			view.getManageAccountUI().addActionListener_deleteBtn(new ManageAccountUI_DeleteBtn_Listener());
			view.getManageAccountUI().addActionListener_backBtn(new User_BackBtn_Listener());
			view.getManageAccountUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		}
	}
}
