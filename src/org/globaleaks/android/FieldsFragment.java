package org.globaleaks.android;

import java.util.ArrayList;
import java.util.List;

import org.globaleaks.model.Context;
import org.globaleaks.model.Field;
import org.globaleaks.model.FieldValue;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FieldsFragment extends Fragment implements SubmissionFragment {
	
    public interface OnFieldValueChangedListener {
        void onFieldValueChanged(FieldValue value);
    }

    
    private ListView fields;
	private LayoutInflater inflater;
	private Context selectedContext;
	private OnFieldValueChangedListener fieldListener;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fields, container, false);
		fields = (ListView) rootView.findViewById(R.id.fields_list);
		this.inflater = inflater;
		fieldListener = new OnFieldValueChangedListener() {
            
            @Override
            public void onFieldValueChanged(FieldValue value) {
                GLApplication app = (GLApplication) getActivity().getApplicationContext();
                app.addField(value);
            }
        };
		refreshFields();
		fields.setOnFocusChangeListener(new OnFocusChangeListener() {
            
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("GL", "view " + v + " has focus = " + hasFocus);
                
            }
        });
		fields.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		return rootView;
	}

	public void refreshFields() {
	    Activity a = getActivity();
	    if(a == null) {
	        // eg display rotation => activity destroyed and recreated
	        return;
	    }
	    
	    Context ctx = ((GLApplication)a.getApplicationContext()).getContext();
        if(selectedContext == ctx) return;
        selectedContext = ctx;
		fields.setAdapter(new ArrayAdapter<Field>(a, R.id.field_item,getFields(ctx)){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			    if(convertView != null) return convertView;
				ViewGroup root = (ViewGroup) inflater.inflate(R.layout.field_item, parent,false);
				Field f = getItem(position);
				TextView name = (TextView) root.findViewById(R.id.field_label);
				name.setText(f.getName());
				View fieldValue = FieldViewFactory.create(getActivity(), f, fieldListener);
				
				root.addView(fieldValue);
			    return root;
			}
			
		});
	}

	private List<Field> getFields(Context ctx) {
		if(ctx == null) return new ArrayList<Field>();
		
		List<Field> list = ctx.getFields();			
		return list;
	}

	@Override
	public String getTitle(Resources a) {
		return a.getString(R.string.title_fragment_fields);
	}
	
}

