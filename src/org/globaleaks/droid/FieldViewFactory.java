package org.globaleaks.droid;

import java.util.Iterator;

import org.globaleaks.droid.FieldsFragment.OnFieldValueChangedListener;
import org.globaleaks.model.Field;
import org.globaleaks.model.Field.FieldType;
import org.globaleaks.model.FieldValue;
import org.globaleaks.model.Option;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class FieldViewFactory {

	public static View create(Context context, final Field field, final OnFieldValueChangedListener listener) {
		if(field.getType().equals(FieldType.TEXT)) {
			final EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;
		} else if(field.getType().equals(FieldType.PARAGRAPHS)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
			e.setMinLines(5);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;
		} else if(field.getType().equals(FieldType.PHONE)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setInputType(InputType.TYPE_CLASS_PHONE);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;		
		} else if(field.getType().equals(FieldType.NUMBER)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setInputType(InputType.TYPE_CLASS_NUMBER);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;		
		} else if(field.getType().equals(FieldType.EMAIL)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;		
		} else if(field.getType().equals(FieldType.URL)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			setFieldListener(field, listener, e);
			return e;		
		} else if(field.getType().equals(FieldType.RADIO)) {
			RadioGroup radio = new RadioGroup(context);
			if(field.getOptions() != null) {
				for (Iterator<Option> i = field.getOptions().iterator(); i.hasNext();) {
					Option o = (Option) i.next();
					RadioButton rb = new RadioButton(context);
					rb.setText(o.getName());
					rb.setTag(o.getValue());
					radio.addView(rb);
				}
			}
			// TODO
			radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int checked = group.getCheckedRadioButtonId();
                    group.getChildCount();
                }
            });
			return radio;
		} else if(field.getType().equals(FieldType.SELECT)) {
			Spinner s = new Spinner(context);
			s.setAdapter(new ArrayAdapter<Option>(context, android.R.layout.simple_list_item_1, field.getOptions()){});
			//TODO set listener
			return s;
		} else if(field.getType().equals(FieldType.CHECKBOXES)) {
			CheckBox ch = new CheckBox(context);
			ch.setHint(field.getHint());
			//TODO set listener
			return ch;
		} else {
			TextView t = new TextView(context);
			t.setText("NOT IMPLEMENTED FIELD TYPE: ");
			//TODO set listener
			return t;
		}
	}

    private static void setFieldListener(final Field field, final OnFieldValueChangedListener listener, final EditText e) {
        e.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                FieldValue value = new FieldValue(field.getName(), e.getText().toString());
                listener.onFieldValueChanged(value);
            }
        });
    }

}
