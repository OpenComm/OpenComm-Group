package regex;

public class Regular_Expressions_Somya {

	/**
	 * TODO: Make sure parameter is an syntactically valid email address.
	 * 		A valid email address is of the type: x@x where x must be at least
	 * 		one symbol.
	 * @param email the email address to validate
	 * @return true if it is a valid email address, false if it is not a valid email address
	 */
	public boolean isValidEmail(String email){
<<<<<<< HEAD

=======

>>>>>>> ca8b45ed0825d12b8f3a1e87a39f1492db4d142a
		 String[] cut=email.split("@");          //splits at @ and checks for the length of the array created
	      if (cut.length==2&&!cut[0].isEmpty())	 //isEmpty required because if the 
	    	                                     //first char is @ then a string[] of length 2 is created with first empty 
		return true;
				return false;
<<<<<<< HEAD

=======

>>>>>>> ca8b45ed0825d12b8f3a1e87a39f1492db4d142a
	
	/**
	 * TODO: Make sure this is a valid password.  The only rule (as of now) for
	 * 			passwords is that it must be 10 characters long.
	 * @param pass the password to validate
	 * @return true if it is a valid password, false if it is not a valid password
	 */
	public boolean isValidPassword(String pass){

		Pattern p=Pattern.compile(".{10,}$");  //any char sequence of length>=10
		Matcher validity= p.matcher(pass);
	boolean validityhere= validity.matches();
	return validityhere;
}

