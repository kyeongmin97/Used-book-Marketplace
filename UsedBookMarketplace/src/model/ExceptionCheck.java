package model;

import controller.InvalidValueException;
import model.data.GeneralUser;
import model.data.User;

public class ExceptionCheck {
	private Model model;
	
	public ExceptionCheck(Model model) {
		this.model = model;
	}
	
	public void checkValidBookForModify(int selectedIndex) throws InvalidValueException, Exception {
		int originalIndex = model.changeToOriginIndex(selectedIndex);
		
		if (model.getBookDB().get(originalIndex).isSold())
			throw new InvalidValueException("You CANNOT modify a book that has already been sold!");
	}
	
	public void checkIsSelected(int selectedIndex) throws InvalidValueException {
		if (selectedIndex == -1)
			throw new InvalidValueException("Please select the row from the table!");
	}
	
	public void checkValidAccount(int selectedIndex) throws InvalidValueException {
		checkIsSelected(selectedIndex);
		if (((GeneralUser)model.getAccountDB().get(selectedIndex)).isActivated())
			throw new InvalidValueException("'Activated' user cannot be deleted!");
	}

	public void checkValidBookForPurchase(int originalIndex) throws InvalidValueException, Exception {
		GeneralUser bookSeller = model.getBookSeller(originalIndex);
		
		if (model.getBookDB().get(originalIndex).isSold())
			throw new InvalidValueException("Already Sold out!");
		if (!bookSeller.isActivated())
			throw new InvalidValueException("Seller of the book is 'Deactivated'!");
		if (bookSeller.getID().equals(model.getCurrentUser().getID()))
			throw new InvalidValueException("You can't buy your own book!");
	}
	
	public void checkUserInfo(String[] userInfo) throws InvalidValueException, Exception {	// input : ID, PW, name, phone number, email
		if (userInfo[0].isEmpty() || userInfo[1].isEmpty() || userInfo[2].isEmpty() || userInfo[3].isEmpty() || userInfo[4].isEmpty())
			throw new InvalidValueException("All blanks must be written.");
		for (User user : model.getAccountDB())
			if (userInfo[0].equals(user.getID()))
				throw new InvalidValueException("Same 'ID' already exists.");
		if (!userInfo[0].matches("[a-z A-Z 0-9]+"))
			throw new InvalidValueException("'ID' must consist of letters or numbers only.");
		if (userInfo[1].length() < 8)
			throw new InvalidValueException("'Password' must be at least 8 digits long.");
		if (!userInfo[2].matches("[a-z A-Z]+"))
			throw new InvalidValueException("'Name' can only be in English.");
		if (!userInfo[3].matches("[0-9]+") || userInfo[3].length() != 11)
			throw new InvalidValueException("'Phone number' must be numeric and 11 digits long.");
		if (!userInfo[4].contains("@"))
			throw new InvalidValueException("'Email' is in an invalid format. Input must include '@'.");
	}
	
	public void checkBookInfo(String[] bookInfo) throws InvalidValueException, Exception {	// input : title, author, publisher, publicationYear, ISBN, Price, Book State 
		if(bookInfo[0].isEmpty())
			throw new InvalidValueException("'Title' must be entered");
		if(!bookInfo[1].matches("[a-z A-Z]*"))
			throw new InvalidValueException("'Author' must be letters");
		if ((!bookInfo[3].isEmpty() && bookInfo[3].length() != 4) || !bookInfo[3].matches("[0-9]*") || bookInfo[3].startsWith("0"))
			throw new InvalidValueException("'Publication Year' must be a 4-digit number");
		if ((!bookInfo[4].isEmpty() && bookInfo[4].length() != 13) || !bookInfo[4].matches("[0-9]*"))
			throw new InvalidValueException("'ISBN' must be a 13-digit number");
		if (!bookInfo[5].matches("[0-9]*"))
			throw new InvalidValueException("'Price' must be a number");
	}
}
