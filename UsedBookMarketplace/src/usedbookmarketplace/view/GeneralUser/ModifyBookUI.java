package usedbookmarketplace.view.GeneralUser;

import usedbookmarketplace.model.data.Book;

public class ModifyBookUI extends InputBookInfoUI {

	public ModifyBookUI(Book book) {
		
    	title_txt.setText(book.getTitle());
    	author_txt.setText(book.getAuthor());
    	publisher_txt.setText(book.getPublisher());
    	publicationYear_txt.setText(book.getPublicationYear());
    	price_txt.setText(book.getPrice());
    	bookState_txt.setText(book.getBookState());
    	
    	btn.setText("Modify");
	}
}
