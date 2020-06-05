package usedbookmarketplace.model.data;

enum BookState{
	EXCELLENT, GOOD, FIAR
}

public class Book {
	private String title;
	private String author;
	private String publisher;
	private int publicationYear;
	private int ISBN;
	private int price;
	private BookState bookState; 
	private String sellerID;
	
	public Book(String[] bookinfo) {		
		title = bookinfo[0];
		author = bookinfo[1];
		publisher = bookinfo[2];
		publicationYear = Integer.parseInt(bookinfo[3]);
		ISBN = Integer.parseInt(bookinfo[4]);
		price = Integer.parseInt(bookinfo[5]);
		bookState = strToBookState(bookinfo[6]);
		sellerID = bookinfo[7];
		
	}
	
	// need to change a condition
	private BookState strToBookState(String s) {
		switch (Integer.parseInt(s)) {
		case 0: return BookState.EXCELLENT;
		case 1: return BookState.GOOD;
		case 2: return BookState.FIAR;
		default:
			return null;
		} 
	}
	
	public String getSellerID() {
		return this.sellerID;
	}
}