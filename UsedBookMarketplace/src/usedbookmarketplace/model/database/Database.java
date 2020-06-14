package usedbookmarketplace.model.database;

import java.util.ArrayList;
import java.util.Vector;

import usedbookmarketplace.controller.SearchStrategy;
import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;
import usedbookmarketplace.model.filesystem.FileProcess;
import usedbookmarketplace.view.Observer;

public class Database implements Observable {

	private ArrayList<Observer> observers = new ArrayList<>();
	private Vector<Book> bookDB = new Vector<Book>();
	private Vector<User> accountDB = new Vector<User>();
	private Vector<Integer> searchedIndex = new Vector<Integer>();			// the index from the searched table
	private FileProcess file = new FileProcess();
	private User currentUser;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public Database() {
		bookDB = file.readBookFile("DB_book.txt");
		accountDB = file.readAccountFile("DB_account.txt");

		int i = 0;
		for (Book book : bookDB) {
			for (User user : accountDB) {
				if (book.getSellerID().equals(user.getID())) {
					((GeneralUser) user).addBook(book);
//					book.setSeller((GeneralUser) user);
				}
			}
			searchedIndex.add(i++);
		}
	}
	
	@Override
	public <T> void notifyObserver(Vector<T> data) {
		for (Observer observer : observers) {
			observer.update(data);
		}
	}

	@Override
	public void addObserver(Observer observer) {
		 observers.add(observer);
	}

	@Override
	public void deleteObserver(Observer observer) {
		int index = observers.indexOf(observer);
		observers.remove(index);
	}

	public boolean authenticate(String id, String pw) throws Exception {
		int i;
		for (i = 0; i < accountDB.size(); i++) {
			if (id.equals(accountDB.get(i).getID())	&& pw.equals(accountDB.get(i).getPW())) {
				currentUser = accountDB.get(i);
				return true;
			}
		}
		throw new Exception("The ID or PW is not valid");
	}
	
	public void registerUser(String[] userInfo) throws Exception {
		if (userInfo[0].isEmpty() || !userInfo[0].matches("([a-z A-Z 0-9]+)") || userInfo[1].isEmpty()
				|| userInfo[2].isEmpty() || !userInfo[3].matches("[0-9|-]+") || userInfo[4].isEmpty())
			throw new Exception("<html>Invalid Input :<br>"
					+ "(1) All blanks must be written.<br>"
					+ "(2) The ID consists of letters or numbers only.<br>"
					+ "(3) Enter only numbers and '-' for phone numbers.</html>");

		for (User user : accountDB) {
			if (userInfo[0].equals(user.getID()))
				throw new Exception("Invalid Input : The same ID exists");
		}
		
		GeneralUser newUser = new GeneralUser(userInfo);		
		accountDB.add(newUser);
		
		file.writeFile(accountDB, "DB_account.txt");
	}
	
	public void searchBook(String searchWord, SearchStrategy searchFilter) {
		notifyObserver(searchFilter.searchBook(searchWord, this));	
	}
	
	public void purchaseBook(int selectedIndex) throws Exception {
		int originIndex = getRealIndex(selectedIndex); 				// the actual index stored in the database(file)
		GeneralUser bookSeller = getBookSeller(originIndex);
		Book selectedBook = bookDB.get(originIndex);
		
		if (selectedBook.isSold())
			throw new Exception("Already Sold out!");
		if (!bookSeller.isActivated())
			throw new Exception("The seller of the book you want to purchase is deactivated!");
		if (bookSeller.getID().equals(getCurrentUser().getID()))
			throw new Exception("You can't buy your own book!");
		
		selectedBook.setSold(true); 							// change the state of a book to "Sold"
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view (table)
	}
	
	public void deleteBook_admin(int selectedIndex) throws Exception {
		int originIndex = getRealIndex(selectedIndex);
		Book book = bookDB.get(originIndex);
		GeneralUser bookSeller = null;
		for (User user : accountDB) {
			if (book.getSellerID().equals(user.getID())) {
				bookSeller = (GeneralUser) user;
				break;
			}
		}
		if (bookSeller.equals(null))
			throw new Exception("There are no users matching the id of the user who registered the book!");
		
		bookSeller.getBookList().remove(bookDB.get(originIndex));	// remove the selected book from the user book list
		bookDB.remove(originIndex);									// remove the selected book from the database
		
		file.writeFile(bookDB, "DB_book.txt");						// update file
		notifyObserver(bookDB); 									// update view (table)
	}	
	
