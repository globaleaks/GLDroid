package org.globaleaks.gldroid;

import org.globaleaks.model.Node;
import org.globaleaks.util.GLClient;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity  {

	public static GLClient gl = new GLClient();
	private ImageView logo;
	private TextView name;
	private TextView description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		setContentView(R.layout.activity_main);
		findViewById(android.R.id.home).setPadding(10, 1, 10, 1);
		View mainLayout = findViewById(R.id.main_layout);
		logo = (ImageView) mainLayout.findViewById(R.id.node_logo);
		name = (TextView) mainLayout.findViewById(R.id.node_name);
		description = (TextView) mainLayout.findViewById(R.id.node_description);
		setTitle(R.string.title_activity_main);
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
				intent = new Intent(this, CreateSubmissionActivity.class);
				break;
			case R.id.action_view:
				
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

	private void refresh(final boolean eraseCache) {
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
        }

        @Override
        protected void onPostExecute(Node result) {
            onCompleteMetadata(result);
        }
    }
}
