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
	private Vector<Integer> searchedIndex = new Vector<Integer>();
	private FileProcess file = new FileProcess();
	private SearchStrategy searchFilter;

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

	public User authenticate(String id, String pw) throws Exception {
		for (int i = 0; i < accountDB.size(); i++) {	//DB·Î »©±â, checkInvalidInput
			if (id.equals(accountDB.get(i).getID())	&& pw.equals(accountDB.get(i).getPW()))
				return accountDB.get(i);
		}
		throw new Exception("The ID or PW is not valid");
	}
	
	public void registerUser(String[] userInfo) throws Exception {
		if (userInfo[0].isEmpty() || userInfo[1].isEmpty() || userInfo[2].isEmpty() || !userInfo[3].matches("[0-9|-]+") || userInfo[4].isEmpty())
			throw new Exception("Invalid Input : All blanks must be written and enter only numbers and '-' for phone numbers.");

		for (int i = 0; i < accountDB.size(); i++) {
			if (userInfo[0].equals(accountDB.get(i).getID()))
				throw new Exception("Invalid Input : The same ID exists");
		}
		
		GeneralUser newUser = new GeneralUser(userInfo);		
		accountDB.add(newUser);
		
		file.writeFile(accountDB, "DB_account.txt");
	}
	
	public void searchBook(String searchWord) {
		notifyObserver(searchFilter.searchBook(searchWord, this));		
	}
	
	public void purchaseBook(int searchedIndex) throws Exception {	// searchedIndex = the index from the searched table	
		int originIndex = getRealIndex(searchedIndex); 				// the actual index stored in the database(file)
		
		for (Integer s : this.searchedIndex)
			System.out.println(s);
		
		if (bookDB.get(originIndex).isSold()) {
			throw new Exception("Already Sold out");
		}
		else {
			bookDB.get(originIndex).setSold(true); 					// change the state of a book to "Sold"
			file.writeFile(bookDB, "DB_book.txt");					// update file
			notifyObserver(bookDB); 								// update view (table)
		}
	}
	
	public void deleteBook(int searchedIndex) throws Exception {					// searchedIndex = the index from the searched table
		int originIndex = getRealIndex(searchedIndex); 

		bookDB.remove(originIndex);								// remove the selected book from the database
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view (table)
	}	
	
	public void modifyBook(String[] bookInfo, int originIndex) {
		bookDB.get(originIndex).setTitle(bookInfo[0]);
		bookDB.get(originIndex).setAuthor(bookInfo[1]);
		bookDB.get(originIndex).setPublisher(bookInfo[2]);
		bookDB.get(originIndex).setPublicationYear(bookInfo[3]);
		bookDB.get(originIndex).setPrice(bookInfo[4]);
		bookDB.get(originIndex).setBookState(bookInfo[5]);
		
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view (table)
	}
	
	public int getRealIndex(int _searchedIndex) throws Exception {
		if (_searchedIndex == -1) 									// if you don't select a book or account, show you a message
			throw new Exception("Please select from the table!");
		return searchedIndex.get(_searchedIndex);
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
	public void setSearchFilter(SearchStrategy _searchFilter) {
		searchFilter = _searchFilter;
	}
	public SearchStrategy getSearchFilter() {
		return searchFilter;
	}
	public Vector<Book> getBookDB(){
		return bookDB;
	}
	public Vector<User> getAccountDB(){
		return accountDB;
	}
}
