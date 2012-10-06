package regex;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;



public class Regular_Expressions_Divya {
	/**
	 * TODO: Make sure parameter is an syntactically name or title (they have the same rules).
	 * 		It must abide by the following rules:
	 * 			-Only contain characters
	 * 			-Only the first letter of each word can be capitalized
	 * @param nameOrTitle the name/title to verify
	 * @return true if it is a valid name/title, false if it is not a valid name/title
	 */
	public boolean isValidNameOrTitle(String nameOrTitle){
		//boolean output=true;
		String RE = "[a-zA-Z. ]+"; //Looks for characters spaces and dots
		String name = nameOrTitle; //input string
		Pattern pattern = Pattern.compile(RE); 
		java.util.regex.Matcher m = pattern.matcher(name);
		if(!m.matches()) 
			return false;
		String sNew[]=nameOrTitle.split(" "); //splitting the array to check if there is any upper case character
		if(sNew.length>1){// Only when the title is more than one word the method enters the loop
			for (int i=0;i<sNew.length;i++){//loops through all the words
				String sub=sNew[i].substring(1);
				for(char c : sub.toCharArray()) {//looks for any capital letters
					if( Character.isUpperCase(c))
						return false;
				}
			}
		}
		else {
			String sub=nameOrTitle.substring(1);
			for(char c : sub.toCharArray()) {
				if( Character.isUpperCase(c))
					return false;
			}
		}
		return true;
	}

		/**
		 * TODO: Look through the java security and cryptography packages to implement hashing 
		 * 		a password.  The basic concept of password hashing is getting a one-way function
		 * 		(for example md5 - this is built into the java libraries), applying it to the 
		 * 		password and returning the result of that function.  We will be using the built-in
		 * 		java libraries because they are more reliable than any function we can implement, so 
		 * 		please become more familiar with these libraries.
		 * @param pass the password
		 * @return the hashed value
		 */
		public String hashPass(String pass){
			String s=null;//string s to return
			try{
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				byte[] theByteArray = pass.getBytes("UTF-8");
				md.update(theByteArray);
				s =new String ( md.digest(theByteArray));
				//return s;
			}
			catch(NoSuchAlgorithmException e){
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;

		}


}
