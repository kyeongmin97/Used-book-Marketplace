package usedbookmarketplace.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {
	protected JButton btn1 = new JButton();
	protected JButton btn2 = new JButton();;
	
	Menu(){		
		JPanel btnsPanel = new JPanel();
		btnsPanel.setOpaque(false);
		btnsPanel.add(btn1);
		btnsPanel.add(btn2);
		
		add(btnsPanel);	
	}
	
	public void addActionListeners(ActionListener action){
		btn1.addActionListener(action);
		btn2.addActionListener(action);
	}
	
	public JButton getBtn1() {
		return btn1;
	}
	public JButton getBtn2() {
		return btn2;
	}
}