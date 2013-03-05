package org.globaleaks.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class Receiver {

	private static DateFormat sdf = new SimpleDateFormat("EEE MMM  d HH:mm:ss yyyy",Locale.US);
	
	private String id;
    private boolean canDeleteSubmission;
    private Set<String> contexts;
    private Date creationDate;
    private Date updateDate;
    private String description; 
    private String Name;  
    private int receiverLevel;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isCanDeleteSubmission() {
		return canDeleteSubmission;
	}
	public void setCanDeleteSubmission(boolean canDeleteSubmission) {
		this.canDeleteSubmission = canDeleteSubmission;
	}
	public Set<String> getContexts() {
		return contexts;
	}
	public void setContexts(Set<String> contexts) {
		this.contexts = contexts;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void setCreationDate(String creationDate) throws ParseException {
		setCreationDate(sdf.parse(creationDate));
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setUpdateDate(String updateDate) throws ParseException {
		setUpdateDate(sdf.parse(updateDate));
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getReceiverLevel() {
		return receiverLevel;
	}
	public void setReceiverLevel(int receiverLevel) {
		this.receiverLevel = receiverLevel;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Receiver [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		builder.append("canDeleteSubmission=");
		builder.append(canDeleteSubmission);
		builder.append(", ");
		if (contexts != null) {
			builder.append("contexts=");
			builder.append(contexts);
			builder.append(", ");
		}
		if (creationDate != null) {
			builder.append("creationDate=");
			builder.append(creationDate);
			builder.append(", ");
		}
		if (updateDate != null) {
			builder.append("updateDate=");
			builder.append(updateDate);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (Name != null) {
			builder.append("Name=");
			builder.append(Name);
			builder.append(", ");
		}
		builder.append("receiverLevel=");
		builder.append(receiverLevel);
		builder.append("]");
		return builder.toString();
	} 

}
