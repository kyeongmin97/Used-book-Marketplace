package usedbookmarketplace.view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CardLayout card = new CardLayout();
	public Login login = new Login();
	public Menu menu = new Menu();
	public Register register = new Register();
	public Search search = new Search();
	
	public MainFrame(){
		
		setLayout(card);
		this.add("LOGIN", login);
		this.add("REGISTER", register);
		this.add("SEARCH", search);
		this.add("MENU", menu);
		
		setTitle("Used-book Marketplace");
		setSize(800, 600);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setVisible(true);
	}
	
	public void addActionListener(ActionListener action) {
		login.addActionListeners(action);
		register.addActionListeners(action);
		search.addActionListeners(action);
		//...
	}
	
	public void setMenu(Menu _menu) {
		this.menu = _menu;
		this.add("MENU", _menu);
	}
	
	public void changeCardToSearch(Search search) {
		this.add("SEARCH", search);
		this.getCardLayout().show(this.getContentPane(), "SEARCH");
	}
	
	public CardLayout getCardLayout(){
		return this.card;
	}
	
	public void setMessageFrame(String msg) {
		new messageFrame(msg);
	}
}

class messageFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	JLabel label = new JLabel("");
    public messageFrame(String str){
            getContentPane().add(label);
           
            label.setText(str.toString());
            label.setHorizontalAlignment(JLabel.CENTER);
            
            this.setSize(360,240);
            setLocation(300, 200);
            this.setModal(true);
            this.setVisible(true);
    }
}
