package usedbookmarketplace.controller;

import java.util.Vector;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.database.Database;

public interface SearchStrategy {
	public Vector<Book> searchBook(String searchWord, Database DB);	
}

class searchByTitle implements SearchStrategy {
	public Vector<Book> searchBook(String title, Database DB) {

		Vector<Book> bookList = new Vector<Book>();
		DB.setSearchedIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getTitle().equals(title)) {
				bookList.add(DB.getBookDB().get(i));
				DB.getSearchedIndex().add(i);
			}
		}
		return bookList;
	}
}

class searchByAuthor implements SearchStrategy {
	public Vector<Book> searchBook(String author, Database DB) {

		Vector<Book> bookList = new Vector<Book>();
		DB.setSearchedIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getAuthor().equals(author)) {
				bookList.add(DB.getBookDB().get(i));
				DB.getSearchedIndex().add(i);
			}
		}
		return bookList;
	}
}

class searchByISBN implements SearchStrategy {
	public Vector<Book> searchBook(String isbn, Database DB) {

		Vector<Book> bookList = new Vector<Book>();
		DB.setSearchedIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getISBN().equals(isbn)) {
				bookList.add(DB.getBookDB().get(i));
				DB.getSearchedIndex().add(i);
			}
		}
		return bookList;
	}
}

class searchBySellerID implements SearchStrategy {
	public Vector<Book> searchBook(String sellerID, Database DB) {

		Vector<Book> bookList = new Vector<Book>();
		DB.setSearchedIndex(new Vector<Integer>());

		for (int i = 0; i < DB.getBookDB().size(); i++) {
			if (DB.getBookDB().get(i).getSellerID().equals(sellerID)) {
				bookList.add(DB.getBookDB().get(i));
				DB.getSearchedIndex().add(i);
			}
		}
		return bookList;
	}
}
