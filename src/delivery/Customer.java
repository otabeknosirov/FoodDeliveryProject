package delivery;

public class Customer {
	private String name;
	private String address;
	private String phone;
	private String email;
	
	public Customer(String name, String address, String phone, String email) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}
	

	public String toString() {
		return  name + ", " + address + ", " + phone + ", " + email;
	}
	

}
