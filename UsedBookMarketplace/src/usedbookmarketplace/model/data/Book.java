package usedbookmarketplace.model.data;

public class Book {
	private String title;
	private String author;
	private String publisher;
	private String publicationYear;
	private String ISBN;
	private String price;
	private String bookState;
	private String sellerID;
//	private GeneralUser seller;
	private boolean isSold;

	// constructor
	public Book(String[] bookInfo) {
		title = bookInfo[0];
		author = bookInfo[1];
		publisher = bookInfo[2];
		publicationYear = bookInfo[3];
		ISBN = bookInfo[4];
		price = bookInfo[5];
		bookState = bookInfo[6];	 // excellent, good, fair
		
		if (bookInfo.length == 9) {
			sellerID = bookInfo[7];
			if (bookInfo[8].equals("Sold"))
				isSold = true;
			else
				isSold = false;
		}
	}
	
	// returns all book informations
	public String[] getBookInfo() {
		String isSoldstr;
		if (isSold == true)
			isSoldstr = "Sold";
		else
			isSoldstr = "Not Sold";
		
		String[] bookInfo = { title, author, publisher, publicationYear, ISBN, price, bookState, sellerID, isSoldstr };
		return bookInfo;
	}
	
	// getter, setter

	
//	public GeneralUser getSeller()		{ return this.seller; }
	
	public String getTitle() 			{ return title; }
	public String getAuthor()			{ return author; }
	public String getPublisher()		{ return publisher; }
	public String getPublicationYear()	{ return publicationYear; }
	public String getISBN()				{ return ISBN; }
	public String getPrice() 			{ return price; }
	public String getBookState() 		{ return bookState; }
	public String getSellerID() 		{ return this.sellerID; }
	public boolean isSold() 			{ return isSold; }

//	public void setSeller(GeneralUser seller) 	{ this.seller = seller;	}
	public void setTitle(String title) 			{ this.title = title;	}
	public void setAuthor(String author) 		{ this.author = author;	}
	public void setPublisher(String publisher) 	{ this.publisher = publisher;	}
	public void setPublicationYear(String publicationYear) {	this.publicationYear = publicationYear;	}
	public void setISBN(String ISBN)			{ this.ISBN = ISBN;	}
	public void setPrice(String price) 			{ this.price = price;	}
	public void setBookState(String bookState) 	{ this.bookState = bookState;	}
	public void setSellerID(String sellerID)	{ this.sellerID = sellerID; }
	public void setSold(boolean isSold) 		{ this.isSold = isSold;	}
}
