package org.globaleaks.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONStringer;

public class Submission {

	private String id;
	private List<FieldValue> fields;
	private List<File> files;
	private boolean finalize;
	private List<Receiver> receivers;
	private Integer pertinance;
	private Integer accessLimit;
	private String receipt;
	private Context ctx;
	private Date creationDate;
	private Date updateDate;
	private Integer escalationThreshold;
	private Integer downloadLimit;
	private String mark;
	
	

	/*
	{"context_gus":"a89b7f94-8d88-4c49-b8cf-9bc5da985792","wb_fields":{},"files":[],"finalize":false,"receivers":[]}
	*/
	
	/*
	{"wb_fields": {}, "pertinence": "0", "receivers": [], "access_limit": 42, "receipt": "", "context_gus": "a89b7f94-8d88-4c49-b8cf-9bc5da985792", 
		"creation_date": "Tue Mar  5 21:16:37 2013", "escalation_threshold": "0", "download_limit": 42, 
		"submission_gus": "574ce9a9-1805-49e1-ae60-29c220120a63", "mark": "submission", 
		"id": "574ce9a9-1805-49e1-ae60-29c220120a63", "files": []
			
	}
	*/
	
	/** Create emptu submission for creation on GLBackend */
	public Submission(Context ctx) {
		this.ctx = ctx;
	}
	
	public Submission() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FieldValue> getFields() {
		return fields;
	}

	public void setFields(List<FieldValue> fields) {
		this.fields = fields;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public boolean getFinalize() {
		return finalize;
	}

	public void setFinalize(boolean finalize) {
		this.finalize = finalize;
	}

	public List<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}

	public Integer getPertinance() {
		return pertinance;
	}

	public void setPertinance(Integer pertinance) {
		this.pertinance = pertinance;
	}

	public Integer getAccessLimit() {
		return accessLimit;
	}

	public void setAccessLimit(Integer accessLimit) {
		this.accessLimit = accessLimit;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getEscalationThreshold() {
		return escalationThreshold;
	}

	public void setEscalationThreshold(Integer escalationThreshold) {
		this.escalationThreshold = escalationThreshold;
	}

	public Integer getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(Integer downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}


	/*
	{"context_gus":"a89b7f94-8d88-4c49-b8cf-9bc5da985792","wb_fields":{},"files":[],"finalize":false,"receivers":[]}
	*/
	
	/*
	{"wb_fields": {}, "pertinence": "0", "receivers": [], "access_limit": 42, "receipt": "", "context_gus": "a89b7f94-8d88-4c49-b8cf-9bc5da985792", 
		"creation_date": "Tue Mar  5 21:16:37 2013", "escalation_threshold": "0", "download_limit": 42, 
		"submission_gus": "574ce9a9-1805-49e1-ae60-29c220120a63", "mark": "submission", 
		"id": "574ce9a9-1805-49e1-ae60-29c220120a63", "files": []
			
	}
	*/

	public String toJSON() throws JSONException {
		JSONStringer j = new JSONStringer();
		j.object();
		if (id != null) {
			j.key("id").value(getId());
		}
		if (ctx != null) {
			j.key("context_gus").value(getCtx().getId());
		}

		if (fields != null) {
			j.key("wb_fields").object();
			for (Iterator<FieldValue> i = fields.iterator(); i.hasNext();) {
				FieldValue f = (FieldValue) i.next();
				j.key(f.getName()).value(f.getValue());
			}
			j.endObject();
		} else {
			j.key("wb_fields").object().endObject();
		}
		if (files != null) {
		} else {
			j.key("files").array().endArray();
		}
		j.key("finalize").value(getFinalize());
		if (receivers != null) {
			j.key("receivers").array();
			for (Iterator<Receiver> i = receivers.iterator(); i.hasNext();) {
				Receiver r = (Receiver) i.next();
				j.value(r.getId());
			}
			j.endArray();

		} else {
			j.key("receivers").array().endArray();
		}
		if (pertinance != null) {
		}
		if (accessLimit != null) {
		}
		if (receipt != null) {
		}
		if (creationDate != null) {
		}
		if (updateDate != null) {
		}
		if (escalationThreshold != null) {
		}
		if (downloadLimit != null) {
		}
		if (mark != null) {
		}
		j.endObject();
		return j.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Submission [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (fields != null) {
			builder.append("fields=");
			builder.append(fields);
			builder.append(", ");
		}
		if (files != null) {
			builder.append("files=");
			builder.append(files);
			builder.append(", ");
		}
		builder.append("finalize=");
		builder.append(finalize);
		builder.append(", ");
		if (receivers != null) {
			builder.append("receivers=");
			builder.append("[");
			for (Iterator<Receiver> i = receivers.iterator(); i.hasNext();) {
				Receiver r = (Receiver) i.next();
				builder.append(r.toShortString());
				builder.append(", ");
			}
			builder.append("], ");
		}
		if (pertinance != null) {
			builder.append("pertinance=");
			builder.append(pertinance);
			builder.append(", ");
		}
		if (accessLimit != null) {
			builder.append("accessLimit=");
			builder.append(accessLimit);
			builder.append(", ");
		}
		if (receipt != null) {
			builder.append("receipt=");
			builder.append(receipt);
			builder.append(", ");
		}
		if (ctx != null) {
			builder.append("ctx=");
			builder.append(ctx.toShortString());
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
		if (escalationThreshold != null) {
			builder.append("escalationThreshold=");
			builder.append(escalationThreshold);
			builder.append(", ");
		}
		if (downloadLimit != null) {
			builder.append("downloadLimit=");
			builder.append(downloadLimit);
			builder.append(", ");
		}
		if (mark != null) {
			builder.append("mark=");
			builder.append(mark);
		}
		builder.append("]");
		return builder.toString();
	}

}
