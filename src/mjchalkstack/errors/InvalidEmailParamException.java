package mjchalkstack.errors;

@SuppressWarnings("serial")
public class InvalidEmailParamException extends RuntimeException {
	
	public InvalidEmailParamException(String addedMsg){
		super("Error: Invalid Parameter Email it must be labeled \"Email\""
				+ ", \"Correo-e\" or \"Email Address\" " + addedMsg);
	}
}
