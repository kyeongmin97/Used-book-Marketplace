package usedbookmarketplace.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuUI extends JPanel {
	protected JButton btn1 = new JButton();
	protected JButton btn2 = new JButton();;
	
	public MenuUI(){
		
		setLayout(null);
		
		JPanel btnsPanel = new JPanel();
		btnsPanel.setOpaque(false);
		btnsPanel.setBounds(280, 200, 200, 100);
		btnsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnsPanel.add(btn1);
		btnsPanel.add(btn2);
		
		add(btnsPanel);	
	}
	
	public void addActionListener_btn1(ActionListener action){
		btn1.addActionListener(action);
	}
	public void addActionListener_btn2(ActionListener action){
		btn2.addActionListener(action);
	}
//	
//	public JButton getBtn1() {
//		return btn1;
//	}
//	public JButton getBtn2() {
//		return btn2;
//	}
}