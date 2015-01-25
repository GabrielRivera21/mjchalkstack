/**
 * SalesForceProcedure.java
 * @author Gabriel
 * Date: 31/July/2014
 * Description: This class contains the method of the procedures to take when creating
 * a case at SalesForce 
 */
package mjchalkstack.salesforce;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import mjchalkstack.email.EmailSender;
import mjchalkstack.formstack.FormStackInfo;
import mjchalkstack.servlets.ParamValidator;

public class SalesForceProcedure {
	
	/**
	 * This method makes all the procedures to look for or create a contact in salesForce and
	 * creates a new Case for that contact.
	 * @param req : HttpServletRequest
	 * @param resp : HttpServletResponse - Remove 
	 * @param info : FormStackInfo
	 * @param context : ServletContext
	 */
	public static void caseProcedure(HttpServletRequest req, HttpServletResponse resp,
									  FormStackInfo info, ServletContext context) {
		try{
			String contactID = SFContact.searchForContact(info);
			String accountID = null;
			if(contactID == null){ //Contact does not exist in organization
				accountID = SFContact.getAccountID(resp);
				String TSPRNum = ParamValidator.validateTSPRParam(req); 
				String PhoneNum = ParamValidator.validatePhoneParam(req);
				String BirthMonth = ParamValidator.validateBirthParam(req);
				contactID = SFContact.createContact(info, accountID, TSPRNum, PhoneNum, BirthMonth);
				resp.getWriter().println("Contact Created his/her ID: " + contactID); //TODO: remove this line 
			}else{
				resp.getWriter().println("Contact Already Exists ID: " + contactID); //TODO: remove this line
				accountID = SFContact.getAccountIDFromUser(contactID);
			}
			String price = ParamValidator.validatePriceParam(req);
			String caseID = SFCases.createCase(info, contactID, accountID, price);
			resp.getWriter().println("Case ID: " + caseID); //TODO: remove
			String caseNumber = SFCases.getCaseNumber(caseID);
			resp.getWriter().println("Case Number: " + caseNumber); //TODO: remove
			EmailSender.sendSFMail(info, caseNumber, context);
		}catch(Exception e){
			try {
				EmailSender.sendSFErrorMail(e.getMessage(), context);
			} catch (IOException e1) {} 
			catch (JSONException e1) {}
		}
	}
	
	

	
	
	
	
	
	
}
