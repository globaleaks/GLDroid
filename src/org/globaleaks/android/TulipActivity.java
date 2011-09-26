package org.globaleaks.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class TulipActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tulip);
        
        Intent i = getIntent();
        if(i == null) return;
        String tulip = i.getStringExtra("tulip");
        if(tulip != null) {
            EditText et = (EditText) findViewById(R.id.tulipText);
            et.setText(tulip);
        }
    }
}
