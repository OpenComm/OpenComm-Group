package edu.cornell.opencomm.controller;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jivesoftware.smack.util.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class EmailController extends Authenticator {
	private static final String TAG = null;
	// sending account constants
	private final String user = "ocuserservice@gmail.com";
	private final String password = "ocuserservice";

	// server constants
	private final String URL = "http://cuopencomm.no-ip.org/plugins/userService/userservice";
	private final String secret = "VyR652Td";

	// mail server constants
	private final String port = "465";;
	private final String sPort = "465";
	private final String host = "smtp.gmail.com";

	private String createAccount;
	private String createAccountPassword;
	private String userEmail;
	private String userName;

	private String[] toRecipient;
	private String fromRecipient;

	private String subject;
	private String body;

	private boolean authenticate;

	private boolean isDebuggable;

	private Multipart multipart;

	private Context context;
	public EmailController(Context context) {
		this.context = context;
		fromRecipient = ""; // email sent from
		subject = ""; // email subject
		body = ""; // email body

		isDebuggable = false; // debug mode on or off - default off
		authenticate = true; // smtp authentication - default on

		multipart = new MimeMultipart();

	}
	

	

	private class EmailTask extends AsyncTask<Session, Void, Void> {
		ProgressDialog emailProgress;
		@Override
		protected void onPreExecute() {
			emailProgress = new ProgressDialog(context);
			emailProgress.setIcon(context.getResources().getDrawable(
					R.drawable.icon));
			emailProgress.setTitle("Sending");
			emailProgress.setMessage("Please wait...");
			emailProgress.show();
		}

		@Override
		protected Void doInBackground(Session... arg0) {

			MimeMessage msg = new MimeMessage(arg0[0]);

			try {
				msg.setFrom(new InternetAddress(fromRecipient));

				InternetAddress[] addressTo = new InternetAddress[toRecipient.length];
				Log.d(TAG, "# of recipients " + toRecipient.length);
				Log.d(TAG, "recipient: " + toRecipient[0]);
				for (int i = 0; i < toRecipient.length; i++) {
					addressTo[i] = new InternetAddress(toRecipient[i]);
				}
				msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

				msg.setSubject(subject);
				msg.setSentDate(new Date());

				// setup message body
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(body);
				multipart.addBodyPart(messageBodyPart);

				// Put parts in message
				msg.setContent(multipart);

				// send email
				Transport.send(msg);
			} catch (AddressException e) {
				Log.v(TAG, "Error sending email");
				Log.v(TAG, e.getMessage());
			} catch (MessagingException e) {
				Log.v(TAG, "Error sending email");
				Log.v(TAG, e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(emailProgress != null){
				emailProgress.dismiss();
			}
			super.onPostExecute(result);
		}
	}

	public boolean send() throws Exception {
		Properties props = _setProperties();

		if (!user.equals("") && !password.equals("") && toRecipient.length > 0
				&& !fromRecipient.equals("") && !subject.equals("")
				&& !body.equals("")) {
			Session session = Session.getInstance(props, this);
			new EmailTask().execute(session).get();
			return true;
		} else {
			return false;
		}
	}

	public void makeAccount(String userEmail, String createAccount,
			String createAccountPassword, String userName) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.createAccount = createAccount;
		this.createAccountPassword = createAccountPassword;

		String[] toArr = { getCreatedAccountUser() };
		setTo(toArr);
		setFrom(getDeveloperEmail());
		setSubject("This is a confirmation email");
		setBody("To add your account:"
				+ "http://cuopencomm.no-ip.org:9090/plugins/userService/userservice?type=add&secret="
				+ getSecret()
				+ "&username="
				+ getCreatedAccountUser()
				+ "&password="
				+ getCreatedAccountPassword()
				+ "&name="
				+ getUserName()
				+ "&email="
				+ getUserEmail()
				+ '\n'
				+ '\n'
				+ "To enable your account:"
				+ "http://example.com:9090/plugins/userService/userservice?type=enable&secret="
				+ getSecret() + "&username=" + getUserName());
		
		try {
			this.send();
		} catch (Exception e) {
			Log.v(TAG, "Error sending email");
		}
		
	}

	public void resetPasword(String email) {
		//try {
			String random = StringUtils.randomString(10);
			String[] username = email.split("@");
			String[] toArr = { email };
			setTo(toArr);
			setFrom(getDeveloperEmail());
			setSubject("OpenComm: Reset password");
			setBody("Click the following link to reset your password to "
					+ random + ":\n\n" + URL + "?type=update&secret=" + secret
					+ "&username=" + username[0] + "&password=" + random);

			/**NetworkService.getInstance().getAccountManager()
					.changePassword(random);
			String userEmail = UserManager.PRIMARY_USER.getVCard()
					.getEmailHome(); */
			toRecipient[0] = email;
		/**} catch (XMPPException e) {
			Log.v(TAG, "Error in resetting password");
			Log.v(TAG, e.getMessage());
		} */
		
		try {
			this.send();
		} catch (Exception e) {
			Log.v(TAG, "Error sending email");
		}
	}

	public void addAttachment(String filename) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);

		multipart.addBodyPart(messageBodyPart);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(password, password);
	}

	private Properties _setProperties() {
		Properties props = new Properties();

		props.put("mail.smtp.host", host);

		if (isDebuggable) {
			props.put("mail.debug", "true");
		}

		if (authenticate) {
			props.put("mail.smtp.auth", "true");
		}

		props.put("mail.smtp.port", port);
		props.put("mail.smtp.socketFactory.port", sPort);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		return props;
	}

	// the getters and setters
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setTo(String[] toArr) {
		this.toRecipient = toArr;
	}

	public void setFrom(String string) {
		this.fromRecipient = string;
	}

	public void setSubject(String string) {
		this.subject = string;
	}

	public String getCreatedAccountUser() {
		return createAccount;
	}

	public String getCreatedAccountPassword() {
		return createAccountPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getDeveloperEmail() {
		return user;
	}

	public String getSecret() {
		return secret;
	}

	public String getUserName() {
		return userName;
	}

}
