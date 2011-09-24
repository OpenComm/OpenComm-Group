package edu.cornell.opencomm;

public class Person {
	private String name;
	private String description;
	private int image; // R.drawable int
	private String xmppID;
	
	public Person(String name, String description, int image, String xmppID){
		this.name = name;
		this.description = description;
		this.image = image;
		this.xmppID = xmppID;
		MainApplication.allPeople.add(this);
		MainApplication.mainspace.add(this);
		MainApplication.id_to_person.put(xmppID, this);
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
	public String getXMPPid(){
		return xmppID;
	}
}
