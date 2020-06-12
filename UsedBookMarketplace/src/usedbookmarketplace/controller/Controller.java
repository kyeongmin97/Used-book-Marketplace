package usedbookmarketplace.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import usedbookmarketplace.model.data.user.Admin;
import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.database.Database;
import usedbookmarketplace.view.*;
import usedbookmarketplace.view.Admin.SearchBookUI_Admin;
import usedbookmarketplace.view.GeneralUser.ModifyBookUI;
import usedbookmarketplace.view.GeneralUser.SearchBookUI_General;

public class Controller {
	Database DB = new Database();
	View view = new View(DB);
	User currentUser;

	public Controller() {
		DB.addObserver(view);
		view.setController(this);
		
		view.getLoginUI().addActionListener_login(new LoginUI_LoginBtn_Listener());
		view.getLoginUI().addActionListener_register(new LoginUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_register(new RegisterAccountUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_back(new RegisterAccountUI_BackBtn_Listener());
	}
	
	private void setActionListener(){
		view.getMenuUI().addActionListener_btn1(new MenuUI_Btn1_Listener());
		view.getMenuUI().addActionListener_btn2(new MenuUI_Btn2_Listener());
		view.getSearchBookUI().addActionListener_searchBtn(new SearchBookUI_SearchBtn_Listener());
		view.getSearchBookUI().addActionListener_backBtn(new User_BackBtn_Listener());
		view.getSearchBookUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		
		if (currentUser instanceof GeneralUser) {
			((SearchBookUI_General)view.getSearchBookUI()).addActionListener_purchase(new SearchBookUI_PurchaseBtn_Listener());
			view.getManageBookUI().addActionListener_registerBtn(new ManageBookUI_RegisterBtn_Listener());
			view.getManageBookUI().addActionListener_modifyBtn(new ManageBookUI_ModifyBtn_Listener());
			view.getManageBookUI().addActionListener_deleteBtn(new ManageBookUI_DeleteBtn_Listener());
			view.getManageBookUI().addActionListener_backBtn(new User_BackBtn_Listener());
			view.getManageBookUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		}
		else {
			((SearchBookUI_Admin)view.getSearchBookUI()).addActionListener_delete(new SearchBookUI_DeleteBtn_Listener());
		}
		
		
	}
	/**
	 * LOGIN VIEW : 1. if "login button" is clicked
	 */
	class LoginUI_LoginBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				currentUser = DB.authenticate(view.getLoginUI().getIDtxt(), view.getLoginUI().getPWtxt());
				
				view.setMode(currentUser);
				setActionListener();
				view.getLoginUI().setTxtEmpty();
				view.changeScene("MENU");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * LOGIN VIEW : 2. if "register button" is clicked
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
	 * REGISTER VIEW : 1. if "register button" is clicked
	 */
	class RegisterAccountUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.registerUser(view.getRegisterAccountUI().getAllTxt());
				
				view.showMessageScreen("Your account has been registered!");
				view.getRegisterAccountUI().setTxtEmpty();
				view.changeScene("LOGIN");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * REGISTER VIEW : 2. if "back button" is clicked
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
	 * MENU VIEW : 1. if "left button" is clicked
	 */
	class MenuUI_Btn1_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.initSearchIndex();
				view.changeScene("SEARCH", view.getSearchBookUI());			// buy books (generalUser mode) or manage books (administrator mode) 
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MENU VIEW : 2. MENU VIEW : if "right button" is clicked
	 */
	class MenuUI_Btn2_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// search index 초기화 해야함
				if (currentUser instanceof GeneralUser)
					view.changeScene("SALE", view.getManageBookUI());
				else
					view.changeScene("ACCOUNT", view.getManageAccountUI());
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * logout user
	 */
	class User_LogoutBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				currentUser = null;
				view.changeScene("LOGIN", null);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * backBtn User
	 */
	class User_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.changeScene("MENU", null);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * 1. SEARCH VIEW : if "search button" is clicked
	 */
	class SearchBookUI_SearchBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// search index 초기화 해야함
				setSearchFilter(view.getSearchBookUI());
				DB.searchBook(view.getSearchBookUI().getSearchTxt());
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * 2. SEARCH VIEW : if "purchase button" is clicked (general user)
	 */
	class SearchBookUI_PurchaseBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int searchedIndex = view.getSearchBookUI().getTable().getSelectedRow();
				
				DB.purchaseBook(searchedIndex);
				signalToEmailServer(searchedIndex);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * 3. SEARCH VIEW : if "delete button" is clicked (administrator)
	 */
	class SearchBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.deleteBook(view.getSearchBookUI().getTable().getSelectedRow());
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	/**
	 * MANAGE BOOK VIEW : if "register button" is clicked
	 */
	class ManageBookUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// @ TODO @ TODO
				// @ TODO @ TODO
				// @ TODO @ TODO
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MANAGE BOOK VIEW : if "modify button" is clicked
	 */
	class ManageBookUI_ModifyBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int originIndex = DB.getRealIndex(view.getManageBookUI().getTable().getSelectedRow());
				
				view.showModifyBookScreen(originIndex);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	/**
	 * MANAGE BOOK VIEW : if "delete button" is clicked
	 */
	class ManageBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// @ TODO @ TODO
				// @ TODO @ TODO
				// @ TODO @ TODO
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}
	
	// @ TODO inputbookInfo 
//	else if (event.getSource() == view.getSaleUI().getManageDialog().getBtn()) {
//		if (view.getSaleUI().getManageDialog() instanceof ModifyBookUI) {
//			DB.modifyBook(view.getSaleUI().getManageDialog().getAllTxt(), view.getSaleUI().getModifyIndex());
//		}
//	}
	

	private void signalToEmailServer(int searchedIndex) throws Exception {
		// what email sever does
		view.showMessageScreen("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser)currentUser).getEmail() + "<br>"
				+ "seller : " + DB.getBookDB().get(DB.getRealIndex(searchedIndex)).getSeller().getEmail() + "</html>");
	}
	
	private void setSearchFilter(SearchBookUI searchView) throws Exception {
		if (!searchView.getSearchTxt().isEmpty()) {
			if (searchView.isTitleSelected())				// Set the search filter as the title.
				DB.setSearchFilter(new searchByTitle());
			else if (searchView.isAuthorSelected())			// Set the search filter as the author
				DB.setSearchFilter(new searchByAuthor());
			else if (searchView.isISBNSelected())			// Set the search filter as the ISBN
				DB.setSearchFilter(new searchByISBN());
			else if (searchView.isSellerIDSelected())		// Set the search filter as the title
				DB.setSearchFilter(new searchBySellerID());
		}
		else
			throw new Exception("Please enter a search word in the text box.");
	}
}
