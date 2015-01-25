package mjchalkstack.errors;

@SuppressWarnings("serial")
public class UserNotCreatedException extends RuntimeException {
	
	public UserNotCreatedException(String msg){
		super(msg);
	}
}
