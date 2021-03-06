package org.globaleaks.droid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.globaleaks.droid.R;
import org.globaleaks.model.Context;
import org.globaleaks.model.Receiver;
import org.globaleaks.util.Logger;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ReceiversFragment extends Fragment implements SubmissionFragment {

    private boolean receiversEdited = false;
    
	public interface OnContextSelectedListener {
		void onContextSelected();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			// restoring fragment
			receiversEdited = true;
		}
	}
	
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_receivers, container, false);
		Spinner ctx = (Spinner) rootView.findViewById(R.id.context_dropdown);
		ctx.setAdapter(new ArrayAdapter<Context>(getActivity(), R.layout.context_item, getContexts()){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return getDropDownView(position, convertView, parent);
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View root = inflater.inflate(R.layout.context_item, parent,false);
				Context i = getItem(position);
				TextView name = (TextView) root.findViewById(R.id.contex_item_name);
				name.setText(i.getName());
				TextView description = (TextView) root.findViewById(R.id.contex_item_description);
				description.setText(i.getDescription());
				return root;
			}
		});
		ctx.setOnItemSelectedListener(new OnItemSelectedListener() {

			private void updateReceiversList(ArrayList<Receiver> list){
				final ListView recv = (ListView) rootView.findViewById(R.id.receivers_list);
				recv.setItemsCanFocus(false);
				Logger.i("checked count="+recv.getCheckedItemCount());
				SparseBooleanArray ch = recv.getCheckedItemPositions();
				if(!receiversEdited) {
				    // pre-select all receivers
				    GLApplication app = (GLApplication) getActivity().getApplicationContext();
				    app.addReceiver(list);
				}
				recv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					    receiversEdited = true;
						Logger.i("position=" + position +", ID="+id);
						Receiver o = (Receiver) recv.getItemAtPosition(position);
						Logger.i("item="+o.toString());
						CheckedTextView ct = (CheckedTextView) view.findViewById(R.id.receiver_item_name);
						ct.setChecked(!ct.isChecked());
						GLApplication app = (GLApplication) getActivity().getApplicationContext();
						if(ct.isChecked()) {
						    app.addReceiver(o);
						} else {
						    app.removeReceiver(o);
						}
					}
				});

				Logger.i("checked idx="+ch);
					
				recv.setAdapter(new ArrayAdapter<Receiver>(getActivity(),R.layout.receiver_item, android.R.layout.simple_list_item_multiple_choice, list){

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						View root = inflater.inflate(R.layout.receiver_item, parent,false);
						Receiver i = getItem(position);
						
						CheckedTextView name = (CheckedTextView) root.findViewById(R.id.receiver_item_name);
						name.setText(i.getName());
						GLApplication app = (GLApplication) getActivity().getApplicationContext();
						name.setChecked(app.getReceivers().contains(i));
						TextView description = (TextView) root.findViewById(R.id.receiver_item_description);
						description.setText(i.getDescription());
						ImageView image = (ImageView) root.findViewById(R.id.receiver_image);
						image.setImageBitmap(i.getImage());
						return root;
					}
				});
			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Context ctx = (Context) parent.getItemAtPosition(position);
				GLApplication app = (GLApplication) getActivity().getApplicationContext();
				app.setContext(ctx);
				updateReceiversList(getReceivers(ctx.getId()));
				((OnContextSelectedListener)getActivity()).onContextSelected();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				updateReceiversList(getReceivers(null));					
			}
		});
		return rootView;
	}

	public ArrayList<Context> getContexts() {
		ArrayList<Context> list = new ArrayList<Context>(MainActivity.gl.contexts.values());
		Collections.sort(list, new Comparator<Context>() {
			@Override
			public int compare(Context lhs, Context rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		return list;					
	}
	
	public ArrayList<Receiver> getReceivers(String ctxId){
		ArrayList<Receiver> list = new ArrayList<Receiver>();
		for (Receiver r : MainActivity.gl.receivers.values()) {
			if(ctxId == null || r.getContexts().contains(ctxId)) list.add(r);
		}
		Collections.sort(list, new Comparator<Receiver>() {

			@Override
			public int compare(Receiver lhs, Receiver rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		return list;
	}
	
	public String getTitle(Resources a) {
		return a.getString(R.string.title_fragment_receivers);
	}

}

