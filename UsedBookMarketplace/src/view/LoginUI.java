package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.*;

public class LoginUI extends JPanel {

	private JButton loginBtn, registerBtn;
	private JLabel id_label, pw_label;
	private JTextField id_txtf; 
	private JPasswordField pw_pwf;

	// constructor
	public LoginUI() {		
		id_label = new JLabel("ID :");
		pw_label = new JLabel("PW :");
		
		id_txtf = new JTextField(15);
		pw_pwf = new JPasswordField(15);
		
		loginBtn = new JButton("Login");
		registerBtn = new JButton("Register");
		
		setLayout(null);
		
		id_label.setBounds(230, 150, 50, 20);
		id_txtf.setBounds(280, 150, 120, 20);
		
		pw_label.setBounds(230, 180, 50, 20);
		pw_pwf.setBounds(280, 180, 120, 20);
		
		JPanel btns_panel = new JPanel();
		btns_panel.setOpaque(false);
		btns_panel.add(loginBtn);
		btns_panel.add(registerBtn);
		btns_panel.setBounds(230, 220, 175, 35);
		
		add(id_label);
		add(id_txtf);
		add(pw_label);
		add(pw_pwf);
		add(btns_panel);
		
	}

	// add the actionListener to buttons
	public void addActionListener_login(ActionListener action){
		loginBtn.addActionListener(action);
	}
	public void addActionListener_register(ActionListener action){
		registerBtn.addActionListener(action);
	}
	
	
	public void resetTxtEmpty() {
		id_txtf.setText("");
		pw_pwf.setText("");
	}
	
	// getter, setter
	public String getIDtxt() {	return id_txtf.getText();	}
	public String getPWtxt() {	return new String(pw_pwf.getPassword());	}
}
