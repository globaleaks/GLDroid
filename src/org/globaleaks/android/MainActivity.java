package org.globaleaks.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.globaleaks.android.net.WebClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import android.widget.Toast;

public class MainActivity extends Activity
implements OnClickListener
{
    
    public static final String LOG_TAG = "GLDroid";
    private static final int CODE_SELECT_IMG = 0;
    private static final int CODE_TAKE_IMG   = 1;
    private static final String TMP_IMAGE = "globaleaks.jpg";
    
    private Button submitButton;
    private Button takePicture;
    private Button selectPicture;
    
    private Bundle bundle;
    private Uri uriImageResult;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");
        setLayout();
    }

    private void setLayout() {
        setContentView(R.layout.main);
        
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        takePicture = (Button) findViewById(R.id.takePictureButton);
        takePicture.setOnClickListener(this);
        selectPicture = (Button) findViewById(R.id.selectPictureButton);
        selectPicture.setOnClickListener(this);
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
            WebClient client = new WebClient(baseUrl);
            EditText title = (EditText) findViewById(R.id.submitTitleText);
            EditText desc  = (EditText) findViewById(R.id.submitDescriptionText);
            Intent intent = new Intent(this, WebClient.class);
            bundle.putString("title", title.getText().toString());
            bundle.putString("description", desc.getText().toString());
            intent.putExtras(bundle);
            try {
                client.submit(intent, this);
            } catch (Exception e) {
                e.printStackTrace();
                showDialog( "Error submitting data");
            }
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

}