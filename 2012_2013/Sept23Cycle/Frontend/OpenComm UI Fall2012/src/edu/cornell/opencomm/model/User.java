package edu.cornell.opencomm.model;

public class User {
	private String firstName;
	private String lastName;
	// private Image picture;
	
	public User(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getFullName(){
		return firstName + ' ' + lastName;
	}
	/*
	public Image getPicture(){
		return picture;
	} */
}
