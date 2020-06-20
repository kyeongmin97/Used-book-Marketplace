package controller;

import java.util.Vector;

import model.data.Book;
import model.database.Database;

public interface SearchStrategy {
	public Vector<Book> searchBook(String searchWord, Database DB);	
}

class SearchByTitle implements SearchStrategy {
	public Vector<Book> searchBook(String title, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getTitle().equals(title)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}

class SearchByAuthor implements SearchStrategy {
	public Vector<Book> searchBook(String author, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getAuthor().equals(author)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}

class SearchByISBN implements SearchStrategy {
	public Vector<Book> searchBook(String isbn, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getISBN().equals(isbn)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}

class SearchBySellerID implements SearchStrategy {
	public Vector<Book> searchBook(String sellerID, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getSellerID().equals(sellerID)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}

class SearchByPublisher implements SearchStrategy {
	public Vector<Book> searchBook(String publisher, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getPublisher().equals(publisher)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}

class SearchByPublicationYear implements SearchStrategy {
	public Vector<Book> searchBook(String publicationYear, Database DB) {

		Vector<Book> searchedBookList = new Vector<Book>();
		DB.setOriginIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getPublicationYear().equals(publicationYear)) {
				searchedBookList.add(DB.getBookDB().get(i));
				DB.getOriginIndex().add(i);
			}
		}
		return searchedBookList;
	}
}