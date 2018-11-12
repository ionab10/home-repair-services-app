package ca.uottawa.service4u;

public class User{
	private String id;
	private String firstName;
	private String lastName;
	private String userType;
	private String phoneNumber;
    private String address;

	public User(){
		this.id = "";
		this.firstName = "";
		this.lastName = "";
		this.userType = null;
		this.phoneNumber = "";
        this.address = "";
	}

	public User(String id, String firstName, String lastName, String userType, String phoneNumber, String address){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
        this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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