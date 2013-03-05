package org.globaleaks.model;

public class Language {
	
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Language [code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
	

}
