package org.globaleaks.model;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import org.globaleaks.util.Parser;

import android.graphics.Bitmap;

public class Receiver extends Item {

	public Receiver() {}
	
	public Receiver(Item i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	/*
	private String id;
    private String description; 
    private String Name;  
	*/
    private boolean canDeleteSubmission;
    private Set<String> contexts;
    private Date creationDate;
    private Date updateDate;
    private int receiverLevel;
    private Bitmap image;
    
    public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	/*
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
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	*/
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
		setCreationDate(Parser.parseDate(creationDate));
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setUpdateDate(String updateDate) throws ParseException {
		setUpdateDate(Parser.parseDate(updateDate));
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
		if (name != null) {
			builder.append("Name=");
			builder.append(name);
			builder.append(", ");
		}
		builder.append("receiverLevel=");
		builder.append(receiverLevel);
		builder.append("]");
		return builder.toString();
	}

	public String toShortString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Receiver [");
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
			builder.append("Name=");
			builder.append(name);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (canDeleteSubmission ? 1231 : 1237);
		result = prime * result
				+ ((contexts == null) ? 0 : contexts.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + receiverLevel;
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receiver other = (Receiver) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (canDeleteSubmission != other.canDeleteSubmission)
			return false;
		if (contexts == null) {
			if (other.contexts != null)
				return false;
		} else if (!contexts.equals(other.contexts))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (receiverLevel != other.receiverLevel)
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		return true;
	} 

}
