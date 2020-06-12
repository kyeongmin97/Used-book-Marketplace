package usedbookmarketplace.view;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import usedbookmarketplace.controller.Controller;
import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.data.user.Admin;
import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.database.Database;
import usedbookmarketplace.view.Admin.ManageAccountUI;
import usedbookmarketplace.view.Admin.MenuUI_Admin;
import usedbookmarketplace.view.Admin.SearchBookUI_Admin;
import usedbookmarketplace.view.GeneralUser.MenuUI_General;
import usedbookmarketplace.view.GeneralUser.RegisterAccountUI;
import usedbookmarketplace.view.GeneralUser.InputBookInfoUI;
import usedbookmarketplace.view.GeneralUser.ManageBookUI;
import usedbookmarketplace.view.GeneralUser.ModifyBookUI;
import usedbookmarketplace.view.GeneralUser.RegisterBookUI;
import usedbookmarketplace.view.GeneralUser.SearchBookUI_General;

public class View extends JFrame implements Observer{

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private Database DB;
	private CardLayout card = new CardLayout();
	
	private LoginUI loginUI = new LoginUI();
	private RegisterAccountUI registerAccUI = new RegisterAccountUI();
	private MenuUI menuUI;
	private SearchBookUI searchBookUI;
	private ManageBookUI manageBookUI;
	private InputBookInfoUI inputBookUI;
	private ManageAccountUI manageAccountUI;
	
	private TableUI currentTableUI;

	/**
	 * Initialize
	 */		
	public View(Database _DB){
		this.DB = _DB;
		
		setLayout(card);
		this.add("LOGIN", loginUI);
		this.add("REGISTER", registerAccUI);
		
		setTitle("Used-book Marketplace");
		setSize(800, 600);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Observer function
	 */
	@Override
	public <T> void update(Vector<T> data) {
		currentTableUI.updateTable(data);
	}
	
	/**
	 * Create a menu based on the user index
	 */
	public void setMode(User currentUser) {
		if (currentUser instanceof GeneralUser)	{			// general user
			setMenuUI(new MenuUI_General());
			setSearchBookUI(new SearchBookUI_General(DB.getBookDB()));
			setManageBookUI(new ManageBookUI(((GeneralUser)currentUser).getBookList()));
		}
		else {												// administrator
			setMenuUI(new MenuUI_Admin());
			setSearchBookUI(new SearchBookUI_Admin(DB.getBookDB()));
			setManageAccountUI(new ManageAccountUI(DB.getAccountDB()));
		}
	}
	
	/**
	 * Show an error message on the screen
	 */
	public void showMessageScreen(String msg) {
		new showMessageScreen(msg);
	}
	
	/**
	 * Show a new screen to the general user
	 */
	public void showRegisterBookScreen() {
		manageBookUI.setManageDialog(new RegisterBookUI());
//		manageBookUI.getManageDialog().addActionListeners(controller);
	}
	public void showModifyBookScreen(int index) {
		manageBookUI.setManageDialog(new ModifyBookUI(DB.getBookDB().get(index)));
		manageBookUI.setModifyIndex(index);
//		manageBookUI.getManageDialog().addActionListeners(controller);
	}
	
	/**
	 * Change the scene(card) : 테이블이 있는 화면은 같이 넘겨서 currentTable저장
	 */
	public void changeScene(String sceneName) {
		this.getCardLayout().show(this.getContentPane(), sceneName);
	}
	public void changeScene(String sceneName, TableUI tableUI) {
		this.setCurrentTableUI(tableUI);
		this.getCardLayout().show(this.getContentPane(), sceneName);
	}
	
	/**
	 *  Setter
	 */
	private void setMenuUI(MenuUI menuUI) {
		this.menuUI = menuUI;
		add("MENU", menuUI);
	}
	private void setSearchBookUI(SearchBookUI searchBookUI) {
		this.searchBookUI = searchBookUI;
		add("SEARCH", searchBookUI);				
	}
	private void setManageBookUI(ManageBookUI manageBookUI) {
		this.manageBookUI = manageBookUI;
		add("SALE", manageBookUI);
	}
	private void setManageAccountUI(ManageAccountUI manageAccountUI) {
		this.manageAccountUI = manageAccountUI;
//		this.manageAccountUI.addActionListeners(controller);
		add("ACCOUNT", manageAccountUI);
	}
	private void setInputBookInfoUI(InputBookInfoUI inputBookUI) {
		this.inputBookUI = inputBookUI;
		add("INPUT", inputBookUI);
	}
	private void setCurrentTableUI(TableUI _currentTableUI) {
		currentTableUI = _currentTableUI;
	}
	public void setController(Controller _controller) {
		controller = _controller;
	}
	
	/**
	 *  Getter
	 */
	public CardLayout getCardLayout()	{	return this.card;	}
	public LoginUI getLoginUI()			{	return loginUI;		}
	public MenuUI getMenuUI() 			{	return menuUI;		}
	public RegisterAccountUI getRegisterAccountUI() 	{	return registerAccUI;	}
	public SearchBookUI getSearchBookUI() 		{	return searchBookUI;	}
	public ManageAccountUI getManageAccountUI()		{	return manageAccountUI;	}
	public ManageBookUI getManageBookUI() 			{	return manageBookUI;		}
}

/**
 *  Class about showing an error message on the screen
 */
class showMessageScreen extends JDialog {
	private static final long serialVersionUID = 1L;
	JLabel label = new JLabel("");
    public showMessageScreen(String str){
            getContentPane().add(label);
           
            label.setText(str);
            label.setHorizontalAlignment(JLabel.CENTER);
            
            setSize(360,120);
            setLocation(300, 200);
            setModal(true);
            setVisible(true);
    }
}