/**
 * Copyright (C) 2012 Infinity Learning Solutions.  All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, is NOT permitted unless the following conditions are
 * met:
 * 1. The redistribution of any kind or in any form must be approved
 *    in writing from an official of Infinity Learning Solutions and a third
 *    party witness.
 * 2. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.  The
 *    redistribution must also be approved in writing from an official
 *    of Infinity Learning Solutions and a third party witness.
 * 3. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ''AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package mjchalkstack.digitalchalk;

import java.io.*;
import java.net.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.*;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;



public class DCApiService {
	
	/**
	 *  set this to true if you get SSL non-trusted certificate errors.  Should always be false in production environment
	 */
	private boolean usingSelfSignedCertificates = false; 
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private static final String OAUTH2TESTOKEN = "Your_Token";
	private static final String DCSANDBOXHOST = "YOUR_URL.digitalchalksandbox.com";
	
	/**
	 * @author Gabriel Rivera Per-ossenkopp
	 * This method does an HTTP PUT on the DigitalChalk Server.
	 * @param path : String
	 * @param postData : JSONObject
	 * @return the HttpResponse from the DigitalChalk Server
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse apiPut(String path, JSONObject postData) throws ClientProtocolException, IOException {

		HttpClient client = HttpClients.createDefault();

		URI uri = makeUri(path, null);

		HttpPut put = new HttpPut(uri);
		put.addHeader("Content-type", "application/json");
		put.addHeader("Accept", "application/json");
		put.addHeader("Authorization", "Bearer " + OAUTH2TESTOKEN);
		put.setEntity(createStringEntity(postData));
		HttpResponse response = client.execute(put);

		return response;
	}
	
	/**
	 * @author Gabriel Rivera Per-Ossenkopp
	 * This method executes an HTTP Post to the Digital Chalk Server with the specified
	 * paths and the JSONObject containing the data to POST.
	 * @param path : String
	 * @param postData : JSONObject
	 * @return HttpResponse from the DigitalChalk Server
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse apiPost(String path, JSONObject postData) throws ClientProtocolException, IOException {

		HttpClient client = HttpClients.createDefault();

		URI uri = makeUri(path, null);

		HttpPost post = new HttpPost(uri);
		post.addHeader("Content-type", "application/json");
		post.addHeader("Accept", "application/json");
		post.addHeader("Authorization", "Bearer " + OAUTH2TESTOKEN);
		post.setEntity(createStringEntity(postData));
		HttpResponse response = client.execute(post);

		return response;
	}
	
	/**
	 * This method does an HTTP GET for the specified path of the DigitalChalk Server
	 * filtering with the parameters
	 * @param path : String
	 * @param parameters : Map<String, String>
	 * @return a DCApiResult Object containing the result of the JSON
	 */
	public DCApiResult apiGet(String path, Map<String,String> parameters) {
			
		URI uri = makeUri(path, parameters);
		
		HttpGet httpGet = new HttpGet(uri);
		
		return executeRequest(httpGet);
	}
	
	/**
	 * 
	 * Executes the httpRequest on the API and returns an ApiResult
	 * 
	 * @param request - the GET method to execute
	 * @return the ApiResult object
	 */
	private DCApiResult executeRequest(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {			
			httpClient = makeHttpClient();
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Bearer " + OAUTH2TESTOKEN);
			response = httpClient.execute(request);
			
			return parseApiResult(response);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			DCApiResult errResult = new DCApiResult();
			if(response != null) {
				errResult.setStatusCode(response.getStatusLine().getStatusCode());
			} else {
				errResult.setStatusCode(0);
			}
			errResult.setError(e.getClass().getSimpleName());
			errResult.setErrorDescription(e.getMessage());
			return errResult;
			
		} finally {

			if(response != null) {
				try {
					response.close();
				} catch(Exception ignoreMe) {
					
				}
			}
			
			if(httpClient != null) {
				try {
					httpClient.close();
				} catch(Exception ignoreMe1) {
					
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DCApiResult parseApiResult(CloseableHttpResponse response) {
		DCApiResult result = new DCApiResult();
		
		if(response != null) {
			
			Header locationHeaders[] = response.getHeaders("Location");
			if(locationHeaders != null && locationHeaders.length > 0) {
				String locationHeader = locationHeaders[0].getValue();
				String parts[] = locationHeader.split("/");
				result.setLocationId(parts[parts.length-1]);
			}
			
			result.setStatusCode(response.getStatusLine().getStatusCode());
			result.setStatusLine(response.getStatusLine().toString());
			
			HttpEntity entity = response.getEntity();
			OutputStream os = new ByteArrayOutputStream();
			if(entity != null) {
				try {
					entity.writeTo(os);
					try {
						Map<String,Object> jsonMap = mapper.readValue(os.toString(), Map.class);
						result.setRawJson(os.toString());
						
						if(jsonMap.containsKey("results")) {
							result.setResults((List<HashMap>) jsonMap.get("results"));					
						}  else {
							List<HashMap> tempResults = new ArrayList<HashMap>();
							tempResults.add((HashMap)jsonMap);
							result.setResults(tempResults);
						}
						
						if(jsonMap.containsKey("errors")) {
							result.setError(((List)jsonMap.get("errors")).toString());
						}
						
						if(jsonMap.containsKey("error")) {
							result.setError((String)jsonMap.get("error"));
						}
						
						if(jsonMap.containsKey("fieldErrors")) {
							result.setFieldErrors((LinkedHashMap)jsonMap.get("fieldErrors"));				
						}
					} catch(JsonMappingException jmex) {
						// ignore, probably just "No content" response
					}
						
				} catch(IOException ioe) {
					ioe.printStackTrace();
					result.setError(ioe.getClass().getSimpleName());
					result.setErrorDescription(ioe.getMessage());
				}
			}
			
		}
		
		return result;
	}
	
	/**
	 * Build the URI
	 */
	private URI makeUri(String path, Map<String,String> parameters) {
		
		URIBuilder uriBuilder = new URIBuilder()
		.setScheme("https")
		.setHost(DCSANDBOXHOST)
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
	
	
	/**
	 * Helps to eliminate SSL errors if you are using self-signed certificates.
	 */
	private CloseableHttpClient makeHttpClient() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		if(this.usingSelfSignedCertificates) {
			builder.setHostnameVerifier(new X509HostnameVerifier() {
				
				@Override
				public boolean verify(String arg0, SSLSession arg1) {						
					return true;
				}

				@Override
				public void verify(String arg0, SSLSocket arg1) throws IOException {						
					
				}

				@Override
				public void verify(String arg0, X509Certificate arg1) throws SSLException {						
					
				}

				@Override
				public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {						
					
				}

			});
		}
		
		return builder.build();
	}
	
	/**
	 * @author Gabriel Rivera Per-ossenkopp
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
	
}