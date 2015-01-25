package mjchalkstack.errors;

@SuppressWarnings("serial")
public class InvalidCourseIdParamException extends RuntimeException {
	
	public InvalidCourseIdParamException(String addedMsg){
		super("Error: Invalid Parameter Name it must be labeled \"Course ID\""
				+ ", \"Course Id\" or \"Course id\" " + addedMsg);
	}
}
