package org.globaleaks.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;


public class Field {

    public enum FieldType {
    	TEXT, RADIO, SELECT, MULTI_SELECT, TOGGLE, CHECKBOXES, PARAGRAPHS, NUMBER, URL, PHONE, EMAIL, HEADING
    };
    
    private static final Map<String, FieldType> fieldNames = new HashMap<String, Field.FieldType>();
    
    static {
    	fieldNames.put("text",     FieldType.TEXT);
    	fieldNames.put("textarea", FieldType.PARAGRAPHS);
    	fieldNames.put("radio",    FieldType.RADIO);
    	fieldNames.put("select",   FieldType.SELECT);
    	fieldNames.put("phone",    FieldType.PHONE);
    	/*
    	fieldNames.put(FieldType.MULTI_SELECT, "text");
    	fieldNames.put(FieldType.TOGGLE,       "text");
    	fieldNames.put(FieldType.CHECKBOXES,   "text");
    	
    	fieldNames.put(FieldType.NUMBER,       "text");
    	fieldNames.put(FieldType.URL,          "text");
    	
    	fieldNames.put(FieldType.EMAIL,        "text");
    	fieldNames.put(FieldType.HEADING,      "text");
    	*/
    }
    
	private String hint; 
    private String label; 
    private String name; 
    private int presentationOrder; 
    private boolean required; 
    private FieldType type; 
    private String value;
    private List<Option> options;
    
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPresentationOrder() {
		return presentationOrder;
	}
	public void setPresentationOrder(int presentation_order) {
		this.presentationOrder = presentation_order;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public FieldType getType() {
		return type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Field [");
		if (hint != null) {
			builder.append("hint=");
			builder.append(hint);
			builder.append(", ");
		}
		if (label != null) {
			builder.append("label=");
			builder.append(label);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		builder.append("presentation_order=");
		builder.append(presentationOrder);
		builder.append(", required=");
		builder.append(required);
		builder.append(", ");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (value != null) {
			builder.append("value=");
			builder.append(value);
		}
		if (options != null) {
			builder.append(options.toString());
		}
		builder.append("]");
		return builder.toString();
	}
	
	public static FieldType getFieldType(String stringType) {
		FieldType res = fieldNames.get(stringType);
		if(res == null) {
			Log.e("GL", "Unrecognized field type: " + stringType);
		}
		return res;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hint == null) ? 0 : hint.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + presentationOrder;
		result = prime * result + (required ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Field other = (Field) obj;
		if (hint == null) {
			if (other.hint != null)
				return false;
		} else if (!hint.equals(other.hint))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (presentationOrder != other.presentationOrder)
			return false;
		if (required != other.required)
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
    
    
}
