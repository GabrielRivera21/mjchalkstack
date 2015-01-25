/**
 * ParamValidator.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 15th, 2014
 * Description: This class tries to extract the different parameter values with 
 * different keys for the same thing and verifies if there are any parameter names error, meaning
 * that if it couldn't extract a value with the stored parameter names it will throw
 * an exception when it verifies if it extracted the parameters.
 */
package mjchalkstack.servlets;

import javax.servlet.http.HttpServletRequest;

import mjchalkstack.errors.*;

public class ParamValidator {
	
	/**
	 * This method verifies if we successfully extracted the Name, Email and Course ID parameter
	 * values, if we did not recover it and it is null it will throw an 
	 * InvalidNameParamException first if it was the name parameter, an 
	 * InvalidEmailParamException if it was the Email parameter or an InvalidCourseIDParamException
	 * if it was the Course ID.
	 * @param name : String
	 * @param email : String
	 * @param courseID : String
	 * @throws InvalidNameParamException
	 * @throws InvalidEmailParamException
	 * @throws InvalidCourseIdParamException
	 */
	protected static void VerifyIfParamErrors(String name, String email, String courseID){
		if(name == null) //Throws an Error if parameter name was wrong
			throw new InvalidNameParamException("Form From Course ID: " + courseID + " Email: " + email); 

		if(email == null) //Throws an Error if parameter name was wrong
			throw new InvalidEmailParamException("Form From Course ID: " + courseID + " Name: " + name);
		
		if(courseID == null) //Throws an Error if parameter name was wrong
			throw new InvalidCourseIdParamException("Name: " + name + " Email: " + email);
	}
	
	/**
	 * This method starts requesting different parameter "keys" for the Name parameter
	 * until it extracts a value. If the parameter keys are all invalid it will return
	 * null.
	 * @param req : HttpServletRequest
	 * @return a String with the Value of the Name Parameter or null if it doesn't exist.
	 */
	protected static String validateNameParameter(HttpServletRequest req){
		String[] nameValidator = {"Name", "Nombre", "First and Last Name"};
		String name = null;
		int idx = 0;
		while(name == null && idx < nameValidator.length)
			name = req.getParameter(nameValidator[idx++]);
		
		return name;
	}
	
	/**
	 * This method requests for the parameter "Apellido" if it exists it will return
	 * the value of "Apellido". If it doesn't exist it will return null.
	 * @param req : HttpServletRequest
	 * @return a String with the Value of "Apellido" parameter or null if it doesn't exist.
	 */
	protected static String CheckRequestLastName(HttpServletRequest req){
		String[] lastNameValidator = {"Apellido", "Last Name"};
		String lastName = null;
		int idx = 0;
		while(lastName == null && idx < lastNameValidator.length)
			lastName = req.getParameter(lastNameValidator[idx++]);
		
		return lastName;
	}
	
	/**
	 * This method starts requesting different parameter "keys" for the Email parameter
	 * until it extracts a value. If the parameter keys are all invalid it will return
	 * null.
	 * @param req : HttpServletRequest
	 * @return a String with the Value of the Email Parameter or null if it doesn't exist
	 */
	protected static String validateEmailParameter(HttpServletRequest req){
		String[] emailParamValidator = {"Email", "Email Address", "Correo-e"};
		int idx = 0;
		String email = null;
		while(email == null && idx < emailParamValidator.length)
			email = req.getParameter(emailParamValidator[idx++]);
		
		return email;
	}
	
	/**
	 * This method starts requesting different parameter "keys" for the Course ID parameter
	 * until it extracts a value. If no value is extracted from the keys it will return null.
	 * @param req : HttpServletRequest
	 * @return a String with the Value of the Course ID Parameter or null if it doesn't exist
	 */
	protected static String validateCourseIdParameter(HttpServletRequest req) {
		String[] courseIdParamValidator = {"Course ID", "Course Id", "Course id"};
		int idx = 0;
		String courseID = null;
		while(courseID == null && idx < courseIdParamValidator.length)
			courseID = req.getParameter(courseIdParamValidator[idx++]);
		
		return courseID;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String validateBirthParam(HttpServletRequest req) {
		String[] birthMonthValidator = {"Mes de cumpleaños", "Birthday Month", "Mes de Cumpleaños"};
		int idx = 0;
		String birthMonth = null;
		while(birthMonth == null && idx < birthMonthValidator.length)
			birthMonth = req.getParameter(birthMonthValidator[idx++]);
		
		return birthMonth;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String validateTSPRParam(HttpServletRequest req){
		String[] tsprValidator = {"TSPR #", "# Del TSPR", "# del TSPR", "#TSPR", "# TSPR"};
		int idx = 0;
		String TSPRNum = null;
		while(TSPRNum == null && idx < tsprValidator.length)
			TSPRNum = req.getParameter(tsprValidator[idx++]);
		
		return TSPRNum;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String validatePhoneParam(HttpServletRequest req){
		String[] phoneValidator = {"Phone Number", "Telephone", "Numero de Telefono", "Teléfono",
									"Phone", "Phone#", "Phone #"};
		int idx = 0;
		String phoneNum = null;
		while(phoneNum == null && idx < phoneValidator.length)
			phoneNum = req.getParameter(phoneValidator[idx++]);
		
		return phoneNum;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String validatePriceParam(HttpServletRequest req){
		String[] priceValidator = {"Precio", "Price"};
		int idx = 0;
		String price = null;
		while(price == null && idx < priceValidator.length)
			price = req.getParameter(priceValidator[idx++]);

		return price;
	}
}
