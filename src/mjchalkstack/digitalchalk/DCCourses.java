/**
 * DCCourses.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 21th, 2014
 * Description: This Class has all the methods referring to the Courses.
 */
package mjchalkstack.digitalchalk;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import mjchalkstack.email.EmailSender;
import mjchalkstack.errors.UserNotRegisteredException;
import mjchalkstack.formstack.FormStackInfo;

public class DCCourses {
	
	/**
	 * Registers a User into a Course and returns the Response from the server
	 * of DigitalChalk if it was successful or not.
	 * @param info : FormStackInfo
	 * @return the HttpResponse of Registering the User from the DigitalChalk Server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void registerUserToCourse(FormStackInfo info) 
													throws ClientProtocolException, IOException{
		//Retrieves the User's ID
		String userID = DCUser.getUserID(info);
		
		//Starts service to register the User to a Course
		DCApiService service = new DCApiService();
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("userId", userID);
			jsonObj.put("offeringId", info.getOfferingID());
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		HttpResponse response = service.apiPost("/dc/api/v5/registrations", jsonObj);
		
		try {
			VerifyIfRegistrationError(response, info);
		} catch (ParseException e) {} catch (org.json.JSONException e) {}
		
	}
	
	/**
	 * Verifies if the User was registered or not. If there was any errors it will throw
	 * an UserNotRegisteredException.
	 * @param registeredResponse : HttpResponse
	 * @param info : FormStackInfo
	 * @throws ParseException
	 * @throws IOException 
	 * @throws org.json.JSONException 
	 * @throws UserNotRegisteredException
	 */
	public static void VerifyIfRegistrationError(HttpResponse registeredResponse, 
							FormStackInfo info) throws ParseException, IOException, org.json.JSONException{
		int registeredCode = registeredResponse.getStatusLine().getStatusCode();
		if(registeredCode != 201){ //If status is not 201 then the user was not registered

			//Recovers cause of the error
			HttpEntity entity = registeredResponse.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			
			//Sends an Email to the User informing of an error.
			EmailSender.sendToUserErrorMail(info);
			
			//Error Exception
			throw new UserNotRegisteredException("Failed to Register "
					+ "the User " + info.getFirstName() + " " + info.getLastName() + " "
					+ "HTTP Error Code: " + registeredCode + " "
					+ responseString + " to the Course with ID of: " 
					+ info.getOfferingID() + " and Course Title: " + info.getCourseTitle());
		}
	}
	
	/**
	 * Returns a List with the ID and Titles of all the available Course 
	 * Offerings in your DigitalChalk account.
	 * @return a List<HashMap> containing the result of the GET Method containing
	 * all the offering id, titles and details.
	 */
	@SuppressWarnings("rawtypes")
	public static List<HashMap> getCourseOfferings(){
		DCApiService service = new DCApiService();
		Map<String, String> paramLimit = new HashMap<String, String>();
		
		//Maximizing the results of all the Offerings offered by our account
		paramLimit.put("limit", "100");
		DCApiResult result = service.apiGet("/dc/api/v5/offerings", paramLimit);
		
		
		return result.getResults();
		
	}
}
