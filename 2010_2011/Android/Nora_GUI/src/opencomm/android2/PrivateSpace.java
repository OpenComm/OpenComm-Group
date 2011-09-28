package opencomm.android2;

import java.util.LinkedList;

public class PrivateSpace {
	LinkedList<Person> allPeople; // all the Persons that are in this Private Space
	
	public PrivateSpace(){
		allPeople = new LinkedList<Person>();
	}
	
	/** Initialize everyone, intended only for the main chat */
	public void initializeEveryone(){
    	allPeople = new LinkedList<Person>(); 
    	allPeople.add(new Person("Nora", "She's the best!", R.drawable.nora));
    	allPeople.add(new Person("Najla", "Is dating Jack Sparrow" , R.drawable.naj));
    	allPeople.add(new Person("Makoto", "Doesn't respond to texts", R.drawable.mak));
    	allPeople.add(new Person("Risa", "Is destined to marry her dog", R.drawable.risa));
	}
	
	/** Returns a linked list of all the people (Person) in this Private Space */
	public LinkedList<Person> getAllPeople(){
		return allPeople;
	}
}
