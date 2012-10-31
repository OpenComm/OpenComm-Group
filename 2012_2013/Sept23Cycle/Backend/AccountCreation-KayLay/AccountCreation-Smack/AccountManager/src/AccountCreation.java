import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


public class AccountCreation {
	
	XMPPConnection connection;
	AccountManager accountManager;
	
	//default constructor
	AccountCreation(){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		accountManager= new AccountManager(connection);		
	}
	
	//Creates account
	AccountCreation(String username, String password){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		accountManager= new AccountManager(connection);
		connect();
		creation(username, password);
	}
	
	public void connect(){
		try {
			connection.connect();
			
		} catch (XMPPException e) {
			System.out.println("There was an error in the connect process of the account creation");
		}
	}
	
	
	public void creation(String username, String password){
		
		try {
			if (accountManager.supportsAccountCreation()){
			accountManager.createAccount(username, password);
			System.out.println("Successful account creation");
			}
			
			else{
				System.out.println("Account Creation is not supported");
				System.out.println("Account Instructions: " + accountManager.getAccountInstructions());
			}
			
		} catch (XMPPException e) {
			System.out.println("Error in account creation");
		}	
	}
	
	public void deleteAccount(String user, String pw){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		try {
			connection.connect();
			connection.login(user, pw);
			while (connection.isAuthenticated() ){
				//System.out.println("hello");
				accountManager.deleteAccount();
			}
			
			
		} catch (XMPPException e) {
			System.out.println("Error occurs deleting account");
			e.printStackTrace();
		}
		catch (IllegalStateException e) {
			System.out.println("not currently logged into server");
			e.printStackTrace();
		}
		
		
	}
	
	public void printMessage(){
		System.out.println("Creation of account is successful");
	}
}
