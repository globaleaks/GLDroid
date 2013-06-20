package org.globaleaks.gldroid;

import java.util.Iterator;

import org.globaleaks.model.AuthSession;
import org.globaleaks.model.Context;
import org.globaleaks.model.Credential;
import org.globaleaks.model.File;
import org.globaleaks.model.Submission;
import org.globaleaks.util.GLException;
import org.globaleaks.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;

public class FetchTipTask extends AsyncTask<String,Integer,AuthSession> {

	private MainActivity parent;
	
    public FetchTipTask(MainActivity main) {
    	parent = main;
	}

	@Override
    protected AuthSession doInBackground(String... params) {
    	try {
    		Credential c = new Credential();
    		String number = params[0];
    		c.setPassword(number);
		    AuthSession session = MainActivity.gl.login(c);
		    Submission s = MainActivity.gl.fetchTip();
		    Logger.i("Tip fetched:" + s.toString());
		    MainActivity.gl.logout();
		    return session;
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

	/*
	@Override
	protected void onPostExecute(final AuthSession session) {
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
    */
}
