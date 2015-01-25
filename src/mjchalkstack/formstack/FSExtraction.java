/**
 * FSExtraction.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 15th, 2014
 * Description: This class contains methods to put all of FormStack's relevant
 * information for Digital Chalk into a single Object
 */
package mjchalkstack.formstack;


public class FSExtraction {
	
	/**
	 * Creates a FormStackInfo Object containing First name, Last Name, Email,
	 * Course Offering ID and Course Title.
	 * @param name : String
	 * @param email : String
	 * @param lastName : String
	 * @param offeringID : String
	 * @param courseTitle : String
	 * @return a FormStackInfo Object
	 */
	public static FormStackInfo makeFormStackObject(String name, String email, String lastName,
													String offeringID, String courseTitle){
		if(lastName == null)
			return DivideNameAndParseFormInfo(name, email, offeringID, courseTitle);
		else
			return new FormStackInfo(name, lastName, email, offeringID, courseTitle);
	}
	
	/**
	 * This extracts the First Name and Last Name of the User from the 
	 * String Name parameter and returns a FormStackInfo Object 
	 * which contains the First Name, Last Name, Email
	 * and the Offering ID of the Digital Chalk platform
	 * @param name : String
	 * @param email : String
	 * @param offeringID : String
	 * @param courseTitle : String
	 * @return FormStack Info Object
	 */
	private static FormStackInfo DivideNameAndParseFormInfo(String name, String email, 
														String offeringID, String courseTitle) {
		//Indexes for Name Extraction
		int startFirstNameIdx = 8;
		int endFirstNameIdx = name.indexOf("last") - 1;
		int startLastNameIdx = endFirstNameIdx + 8;
		
		//Extraction of name
		String firstName = name.substring(startFirstNameIdx, endFirstNameIdx);
		String lastName = name.substring(startLastNameIdx);
		
		return new FormStackInfo(firstName, lastName, email, offeringID, courseTitle);
	}
	
	
	
	
}


