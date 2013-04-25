package org.globaleaks.model;

public class Item {
	
	protected String id;
	protected String name;
	protected String description;
	
	public Item(Item i) {
		// TODO copy constructor
		setId(i.getId());
		setName(i.getName());
		setDescription(i.getDescription());

	}
	public Item() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return getName();
	}
	
	public String toLongString() {
		return getName() + " - " + getDescription();
	}

}
