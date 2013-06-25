package org.globaleaks.droid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.globaleaks.model.Node;
import org.globaleaks.util.GLClient;
import org.globaleaks.util.Logger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity  {

	private static final String  TOR_PACKAGE = "org.torproject.android";
	public static GLClient gl = new GLClient();
	private ImageView logo;
	private TextView name;
	private TextView description;
	private ImageView torStatus;
	
	private final static int PICK_RECEIPT  = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(android.R.id.home).setPadding(1, 1, 10, 1);
		View mainLayout = findViewById(R.id.main_layout);
		logo = (ImageView) mainLayout.findViewById(R.id.node_logo);
		name = (TextView) mainLayout.findViewById(R.id.node_name);
		description = (TextView) mainLayout.findViewById(R.id.node_description);
		setTitle(R.string.title_activity_main);
		ActionBar bar = getActionBar();
		torStatus = new ImageView(getApplicationContext());
		refreshTorStatus();
		torStatus.setPadding(5, 5, 5, 5);
		bar.setCustomView(torStatus);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		refresh(false);
	}

	@Override
    protected void onStop() {
	    HttpResponseCache cache = HttpResponseCache.getInstalled();
	    if (cache != null) {
	        cache.flush();
	    }
        super.onStop();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
			case R.id.action_create:
				if(!checkTor()) return true;
				intent = new Intent(this, CreateSubmissionActivity.class);
				break;
			case R.id.action_view:
				if(!checkTor()) return true;
				pickReceipt();
				break;
			case R.id.action_settings:
				intent = new Intent(this, SettingsActivity.class);
				break;
			case R.id.action_refresh:
			    refresh(true);
			    break;
		}
		if(intent != null) startActivity(intent);
		return super.onMenuItemSelected(featureId, item);
	}

	private void pickReceipt() {
		Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(i, PICK_RECEIPT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null) {
			Logger.i("No intent data returned");
			return;
		}
		switch (requestCode) {
			case PICK_RECEIPT:
				showTip(data.getData());
				break;
	
			default:
				break;
		}
	}

	private void showTip(Uri uri) {
		String number = null;
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if(cursor != null) {
			try {
				cursor.moveToNext();
				String[] names = cursor.getColumnNames();
				Logger.i(names.toString());
				int numIdx = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
				number = cursor.getString(numIdx);
				number = PhoneNumberUtils.stripSeparators(number);
				Logger.i("receipt number: " + number);
			} finally {
				cursor.close();
			}
		}
		if(number == null) {
			Logger.e("No receipt number retrieved");
			return;
		}
		new FetchTipTask(this).execute(number);
	}

	private void refresh(final boolean eraseCache) {
		if(!checkTor()) return;
	    name.setText(R.string.title_node_loading);
	    description.setText("...");
        if(eraseCache)
            gl.eraseCache(getApplicationContext());
	    FetchMetadataTask task = new FetchMetadataTask();
        task.execute(getApplication());
	    
    }

	public void onCompleteMetadata(Node node) {
		if(node == null) return;
		logo.setImageBitmap(node.getImage());
		name.setText(node.getName());
		description.setText(node.getDescription());
	}

    public class FetchMetadataTask extends AsyncTask<Application, Integer, Node> {

        @Override
        protected Node doInBackground(Application...params) {
        	try {
	            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(params[0]);
	            String url = pref.getString("base_url", GLClient.DEMO_GLOBALEAKS);
	            int cache = 60*60*24;
	            try {
	                String c = pref.getString("cache_data", Integer.toString(cache));
	                cache = Integer.parseInt(c);
	            } catch (Exception e) {
	            }
	
	            gl.setBaseUrl(url, params[0].getApplicationContext());
	            gl.setCacheExpiration(cache);
	            gl.fetchMetadata();
	            return gl.node;
        	} catch (Exception e) {
        		return null;
        	}
        }

        @Override
        protected void onPostExecute(Node result) {
        	if(result == null) {
        		result = new Node();
        		result.setName("ERROR!");
        		result.setDescription("Cannot connect to GlobaLeaks node \"" + MainActivity.gl.getBaseUrl() + 
        				"\". Is it the right URL? Edit GLDroid settings, prepend \"http://\" or \"https://\" protocol and verify " +
        				"that your node is up and running.");
        	}
            onCompleteMetadata(result);
        }
    }
    
	public static final int PROXY_PORT_HTTP  = 8118;
	public static final int PROXY_PORT_SOCKS = 9050;
	public static final String PROXY_HOST    = "127.0.0.1";
	
	private boolean isTorInstalled() {
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo("org.torproject.android", PackageManager.GET_META_DATA);
            return true;
        } catch (Exception e) {
        	// not orbot package installed
        	return false;
        }
    }

    private boolean isTorProxyRunning() {
    	BufferedReader reader = null;
    	try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("/system/bin/netstat -tln");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while((line = reader.readLine()) != null) {
            	if(line.contains("127.0.0.1:9050")) return true;
            }
            return false;
        } catch (Exception e) {
        	// no proxy running
        	return false;
        } finally {
        	try {
				reader.close();
			} catch (IOException e) {
			}
        }
    }
    
    private boolean isTorEnabled() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean tor = sp.getBoolean(getResources().getString(R.string.settings_tor_key), false);
        return tor;
    }

    public boolean torProxy() {
        return isTorEnabled() && isTorInstalled() && isTorProxyRunning();
    }

    private void refreshTorStatus() {
    	/*
        if(isTorInstalled() && isTorEnabled()) {
            torStatus.setVisibility(ImageView.VISIBLE);
        } else {
            torStatus.setVisibility(ImageView.INVISIBLE);
        }
        */
        int iconId = R.drawable.tor_off;
        if(torProxy()) {
            iconId = R.drawable.tor_on;
        }
        torStatus.setImageDrawable(getResources().getDrawable(iconId));
        gl.setTor(isTorEnabled());
    }

    private boolean checkTor() {
    	boolean enabled = validateTorConfiguration();
    	refreshTorStatus();
    	return enabled;
    }
    
    private boolean validateTorConfiguration() {
    	if(!isTorEnabled()) {
    		// TOR disabled => continue
    		return true;
    	}
		if(!isTorInstalled()) {
			AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Secure communication")
				.setMessage("Your configuration requires to proxy your traffic through TOR in order to provide you best security and anonimity. "
						  + "Install it now before continuing or, if you know what you are doing, disable it in Settings")
				.setPositiveButton("Install", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						installTor();
					}
				})
				.setNegativeButton("Disable", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						disableTor();
					}
				})
				.create();
			dialog.show();
			return false;
		}
		if(!isTorProxyRunning()) {
			AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Secure communication")
				.setMessage("Your configuration requires to proxy your traffic through TOR in order to provide you best security and anonimity. "
						  + "TOR is installed but is not running: start it or, if you know what you are doing, disable it in Settings")
				.setPositiveButton("Start", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startTor();
					}})
				.setNegativeButton("Disable", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						disableTor();
					}
				})
				.create();
			dialog.show();
			return false;
		}
		return true;
    }
    
    private void disableTor() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean(getResources().getString(R.string.settings_tor_key), false).commit();
    }
    
    private void installTor() {
        Uri uri = Uri.parse("market://search?q=pname:" + TOR_PACKAGE);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    
    private void startTor() {
        Intent intent = new Intent(TOR_PACKAGE);
        intent.setAction("org.torproject.android.START_TOR");
        startActivityForResult(intent, 1);
    }

}
