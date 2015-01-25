/**
 * SFContact.java
 * @author Gabriel
 * Date: 31/July/2014
 * Description: This class contains all the methods to Search, Create and Get the Contact
 * from SalesForce.
 */
package mjchalkstack.salesforce;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mjchalkstack.formstack.FormStackInfo;

public class SFContact {
	
	/**
	 * Performs a Search in the SalesForce Organization using the Email Address
	 * extracted from FormStack and retrieves it's Contact ID.
	 * @param info : FormStackInfo
	 * @return the Contact's ID
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected static String searchForContact(FormStackInfo info) throws ClientProtocolException, IOException{
		SFApiService service = new SFApiService();
		Map<String, String> emailParam = new HashMap<String, String>();
		emailParam.put("q", "Find {" + info.getEmail() + "}");
		HttpResponse searchResponse = service.apiGet("/services/data/v30.0/search/", emailParam);
		
		int searchCode = searchResponse.getStatusLine().getStatusCode();
		String responseString = EntityUtils.toString(searchResponse.getEntity());
		String userID = null;
		if (searchCode == 200 ) {
			try {
				JSONArray json = new JSONArray(responseString);
				int idx = 0;
				do{
					userID = json.getJSONObject(idx++).getString("Id");
				}while(!isSameUser(info, userID, service) && idx < json.length());
			} catch (JSONException je) {}
		}
		return userID;
	}
	
	/**
	 * This verifies that the extracted Contact ID is contains the same Email Address
	 * in it's field meaning that it will be the same User.
	 * @param info : FormStackInfo
	 * @param userID : String
	 * @param service : String
	 * @param resp : String
	 * @return True if it retrieves the same Email that it used for Search else False.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static boolean isSameUser(FormStackInfo info, String userID, SFApiService service) throws ClientProtocolException, IOException{
		HttpResponse contactResponse = service.apiGet("/services/data/v30.0/sobjects/Contact/" + userID, null);
		String responseString = EntityUtils.toString(contactResponse.getEntity());
		JSONObject json;
		String email = null;
		try {
			json = new JSONObject(responseString);
			email = json.getString("Email");
		} catch (JSONException e) {
			return false;
		}
		
		return email.equals(info.getEmail());
		
	}
	
	/**
	 * This method creates a Contact on the SalesForce Organization.
	 * @param info : FormStackInfo
	 * @param accountID : String
	 * @param TSPRNum : String
	 * @param PhoneNum : String
	 * @param BirthMonth : String
	 * @return the Contact's ID 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected static String createContact(FormStackInfo info, 
						String accountID, String TSPRNum, String PhoneNum, String BirthMonth) 
						throws ClientProtocolException, IOException {
		SFApiService service = new SFApiService();
		JSONObject json = new JSONObject();
		try{
			json.put("FirstName", info.getFirstName());
			json.put("LastName", info.getLastName());
			json.put("AccountId", accountID);
			json.put("Email", info.getEmail());
			json.put("BirthMonth__c", BirthMonth); //TODO: Edit this field to the name(key) 
			json.put("TSPR__c", TSPRNum); //TODO: Edit this field to the name(key) 
			json.put("Phone", PhoneNum); //TODO: Edit this field to the name(key)
		}catch(JSONException e){e.printStackTrace();}
		
		HttpResponse response = service.apiPost("/services/data/v30.0/sobjects/Contact", json);
		String responseString = EntityUtils.toString(response.getEntity());
		String contactID = null;
		try {
			json = new JSONObject(responseString);
			contactID = json.getString("id");
		} catch (JSONException e) {}
		
		return contactID;
	}
	
	
	
	/**
	 * Retrieves the "Cursos" Account ID from our organization in SalesForce
	 * @return Cursos Account ID from SalesForce
	 * @throws IOException
	 */
	protected static String getAccountID(HttpServletResponse resp) throws IOException {
		SFApiService service = new SFApiService();
		Map<String, String> queryParam = new HashMap<String, String>();
		queryParam.put("q", "Select id,name From Account");
		HttpResponse response = service.apiGet("/services/data/v30.0/query", queryParam);
		String responseString = EntityUtils.toString(response.getEntity());
		resp.getWriter().println(responseString); //TODO: remove this line for production
		String accountID = null;
		try{
			JSONObject json = new JSONObject(responseString);
			int index = 0;
			
			String accountName;
			do{ //Not really necessary if you have the Account ID for Cursos of SalesForce, but this will always find it.
				accountID = json.getJSONArray("records").getJSONObject(index).getString("Id");
				accountName = json.getJSONArray("records").getJSONObject(index++).getString("Name");
				resp.getWriter().println(accountName); //TODO: Remove This Line for Production
			}while(!AccountFound(accountName) && index < json.getJSONArray("records").length());
			
		}catch(JSONException e){}
		
		return accountID;
	}
	
	/**
	 * Checks if the Account Name is "Cursos"
	 * @param accountName
	 * @return True if Account Name is Cursos else False.
	 */
	private static boolean AccountFound(String accountName) {
		return accountName.equalsIgnoreCase("Cursos");
	}
	
	/**
	 * Returns the Account ID the Contact that already exists is assigned.
	 * @param contactID
	 * @return the Account ID of the Contact.
	 * @throws ParseException
	 * @throws IOException
	 */
	protected static String getAccountIDFromUser(String contactID) throws ParseException, IOException {
		SFApiService service = new SFApiService();
		HttpResponse response = service.apiGet("/services/data/v30.0/sobjects/Contact/" + contactID, null);
		String responseString = EntityUtils.toString(response.getEntity());
		String accountID = null;
		
		try{
			JSONObject json = new JSONObject(responseString);
			accountID = json.getString("AccountId");
		}catch(JSONException e){}
		
		return accountID;
	}
}

