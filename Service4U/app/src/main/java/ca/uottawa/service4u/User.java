package ca.uottawa.service4u;

public class User{
	private String firstName;
	private String lastName;
	private String userType;
	private String phoneNumber;
    private String address;

	public User(){
		this.firstName = "";
		this.lastName = "";
		this.userType = null;
		this.phoneNumber = "";
        this.address = "";
	}

	public User(String firstName, String lastName, String userType, String phoneNumber, String address){
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
        this.address = address;
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

    public String getAddress(){
        return address;
    }

}