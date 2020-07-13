package model;

import java.util.ArrayList;
import java.util.Vector;

import controller.InvalidValueException;
import controller.SearchStrategy;
import model.data.*;
import model.filesystem.FileProcess;
import view.Observer;

public class Model implements Observable {

	// database
	private Vector<Book> bookDB = new Vector<Book>();
	private Vector<User> accountDB = new Vector<User>();
	
	private Vector<Integer> originIndex = new Vector<Integer>();
	private User currentUser;

	private ArrayList<Observer> observers = new ArrayList<>();
	private ExceptionCheck exceptCheck = new ExceptionCheck(this);
	private FileProcess file = new FileProcess();
	
	// constructor
	public Model() {
		bookDB = file.readBookFile("DB_book.txt");
		accountDB = file.readAccountFile("DB_account.txt");

		int i = 0;
		for (Book book : bookDB) {
			for (User user : accountDB) {
				if (book.getSellerID().equals(user.getID())) {
					((GeneralUser) user).getBookList().add(book);
				}
			}
			originIndex.add(i++);
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

	/*
	 * authenticate the user
	 */
	public boolean authenticate(String id, String pw) throws InvalidValueException, Exception {
		for (int i = 0; i < accountDB.size(); i++) {
			if (id.equals(accountDB.get(i).getID()) && pw.equals(accountDB.get(i).getPW())) {
				if (accountDB.get(i) instanceof Admin || ((GeneralUser) accountDB.get(i)).isActivated()) {
					currentUser = accountDB.get(i);
					return true;
				} else
					throw new InvalidValueException("<html>INVALID ACCOUNT<br>- deactivated account</html>");
			}
		}
		throw new InvalidValueException("<html>INVALID INPUT<br>- The ID or PW is not valid.</html>");
	}

	/*
	 * register the new account(general user)
	 */
	public void registerUser(String[] userInfo) throws InvalidValueException, Exception {
		exceptCheck.checkUserInfo(userInfo);										// check exception
		
		accountDB.insertElementAt(new GeneralUser(userInfo), accountDB.size()-1);	// register the new account with DB		
		file.writeFile(accountDB, "DB_account.txt");								// update file
	}
	
	/*
	 * register new book (general user)
	 */
	public void registerBook(String[] bookInfo) throws InvalidValueException, Exception {
		exceptCheck.checkBookInfo(bookInfo);									// check exception

		((GeneralUser) currentUser).registerBook(bookInfo);
		
		Vector<Book> userBookList = ((GeneralUser) currentUser).getBookList();	// get new book from user's book list
		bookDB.add(userBookList.get(userBookList.size()-1));					// add new book to DB
		
		file.writeFile(bookDB, "DB_book.txt");						// update file
		notifyObserver(((GeneralUser)currentUser).getBookList());	// update view/table
	}

	/*
	 * search entered book
	 */
	public void searchBook(String searchWord, SearchStrategy searchFilter) throws InvalidValueException, Exception {
		notifyObserver(searchFilter.searchBook(searchWord, this));		// search books and update view
		if (originIndex.isEmpty())
			throw new InvalidValueException("No search results!");
	}
	
	/*
	 * purchase selected book (general user)
	 */
	public void purchaseBook(int selectedIndex) throws InvalidValueException, Exception {
		int originalIndex = changeToOriginIndex(selectedIndex); 			// the actual index stored in the database(file)
		exceptCheck.checkValidBookForPurchase(originalIndex);				// check exception
		
		bookDB.get(originalIndex).setSold(true); 				// change the state of a book to "Sold"
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view
	}
	
	/*
	 * delete selected book (administrator)
	 */
	public void deleteBook_admin(int selectedIndex) throws InvalidValueException, Exception {
		int originalIndex = changeToOriginIndex(selectedIndex);
		
		getBookSeller(originalIndex).deleteBook(bookDB.get(originalIndex));		// delete the selected book from the user book list
		bookDB.remove(originalIndex);											// delete the selected book from the database
		
		file.writeFile(bookDB, "DB_book.txt");						// update file
		notifyObserver(bookDB); 									// update view
	}	
	
	/*
	 * delete selected book (general user)
	 */
	public void deleteBook_user(int selectedIndex) throws InvalidValueException, Exception {
		exceptCheck.checkIsSelected(selectedIndex);										// check exception
		
		bookDB.remove(((GeneralUser)currentUser).getBookList().get(selectedIndex));		// remove the selected book from the database
		((GeneralUser)currentUser).deleteBook(selectedIndex);							// remove the selected book from the user book list
		
		file.writeFile(bookDB, "DB_book.txt");											// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 						// update view
	}
	
	/*
	 * delete selected account (administrator)
	 */
	public void deleteUser(int selectedIndex) throws InvalidValueException, Exception {
		exceptCheck.checkValidAccount(selectedIndex);		// check exception
		
		Vector<Book> bookList = ((GeneralUser)accountDB.get(selectedIndex)).getBookList();
		
		for (int i = 0; i < bookList.size(); i++) {			// delete all books registered by selected user
			bookDB.remove(bookList.get(i));
		}
		accountDB.remove(selectedIndex);					// delete general user from the DB
		
		file.writeFile(bookDB, "DB_book.txt");				// update file
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}

	/*
	 * modify selected book (general user)
	 */
	public void modifyBook(String[] bookInfo, int selectedIndex) throws InvalidValueException, Exception {
		exceptCheck.checkBookInfo(bookInfo);								// check exception
		Book book = bookDB.get(changeToOriginIndex(selectedIndex));
		
		book.setTitle(bookInfo[0]);											// modify
		book.setAuthor(bookInfo[1]);
		book.setPublisher(bookInfo[2]);
		book.setPublicationYear(bookInfo[3]);
		book.setISBN(bookInfo[4]);
		book.setPrice(bookInfo[5]);
		book.setBookState(bookInfo[6]);
		
		file.writeFile(bookDB, "DB_book.txt");								// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 			// update view (table)
	}
	
	/*
	 * change the state of selected general user (by administrator)
	 */
	public void changeUserState(int selectedIndex) throws InvalidValueException, Exception {
		exceptCheck.checkIsSelected(selectedIndex);			// check exception
		
		GeneralUser user = (GeneralUser) accountDB.get(selectedIndex);
		if (user.isActivated())								// change selected user state
			user.setActivated(false);
		else
			user.setActivated(true);
		
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}

	public GeneralUser getBookSeller(int index) throws InvalidValueException, Exception {
		for (User user : accountDB) {
			if (bookDB.get(index).getSellerID().equals(user.getID()))
				return (GeneralUser) user;
		}
		throw new InvalidValueException("There are no users matching the id of the user who registered the book!");
	}
	
	public void resetToInitalIndex() {
		originIndex = new Vector<Integer>();
		for (int i = 0; i < bookDB.size(); i++)
			originIndex.add(i);
	}
	public void resetToUserBookIndex() {
		originIndex = new Vector<Integer>();
		int i = 0;
		for (Book book : ((GeneralUser) currentUser).getBookList()) {
			for (; i < bookDB.size(); i++)
				if (book.equals(bookDB.get(i))) {
					originIndex.add(i);
					break;
				}
		}
	}
	
	public int changeToOriginIndex(int selectedIndex) throws InvalidValueException {
		if (selectedIndex == -1)
			throw new InvalidValueException("Please select the row from the table!");
		return originIndex.get(selectedIndex);
	}	
	
	/*
	 * getter, setter
	 */
	public Vector<Book> getBookDB(){
		return bookDB;
	}
	public Vector<User> getAccountDB(){
		return accountDB;
	}
	public Vector<Integer> getOriginIndex() {
		return originIndex;
	}
	public void setOriginIndex(Vector<Integer> originIndex) {
		this.originIndex = originIndex;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public ExceptionCheck getExceptionCheck() {
		return exceptCheck;
	}
}
