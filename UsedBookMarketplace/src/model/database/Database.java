package model.database;

import java.util.ArrayList;
import java.util.Vector;

import controller.InvalidException;
import controller.SearchStrategy;
import model.data.Admin;
import model.data.Book;
import model.data.GeneralUser;
import model.data.User;
import model.filesystem.FileProcess;
import view.Observer;

public class Database implements Observable {

	private ArrayList<Observer> observers = new ArrayList<>();
	private Vector<Book> bookDB = new Vector<Book>();
	private Vector<User> accountDB = new Vector<User>();
	private Vector<Integer> originIndex = new Vector<Integer>();
	private FileProcess file = new FileProcess();
	private User currentUser;

	/*
	 * constructor
	 */
	public Database() {
		bookDB = file.readBookFile("DB_book.txt");
		accountDB = file.readAccountFile("DB_account.txt");

		int i = 0;
		for (Book book : bookDB) {
			for (User user : accountDB) {
				if (book.getSellerID().equals(user.getID())) {
					((GeneralUser) user).registerBook(book);
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
	public boolean authenticate(String id, String pw) throws InvalidException, Exception {
		for (int i = 0; i < accountDB.size(); i++) {
			if (id.equals(accountDB.get(i).getID()) && pw.equals(accountDB.get(i).getPW())) {
				if (accountDB.get(i) instanceof Admin || ((GeneralUser) accountDB.get(i)).isActivated()) {
					currentUser = accountDB.get(i);
					return true;
				} else
					throw new InvalidException("<html>INVALID ACCOUNT<br>- deactivated account</html>");
			}
		}
		throw new InvalidException("<html>INVALID INPUT<br>- The ID or PW is not valid.</html>");
	}

	/*
	 * register the new account(general user)
	 */
	public void registerUser(String[] userInfo) throws InvalidException, Exception {
		checkUserInfo(userInfo);													// check exception
		accountDB.insertElementAt(new GeneralUser(userInfo), accountDB.size()-1);	// register the new account with DB		
		file.writeFile(accountDB, "DB_account.txt");								// update file
	}
	
	/*
	 * register new book (general user)
	 */
	public void registerBook(String[] bookInfo) throws InvalidException, Exception {
		checkBookInfo(bookInfo);								// check exception

		Book newBook = new Book(bookInfo);
		newBook.setSold(false);									// set new book to 'Not Sold'
		newBook.setSellerID(currentUser.getID());				// set sellerID

		((GeneralUser) currentUser).registerBook(newBook);		// add new book to user book list
		bookDB.add(newBook);									// add new book to DB

		file.writeFile(bookDB, "DB_book.txt");						// update file
		notifyObserver(((GeneralUser)currentUser).getBookList());	// update view/table
	}

	/*
	 * search entered book
	 */
	public void searchBook(String searchWord, SearchStrategy searchFilter) throws InvalidException, Exception {
		notifyObserver(searchFilter.searchBook(searchWord, this));		// search books and update view
		if (originIndex.isEmpty())
			throw new InvalidException("No search results!");
	}
	
	/*
	 * purchase selected book (general user)
	 */
	public void purchaseBook(int selectedIndex) throws InvalidException, Exception {
		int originalIndex = changeToOriginIndex(selectedIndex); 			// the actual index stored in the database(file)
		checkValidBook(originalIndex);							// check exception
		
		bookDB.get(originalIndex).setSold(true); 					// change the state of a book to "Sold"
		file.writeFile(bookDB, "DB_book.txt");					// update file
		notifyObserver(bookDB); 								// update view
	}
	
	/*
	 * delete selected book (administrator)
	 */
	public void deleteBook_admin(int selectedIndex) throws InvalidException, Exception {
		int originalIndex = changeToOriginIndex(selectedIndex);
		
		getBookSeller(originalIndex).deleteBook(bookDB.get(originalIndex));		// delete the selected book from the user book list
		bookDB.remove(originalIndex);											// delete the selected book from the database
		
		file.writeFile(bookDB, "DB_book.txt");						// update file
		notifyObserver(bookDB); 									// update view
	}	
	
	/*
	 * delete selected book (general user)
	 */
	public void deleteBook_user(int selectedIndex) throws InvalidException, Exception {
		checkIsSelected(selectedIndex);													// check exception
		
		bookDB.remove(((GeneralUser)currentUser).getBookList().get(selectedIndex));		// remove the selected book from the database
		((GeneralUser)currentUser).deleteBook(selectedIndex);							// remove the selected book from the user book list
		
		file.writeFile(bookDB, "DB_book.txt");											// update file
		notifyObserver(((GeneralUser)currentUser).getBookList()); 						// update view
	}
	
	/*
	 * delete selected account (administrator)
	 */
	public void deleteAccount(int selectedIndex) throws InvalidException, Exception {
		checkValidAccount(selectedIndex);					// check exception
		
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
	public void modifyBook(String[] bookInfo, int selectedIndex) throws InvalidException, Exception {
		checkBookInfo(bookInfo);											// check exception
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
	public void changeUserState(int selectedIndex) throws InvalidException, Exception {
		checkIsSelected(selectedIndex);						// check exception
		
		GeneralUser user = (GeneralUser) accountDB.get(selectedIndex);
		if (user.isActivated())								// change selected user state
			user.setActivated(false);
		else
			user.setActivated(true);
		
		file.writeFile(accountDB, "DB_account.txt");		// update file
		notifyObserver(accountDB); 							// update view (table)
	}
	
	
	public void resetToSearchedIndex() {
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

	public GeneralUser getBookSeller(int index) throws InvalidException, Exception {
		for (User user : accountDB) {
			if (bookDB.get(index).getSellerID().equals(user.getID()))
				return (GeneralUser) user;
		}
		throw new InvalidException("There are no users matching the id of the user who registered the book!");
	}
	
	public int changeToOriginIndex(int selectedIndex) throws InvalidException {
		if (selectedIndex == -1)
			throw new InvalidException("Please select the row from the table!");
		return originIndex.get(selectedIndex);
	}
	
	/*
	 * exception handle
	 */
	public void checkCanModifyBook(int selectedIndex) throws InvalidException, Exception {
		int originalIndex = changeToOriginIndex(selectedIndex);
		
		if (bookDB.get(originalIndex).isSold())
			throw new InvalidException("You CANNOT modify a book that has already been sold.!");
	}
	
	private void checkIsSelected(int selectedIndex) throws InvalidException {
		if (selectedIndex == -1)
			throw new InvalidException("Please select the row from the table!");
	}
	
	private void checkUserInfo(String[] userInfo) throws InvalidException, Exception {	// input : ID, PW, name, phone number, email
		if (userInfo[0].isEmpty() || userInfo[1].isEmpty() || userInfo[2].isEmpty() || userInfo[3].isEmpty() || userInfo[4].isEmpty())
			throw new InvalidException("<html>INVALID INPUT<br>- All blanks must be written.</html>");
		for (User user : accountDB)
			if (userInfo[0].equals(user.getID()))
				throw new InvalidException("<html>INVALID INPUT<br>- Same 'ID' already exists.</html>");
		if (!userInfo[0].matches("[a-z A-Z 0-9]+"))
			throw new InvalidException("<html>INVALID INPUT<br>- 'ID' must consist of letters or numbers only.</html>");
		if (userInfo[1].length() < 8)
			throw new InvalidException("<html>INVALID INPUT<br>- 'Password' must be at least 8 digits long.</html>");
		if (!userInfo[2].matches("[a-z A-Z]+"))
			throw new InvalidException("<html>INVALID INPUT<br>- 'Name' can only be in English.</html>");
		if (!userInfo[3].matches("[0-9]+") || userInfo[3].length() != 11)
			throw new InvalidException("<html>INVALID INPUT<br>- 'Phone number' must be numeric and 11 digits long.</html>");
		if (!userInfo[4].contains("@"))
			throw new InvalidException("<html>INVALID INPUT<br>- 'Email' is in an invalid format. Input must include '@'.</html>");
	}

	private void checkValidBook(int originalIndex) throws InvalidException, Exception {
		GeneralUser bookSeller = getBookSeller(originalIndex);
		
		if (bookDB.get(originalIndex).isSold())
			throw new InvalidException("Already Sold out!");
		if (!bookSeller.isActivated())
			throw new InvalidException("Seller of the book is 'Deactivated'!");
		if (bookSeller.getID().equals(getCurrentUser().getID()))
			throw new InvalidException("You can't buy your own book!");
	}
	
	private void checkBookInfo(String[] bookInfo) throws InvalidException, Exception {	// input : title, author, publisher, publicationYear, ISBN, Price, Book State 
		if(bookInfo[0].isEmpty())
			throw new InvalidException("'Title' must be entered");
		if(!bookInfo[1].matches("[a-z A-Z]*"))
			throw new InvalidException("'Author' must be letters");
		if ((!bookInfo[3].isEmpty() && bookInfo[3].length() != 4) || !bookInfo[3].matches("[0-9]*"))
			throw new InvalidException("'Publication Year' must be a 4-digit number");
		if ((!bookInfo[4].isEmpty() && bookInfo[4].length() != 13) || !bookInfo[4].matches("[0-9]*"))
			throw new InvalidException("'ISBN' must be a 13-digit number");
		if (!bookInfo[5].matches("[0-9]*"))
			throw new InvalidException("'Price' must be a number");
	}
	
	private void checkValidAccount(int selectedIndex) throws InvalidException {
		checkIsSelected(selectedIndex);
		if (((GeneralUser)accountDB.get(selectedIndex)).isActivated())
			throw new InvalidException("'Activated' user cannot be deleted!");
	}
	
	/*
	 * getter, setter
	 */
	public void setOriginIndex(Vector<Integer> originIndex) {
		this.originIndex = originIndex;
	}
	public Vector<Integer> getOriginIndex() {
		return originIndex;
	}
	public Vector<Book> getBookDB(){
		return bookDB;
	}
	public Vector<User> getAccountDB(){
		return accountDB;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
