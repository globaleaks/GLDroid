package org.globaleaks.android;

import java.net.Socket;

import org.globaleaks.android.net.WebClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity
implements OnClickListener
{
    
    public static final String LOG_TAG = "GLDroid";
    private static final int CODE_SELECT_IMG = 0;
    private static final int CODE_TAKE_IMG   = 1;
    private static final String TMP_IMAGE = "globaleaks.jpg";
    
    private Button submitButton;
    private Button clearButton;
    private Button takePicture;
    private Button selectPicture;
    private ImageView torStatus;
    
    private Bundle bundle;
    private Uri uriImageResult;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");
        setLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTorIcon();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    private void setLayout() {
        setContentView(R.layout.main);
        
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);
        takePicture = (Button) findViewById(R.id.takePictureButton);
        takePicture.setOnClickListener(this);
        selectPicture = (Button) findViewById(R.id.selectPictureButton);
        selectPicture.setOnClickListener(this);
        
        torStatus = (ImageView) findViewById(R.id.torStatusImage);
        refreshTorIcon();
    }

    private void refreshTorIcon() {
        if(isTorInstalled() && isTorEnabled()) {
            torStatus.setVisibility(ImageView.VISIBLE);
        } else {
            torStatus.setVisibility(ImageView.INVISIBLE);
        }
        int iconId = R.drawable.tor_off;
        if(torProxy()) {
            iconId = R.drawable.tor_on;
        }
        torStatus.setImageDrawable(getResources().getDrawable(iconId));
        
        if(isTorEnabled() && !(isTorInstalled() && isTorProxyRunning())) {
            submitButton.setEnabled(false);
            //submitButton.setHint(getResources().getString(R.string.disable_tor_hint));
        } else {
            submitButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if(bundle == null) {
            bundle = new Bundle();
        }
        System.out.println("onClick");
        if(view == submitButton) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String baseUrl = sp.getString(getResources().getString(R.string.base_url), "demo.globaleaks.org");
            WebClient client = new WebClient(baseUrl, this);
            EditText title = (EditText) findViewById(R.id.submitTitleText);
            EditText desc  = (EditText) findViewById(R.id.submitDescriptionText);
            Intent intent = new Intent(this, WebClient.class);
            bundle.putString("title", title.getText().toString());
            bundle.putString("description", desc.getText().toString());
            intent.putExtras(bundle);
            try {
                client.submit(intent);
            } catch (Exception e) {
                e.printStackTrace();
                showDialog( "Error submitting data");
            }
        } else if(view == clearButton) {
            resetFields();
        } else if (view == selectPicture) {
            try {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CODE_SELECT_IMG);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to open Gallery", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Error loading gallery to select image: " + e.getMessage(), e);
            }
        } else if (view == takePicture) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, TMP_IMAGE);
            values.put(MediaStore.Images.Media.DESCRIPTION,"globaleaks");
            uriImageResult = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra( MediaStore.EXTRA_OUTPUT, uriImageResult);
            startActivityForResult(intent, CODE_TAKE_IMG);

        }
    }

    private void resetFields() {
        if(bundle != null) bundle.clear();
        EditText title = (EditText) findViewById(R.id.submitTitleText);
        title.setText("");
        EditText desc = (EditText) findViewById(R.id.submitDescriptionText);
        desc.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_SELECT_IMG) {
                if (data != null) {
                    uriImageResult = data.getData();
                    if (uriImageResult != null){
                        Log.i(LOG_TAG, "Selected image at " + uriImageResult);
                        bundle.putString("img", uriImageResult.toString());
                    } else {
                        Toast.makeText(this, "Unable to load photo.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Unable to load photo.", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == CODE_TAKE_IMG) {
                bundle.putString("img", uriImageResult.toString());
            }
        } else {
            setLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                return true;
            case R.id.menu_settings:
                Intent i = new Intent(this, Preferences.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.options, menu);
        return true;
    }

    private void showDialog(String msg)
    {
         new AlertDialog.Builder(this)
         .setTitle(getString(R.string.app_name))
         .setMessage(msg)
         .setPositiveButton("OK", null)
         .setNegativeButton("Cancel", null)
         .create().show();
    }
    
    private boolean isTorInstalled() {
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo("org.torproject.android", PackageManager.GET_META_DATA);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private boolean isTorProxyRunning() {
        try {
            Socket s = new Socket("127.0.0.1",8118);
            s.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    
    private boolean isTorEnabled() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean tor = sp.getBoolean(getResources().getString(R.string.settings_tor_key), false);
        return tor;
    }

    public boolean torProxy() {
        return isTorEnabled() && isTorInstalled() && isTorProxyRunning();
    }
    
}