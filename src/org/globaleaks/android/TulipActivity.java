package org.globaleaks.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TulipActivity extends Activity
implements OnClickListener
{

    
    private static final int CODE_ADD_CONTACT = 0;
    private Button shareButton;
    private Button addContact;
    private String tulip;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tulip);
        
        setLayout();
        
        Intent i = getIntent();
        if(i == null) return;
        tulip = i.getStringExtra("tulip");
        if(tulip != null) {
            EditText et = (EditText) findViewById(R.id.tulipText);
            et.setText(tulip);
            shareButton.setEnabled(true);
            addContact.setEnabled(true);
        } else {
            shareButton.setEnabled(false);
            addContact.setEnabled(false);
        }
    }

    private void setLayout() {
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(this);
        
        addContact = (Button) findViewById(R.id.addContactButton);
        addContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == shareButton) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/*");
            i.putExtra(Intent.EXTRA_TEXT, tulip);
            startActivity(i);
        } else if(view == addContact) {
            Intent add = new Intent(ContactsContract.Intents.Insert.ACTION, ContactsContract.Contacts.CONTENT_URI);
            add.putExtra(ContactsContract.Intents.Insert.PHONE, tulip);
            startActivityForResult(add, CODE_ADD_CONTACT);
        }
    }
}
