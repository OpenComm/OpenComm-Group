package com.example.accountcreationbackend;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMailerClass extends javax.mail.Authenticator { 
	  private final String user= "ocuserservice@gmail.com"; //DEVELOPER'S USERNAME: OCUSERSERVICE@GMAIL.COM
	  private final String password="ocuserservice"; //DEVELOPER'S PASSWORD
	  private final String secret= "VyR652Td";
	

	  private String[] toRecipient; 
	  private String fromRecipient; 

	  private String port; 
	  private String sPort; 

	  private String host; 

	  private String subject; 
	  private String body; 

	  private boolean authenticate; 

	  private boolean isDebuggable; 

	  private Multipart multipart; 


	  public GMailerClass() { 
	    host = "smtp.gmail.com"; // default smtp server 
	    port = "465"; // default smtp port 
	    sPort = "465"; // default socketfactory port 

	    //user = ""; // username 
	    //password = ""; // password 
	    fromRecipient = ""; // email sent from 
	    subject = ""; // email subject 
	    body = ""; // email body 

	    isDebuggable = false; // debug mode on or off - default off 
	    authenticate = true; // smtp authentication - default on 

	    multipart = new MimeMultipart(); 

	  } 

	  public GMailerClass(String user, String pass) { 
	    this(); 
	   // this.user = user; 
	    //password = pass; 
	  } 

	  public boolean send() throws Exception { 
	    Properties props = _setProperties(); 

	    if(!user.equals("") && !password.equals("") && toRecipient.length > 0 && !fromRecipient.equals("") && !subject.equals("") && !body.equals("")) { 
	      Session session = Session.getInstance(props, this); 

	      MimeMessage msg = new MimeMessage(session); 

	      msg.setFrom(new InternetAddress(fromRecipient)); 

	      InternetAddress[] addressTo = new InternetAddress[toRecipient.length]; 
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

	      return true; 
	    } else { 
	      return false; 
	    } 
	  } 
	  
	  public void enableAccount(String userEmail, String userName, String userPassword){
		  
		  String[] toArr = {userEmail}; 
	      setTo(toArr); 
	      setFrom(getDeveloperEmail()); 
	      setSubject("This is a confirmation email"); 
	      setBody("To enable your account:" + "http://example.com:9090/plugins/userService/userservice?type=enable&secret="+ getSecret() +"&username=" + userName); 
	      
	      //"To add your account:"+"http://cuopencomm.no-ip.org:9090/plugins/userService/userservice?type=add&secret=" +
    		//getSecret() +"&username=" + createAccount + "&password=" + createAccountPassword + "&name="+ userName +"&email=" + userEmail + '\n' + '\n' +
	     	
	      //CODE TO SEND AN ATTACHED FILE
	      
	      try { 
	        //ATTCHEMENT FROM FILE

	        if(send()) { 
	          //TOAST or whatever frontend/design wants
	        } else { 
	          //TOAST 
	        } 
	      } catch(Exception e) { 
	        //TOAST
	  
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
	  public PasswordAuthentication getPasswordAuthentication() { 
	    return new PasswordAuthentication(user, password); 
	  } 

	  private Properties _setProperties() { 
	    Properties props = new Properties(); 

	    props.put("mail.smtp.host", host); 

	    if(isDebuggable) { 
	      props.put("mail.debug", "true"); 
	    } 

	    if(authenticate) { 
	      props.put("mail.smtp.auth", "true"); 
	    } 

	    props.put("mail.smtp.port", port); 
	    props.put("mail.smtp.socketFactory.port", sPort); 
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
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
	      // TODO Auto-generated method stub
	      this.toRecipient=toArr;
	  }

	  public void setFrom(String string) {
	      // TODO Auto-generated method stub
	      this.fromRecipient=string;
	  }

	  public void setSubject(String string) {
	      // TODO Auto-generated method stub
	      this.subject=string;
	  }  
	  
	  public String getDeveloperEmail(){
		  return user;
	  }
	  
	  public String getSecret(){
		  return secret;
	  }
	  
	  // more of the getters and setters É.. 
	}
