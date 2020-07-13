package junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import controller.InvalidValueException;
import model.Model;
import model.data.GeneralUser;

class TestExceptionCheck {

	private String[] userInfo1 = { "user1", "12345678", "Park", "01098765432", "user1@gmail.com" };
	private String[] userInfo2 = { "user2", "87654321", "Park", "01012345678", "user2@naver.com" };
	private String[] bookInfo1 = { "Title1", "Author", "Publisher1", "1000", "1111222233334", "30", "Excellent" };
	
	private InvalidValueException e;
	private Model model;
	
	@BeforeEach
	public void setUp() throws InvalidValueException, Exception {
		model = new Model();
		model.registerUser(userInfo1);
		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 2));
		model.registerBook(bookInfo1);
	}
	
	@AfterEach 
	public void tearDown() throws InvalidValueException, Exception {
		model.changeUserState(model.getAccountDB().size() - 2);
		model.deleteUser(model.getAccountDB().size() - 2);
	}
	
	@Test
	public void test_Authenticate() throws InvalidValueException, Exception {
		// true case
		model.authenticate("user1", "12345678");
		
		// invalid PW
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.authenticate("user1", "1234");
		});
		
		// invalid ID
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.authenticate("user2", "12345678");
		});
		
		// deactivated account
		model.changeUserState(model.getAccountDB().size() - 2);
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.authenticate("user1", "12345678");
		});
		model.changeUserState(model.getAccountDB().size() - 2);
	}
	
	@Test
	public void test_CheckValidBookForModify() throws InvalidValueException, Exception {
		// true case
		((GeneralUser) model.getCurrentUser()).getBookList().get(0).setSold(false);
		model.resetToInitalIndex();
		model.getExceptionCheck().checkValidBookForModify(model.getBookDB().size() - 1);
		
		// false case
		((GeneralUser) model.getCurrentUser()).getBookList().get(0).setSold(true);
		model.resetToInitalIndex();
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkValidBookForModify(model.getBookDB().size() - 1);
		});
		assertTrue(e.getMessage().contains("CANNOT modify"));
	}
	
	@Test
	public void test_CheckIsSelected() throws InvalidValueException, Exception {
		// true case
		model.getExceptionCheck().checkIsSelected(0);
		
		// false case
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkIsSelected(-1);
		});
		assertTrue(e.getMessage().contains("Please select"));
	}
	
	@Test
	public void test_CheckValidAccount() throws InvalidValueException, Exception {
		// false case
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkValidAccount(model.getAccountDB().size() - 2);
		});
		assertTrue(e.getMessage().startsWith("'Activated'"));
		
		// true case
		model.changeUserState(model.getAccountDB().size() - 2);
		model.getExceptionCheck().checkValidAccount(model.getAccountDB().size() - 2);
		model.changeUserState(model.getAccountDB().size() - 2);
	}
	
	@Test
	public void test_CheckValidBookForPurchase() throws InvalidValueException, Exception {
		int focusBookIndex = model.getBookDB().size() - 1;
		GeneralUser bookSeller = model.getBookSeller(focusBookIndex);
		// register user2 & set user2 to the current user
		model.registerUser(userInfo2);
		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 2));
		
		// true case (all if statements are true)
		model.resetToInitalIndex();
		model.getExceptionCheck().checkValidBookForPurchase(focusBookIndex);
		
		// 1st if statement
		model.getBookDB().get(focusBookIndex).setSold(true);
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkValidBookForPurchase(focusBookIndex);
		});
		assertTrue(e.getMessage().contains("Already Sold"));
		model.getBookDB().get(focusBookIndex).setSold(false);
		
		// 2nd if statement
		bookSeller.setActivated(false);
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkValidBookForPurchase(focusBookIndex);
		});
		assertTrue(e.getMessage().contains("Deactivated"));
		bookSeller.setActivated(true);
		
		// 3rd if statement
		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 3));
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkValidBookForPurchase(focusBookIndex);
		});
		assertTrue(e.getMessage().contains("own book"));
		
		// delete user2
		model.changeUserState(model.getAccountDB().size() - 2);
		model.deleteUser(model.getAccountDB().size() - 2);
	}
	
	@Test
	public void test_CheckUserInfo() throws InvalidValueException, Exception {
		// true case (all 'if' statements are true)
		String[] userInfo1 = new String[]{ "user2", "12345678", "Park", "01012345678", "user2@naver.com" };
		model.getExceptionCheck().checkUserInfo(userInfo1);
		
		// 1st 'if' statement
		String[] userInfo2 = { "", "12345678", "Park", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo2);
		});
		assertTrue(e.getMessage().startsWith("All"));
		
		String[] userInfo3 = new String[]{ "user2", "", "Park", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo3);
		});
		assertTrue(e.getMessage().startsWith("All"));
		
		String[] userInfo4 = new String[]{ "user2", "12345678", "", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo4);
		});
		assertTrue(e.getMessage().startsWith("All"));
		
		String[] userInfo5 = new String[]{ "user2", "12345678", "Park", "", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo5);
		});
		assertTrue(e.getMessage().startsWith("All"));
		
		String[] userInfo6 = new String[]{ "user2", "12345678", "Park", "01012345678", "" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo6);
		});
		assertTrue(e.getMessage().startsWith("All"));
		
		// 2nd 'if' statement
		String[] userInfo7 = new String[]{ "user1", "12345678", "Park", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo7);
		});
		assertTrue(e.getMessage().startsWith("Same 'ID'"));
		
		// 3rd 'if' statement
		String[] userInfo8 = new String[]{ "user2_", "12345678", "Park", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo8);
		});
		assertTrue(e.getMessage().startsWith("'ID'"));
		
		// 4th 'if' statement
		String[] userInfo9 = new String[]{ "user2", "1234", "Park", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo9);
		});
		assertTrue(e.getMessage().startsWith("'Password'"));
		
		// 5th 'if' statement
		String[] userInfo10 = new String[]{ "user2", "12345678", "Park12", "01012345678", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo10);
		});
		assertTrue(e.getMessage().startsWith("'Name'"));
		
		// 6th 'if' statement
		String[] userInfo12 = new String[]{ "user2", "12345678", "Park", "010123456", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo12);
		});
		assertTrue(e.getMessage().startsWith("'Phone number'"));
		
		String[] userInfo13 = new String[]{ "user2", "12345678", "Park", "010123456aa", "user2@naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo13);
		});
		assertTrue(e.getMessage().startsWith("'Phone number'"));
		
		// 7th 'if' statement
		String[] userInfo14 = new String[]{ "user2", "12345678", "Park", "01012345678", "naver.com" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkUserInfo(userInfo14);
		});
		assertTrue(e.getMessage().startsWith("'Email'"));
	}
	
	@Test
	public void test_CheckBookInfo() throws InvalidValueException, Exception {
		// true case
		String[] bookInfo = { "Title2", "Author", "Publisher2", "1001", "1111222233334", "20", "Good" };
		model.getExceptionCheck().checkBookInfo(bookInfo);
		
		// 1st if statement
		String[] bookInfo1 = { "", "", "", "", "", "", "" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo1);
		});
		assertTrue(e.getMessage().startsWith("'Title'"));
		
		// 2nd if statement
		String[] bookInfo2 = { "Title2", "Author2", "", "", "", "", "" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo2);
		});
		assertTrue(e.getMessage().startsWith("'Author'"));
		
		// 3rd if statement														// MCDC 
		String[] bookInfo3 = { "Title2", "Author", "", "2020a", "", "", "" };	// T T F F -> outcome: T
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo3);
		});
		assertTrue(e.getMessage().startsWith("'Publication Year'"));
		
		String[] bookInfo4 = { "Title2", "Author", "", "20aa", "", "", "" };	// T F T F -> outcome: T
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo4);
		});
		assertTrue(e.getMessage().startsWith("'Publication Year'"));
		
		String[] bookInfo5 = { "Title2", "Author", "", "0111", "", "", "" };	// T F F T -> outcome: T
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo5);
		});
		assertTrue(e.getMessage().startsWith("'Publication Year'"));
		
		String[] bookInfo6 = { "Title2", "Author", "", "2000", "", "", "" };	// T F F F -> outcome: F
		model.getExceptionCheck().checkBookInfo(bookInfo6);
		
		String[] bookInfo7 = { "Title2", "Author", "", "", "", "", "" };		// F T F F -> outcome: F
		model.getExceptionCheck().checkBookInfo(bookInfo7);
		
		// 4th if statement																// MCDC
		String[] bookInfo8 = { "Title2", "Author", "", "", "0123456789abc", "", "" };	// T F T -> outcome: T
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo8);
		});
		assertTrue(e.getMessage().startsWith("'ISBN'"));
		
		String[] bookInfo9 = { "Title2", "Author", "", "", "0123456789", "", "" };		// T T F -> outcome: T
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo9);
		});
		assertTrue(e.getMessage().startsWith("'ISBN'"));
		
		String[] bookInfo10 = { "Title2", "Author", "", "", "0123456789123", "", "" };	// T F F -> outcome: F
		model.getExceptionCheck().checkBookInfo(bookInfo10);
		
		String[] bookInfo11 = { "Title2", "Author", "", "", "", "", "" };				// F T F -> outcome: F
		model.getExceptionCheck().checkBookInfo(bookInfo11);
		
		// 5th if statement
		String[] bookInfo12 = { "Title2", "Author", "", "", "", "100a", "" };
		e = Assertions.assertThrows(InvalidValueException.class, () -> {
			model.getExceptionCheck().checkBookInfo(bookInfo12);
		});
		assertTrue(e.getMessage().startsWith("'Price'"));
	}
}
