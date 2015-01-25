/**
 * EmailSender.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 21th, 2014
 * Description: This class sends an email to the User and Staff of Microjuris.com
 * if it was successful in doing it's processes or if an error ocurred.
 */
package mjchalkstack.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import org.json.JSONException;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGridException;

import mjchalkstack.formstack.FormStackInfo;

public class EmailSender {
	
	/**
	 * Sends an email to the User and to the Staff of Microjuris.com with the
	 * User's Account information, and the link of the DigitalChalk Course
	 * @param info : FormStackInfo
	 * @param context 
	 * @throws JSONException 
	 * @throws org.json.JSONException 
	 * @throws IOException 
	 */
	public static void sendSuccessMail(FormStackInfo info, ServletContext context) throws JSONException, IOException {
		String title = "";
		
		if(info.getCourseTitle() != null)
			title = " de <b>" + info.getCourseTitle() + "</b>";
		
		SendGrid sendGrid = new SendGrid("user", "password");
		Email email = new Email();
		
		String fileEmails = "/WEB-INF/emails.txt";
		InputStream resourceEmail = context.getResourceAsStream(fileEmails);
		if(resourceEmail != null)
			addToCompanyEmails(email, resourceEmail);
		email.addTo(info.getEmail());
		email.setFrom("example@example.com");
		email.setFromName("Your_Name");
		email.setReplyTo("example@example.com");
		email.setSubject("example@example.com");
		
		String fileText = "/WEB-INF/textemail.txt";
		InputStream resourceText = context.getResourceAsStream(fileText);
		if(resourceText != null){
			String fulltext = extractTextFromFile(resourceText, info, info.getPassword(), title);
			email.setHtml(fulltext);
		} else {
			email.setHtml("<h1>My Message</h1>");
		}
		try{
			sendGrid.send(email);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends an Email to the Staff containing an error from MJChalkStack
	 * with it's status code and the cause of the error.
	 * @param responseString
	 * @param context 
	 * @throws JSONException 
	 * @throws org.json.JSONException 
	 * @throws IOException 
	 */
	public static void sendErrorMail(String responseString, ServletContext context) throws JSONException, IOException {
		SendGrid sendGrid = new SendGrid("user", "password");
		Email email = new Email();
		
		String fileEmails = "/WEB-INF/emails.txt";
		InputStream resourceEmail = context.getResourceAsStream(fileEmails);
		if(resourceEmail != null)
			addToCompanyEmails(email, resourceEmail);
		email.setFrom("example@example.com");
		email.setFromName("MJChalkStack App");
		email.setReplyTo("no-reply@example.com");
		email.setSubject("Error: MJChalkStack");
		email.setText("We have received the following error from MJChalkStack App "
				+ "on our server: \n\n" 
				+ responseString + "\n\n"
				+ "Please Manually Register this User \n\n" 
				+ "Thank you, \n\n"
				+ "MJChalkStack App.");
		
		try{
			sendGrid.send(email);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends an Email to the User when an error occurs in registering or creating
	 * it's user inside DigitalChalk.
	 * @param info : FormStackInfo
	 * @throws JSONException 
	 * @throws org.json.JSONException 
	 */
	public static void sendToUserErrorMail(FormStackInfo info) throws JSONException{
		SendGrid sendGrid = new SendGrid("user", "password");
		Email email = new Email();
		email.addTo(info.getEmail());
		email.setFrom("example@example.com");
		email.setReplyTo("example@example.com");
		email.setSubject("Error");
		email.setHtml("Your Error Message to User");
		try{
			sendGrid.send(email);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This extracts all the emails in the emails.txt file and adds them to
	 * the To field in emails, sending them the emails.
	 * @param email
	 * @param resourceEmail
	 * @throws IOException
	 * @throws JSONException
	 */
	private static void addToCompanyEmails(Email email, InputStream resourceEmail) throws IOException, JSONException {
		InputStreamReader isr = new InputStreamReader(resourceEmail);
		BufferedReader reader = new BufferedReader(isr);
		String lineEmail = "";
		
		// We read the file line by line
		while ((lineEmail = reader.readLine()) != null) {
			email.addTo(lineEmail); //Adds the email to where it will be sent
		}
		
	}
	
	/**
	 * This extracts the the text in the text file
	 * @param is : inputStream
	 * @param info : FormStackInfo
	 * @param password : String
	 * @param title : String
	 * @return The full text extracted from the Text File
	 * @throws IOException
	 */
	private static String extractTextFromFile(InputStream is, FormStackInfo info, 
											String password, String title) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String linetext = "";
		String fulltext = "";
		
		// We read the file line by line
		while ((linetext = reader.readLine()) != null) {
			fulltext += linetext; //Appends the line to the Full Text
		}
		fulltext = fulltext.replace("$CourseTitle", title)
							.replace("$Email", info.getEmail())
							.replace("$Password", password);
		return fulltext;
	}

	/**
	 * Sends an email to the Staff with the Case Number created in SalesForce.
	 * @param info
	 * @param caseID
	 * @param context
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void sendSFMail(FormStackInfo info, String caseID, ServletContext context) throws IOException, JSONException {
		SendGrid sendGrid = new SendGrid("user", "password");
		Email email = new Email();
		
		String fileEmails = "/WEB-INF/emails.txt";
		InputStream resourceEmail = context.getResourceAsStream(fileEmails);
		if(resourceEmail != null)
			addToCompanyEmails(email, resourceEmail);
		email.setFrom("example@example.com");
		email.setFromName("Your Name");
		email.setReplyTo("example@example.com");
		email.setSubject("SalesForce Case");
		
		String fileText = "/WEB-INF/salesforcetext.txt";
		InputStream resourceText = context.getResourceAsStream(fileText);
		if(resourceText != null){
			String fulltext = extractSFTextFromFile(resourceText, info, caseID);
			email.setHtml(fulltext);
		}
		
		try{
			sendGrid.send(email);
		}catch(SendGridException e){}
	}
	
	/**
	 * Extracts the text from the SalesForceText.txt File and returns it as a String
	 * @param resourceText
	 * @param info
	 * @param caseID
	 * @return a String with the Contents of the salesforcetext.txt File.
	 * @throws IOException
	 */
	private static String extractSFTextFromFile(InputStream resourceText,
			FormStackInfo info, String caseNum) throws IOException {
		InputStreamReader isr = new InputStreamReader(resourceText);
		BufferedReader reader = new BufferedReader(isr);
		String linetext = "";
		String fulltext = "";
		
		// We read the file line by line
		while ((linetext = reader.readLine()) != null) {
			fulltext += linetext; //Appends the line to the Full Text
		}
		fulltext = fulltext.replace("$CourseTitle", info.getCourseTitle())
							.replace("$Email", info.getEmail())
							.replace("$FirstName", info.getFirstName())
							.replace("$LastName", info.getLastName())
							.replace("$CaseNumber", caseNum);
		return fulltext;
	}
	
	/**
	 * Used for Testing Purposes
	 * @param responseString
	 * @param context
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void sendSFErrorMail(String responseString, ServletContext context) throws IOException, JSONException {
		SendGrid sendGrid = new SendGrid("user", "password");
		Email email = new Email();
		
		String fileEmails = "/WEB-INF/emails.txt";
		InputStream resourceEmail = context.getResourceAsStream(fileEmails);
		if(resourceEmail != null)
			addToCompanyEmails(email, resourceEmail);
		email.setFrom("Example@example.com");
		email.setFromName("MJChalkStack App");
		email.setReplyTo("no-reply@example.com");
		email.setSubject("Error: MJChalkStack");
		email.setText("We have received the following error from MJChalkStack App "
				+ "on our server: \n\n" 
				+ responseString + "\n\n"
				+ "Please Make a Case for this User\n\n" 
				+ "Thank you, \n\n"
				+ "MJChalkStack App.");
		
		try{
			sendGrid.send(email);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}