	public void deleteBook_user(int selectedIndex) throws Exception {
		checkIsSelected(selectedIndex);
		
		bookDB.remove(((GeneralUser)currentUser).getBookList().get(selectedIndex));	// remove the selected book from the database
		((GeneralUser)currentUser).getBookList().remove(selectedIndex);				// remove the selected book from the user book list
		file.writeFile(bookDB, "DB_book.txt");							// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 						// update view (table)
	}
	
	private void checkBookInfo(String[] bookInfo) throws Exception {

		if(bookInfo[0].isEmpty())
			throw new Exception("Title must be entered!");
	}
	public void registerBook(String[] bookInfo) throws Exception {
		checkBookInfo(bookInfo);
		
		Book newBook = new Book(bookInfo);
		newBook.setSold(false);
		newBook.setSellerID(currentUser.getID());
		
		((GeneralUser)currentUser).addBook(newBook);
		bookDB.add(newBook);
		
		file.writeFile(bookDB, "DB_book.txt");
		notifyObserver(((GeneralUser)currentUser).getBookList());
	}
	public void modifyBook(String[] bookInfo, int selectedIndex) throws Exception {
		checkBookInfo(bookInfo);
		
		Book book = ((GeneralUser)currentUser).getBookList().get(selectedIndex);
		
		book.setTitle(bookInfo[0]);
		book.setAuthor(bookInfo[1]);
		book.setPublisher(bookInfo[2]);
		book.setPublicationYear(bookInfo[3]);
		book.setISBN(bookInfo[4]);
		book.setPrice(bookInfo[5]);
		book.setBookState(bookInfo[6]);
		
		file.writeFile(bookDB, "DB_book.txt");								// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 			// update view (table)
	}
	
	public void changeUserState(int selectedIndex) throws Exception {
		checkIsSelected(selectedIndex);
		
		GeneralUser user = (GeneralUser) accountDB.get(selectedIndex);
		if (user.isActivated())
			user.setActivated(false);
		else
			user.setActivated(true);
		
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}
	
	private void checkIsSelected(int selectedIndex) throws Exception {
		if (selectedIndex == -1)
			throw new Exception("Please select the row from the table!");
	}

	public void deleteAccount(int selectedIndex) throws Exception {
		checkIsSelected(selectedIndex);
		if (((GeneralUser)accountDB.get(selectedIndex)).isActivated())
			throw new Exception("'Activated' user cannot be deleted!");
		
		Vector<Book> bookList = ((GeneralUser)accountDB.get(selectedIndex)).getBookList();
		
		for (int i = 0; i < bookList.size(); i++) {
			bookDB.remove(bookList.get(i));
		}
		accountDB.remove(selectedIndex);
		
		file.writeFile(bookDB, "DB_book.txt");				// update file
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}
	
	public GeneralUser getBookSeller(int index) throws Exception {
		for (User user : accountDB) {
			if (bookDB.get(index).getSellerID().equals(user.getID())) {
				return (GeneralUser) user;
			}
		}
		throw new Exception("There are no users matching the id of the user who registered the book!");
	}
	
	// if you don't select a book or account, show you a message
	public int getRealIndex(int selectedIndex) throws Exception {
		if (selectedIndex == -1)
			throw new Exception("Please select the row from the table!");
		return searchedIndex.get(selectedIndex);
	}
	
	public void initSearchIndex() {
		searchedIndex = new Vector<Integer>();
		for (int i = 0; i < bookDB.size(); i++)
			searchedIndex.add(i);
	}

	public void setSearchedIndex(Vector<Integer> searchedIndex) {
		this.searchedIndex = searchedIndex;
	}
	public Vector<Integer> getSearchedIndex() {
		return searchedIndex;
	}
	public Vector<Book> getBookDB(){
		return bookDB;
	}
	public Vector<User> getAccountDB(){
		return accountDB;
	}
}
