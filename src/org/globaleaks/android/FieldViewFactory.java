package org.globaleaks.android;

import java.util.Iterator;

import org.globaleaks.model.Field;
import org.globaleaks.model.Field.FieldType;
import org.globaleaks.model.Option;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FieldViewFactory {

	public static View create(Context context, Field field) {
		if(field.getType().equals(FieldType.TEXT)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			return e;
		} else if(field.getType().equals(FieldType.PARAGRAPHS)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
			e.setMinLines(5);
			e.setHint(field.getHint());
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
			return radio;
		} else if(field.getType().equals(FieldType.PHONE)) {
			EditText e = new EditText(context);
			e.setRawInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			e.setInputType(InputType.TYPE_CLASS_PHONE);
			e.setMaxLines(1);
			e.setHint(field.getHint());
			return e;			
		} else {
			TextView t = new TextView(context);
			t.setText("NOT IMPLEMENTED FIELD TYPE");
			return t;
		}
	}

}
