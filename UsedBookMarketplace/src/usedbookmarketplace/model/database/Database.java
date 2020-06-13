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

		for (int i = 0; i < bookDB.size(); i++) {
			for (int j = 0; j < accountDB.size() - 1; j++) {
				if (bookDB.get(i).getSellerID().equals(accountDB.get(j).getID())) {
					((GeneralUser)accountDB.get(j)).addBook(bookDB.get(i));
					bookDB.get(i).setSeller((GeneralUser)accountDB.get(j));
				}
			}
			searchedIndex.add(i);
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
		for (i = 0; i < accountDB.size(); i++) {	//DB·Î »©±â, checkInvalidInput
			if (id.equals(accountDB.get(i).getID())	&& pw.equals(accountDB.get(i).getPW())) {
				currentUser = accountDB.get(i);
				return true;
			}
		}
		throw new Exception("The ID or PW is not valid");
	}
	
	public void registerUser(String[] userInfo) {
		GeneralUser newUser = new GeneralUser(userInfo);		
		accountDB.add(newUser);
		
		file.writeFile(accountDB, "DB_account.txt");
	}
	
	public void searchBook(String searchWord, SearchStrategy searchFilter) {
		notifyObserver(searchFilter.searchBook(searchWord, this));	
	}
	
	public void purchaseBook(int originIndex) {		
		bookDB.get(originIndex).setSold(true); 					// change the state of a book to "Sold"
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view (table)
		
	}
	
	public void deleteBook_admin(int selectedIndex) {
		int originIndex = getRealIndex(selectedIndex);
		
		bookDB.get(originIndex).getSeller().getBookList().remove(bookDB.get(originIndex));	// remove the selected book from the user book list
		bookDB.remove(originIndex);								// remove the selected book from the database
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view (table)
	}	
	
	public void deleteBook_user(int selectedIndex) {
		bookDB.remove(((GeneralUser)currentUser).getBookList().get(selectedIndex));	// remove the selected book from the database
		((GeneralUser)currentUser).getBookList().remove(selectedIndex);				// remove the selected book from the user book list
		file.writeFile(bookDB, "DB_book.txt");							// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 						// update view (table)
	}	
	public void registerBook(String[] bookInfo) {
		Book newBook = new Book(bookInfo);
		newBook.setSold(false);
		newBook.setSeller((GeneralUser)currentUser);
		
		((GeneralUser)currentUser).addBook(newBook);
		bookDB.add(newBook);
		
		file.writeFile(bookDB, "DB_book.txt");
		notifyObserver(((GeneralUser)currentUser).getBookList());
	}
	public void modifyBook(String[] bookInfo, int selectedIndex) {
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
	
	public void changeUserState(int selectedIndex) {
		GeneralUser user = (GeneralUser) accountDB.get(selectedIndex);
		if (user.isActivated())
			user.setActivated(false);
		else
			user.setActivated(true);
		
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}
	
	public void deleteAccount(int selectedIndex) {
		Vector<Book> bookList = ((GeneralUser)accountDB.get(selectedIndex)).getBookList();
		
		for (int i = 0; i < bookList.size(); i++) {
			bookDB.remove(bookList.get(i));
		}
		accountDB.remove(selectedIndex);
		
		file.writeFile(bookDB, "DB_book.txt");				// update file
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}
	
	public int getRealIndex(int selectedIndex) {
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
