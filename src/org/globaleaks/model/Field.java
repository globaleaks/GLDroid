package org.globaleaks.model;

import java.util.HashMap;
import java.util.Map;


public class Field {

    public enum FieldType {
    	TEXT, RADIO, SELECT, MULTI_SELECT, TOGGLE, CHECKBOXES, PARAGRAPHS, NUMBER, URL, PHONE, EMAIL, HEADING
    };
    
    private static final Map<String, FieldType> fieldNames = new HashMap<String, Field.FieldType>();
    
    static {
    	fieldNames.put("text",   FieldType.TEXT);
    	fieldNames.put("radio",  FieldType.RADIO);
    	fieldNames.put("select", FieldType.SELECT);
    	/*
    	fieldNames.put(FieldType.MULTI_SELECT, "text");
    	fieldNames.put(FieldType.TOGGLE,       "text");
    	fieldNames.put(FieldType.CHECKBOXES,   "text");
    	fieldNames.put(FieldType.PARAGRAPHS,   "text");
    	fieldNames.put(FieldType.NUMBER,       "text");
    	fieldNames.put(FieldType.URL,          "text");
    	fieldNames.put(FieldType.PHONE,        "text");
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
		builder.append("]");
		return builder.toString();
	}
	
	public static FieldType getFieldType(String stringType) {
		return fieldNames.get(stringType);
	}
    
    
}
