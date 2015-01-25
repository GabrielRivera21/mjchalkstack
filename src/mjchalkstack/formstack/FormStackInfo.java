package mjchalkstack.formstack;

public class FormStackInfo {
	
	private String firstName, lastName, email, offeringID, courseTitle, password;

	public FormStackInfo(String firstName, String lastName, String email, String offeringID, String courseTitle){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.offeringID = offeringID;
		this.courseTitle = courseTitle;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOfferingID() {
		return offeringID;
	}

	public void setOfferingID(String offeringID) {
		this.offeringID = offeringID;
	}
	
	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString(){
		return "First Name: " + firstName + 
				"\nLast Name: " + lastName +
				"\nEmail: " + email +
				"\nCourse Offerring: " + offeringID;
	}
}
