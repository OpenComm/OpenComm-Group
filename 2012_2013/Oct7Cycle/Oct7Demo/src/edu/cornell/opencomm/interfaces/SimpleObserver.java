package edu.cornell.opencomm.interfaces;

public interface SimpleObserver {

	public void onUpdate(Object event);
	
	public void onError(String message);
}
