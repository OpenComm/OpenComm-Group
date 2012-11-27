package edu.cornell.opencomm.interfaces;

public interface OCResponseListner {

	public void onResponse(int eventid, Object event);
	
	public void onError(String message);
}
