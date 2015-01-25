/**
 * SFCases.java
 * @author Gabriel
 * Date: 31/July/2014
 * Description: This class contains all the methods that Creates a Case in SalesForce
 * and Retrieve its Number.
 */
package mjchalkstack.salesforce;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import mjchalkstack.formstack.FormStackInfo;

public class SFCases {
	
	/**
	 * This method creates a Case in SalesForce and returns it's Case ID
	 * @param info
	 * @param contactID
	 * @param accountID
	 * @param price
	 * @return the Case ID if successful, else it returns null
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected static String createCase(FormStackInfo info, String contactID, String accountID, String price) throws ClientProtocolException, IOException {
		SFApiService service = new SFApiService();
		JSONObject json = new JSONObject();
		try{
			json.put("ContactId", contactID);
			json.put("AccountId", accountID);
			json.put("Type", "Type of Case");
			json.put("Origin", "FormStack");
			json.put("Subject", "Curso: " + info.getCourseTitle() + " Forma de Pago - FormStack ");
			json.put("Price__c", price); //TODO: Edit this Field name for yours
		}catch(JSONException e){e.printStackTrace();}
		
		HttpResponse response = service.apiPost("/services/data/v30.0/sobjects/Case", json);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		String caseID = null;
		try{
			json = new JSONObject(responseString);
			caseID = json.getString("id");
		}catch(JSONException e){}
		
		return caseID;
		
	}
	
	/**
	 * This method does an HTTP Get with the Case ID and retrieves the Case Number
	 * @param caseID
	 * @return the Case Number
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected static String getCaseNumber(String caseID) throws ClientProtocolException, IOException {
		SFApiService service = new SFApiService();
		HttpResponse response = service.apiGet("/services/data/v30.0/sobjects/Case/" + caseID, null);
		String responseString = EntityUtils.toString(response.getEntity());
		String caseNumber = null;
		try{
			JSONObject json = new JSONObject(responseString);
			caseNumber = json.getString("CaseNumber");
		}catch(JSONException e){}
		
		
		return caseNumber;
	}
	
}

