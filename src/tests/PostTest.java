/**
 * PostTest.java
 * @author Gabriel
 * Description: Does an HttpPost to the server with the parameters and retrieves the Response.
 */
package tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class PostTest {
	
	public static void main(String[] args) {
		 
		 try {
			HttpClient client = new DefaultHttpClient();
			 
			 //Test #1
			 System.out.println("Test #1");
			 List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			 params.add(new BasicNameValuePair("FormID", ""));
			 params.add(new BasicNameValuePair("HandshakeKey", ""));
			 params.add(new BasicNameValuePair("Course ID", ""));
			 params.add(new BasicNameValuePair("First and Last Name", ""));
			 params.add(new BasicNameValuePair("Precio", "$75 | Suscriptor($75)"));
			 params.add(new BasicNameValuePair("# del TSPR", ""));
			 params.add(new BasicNameValuePair("Teléfono", ""));
			 params.add(new BasicNameValuePair("Mes de cumpleaños", "Abril"));
			 params.add(new BasicNameValuePair("Email", "GabrielYoda@testexample.com"));
			 params.add(new BasicNameValuePair("Course Title", ""));
			 HttpPost post = new HttpPost("http://localhost:8088/mjchalkstack/register_course");
			 post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			 HttpResponse response = client.execute(post);

			 System.out.println(response.getStatusLine().getStatusCode());
			 System.out.println("Output from Server .... \n");
			 HttpEntity entity = response.getEntity();
			 String responseString = EntityUtils.toString(entity, "UTF-8");
			 System.out.println(responseString);
			 

			 
			 System.out.println("End of Test");
			 System.exit(0);
		 } catch (Exception e) {

			 e.printStackTrace();

		 }

	 }
}
