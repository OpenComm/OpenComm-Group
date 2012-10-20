package edu.cornell.opencomm.interfaces;

import java.util.ArrayList;
 
public class SimpleObservable  {
 
 

	private  ArrayList<SimpleObserver>listeners = new ArrayList<SimpleObserver>();
 
	public void addListener(SimpleObserver listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	public void removeListener(SimpleObserver listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	//TODO create a event class with notification status codes
	public void notifyUpdate(Object event) {
		synchronized (listeners) {
			for (SimpleObserver listener : listeners) {
				listener.onUpdate(event);
			}
		}
	}
	//
	public void notifyError(String errorMessage) {
		synchronized (listeners) {
			for (SimpleObserver listener : listeners) {
				listener.onError(errorMessage);
			}
		}
	}
}