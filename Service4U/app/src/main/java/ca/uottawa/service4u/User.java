package ca.uottawa.service4u;

public class User{
	private String firstName;
	private String lastName;
	private String userType;
	private String phoneNumber;

	public User(String firstName, String lastName, String userType, String phoneNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
	}


	public String getfirstName(){
		return firstName;
	}


	public String getlastName(){
		return lastName;
	}


	public String getuserType(){
		return userType;
	}


	public String getphoneNumber(){
		return phoneNumber;
	}



}