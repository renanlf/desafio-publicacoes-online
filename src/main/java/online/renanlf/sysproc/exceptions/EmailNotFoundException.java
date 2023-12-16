package online.renanlf.sysproc.exceptions;

public class EmailNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1059874468610465384L;

	public EmailNotFoundException(String email) {
		super("User not found for email " + email);
	}
}
