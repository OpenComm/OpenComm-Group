package edu.cornell.opencomm.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
 
public class HttpRequest {
	

	static HttpClient httpClient;
	
	static {
		HttpClient httpClient = new DefaultHttpClient();
	}
	

	
	public static String getResponse(String jid, String email){
		String reponse = null;
		try {
			HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/forgotPassword.php?jid="+jid+"&email="+email);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
 
			try {
				reponse = httpClient.execute(httpget, gestionnaire_reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
        	} finally {
            		httpClient.getConnectionManager().shutdown();
        	}
		return reponse;
	}
}