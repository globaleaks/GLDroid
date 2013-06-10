package org.globaleaks.android;

import java.util.Iterator;

import org.globaleaks.model.Context;
import org.globaleaks.model.File;
import org.globaleaks.model.Submission;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class CreateSubmissionTask extends AsyncTask<GLApplication,Integer,Submission> {

	private GLApplication app;
	private Activity parent;
	
    public CreateSubmissionTask(CreateSubmissionActivity createSubmissionActivity) {
    	parent = createSubmissionActivity;
	}

	@Override
    protected Submission doInBackground(GLApplication... params) {
    	try {
			app = params[0];
			Context ctx = app.getContext();
		    Submission s = MainActivity.gl.createSubmission(ctx);
		    for (Iterator<File> i = app.getFiles().iterator(); i.hasNext();) {
				File file = (File) i.next();
				MainActivity.gl.uploadFile(app,s,file);
			}
		    s.setFinalize(true);
		    s.setReceivers(params[0].getReceivers());
		    s.setFields(params[0].getFields());
		    s = MainActivity.gl.updateSubmission(s);
		    return s;
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

	@Override
	protected void onPostExecute(Submission result) {
		app.setSubmission(result);
		Intent intent = new Intent(app.getApplicationContext(), MainActivity.class);
		intent.putExtra("receipt", result.getReceipt());
		parent.startActivity(intent);
		parent = null;
	}
    
    

}
