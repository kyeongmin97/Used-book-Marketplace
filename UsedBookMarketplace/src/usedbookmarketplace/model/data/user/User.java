package usedbookmarketplace.model.data.user;

import usedbookmarketplace.model.data.Book;

public abstract class User {
	protected String id;
	protected String pw;
	
	public String getID() {
		return id;
	}
	public String getPW() {
		return pw;
	}
	
	public abstract String[] getUserInfo();
}
