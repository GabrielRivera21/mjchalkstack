/**
 * AccessCoursesServlet.java
 * @author Gabriel Rivera Per-ossenkopp
 * Last Modified: July 15th, 2014
 * Description: This is a java servlet that gives access to the staff of microjuris.com
 * in order to see the offering ID available in Digital Chalk in order to extract them and
 * put them in a form at FormStack
 */
package mjchalkstack.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mjchalkstack.digitalchalk.DCCourses;

@SuppressWarnings("serial")
public class AccessCoursesServlet extends HttpServlet {
	
	private static final String USEREMAIL = "USER";
	private static final String USERPASSWORD = "PASSWORD";
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect("/");
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		if(email.equals(USEREMAIL) && password.equals(USERPASSWORD)){
			//This can be moved to a .jsp, but this was used for demonstration
			resp.setContentType("text/html");
			resp.getWriter().println("<!doctype html>");
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title>MJChalkStack Courses</title>");
			resp.getWriter().println("<meta charset=\"utf-8\"");
			resp.getWriter().println("</head>");
			resp.getWriter().println("<body>");
			resp.getWriter().println("<h1>Microjuris.com</h1>");
			resp.getWriter().println("<link rel=\"stylesheet\" href=\"css/course.css\">");
			
			//Prints out the courses offered in DigitalChalk
			resp.getWriter().println("Here are the Courses ID <br>");
			List<HashMap> list = DCCourses.getCourseOfferings();
			resp.getWriter().println("<table>");
			for(int x = 0; x < list.size(); x++){
				resp.getWriter().println("<tr>");
				resp.getWriter().println("<td>" + (x + 1) + ". Course ID:</td> "
										+ "<td><b>" +  list.get(x).get("id") + "</b></td>"
										+ "<td>Course Title:</td> "
										+ "<td><b>" + list.get(x).get("title") + "</b></td>");
				resp.getWriter().println("</tr>");
			}
			resp.getWriter().println("</table>");
			resp.getWriter().println("</body>");
			resp.getWriter().println("</html>");
        }else{
        	req.setAttribute("errorMessage", "Email or Password is Wrong.");

        	req.getRequestDispatcher("/").forward(req, resp);
        }
	}
}
