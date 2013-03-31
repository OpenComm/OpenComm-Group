package edu.cornell.opencomm.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
 
public class HttpRequest {
	
	private String reponse = null;
	
	public HttpRequest(String jid, String email) {
		HttpClient httpclient = new DefaultHttpClient();
 
		try {
			HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/openComm.php?jid="+jid+"&email="+email);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
 
			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
        	} finally {
            		httpclient.getConnectionManager().shutdown();
        	}
	}
	
	public String getResponse(){
		return reponse;
	}
}