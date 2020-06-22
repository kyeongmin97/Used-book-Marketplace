package view;

import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Model;
import model.data.Book;
import model.data.GeneralUser;
import view.Admin.*;
import view.GeneralUser.*;

public class View extends JFrame implements Observer{

	private static final long serialVersionUID = 1L;
	private CardLayout card = new CardLayout();	
	private TableUI currentTableUI;
	
	private LoginUI loginUI = new LoginUI();
	private RegisterAccountUI registerAccUI = new RegisterAccountUI();
	private MenuUI menuUI;
	private SearchBookUI searchBookUI;
	private ManageBookUI manageBookUI;
	private InputBookInfoUI inputBookUI;
	private ManageAccountUI manageAccountUI;

	
	/**
	 * Initialize
	 */		
	public View(){
		setLayout(card);
		setTitle("Used-book Marketplace");
		setSize(800, 600);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.add("LOGIN", loginUI);
		this.add("REGISTER", registerAccUI);
	}
	
	
	/**
	 * Observer function
	 */
	@Override
	public <T> void update(Vector<T> data) {
		currentTableUI.updateTable(data);
	}
	
	
	/**
	 * Show an message on the screen
	 */
	public void showMessageScreen(String msg) {
		new MessageScreen(msg);
	}
	
	
	/**
	 * Change the scene(card)
	 */
	public void changeScreen(String sceneName) {
		this.getCardLayout().show(this.getContentPane(), sceneName);
	}
	
	public void changeScene(String sceneName, TableUI tableUI, Model DB) {
		this.setCurrentTableUI(tableUI);
		if (tableUI == null)
			currentTableUI = null;
		else if (tableUI instanceof ManageBookUI)
			tableUI.updateTable(((GeneralUser)DB.getCurrentUser()).getBookList());
		else if (tableUI instanceof ManageAccountUI)
			tableUI.updateTable(DB.getAccountDB());
		else
			tableUI.updateTable(DB.getBookDB());
		this.getCardLayout().show(this.getContentPane(), sceneName);
	}
	
	
	/**
	 * Create a menu based on the user index
	 */
	public void setMode(Model DB) {
		if (DB.getCurrentUser() instanceof GeneralUser)	{			// set general user mode
			setMenuUI(new MenuUI_General());
			setSearchBookUI(new SearchBookUI_General(DB.getBookDB()));
			setManageBookUI(new ManageBookUI(((GeneralUser)DB.getCurrentUser()).getBookList()));
		}
		else {														// set administrator mode
			setMenuUI(new MenuUI_Admin());
			setSearchBookUI(new SearchBookUI_Admin(DB.getBookDB()));
			setManageAccountUI(new ManageAccountUI(DB.getAccountDB()));
		}
	}
	
	
	/**
	 * Set the input mode to 'register' or 'modify' 
	 */
	public void setInputMode() {
		setInputBookInfoUI(new RegisterBookUI());
	}
	public void setInputMode(Book book) {
		setInputBookInfoUI(new ModifyBookUI(book));
	}
	
	
	/**
	 *  Setter
	 */
	private void setMenuUI(MenuUI menuUI) {
		this.menuUI = menuUI;
		add("MENU", menuUI);
	}
	public void setSearchBookUI(SearchBookUI searchBookUI) {
		this.searchBookUI = searchBookUI;
		add("SEARCH", searchBookUI);				
	}
	private void setManageBookUI(ManageBookUI manageBookUI) {
		this.manageBookUI = manageBookUI;
		add("MANAGE_BOOK", manageBookUI);
	}
	private void setInputBookInfoUI(InputBookInfoUI inputBookUI) {
		this.inputBookUI = inputBookUI;
		add("INPUT", inputBookUI);
	}
	private void setManageAccountUI(ManageAccountUI manageAccountUI) {
		this.manageAccountUI = manageAccountUI;
		add("MANAGE_ACCOUNT", manageAccountUI);
	}
	public void setCurrentTableUI(TableUI _currentTableUI) {
		currentTableUI = _currentTableUI;
	}
	
	/**
	 *  Getter
	 */
	public CardLayout getCardLayout()	{	return this.card;	}
	public LoginUI getLoginUI()			{	return loginUI;		}
	public RegisterAccountUI getRegisterAccountUI() 	{	return registerAccUI;	}
	public MenuUI getMenuUI() 					{	return menuUI;		}
	public SearchBookUI getSearchBookUI() 		{	return searchBookUI;	}
	public ManageBookUI getManageBookUI() 		{	return manageBookUI;	}
	public InputBookInfoUI getInputBookUI()		{	return inputBookUI;		}
	public ManageAccountUI getManageAccountUI()	{	return manageAccountUI;	}
}


/**
 *  Class about showing an error message on the screen
 */
class MessageScreen extends JDialog {
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel("");
	
    public MessageScreen(String str){
            getContentPane().add(label);
           
            label.setText(str);
            label.setHorizontalAlignment(JLabel.CENTER);
            
            setSize(400,120);
            setLocation(300, 200);
            setModal(true);
            setVisible(true);
    }
}