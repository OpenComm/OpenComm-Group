package edu.cornell.opencomm.interfaces;


/**
 *@author  Ankit Singh
 *
 */
public interface OCUpdateListener {

	/**
	 * @param eventId
	 * @param data
	 */
	public void onUpdate(int eventId,Object data);
}
