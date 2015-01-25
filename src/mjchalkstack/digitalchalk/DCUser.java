/**
 * DCUser.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 21th, 2014
 * Description: This class has all the methods referring to the User of Digital Chalk.
 */
package mjchalkstack.digitalchalk;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONException;

import mjchalkstack.email.EmailSender;
import mjchalkstack.errors.UserNotCreatedException;
import mjchalkstack.formstack.FormStackInfo;

public class DCUser {
	
	/**
	 * Returns True if the User already exists in DigitalChalk or False if there is no user
	 * with that email address in DigitalChalk
	 * @param info
	 * @return True if User exists in DigitalChalk or False if he doesn't exist in DigitalChalk.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean userExist(FormStackInfo info) {
		//Service Digital Chalk API Start up
		DCApiService service = new DCApiService();
		Map<String, String> paramEmail = new HashMap<String, String>();
		
		//Checking by Email
		paramEmail.put("email", info.getEmail());
		DCApiResult result = service.apiGet("/dc/api/v5/users", paramEmail);
		List<HashMap> user = result.getResults();
		
		//Returns False if there wasn't any email.
		try{
			return user.get(0).get("email").equals(info.getEmail());
		}catch(IndexOutOfBoundsException e){
			return false;
		}
		
	}
	
	/**
	 * Returns the User ID of the User in DigitalChalk
	 * @param info : FormStackInfo
	 * @return a String containing the User ID of Digital Chalk
	 */
	@SuppressWarnings("rawtypes")
	protected static String getUserID(FormStackInfo info){
		//Service Digital Chalk API Start up
		DCApiService service = new DCApiService();
		Map<String, String> paramEmail = new HashMap<String, String>();
		
		//Checking by Email
		paramEmail.put("email", info.getEmail());
		DCApiResult result = service.apiGet("/dc/api/v5/users", paramEmail);
		List<HashMap> user = result.getResults();
		
		return (String) user.get(0).get("id");
	}
	
	/**
	 * Creates a User inside the DigitalChalk platform and returns the response
	 * from DigitalChalk if it was successful or if there was any errors.
	 * @param info
	 * @return an HttpResponse containing the response from the server of DigitalChalk
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void createUser(FormStackInfo info) 
										throws ClientProtocolException, IOException {
		DCApiService service = new DCApiService();
		
		String password = passwordMaker(info);
		info.setPassword(password);
		
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("firstName", info.getFirstName());
			jsonObj.put("lastName", info.getLastName());
			jsonObj.put("email", info.getEmail());
			jsonObj.put("password", password);
			jsonObj.put("locale", "es");
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = service.apiPost("/dc/api/v5/users", jsonObj);
		
		try {
			verifyIfUserCreationError(response, info);
		} catch (ParseException e) {} catch (org.json.JSONException e) {}
		
	}
	
	/**
	 * Verifies if there was any error when creating the User and if there was
	 * an error it will throw a UserNotCreatedException.
	 * @param userCreatedResponse : HttpResponse
	 * @param info : HttpResponse
	 * @throws ParseException
	 * @throws IOException
	 * @throws JSONException 
	 * @throws org.json.JSONException 
	 * @throws UserNotCreatedException
	 */
	public static void verifyIfUserCreationError(HttpResponse userCreatedResponse,
														FormStackInfo info) 
														throws ParseException, IOException, org.json.JSONException{
		//Verify if user was successfully created
		int createdCode = userCreatedResponse.getStatusLine().getStatusCode();
		if(createdCode != 201){
			
			//Recovers cause of the Error
			HttpEntity entity = userCreatedResponse.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			
			//Sends an Email to the User informing of an error.
			EmailSender.sendToUserErrorMail(info);
			
			//Error Exception
			throw new UserNotCreatedException("Failed to Create the "
					+ "User " + info.getFirstName() + " " + info.getLastName() 
					+ "\n\n HTTP Error Code: " + createdCode + " " + responseString
					+ "\n\nyou need to create and register this User to the following"
					+ " Course with ID of: " + info.getOfferingID() + " and Course Title: "
					+ info.getCourseTitle());
		}
	}
	
	/**
	 * Updates an Account User's password if the user already has an account. If
	 * it failed to update the password it will notify the Staff of Microjuris.com
	 * Reason: So the password that passwordMaker does is the current password
	 * for the account.
	 * @param info : FormStackInfo
	 * @param context : ServletContext
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws org.json.JSONException
	 */
	public static void updateUserPassword(FormStackInfo info, ServletContext context) throws ClientProtocolException, IOException, org.json.JSONException {
		DCApiService service = new DCApiService();
		String userID = getUserID(info);
		
		String password = passwordMaker(info);
		info.setPassword(password);
		
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("password", password);
		}catch(JSONException e){}
		
		HttpResponse response = service.apiPut("/dc/api/v5/users/" + userID, jsonObj);
		
		int success = response.getStatusLine().getStatusCode();
		
		if(success != 204){
			//Recovers cause of the Error
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			
			//Sends an email to the staff saying that password was not updated
			EmailSender.sendErrorMail("User already has account, "
					+ "but failed to update his password: " + responseString, context);
		}
	}
	
	/**
	 * Creates the Password for the User in DigitalChalk.
	 * @param info : FormStackInfo
	 * @return a String which is used for the password field in DigitalChalk.
	 */
	private static String passwordMaker(FormStackInfo info) {
		String password = info.getLastName() + "1";
		
		if(password.length() < 6)
			password = info.getFirstName() + info.getLastName() + "1";
		
		password = password.toLowerCase();
		password = removeAccentedCharacters(password);
		
		//Removes WhiteSpaces and Special Characters
		password = password.replaceAll("[\\W\\s]+", ""); 
		
		//In case the password still doesn't meet requirements add 1s to the password
		//Note: This rarely occurs.
		while(password.length() < 6)
			password += "1";
		
		return password;
	}
	
	/**
	 * Transforms any accented letters to normal ASCII letters
	 * @param password : String
	 * @return the password without any special letters or accented letters
	 */
	private static String removeAccentedCharacters(String password) {
		StringBuilder sb = new StringBuilder(password.length());
		password = Normalizer.normalize(password, Normalizer.Form.NFD);
		for (char c : password.toCharArray()) {
			if (c <= '\u007F') sb.append(c);
		}
		return sb.toString();
	}
	
	
		
}
