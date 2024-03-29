package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuUI extends JPanel {
	protected JButton btn1 = new JButton();
	protected JButton btn2 = new JButton();
	private JButton logoutBtn = new JButton("Logout");
	
	public MenuUI(){
		
		setLayout(null);
		
		JPanel btnsPanel = new JPanel();
		btnsPanel.setOpaque(false);
		btnsPanel.setBounds(280, 200, 150, 100);
		btnsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnsPanel.add(btn1);
		btnsPanel.add(btn2);
		btnsPanel.add(logoutBtn);
		add(btnsPanel);	
	}
	
	public void addActionListener_btn1(ActionListener action){
		btn1.addActionListener(action);
	}
	public void addActionListener_btn2(ActionListener action){
		btn2.addActionListener(action);
	}
	public void addActionListener_logoutBtn(ActionListener action){
		logoutBtn.addActionListener(action);
	}
}