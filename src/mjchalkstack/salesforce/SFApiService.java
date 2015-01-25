/**
 * SFApiService.java
 * @author Gabriel
 * Date: 31/July/2014
 * Description: This class builds the URI and holds the HTTP Methods and authentification
 * of the server for SalesForce
 */
package mjchalkstack.salesforce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SFApiService {
	
	/**
	 * This class contains the Credentials necessary to access the API for SalesForce
	 * TODO: EDIT ALL this fields for the production account for your values.
	 */
	class UserCredentials {
		private static final String securityToken = "your_security_token";
		final static String SFHOST = "na17.salesforce.com";
		final static String CLIENTID = "Your_consumer_key";
		final static String CLIENTSECRET = "Your_Consumer_Secret";
		final static String USERNAME = "your_email@example.com";
		final static String PASSWORD = "your_password" + securityToken;
	}
	
	/**
	 * This method authenticates with the SalesForce Server and retrieves
	 * it's access token.
	 * @param client
	 * @return the OAuth2 Access Token
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String Authenticate(HttpClient client) throws ClientProtocolException, IOException{
		String path = "/services/oauth2/token";
		
		URI uri = makeUri(path, null);
		HttpPost post = new HttpPost(uri);
		
		//Putting the RequestBody as in URL Encoded Format
		StringBuffer requestBodyText = new StringBuffer("grant_type=password");
		requestBodyText.append("&username=");
		requestBodyText.append(UserCredentials.USERNAME);
		requestBodyText.append("&password=");
		requestBodyText.append(UserCredentials.PASSWORD);
		requestBodyText.append("&client_id=");
		requestBodyText.append(UserCredentials.CLIENTID);
		requestBodyText.append("&client_secret=");
		requestBodyText.append(UserCredentials.CLIENTSECRET);
		StringEntity requestBody = new StringEntity(requestBodyText.toString());
		requestBody.setContentType("application/x-www-form-urlencoded");
		post.setEntity(requestBody);
	
		HttpResponse response = client.execute(post);
		
		//Retrieves the JSON Response and we extract the Access Token
		String accessToken = null;
		if (response.getStatusLine().getStatusCode() == 200 ) {
			String response_string = EntityUtils.toString(response.getEntity());
			try {
				JSONObject json = new JSONObject(response_string);
				accessToken = json.getString("access_token");
			} catch (JSONException je) {
				je.printStackTrace();
			} 
		}
		return accessToken;
	}
	
	/**
	 * Does an HTTP POST to the SalesForce Server
	 * @param path
	 * @param postData
	 * @return the HTTP Response from the SalesForce Server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse apiPost(String path, JSONObject postData) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClients.createDefault();
		
		URI uri = makeUri(path, null);
		String accessToken = Authenticate(client);
		
		HttpPost post = new HttpPost(uri);
		post.addHeader("Content-type", "application/json");
		post.addHeader("Accept", "application/json");
		post.addHeader("Authorization", "Bearer " + accessToken);
		post.setEntity(createStringEntity(postData));
		HttpResponse response = client.execute(post);
		
		return response;
	}
	
	/**
	 * Does an HTTP PATCH to the SalesForce Server.
	 * PATCH is a method defined in SalesForce that either PUTs the data
	 * or POSTs it, if it doesn't exist. 
	 * 
	 * Note: It is currently not Used for this application, but only defined the method
	 * for future use if necessary.
	 * @param path : String
	 * @param putData : JSONObject
	 * @return the HTTP Response from SalesForce.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse apiPatch(String path, JSONObject putData) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClients.createDefault();
		
		URI uri = makeUri(path, null);
		String accessToken = Authenticate(client);
		
		HttpPatch patch = new HttpPatch(uri);
		patch.addHeader("Content-type", "application/json");
		patch.addHeader("Accept", "application/json");
		patch.addHeader("Authorization", "Bearer " + accessToken);
		patch.setEntity(createStringEntity(putData));
		HttpResponse response = client.execute(patch);
		
		return response;
	}
	
	/**
	 * Does an HTTP GET request to the SalesForce Server.
	 * @param path
	 * @param params
	 * @return the HTTP response of the SalesForce Server
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse apiGet(String path, Map<String, String> params) throws ClientProtocolException, IOException{
		HttpClient client = HttpClients.createDefault();
		
		URI uri = makeUri(path, params);
		String accessToken = Authenticate(client);
		
		HttpGet get = new HttpGet(uri);
		get.addHeader("Content-type", "application/json; charset=UTF-8");
		get.addHeader("Accept", "application/json");
		get.addHeader("Authorization", "Bearer " + accessToken);
		HttpResponse response = client.execute(get);
		
		return response;
	}
	
	/**
	 * Creates a String Entity of content type JSON in order to process
	 * as an entity the JSONObject
	 * @param params : JSONObject
	 * @return an HttpEntity in order to process JSONObject
	 * @throws UnsupportedEncodingException 
	 */
	private HttpEntity createStringEntity(JSONObject params) throws UnsupportedEncodingException {
		StringEntity se = null;
		se = new StringEntity(params.toString(), "UTF-8");
		se.setContentType("application/json");
		return se;
	}
	
	/**
	 *  Builds the URI
	 */
	private URI makeUri(String path, Map<String,String> parameters) {
		
		URIBuilder uriBuilder = new URIBuilder()
		.setScheme("https")
		.setHost(UserCredentials.SFHOST)
		.setPath(path);

		if(parameters != null && !parameters.isEmpty()) {
			for(String param : parameters.keySet()) {
				uriBuilder.addParameter(param, parameters.get(param));
			}
		}
		try {
			URI uri = uriBuilder.build();
			return uri;
		} catch(URISyntaxException usex) {
			usex.printStackTrace();
			return null;
		}
	}
}
