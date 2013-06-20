package org.globaleaks.model;

public class ErrorObject {

	public static ErrorObject  GENERIC = new ErrorObject(-1, "Generic error");
	
	private int code;
	private String message;
	
	public ErrorObject(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public ErrorObject() {
		this(GENERIC.getCode(),GENERIC.getMessage());
	}

	@Override
	public String toString() {
		return "ErrorObject [code=" + code + ", message=" + message + "]";
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
