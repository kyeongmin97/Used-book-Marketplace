package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.data.GeneralUser;
import model.database.Database;
import view.View;
import view.Admin.SearchBookUI_Admin;
import view.GeneralUser.RegisterBookUI;
import view.GeneralUser.SearchBookUI_General;

public class Controller {
	private Database DB = new Database();
	private View view = new View();
	private SearchStrategy searchFilter = new SearchByTitle();

	/**
	 * constructor
	 */
	public Controller() {
		DB.addObserver(view);

		view.getLoginUI().addActionListener_login(new LoginUI_LoginBtn_Listener());
		view.getLoginUI().addActionListener_register(new LoginUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_register(new RegisterAccountUI_RegisterBtn_Listener());
		view.getRegisterAccountUI().addActionListener_back(new RegisterAccountUI_BackBtn_Listener());
	}

	/**
	 * LOGIN UI - login
	 */
	private class LoginUI_LoginBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.authenticate(view.getLoginUI().getIDtxt(), view.getLoginUI().getPWtxt());	// authenticate the user
				
				view.setMode(DB);					// set the view mode according to the administrator or general user 	
				setActionListener();				// add the actionListener on the buttons according to the mode
				view.getLoginUI().setTxtEmpty();
				view.changeScreen("MENU");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * LOGIN UI - goto register screen
	 */
	private class LoginUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.getLoginUI().setTxtEmpty();
				view.changeScreen("REGISTER");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * REGISTER UI - register account
	 */
	private class RegisterAccountUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String[] userInfo = view.getRegisterAccountUI().getAllTxt();
				DB.registerUser(userInfo);		// register the general user with DB
				
				view.showMessageScreen("Your account has been registered!");
				view.getRegisterAccountUI().resetTxt();
				view.changeScreen("LOGIN");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * REGISTER UI - back to login screen
	 */
	private class RegisterAccountUI_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.getRegisterAccountUI().resetTxt();
				view.changeScreen("LOGIN");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/**
	 * MENU UI - goto purchase book screen in generalUser mode or delete book screen in administrator mode 
	 */
	private class MenuUI_Btn1_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.resetToSearchedIndex();
				view.getSearchBookUI().resetSearchTxt();
				view.changeScene("SEARCH", view.getSearchBookUI(), DB);
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/**
	 * MENU UI - goto manage book screen in generalUser mode or manage account screen in administrator mode 
	 */
	private class MenuUI_Btn2_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				if (DB.getCurrentUser() instanceof GeneralUser) {
					DB.resetToUserBookIndex();
					view.changeScene("MANAGE_BOOK", view.getManageBookUI(), DB);
				}
				else
					view.changeScene("MANAGE_ACCOUNT", view.getManageAccountUI(), DB);
			} catch (Exception e) {
				e.printStackTrace();
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/**
	 * SEARCH UI - search books
	 */
	private class SearchBookUI_SearchBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				DB.searchBook(view.getSearchBookUI().getSearchTxt(), searchFilter);
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SEARCH UI - purchase book
	 */
	private class SearchBookUI_PurchaseBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getSearchBookUI().getTable().getSelectedRow();
				int originIndex = DB.changeToOriginIndex(selectedIndex);
				
				DB.purchaseBook(selectedIndex);
				signalToEmailServer(originIndex);
				view.showMessageScreen("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser) DB.getCurrentUser()).getEmail()
						+ "<br>" + "seller : " + DB.getBookSeller(originIndex).getEmail() + "</html>");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SEARCH UI - delete book (administrator)
	 */
	private class SearchBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getSearchBookUI().getTable().getSelectedRow();

				DB.deleteBook_admin(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * MANAGE BOOK UI - goto register screen
	 */
	private class ManageBookUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.setInputMode();
				setInputActionListener();
				view.changeScreen("INPUT");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/**
	 * MANAGE BOOK UI - goto modify screen
	 */
	private class ManageBookUI_ModifyBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();
				DB.checkCanModifyBook(selectedIndex);
				
				view.setInputMode(DB.getBookDB().get(DB.changeToOriginIndex(selectedIndex)));
				setInputActionListener();
				view.changeScreen("INPUT");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * MANAGE BOOK UI - delete book (general user)
	 */
	private class ManageBookUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();

				DB.deleteBook_user(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * INPUT BOOK UI - register book
	 */
	private class InputBookInfoUI_RegisterBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String[] bookInfo = view.getInputBookUI().getAllTxt();

				DB.registerBook(bookInfo);
				view.showMessageScreen("complete registration!");
				view.changeScreen("MANAGE_BOOK");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * INPUT BOOK UI - modify book
	 */
	private class InputBookInfoUI_ModifyBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageBookUI().getTable().getSelectedRow();
				String[] bookInfo = view.getInputBookUI().getAllTxt();

				DB.modifyBook(bookInfo, selectedIndex);
				view.showMessageScreen("complete modification!");
				view.changeScreen("MANAGE_BOOK");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * INPUT BOOK UI - go back to manage book screen 
	 */
	private class InputBookInfoUI_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.changeScreen("MANAGE_BOOK");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/*
	 * MANAGE ACCOUNT UI - change general user's state (administrator)
	 */
	private class ManageAccountUI_ChangeStateBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageAccountUI().getTable().getSelectedRow();
				DB.changeUserState(selectedIndex);
				view.showMessageScreen("User's state has been changed.");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * MANAGE ACCOUNT UI - delete account (administrator)
	 */
	private class ManageAccountUI_DeleteBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int selectedIndex = view.getManageAccountUI().getTable().getSelectedRow();

				DB.deleteAccount(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidException e) {
				view.showMessageScreen(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * User : Logout
	 */
	private class User_LogoutBtn_Listener implements ActionListener {
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
	private class User_BackBtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				view.changeScreen("MENU");
			} catch (Exception e) {
				view.showMessageScreen(e.getMessage());
			}
		}
	}

	/*
	 * Set the search filter
	 */
	private class SearchBookUI_TitleRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchByTitle();
		}
	}

	private class SearchBookUI_AuthorRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchByAuthor();
		}
	}

	private class SearchBookUI_PublisherRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchByPublisher();
		}
	}

	private class SearchBookUI_PublicationYearRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchByPublicationYear();
		}
	}

	private class SearchBookUI_IsbnRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchByISBN();
		}
	}

	private class SearchBookUI_SellerIdRbtn_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			searchFilter = new SearchBySellerID();
		}
	}

	private void signalToEmailServer(int selectedIndex) throws Exception {
		/**
		 * signal to email server
		 */
	}

	private void setActionListener() {
		// Add listeners only to common parts of both modes.
		view.getMenuUI().addActionListener_btn1(new MenuUI_Btn1_Listener());
		view.getMenuUI().addActionListener_btn2(new MenuUI_Btn2_Listener());
		view.getMenuUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
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
			((SearchBookUI_General) view.getSearchBookUI()).addActionListener_purchase(new SearchBookUI_PurchaseBtn_Listener());
			view.getManageBookUI().addActionListener_registerBtn(new ManageBookUI_RegisterBtn_Listener());
			view.getManageBookUI().addActionListener_modifyBtn(new ManageBookUI_ModifyBtn_Listener());
			view.getManageBookUI().addActionListener_deleteBtn(new ManageBookUI_DeleteBtn_Listener());
			view.getManageBookUI().addActionListener_backBtn(new User_BackBtn_Listener());
			view.getManageBookUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		} else {
			((SearchBookUI_Admin) view.getSearchBookUI()).addActionListener_delete(new SearchBookUI_DeleteBtn_Listener());
			view.getManageAccountUI().addActionListener_changeStateBtn(new ManageAccountUI_ChangeStateBtn_Listener());
			view.getManageAccountUI().addActionListener_deleteBtn(new ManageAccountUI_DeleteBtn_Listener());
			view.getManageAccountUI().addActionListener_backBtn(new User_BackBtn_Listener());
			view.getManageAccountUI().addActionListener_logoutBtn(new User_LogoutBtn_Listener());
		}
	}
	private void setInputActionListener() {
		if (view.getInputBookUI() instanceof RegisterBookUI)
			view.getInputBookUI().addActionListener_btn(new InputBookInfoUI_RegisterBtn_Listener());
		else
			view.getInputBookUI().addActionListener_btn(new InputBookInfoUI_ModifyBtn_Listener());
		view.getInputBookUI().addActionListener_backBtn(new InputBookInfoUI_BackBtn_Listener());
	}
}
