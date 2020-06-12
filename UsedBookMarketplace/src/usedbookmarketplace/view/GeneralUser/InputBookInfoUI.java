package usedbookmarketplace.view.GeneralUser;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class InputBookInfoUI extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel title_label = new JLabel("Title :"); 
	private JLabel author_label = new JLabel("Author :");
	private JLabel publisher_label = new JLabel("Publisher :");
	private JLabel publicationYear_label = new JLabel("Publication Year :");
	private JLabel price_label = new JLabel("Price :");
	private JLabel bookState_label = new JLabel("Book State :");
	
	protected JTextField title_txt = new JTextField();
	protected JTextField author_txt = new JTextField();
	protected JTextField publisher_txt = new JTextField();
	protected JTextField publicationYear_txt = new JTextField();
	protected JTextField price_txt = new JTextField();
	protected JTextField bookState_txt = new JTextField();
	
	protected JButton btn = new JButton();

	public InputBookInfoUI() {
		
		initialize();
	}
    
    public void addActionListener_btn(ActionListener action) {
    	btn.addActionListener(action);
	}
    
    public String[] getAllTxt() {
		String[] tokens = new String[6];
		
		tokens[0] = title_txt.getText();
		tokens[1] = author_txt.getText();
		tokens[2] = publisher_txt.getText();
		tokens[3] = publicationYear_txt.getText();
		tokens[4] = price_txt.getText();
		tokens[5] = bookState_txt.getText();
		
		return tokens;
	}

    private void initialize() {
    	setLayout(null);
        
    	title_label.setBounds(160, 100, 100, 20); 		title_label.setHorizontalAlignment(JLabel.RIGHT);
    	author_label.setBounds(160, 130, 100, 20);  	author_label.setHorizontalAlignment(JLabel.RIGHT);
    	publisher_label.setBounds(160, 160, 100, 20); 	publisher_label.setHorizontalAlignment(JLabel.RIGHT);
    	publicationYear_label.setBounds(160, 190, 100, 20); publicationYear_label.setHorizontalAlignment(JLabel.RIGHT);
    	price_label.setBounds(160, 220, 100, 20); 		price_label.setHorizontalAlignment(JLabel.RIGHT);
    	bookState_label.setBounds(160, 250, 100, 20); 	bookState_label.setHorizontalAlignment(JLabel.RIGHT);
    	
    	title_txt.setBounds(280, 100, 120, 20);
    	author_txt.setBounds(280, 130, 120, 20);
    	publisher_txt.setBounds(280, 160, 120, 20);
    	publicationYear_txt.setBounds(280, 190, 120, 20);
    	price_txt.setBounds(280, 220, 120, 20);
    	bookState_txt.setBounds(280, 250, 120, 20);
    	
    	btn.setBounds(250, 300, 100, 35);

    	add(title_label);			add(title_txt);
    	add(author_label);			add(author_txt);
    	add(publisher_label);		add(publisher_txt);
    	add(publicationYear_label);	add(publicationYear_txt);
    	add(price_label);			add(price_txt);
    	add(bookState_label);		add(bookState_txt);
        add(btn);
    }
}