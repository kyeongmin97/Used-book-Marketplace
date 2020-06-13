package usedbookmarketplace.view.GeneralUser;

import usedbookmarketplace.model.data.Book;

public class ModifyBookUI extends InputBookInfoUI {

	public ModifyBookUI(Book book) {
		super();
    	btn.setText("Modify");
		
    	title_txt.setText(book.getTitle());
    	author_txt.setText(book.getAuthor());
    	publisher_txt.setText(book.getPublisher());
    	publicationYear_txt.setText(book.getPublicationYear());
    	isbn_txt.setText(book.getISBN());
    	price_txt.setText(book.getPrice());
    	
    	
    	if (book.getBookState().equals("Excellent"))
    		excellentStateRbtn.setSelected(true);
		else if (book.getBookState().equals("Good"))
			goodStateRbtn.setSelected(true);
		else if (book.getBookState().equals("Fair"));
    		fairStateRbtn.setSelected(true);
	}
}
