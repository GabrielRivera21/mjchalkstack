package mjchalkstack.salesforce.http;

import org.apache.http.client.methods.HttpPost;

/**
 * Supports the Patch Method of SalesForce
 * @author Gabriel
 *
 */
public class HttpPatch extends HttpPost {
	
	public HttpPatch(String uri){
		super(uri);
	}
	
	@Override
	public String getMethod(){
		return "PATCH";
	}

}
