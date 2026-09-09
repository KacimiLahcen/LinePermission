package ma.youcode.lineperm.model;


public class User {
	
	private final String login;
	private final String hashedPassword;

	public User(String login, String hashedPassword) {
		
		this.login = login;
		this.hashedPassword = hashedPassword;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
                return hashedPassword;
        }
}
