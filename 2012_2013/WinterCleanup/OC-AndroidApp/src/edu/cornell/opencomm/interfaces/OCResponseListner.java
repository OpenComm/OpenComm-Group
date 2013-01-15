package edu.cornell.opencomm.interfaces;


/**
 * @author   Ankit Singh
 *
 */
public interface OCResponseListner {

	/**
	 * @param eventid
	 * @param event
	 */
	public void onResponse(int eventid, Object event);
	
	/**
	 * @param message
	 */
	public void onError(String message);
}
