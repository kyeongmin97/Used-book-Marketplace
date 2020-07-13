package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Model;
import model.data.GeneralUser;
import view.View;
import view.Admin.SearchBookUI_Admin;
import view.GeneralUser.RegisterBookUI;
import view.GeneralUser.SearchBookUI_General;

public class Controller {
	private Model model = new Model();
	private View view = new View();
	private SearchStrategy searchFilter = new SearchByTitle();

	/**
	 * constructor
	 */
	public Controller() {
		model.addObserver(view);

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
				model.authenticate(view.getLoginUI().getIDtxt(), view.getLoginUI().getPWtxt());	// authenticate the user
				
				view.setMode(model);				// set the view mode according to the administrator or general user 	
				setActionListener();				// add the actionListener on the buttons according to the mode
				view.getLoginUI().resetTxtEmpty();
				view.changeScreen("MENU");
			} catch (InvalidValueException e) {
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
				view.getLoginUI().resetTxtEmpty();
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
				model.registerUser(userInfo);		// register the general user with DB
				
				view.showMessageScreen("Your account has been registered!");
				view.getRegisterAccountUI().resetTxt();
				view.changeScreen("LOGIN");
			} catch (InvalidValueException e) {
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
				model.resetToInitalIndex();
				view.getSearchBookUI().setSearchTxt(null);
				view.changeScene("SEARCH", view.getSearchBookUI(), model);
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
				if (model.getCurrentUser() instanceof GeneralUser) {
					model.resetToUserBookIndex();
					view.changeScene("MANAGE_BOOK", view.getManageBookUI(), model);
				}
				else
					view.changeScene("MANAGE_ACCOUNT", view.getManageAccountUI(), model);
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
				model.searchBook(view.getSearchBookUI().getSearchTxt(), searchFilter);
			} catch (InvalidValueException e) {
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
				model.purchaseBook(selectedIndex);
				
				int originIndex = model.changeToOriginIndex(selectedIndex);
				signalToEmailServer(originIndex);
				
				view.showMessageScreen("<html> Send E-mail <br>" + "buyer : " + ((GeneralUser) model.getCurrentUser()).getEmail()
						+ "<br>" + "seller : " + model.getBookSeller(originIndex).getEmail() + "</html>");
			} catch (InvalidValueException e) {
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

				model.deleteBook_admin(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidValueException e) {
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
				model.resetToUserBookIndex();
				model.getExceptionCheck().checkValidBookForModify(selectedIndex);
				
				view.setInputMode(model.getBookDB().get(model.changeToOriginIndex(selectedIndex)));
				setInputActionListener();
				view.changeScreen("INPUT");
			} catch (InvalidValueException e) {
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

				model.deleteBook_user(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidValueException e) {
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

				model.registerBook(bookInfo);
				view.showMessageScreen("complete registration!");
				view.changeScreen("MANAGE_BOOK");
			} catch (InvalidValueException e) {
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
				
				model.modifyBook(bookInfo, selectedIndex);
				view.showMessageScreen("complete modification!");
				view.changeScreen("MANAGE_BOOK");
			} catch (InvalidValueException e) {
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
				model.changeUserState(selectedIndex);
				view.showMessageScreen("User's state has been changed.");
			} catch (InvalidValueException e) {
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

				model.deleteUser(selectedIndex);
				view.showMessageScreen("complete deletion!");
			} catch (InvalidValueException e) {
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
				model.setCurrentUser(null);
				view.changeScene("LOGIN", null, model);
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
		if (model.getCurrentUser() instanceof GeneralUser) {
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
