package edu.cornell.opencomm;

public class Person {
	private String name;
	private String description;
	private int image; // R.drawable int
	
	public Person(String name, String description, int image){
		this.name = name;
		this.description = description;
		this.image = image;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getImage(){
		return image;
	}
	
	public void setImage(int image){
		this.image = image;
	}
}
