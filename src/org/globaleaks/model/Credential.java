package org.globaleaks.model;

public class Credential {

	private String username;
	private String password;
	private String role;
	
	public Credential(){
		username = "";
		password = "";
		role = "wb";
				
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Credential [username=" + username + ", password=" + password
				+ ", role=" + role + "]";
	}
	public String toJSON() {
		return "{\"username\":\"" + username + "\",\"password\":\"" + password
				+ "\",\"role\":\"" + role + "\"}\n";
	}	
}
