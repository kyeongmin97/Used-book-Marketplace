package model.data;

import java.util.Vector;

public class GeneralUser extends User {
	private String name;
	private String phoneNum;
	private String email;
	private boolean isActivated;
	
	private Vector<Book> bookList = new Vector<Book>();
	
	public GeneralUser(String[] tokens) {
		super.id = tokens[0];
		super.pw = tokens[1];
		name = tokens[2];
		phoneNum = tokens[3];
		email = tokens[4];
		if (tokens.length == 6) {
			if (tokens[5].equals("Activated"))
				isActivated = true;
			else
				isActivated = false;
		}
		else if (tokens.length == 5)
			isActivated = true;
		else
			System.out.print("error");
	}
	
	public void registerBook(String[] bookInfo) {
		Book newBook = new Book(bookInfo);
		newBook.setSold(false);		// set new book to 'Not Sold'
		newBook.setSellerID(id);	// set sellerID

		bookList.add(newBook);		// add new book to user book list

	}
	public void deleteBook(int index) {
		bookList.remove(index);
	}
	public void deleteBook(Book book) {
		bookList.remove(book);
	}
	
	@Override
	public String[] getUserInfo() {
		String[] userInfo = new String[6];
		
		userInfo[0] = id;
		userInfo[1] = pw;
		userInfo[2] = name;
		userInfo[3] = phoneNum;
		userInfo[4] = email;
		if (isActivated)
			userInfo[5] = "Activated";
		else
			userInfo[5] = "Deactivated";
		
		return userInfo;
	}
	
	// getter, setter
	public String getEmail() {
		return email;
	}
	public Vector<Book> getBookList() {
		return bookList;
	}
	public boolean isActivated() {
		return isActivated;
	}
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
}
