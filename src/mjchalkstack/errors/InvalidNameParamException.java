package mjchalkstack.errors;

@SuppressWarnings("serial")
public class InvalidNameParamException extends RuntimeException {
	
	public InvalidNameParamException(String addedMsg){
		super("Error: Invalid Parameter Name it must be labeled \"Name\""
				+ ", \"Nombre\" or \"First and Last Name\" " + addedMsg);
	}
}
