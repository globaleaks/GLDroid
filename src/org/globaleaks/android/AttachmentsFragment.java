package org.globaleaks.android;

import java.util.ArrayList;

import org.globaleaks.model.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AttachmentsFragment extends Fragment implements SubmissionFragment {
	
    private static final int CODE_SELECT_IMG = 0;
    private static final int CODE_TAKE_IMG   = 1;
    private static final String TMP_IMAGE = "globaleaks.jpg";
    
    private Uri uriImageResult;
    private ListView attachments;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_attachments, container, false);
		attachments = (ListView) rootView.findViewById(R.id.attachment_list);
		attachments.setAdapter(new ArrayAdapter<Uri>(getActivity(), R.id.attachment_item, new ArrayList<Uri>()){

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if(convertView == null)
                    convertView = inflater.inflate(R.layout.attachment_item, parent,false);
                Uri a = getItem(position);
                TextView name = (TextView) convertView.findViewById(R.id.attachment_label);
                name.setText(a.toString());
                ImageButton delete = (ImageButton) convertView.findViewById(R.id.attachment_delete);
                delete.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        removePicture(getItem(position));
                    }
                });
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getView(position, convertView, parent);
            }
		    
		});

		final Button takePhoto = (Button) rootView.findViewById(R.id.takePictureButton);
		takePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v != takePhoto) return;

	            ContentValues values = new ContentValues();
	            values.put(MediaStore.Images.Media.TITLE, TMP_IMAGE);
	            values.put(MediaStore.Images.Media.DESCRIPTION,"globaleaks");
	            uriImageResult = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            intent.putExtra( MediaStore.EXTRA_OUTPUT, uriImageResult);
	            startActivityForResult(intent, CODE_TAKE_IMG);
			}
		});
		final Button selectPhoto = (Button) rootView.findViewById(R.id.selectPictureButton);
		selectPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v != selectPhoto) return;
	            try {
	                Intent intent = new Intent(Intent.ACTION_PICK);
	                intent.setType("image/*");
	                startActivityForResult(intent, CODE_SELECT_IMG);
	            } catch (Exception e) {
	                Toast.makeText(getActivity(), "Unable to open Gallery", Toast.LENGTH_LONG).show();
	                Log.e("GL", "Error loading gallery to select image: " + e.getMessage(), e);
	            }	
			}
		});
		return rootView;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_SELECT_IMG) {
                if (data != null) {
                    uriImageResult = data.getData();
                    if (uriImageResult != null){
                        Log.i("GL", "Selected image at " + uriImageResult);
                        addPicture(uriImageResult);
                    } else {
                        Toast.makeText(getActivity(), "Unable to load photo.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to load photo.", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == CODE_TAKE_IMG) {
                addPicture(uriImageResult);
            }
        } 
    }

	private void addPicture(Uri uri) {
        ArrayAdapter<Uri> list = (ArrayAdapter<Uri>) attachments.getAdapter();
        list.remove(uri);
		list.add(uri);
		GLApplication app = (GLApplication) getActivity().getApplication();
		File file = new File(uri);
		app.addFile(file);
	}
	
	private void removePicture(Uri uri){
	    @SuppressWarnings("unchecked")
        ArrayAdapter<Uri> list = (ArrayAdapter<Uri>) attachments.getAdapter();
	    list.remove(uri);
        GLApplication app = (GLApplication) getActivity().getApplication();
        app.removeFile(new File(uri));

	}

	@Override
	public String getTitle(Resources a) {
		return a.getString(R.string.title_fragment_attachments);
	}

	
}