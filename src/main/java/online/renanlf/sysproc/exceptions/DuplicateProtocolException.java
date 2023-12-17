package online.renanlf.sysproc.exceptions;

public class DuplicateProtocolException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9077405807523378529L;

	public DuplicateProtocolException(String protocol) {
		super("Protocolo " + protocol + " já existe!");
	}
	
	

}
