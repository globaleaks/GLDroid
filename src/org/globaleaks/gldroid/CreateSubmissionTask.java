package org.globaleaks.gldroid;

import java.util.Iterator;

import org.globaleaks.model.Context;
import org.globaleaks.model.File;
import org.globaleaks.model.Submission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

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
	protected void onPostExecute(final Submission result) {
		app.setSubmission(result);
		AlertDialog ad = new AlertDialog.Builder(parent)
						.setTitle("Submission Receipt")
						.setMessage(result.getReceipt())
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
	       		Intent intent = new Intent(app.getApplicationContext(), MainActivity.class);
	    		intent.putExtra("receipt", result.getReceipt());
	    		parent.startActivity(intent);
	    		parent = null;
           }
       }).create();
		ad.show();
	}
    
    

}
