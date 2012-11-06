package edu.cornell.opencomm.interfaces;

public interface SimpleObserver {

	public void onUpdate(int eventid, Object event);
	
	public void onError(String message);
}
