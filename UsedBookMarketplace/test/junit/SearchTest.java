package junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.InvalidValueException;
import model.Model;
import view.View;
import view.GeneralUser.SearchBookUI_General;

class SearchTest {

	private Controller controller;
	private Model model;
	private View view;
	
	String[] userInfo1 = { "user1", "12345678", "Park", "01098765432", "current@gmail.com" };
	String[] userInfo2 = { "user2", "12345678", "Park", "01012345678", "kio1015@naver.com" };
	String[] bookInfo1 = { "Title1", "Author", "Publisher1", "1000", "1111222233334", "30", "Excellent" };
	String[] bookInfo2 = { "Title2", "Author", "Publisher2", "1001", "1111222233334", "20", "Good" };
	String[] bookInfo3 = { "Title3", "Author", "Publisher3", "1002", "1111222233334", "10", "Fair" };	
	
	@Test
	void testSearch() throws InvalidValueException, Exception {
//		controller = new Controller();
//		model = controller.getModel();
//		view = controller.getView();
//		view.setCurrentTableUI(new SearchBookUI_General(null));
//		view.setSearchBookUI(new SearchBookUI_General(null));
//		
//		model.registerUser(userInfo1);
//		model.setCurrentUser(model.getAccountDB().get(model.getAccountDB().size() - 2));
//		model.registerBook(bookInfo1);
//		
//		view.getSearchBookUI().getSellerIdRBtn().doClick();
//		view.getSearchBookUI().setSearchTxt("Title1");
//		view.getSearchBookUI().getSearchBtn().doClick();
//		
//		assertEquals(1, model.getOriginIndex().size());
//		assertEquals(model.getBookDB().size() - 1, model.getOriginIndex().get(0));
//		
//		model.changeUserState(model.getAccountDB().size() - 2);
//		model.deleteUser(model.getAccountDB().size() - 2);
	}
}
