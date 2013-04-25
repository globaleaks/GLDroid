package org.globaleaks.android;

import org.globaleaks.model.Node;
import org.globaleaks.util.GLClient;
import org.globaleaks.util.GLClientCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements GLClientCallback  {

	public static GLClient gl = new GLClient();
	private ImageView logo;
	private TextView name;
	private TextView description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(android.R.id.home).setPadding(10, 1, 10, 1);
		View mainLayout = findViewById(R.id.main_layout);
		logo = (ImageView) mainLayout.findViewById(R.id.node_logo);
		name = (TextView) mainLayout.findViewById(R.id.node_name);
		description = (TextView) mainLayout.findViewById(R.id.node_description);

		setTitle(R.string.title_activity_main);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplication());
				String url = pref.getString("base_url", GLClient.DEMO_GLOBALEAKS);
				gl.setBaseUrl(url);
				gl.fetchMetadata(MainActivity.this);
			}
		});
		t.start();
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
		}
		if(intent != null) startActivity(intent);
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCompleteMetadata() {
		final Node node = gl.node;
		if(node == null) return;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				logo.setImageBitmap(node.getImage());
				name.setText(node.getName());
				description.setText(node.getDescription());
			}
		});
	}

}
