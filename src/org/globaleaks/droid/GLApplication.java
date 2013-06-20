package org.globaleaks.droid;

import java.util.ArrayList;
import java.util.Iterator;

import org.globaleaks.model.Context;
import org.globaleaks.model.FieldValue;
import org.globaleaks.model.File;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;

import android.app.Application;

public class GLApplication extends Application {

    private Context context;
    private ArrayList<Receiver> receivers = new ArrayList<Receiver>();
    private ArrayList<FieldValue> fields = new ArrayList<FieldValue>();
    private ArrayList<File> files = new ArrayList<File>();
    private Submission submission;
    
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        if(this.context != null && !this.context.equals(context)) {
            reset();
        }
        this.context = context;
    }
    
    public void addReceiver(Receiver r) {
        receivers.add(r);
    }
    
    public boolean removeReceiver(Receiver r) {
        return receivers.remove(r);
    }
    
    public ArrayList<Receiver> getReceivers() {
        return receivers;
    }
    
    public void addField(FieldValue fv) {
        boolean found = false;
        for (Iterator<FieldValue> i = fields.iterator(); i.hasNext();) {
            FieldValue field = (FieldValue) i.next();
            if(field.getName().equals(fv.getName())) {
                field.setValue(fv.getValue());
                found = true;
                continue;
            }
        }
        if(!found) {
            fields.add(fv);
        }
    }
    
    public boolean removeField(FieldValue value) {
        return fields.remove(value);
    }
    
    public ArrayList<FieldValue> getFields() {
        return fields;
    }
    
    public void addFile(File file) {
        files.add(file);
    }
    
    public boolean removeFile(File file) {
        return files.remove(file);
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }
    
    private void reset() {
        receivers.clear();
        fields.clear();
        files.clear();
    }
    
    public void addReceiver(ArrayList<Receiver> list) {
        receivers.clear();
        for (Iterator<Receiver> i = list.iterator(); i.hasNext();) {
            Receiver r = (Receiver) i.next();
            addReceiver(r);
        }
    }
    public void resetSubmission() {
        context = null;
        reset();
    }
    
    public Submission getSubmission(){
    	return submission;
    }
    
	public void setSubmission(Submission result) {
		submission = result;
	}
    
}
