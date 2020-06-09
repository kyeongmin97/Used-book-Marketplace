package usedbookmarketplace.model.data.user;

public class Admin extends User {
	
	public Admin(String[] tokens) {
		super.id = tokens[0];
		super.pw = tokens[1];
	}
	
	public String[] getUserInfo() {
		String[] userInfo = new String[2];
		
		userInfo[0] = id;
		userInfo[1] = pw;
		
		return userInfo;
	}
}
