/**
 * RegisterServlet.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 21th, 2014
 * Description: a Java Servlet class that processes HTTP Posts from FormStack and
 * creates and registers Users to Digital Chalk Courses
 */
package mjchalkstack.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mjchalkstack.digitalchalk.*;
import mjchalkstack.email.*;
import mjchalkstack.errors.*;
import mjchalkstack.formstack.*;
import mjchalkstack.salesforce.SalesForceProcedure;

import org.json.JSONException;



@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	private static final String HANDSHAKE_KEY = "YOUR_HANDSHAKE_KEY";
	
	/**
	 * Captures an HTTP GET request
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}
	
	/**
	 * Captures and processes an HTTP Post request
	 * @throws IOException 
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ServletContext context = getServletContext(); //For Extracting Text File
		try {
			if(req.getParameter("HandshakeKey").equals(HANDSHAKE_KEY)){
				//Phase 1: Extraction of Information
				//Returns the value of the Parameters
				String name = ParamValidator.validateNameParameter(req);
				String email = ParamValidator.validateEmailParameter(req);
				String courseID = ParamValidator.validateCourseIdParameter(req);
				
				//Verify if any Parameter Errors
				ParamValidator.VerifyIfParamErrors(name, email, courseID);
				
				//Checks if there is a last name parameter separately
				String lastName = ParamValidator.CheckRequestLastName(req);

				//Here we put all the information we get from FormStack into one Object
				FormStackInfo info = FSExtraction.makeFormStackObject(name, email, lastName, 
													courseID, req.getParameter("Course Title"));
				
				//Phase 2: Verification of User
				if(!DCUser.userExist(info)){
					//Creates the User and verifies if there are any errors
					DCUser.createUser(info);
				} else {
					//Update Account Password.
					DCUser.updateUserPassword(info, context); 
				}
				
				//Phase 3: Registration of User
				//Registers the User into course and verify if there are any errors
				DCCourses.registerUserToCourse(info);
				
				//Phase 4: Confirmation Email
				//Every Step was Successful
				resp.setStatus(200);
				EmailSender.sendSuccessMail(info, context);
				//Creates a New Case in SalesForce
				SalesForceProcedure.caseProcedure(req, resp, info, context); 
			}else{
				//Handshake Key is not valid.
				resp.getWriter().println("Error: 401 Unauthorized");
				resp.setStatus(401);
			}
		}catch (UserNotCreatedException userCreationError) {
			try {
				EmailSender.sendErrorMail(userCreationError.getMessage(), context);
			} catch (JSONException e) {}
			
		}catch(UserNotRegisteredException userRegistrationError){
			try {
				EmailSender.sendErrorMail(userRegistrationError.getMessage(), context);
			} catch (JSONException e) {}
			
		}catch(InvalidNameParamException invalidNameParam){
			try {
				EmailSender.sendErrorMail(invalidNameParam.getMessage(), context);
			} catch (JSONException e) {}
			
		}catch(InvalidEmailParamException invalidEmailParam){
			try {
				EmailSender.sendErrorMail(invalidEmailParam.getMessage(), context);
			} catch (JSONException e) {}
			
		}catch(InvalidCourseIdParamException invalidCourseParam){
			try {
				EmailSender.sendErrorMail(invalidCourseParam.getMessage(), context);
			} catch (JSONException e) {}
			
		}catch(Exception e){
			try {
				EmailSender.sendErrorMail(e.getMessage(), context);
			} catch (JSONException e1) {}
		}
	}
	
}
