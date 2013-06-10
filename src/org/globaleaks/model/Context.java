package org.globaleaks.model;

import java.util.List;
import java.util.Set;

public class Context extends Item {

	public Context() {}
	
	public Context(Item i) {
		super(i);
	}

	private List<Field> fields;
	private Set<String> receivers;
	private boolean selectableReceiver;
	private String receiptDescription;
	private String submissinoDisclaimer;
	private String submissionIntroduction;
	
	public String getReceiptDescription() {
        return receiptDescription;
    }

    public void setReceiptDescription(String receiptDescription) {
        this.receiptDescription = receiptDescription;
    }

    public String getSubmissinoDisclaimer() {
        return submissinoDisclaimer;
    }

    public void setSubmissinoDisclaimer(String submissinoDisclaimer) {
        this.submissinoDisclaimer = submissinoDisclaimer;
    }

    public String getSubmissionIntroduction() {
        return submissionIntroduction;
    }

    public void setSubmissionIntroduction(String submissionIntroduction) {
        this.submissionIntroduction = submissionIntroduction;
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
		if (receiptDescription != null) {
		    builder.append("receiptDescription=");
		    builder.append(receiptDescription);
		    builder.append(", ");
		}
		if (submissinoDisclaimer != null) {
		    builder.append("submissionDisclaimer=");
		    builder.append(submissinoDisclaimer);
		    builder.append(", ");
		}
		if (submissionIntroduction != null) {
		    builder.append("submissionInstroduction=");
		    builder.append(submissionIntroduction);
		    builder.append(", ");
		}
		builder.append("selectableReceiver=");
		builder.append(selectableReceiver);
		builder.append("]");
		return builder.toString();
	}
	public String toShortString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Context [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
	public boolean isSelectableReceiver() {
		return selectableReceiver;
	}
	public void setSelectableReceiver(boolean selectableReceiver) {
		this.selectableReceiver = selectableReceiver;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((receivers == null) ? 0 : receivers.hashCode());
		result = prime * result + (selectableReceiver ? 1231 : 1237);
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
		Context other = (Context) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (receivers == null) {
			if (other.receivers != null)
				return false;
		} else if (!receivers.equals(other.receivers))
			return false;
		if (selectableReceiver != other.selectableReceiver)
			return false;
		return true;
	}
}
