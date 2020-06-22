package junit;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import controller.InvalidValueException;
import model.Model;
import model.data.GeneralUser;
import model.data.User;

public class ModelTest{
	
	String[] userInfo1 = { "user1", "12345678", "Park", "01098765432", "user1@gmail.com" };
	String[] userInfo2 = { "user2", "12345678", "Park", "01012345678", "user2@naver.com" };
	String[] bookInfo1 = { "Title1", "Author", "Publisher1", "1000", "1111222233334", "30", "Excellent" };
	String[] bookInfo2 = { "Title2", "Author", "Publisher2", "1001", "1111222233334", "20", "Good" };
	String[] bookInfo3 = { "Title3", "Author", "Publisher3", "1002", "1111222233334", "10", "Fair" };
	
	private Model model;
	
	int prevAccountDBNum;
	int prevBookDBNum;
	
	@BeforeEach
	public void setUp() throws InvalidValueException, Exception {
		
		model = new Model();
		prevAccountDBNum = model.getAccountDB().size();
		prevBookDBNum = model.getBookDB().size();
		
		model.registerUser(userInfo1);
		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 2));
	}
	@AfterEach
	public void tearDown() throws InvalidValueException, Exception {

		model.changeUserState(prevAccountDBNum - 1);
		model.deleteUser(prevAccountDBNum - 1);

		assertEquals(prevAccountDBNum, model.getAccountDB().size());
	}
	
	/*
	 *  Test book allocation to all users' bookList
	 *  - test whether the number of books registered in all users' bookList equals the number of books registered in bookDB
	 */
	@Test
	public void testConstructor() {
		int linkedBookNum = 0;
						
		for (User user : model.getAccountDB())
			if (user instanceof GeneralUser)
				linkedBookNum += ((GeneralUser) user).getBookList().size();
		
		assertEquals(prevBookDBNum, linkedBookNum);
	}
	
	@Test
	public void testRegisterUser( ) throws InvalidValueException, Exception {
		assertEquals("user1", model.getAccountDB().get(prevAccountDBNum - 1).getID());
		assertEquals(prevAccountDBNum + 1, model.getAccountDB().size());
		assertEquals(true, ((GeneralUser) model.getAccountDB().get(prevAccountDBNum - 1)).isActivated());
	}
	
	@Test
	public void testRegisterBook( ) throws InvalidValueException, Exception {
		model.registerBook(bookInfo1);
		model.registerBook(bookInfo2);
		
		assertEquals(prevBookDBNum + 2, model.getBookDB().size());
		assertEquals(2, ((GeneralUser) model.getCurrentUser()).getBookList().size());
		
		assertEquals("Title2", model.getBookDB().get(prevBookDBNum + 1).getTitle());
		assertEquals("user1", model.getBookDB().get(prevBookDBNum + 1).getSellerID());
		assertEquals(false, model.getBookDB().get(prevBookDBNum + 1).isSold());
	}
	
	@Test
	public void testPurchaseBook( ) throws InvalidValueException, Exception {
		model.registerBook(bookInfo1);
		
		model.registerUser(userInfo2);
		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 2));
		
		assertEquals(false, model.getBookDB().get(prevBookDBNum).isSold());
		model.resetToInitalIndex();
		model.purchaseBook(prevBookDBNum);
		assertEquals(true, model.getBookDB().get(prevBookDBNum).isSold());
		
		model.changeUserState(model.getAccountDB().size() - 2);
		model.deleteUser(model.getAccountDB().size() - 2);
	}
	
	@Test
	public void testDeleteBook_admin( ) throws InvalidValueException, Exception {
		model.registerBook(bookInfo1);		// register 2 books
		model.registerBook(bookInfo2);
		model.resetToInitalIndex();
		
		model.deleteBook_admin(prevBookDBNum + 1);		// delete a book
		assertEquals(prevBookDBNum + 1, model.getBookDB().size());
		assertEquals(1, ((GeneralUser) model.getCurrentUser()).getBookList().size());
		
		model.deleteBook_admin(prevBookDBNum);			// delete a book
		assertEquals(prevBookDBNum, model.getBookDB().size());
		assertEquals(0, ((GeneralUser) model.getCurrentUser()).getBookList().size());
	}
	
	@Test
	public void testDeleteBook_user( ) throws InvalidValueException, Exception {
		model.registerBook(bookInfo1);		// register 2 books
		model.registerBook(bookInfo2);
		model.resetToUserBookIndex();
		
		model.deleteBook_user(1);
		assertEquals(prevBookDBNum + 1, model.getBookDB().size());
		assertEquals(1, ((GeneralUser) model.getCurrentUser()).getBookList().size());
		
		model.deleteBook_user(0);
		assertEquals(prevBookDBNum, model.getBookDB().size());
		assertEquals(0, ((GeneralUser) model.getCurrentUser()).getBookList().size());
	}
	
	@Test
	public void testMdifyBook( ) throws InvalidValueException, Exception {
		model.registerBook(bookInfo1);
		model.resetToUserBookIndex();
		assertEquals("Title1", ((GeneralUser) model.getCurrentUser()).getBookList().get(0).getTitle());
		
		model.modifyBook(bookInfo2, 0);
		assertEquals("Title2", ((GeneralUser) model.getCurrentUser()).getBookList().get(0).getTitle());
	}
	
	@Test
	public void testChangeUserState( ) throws InvalidValueException, Exception {
		assertEquals(true, ((GeneralUser) model.getAccountDB().get(prevAccountDBNum - 1)).isActivated());
		model.changeUserState(prevAccountDBNum - 1);
		assertEquals(false, ((GeneralUser) model.getAccountDB().get(prevAccountDBNum - 1)).isActivated());
		model.changeUserState(prevAccountDBNum - 1);
		assertEquals(true, ((GeneralUser) model.getAccountDB().get(prevAccountDBNum - 1)).isActivated());
	}
	
	@Test
	public void testDeleteUser( ) throws InvalidValueException, Exception {
		model.registerUser(userInfo2);		// register user2
		int indexOfUser2 = model.getAccountDB().size() - 2;
		
		model.setCurrentUser(model.getAccountDB().get(indexOfUser2));	// currentUser: user2
		
		model.registerBook(bookInfo1);		// register 2 books
		model.registerBook(bookInfo2);

		assertEquals(prevBookDBNum + 2, model.getBookDB().size());
		model.changeUserState(indexOfUser2);
		model.deleteUser(indexOfUser2);
		assertEquals(prevBookDBNum, model.getBookDB().size());
		assertEquals(prevAccountDBNum + 1, model.getAccountDB().size());
	}
}
