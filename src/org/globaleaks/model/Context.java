package org.globaleaks.model;

import java.util.List;
import java.util.Set;

public class Context {

	private String id;
	private String description;
	private String name;
	private List<Field> fields;
	private Set<String> receivers;
	private boolean selectableReceiver;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public Set<String> getReceivers() {
		return receivers;
	}
	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Context [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (fields != null) {
			builder.append("fields=");
			builder.append(fields);
			builder.append(", ");
		}
		if (receivers != null) {
			builder.append("receivers=");
			builder.append(receivers);
			builder.append(", ");
		}
		builder.append("selectableReceiver=");
		builder.append(selectableReceiver);
		builder.append("]");
		return builder.toString();
	}
	public boolean isSelectableReceiver() {
		return selectableReceiver;
	}
	public void setSelectableReceiver(boolean selectableReceiver) {
		this.selectableReceiver = selectableReceiver;
	}
}
