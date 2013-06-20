package org.globaleaks.gldroid;

import org.globaleaks.model.AuthSession;
import org.globaleaks.model.Credential;
import org.globaleaks.model.Submission;
import org.globaleaks.util.Logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

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

	@Override
	protected void onPostExecute(final AuthSession session) {
		AlertDialog ad = new AlertDialog.Builder(parent)
				.setTitle("TIP fetched")
				.setMessage("TODO: show tip summary for review and commenting")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				.create();
		ad.show();
	}
}
