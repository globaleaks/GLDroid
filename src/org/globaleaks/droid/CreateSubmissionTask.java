package org.globaleaks.droid;

import java.util.Iterator;

import org.globaleaks.model.Context;
import org.globaleaks.model.File;
import org.globaleaks.model.Submission;
import org.globaleaks.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;

public class CreateSubmissionTask extends AsyncTask<GLApplication,Integer,Submission> {

	private GLApplication app;
	private CreateSubmissionActivity parent;
	
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
		Logger.d("Submission receipt: " + result.getReceipt());
		AlertDialog ad = new AlertDialog.Builder(parent)
				.setTitle("Submission Receipt")
				.setMessage(result.getReceipt())
				.setPositiveButton("Add contact", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {		        	   
		        	   	Logger.i("Adding receipt as a contact");
		        	   	addReceipt(result.getReceipt());
		           }
		       })
		       .setNeutralButton(android.R.string.copy, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Logger.i("Copying receipt to Clipboard");
						ClipboardManager cm = (ClipboardManager) parent.getSystemService(Activity.CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("receipt", result.getReceipt());
						cm.setPrimaryClip(clip);
					}
				})
		       .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Logger.d("Receipt ignored");
					}
				})       
		       .create();
		ad.show();
	}
    
	private void addReceipt(String receipt) {
		if(receipt == null) {
			Logger.e("Receipt number is null, cannot add as a contact");
			return;
		}
		Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
		i.putExtra(ContactsContract.Intents.Insert.PHONE, receipt);
		parent.startActivityForResult(i, CreateSubmissionActivity.REQUEST_STORE_RECEIPT);
	}

}
