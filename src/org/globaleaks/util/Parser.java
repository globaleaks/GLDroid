package org.globaleaks.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.globaleaks.model.Context;
import org.globaleaks.model.Field;
import org.globaleaks.model.Node;
import org.globaleaks.model.Receiver;

import android.util.JsonReader;

public class Parser {


	List<Context> parseContexts(InputStream in) throws UnsupportedEncodingException, IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			List<Context> ctxs = new ArrayList<Context>();
		    reader.beginArray();
		    while (reader.hasNext()) {
		    	Context ctx = readContext(reader);
		    	ctxs.add(ctx);
		    }
		    reader.endArray();
		    return ctxs;
		} finally {
			reader.close();
		}
	}


	Node parseNode(InputStream in) throws UnsupportedEncodingException, IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			Node n = new Node();
		    reader.beginObject();
		    while (reader.hasNext()) {
		      String name = reader.nextName();
		      if (name.equals("name")) {
		        n.setName(reader.nextString());
		      } else if (name.equals("description")) {
		        n.setDescription(reader.nextString());
		      } else if (name.equals("description")) {
		        n.setEmail(reader.nextString());
		        /* TBV add language
		      } else if (name.equals("languages")) {
		    	n.setLanguages(readLanguages(reader));
		    	*/
		      } else {
		        reader.skipValue();
		      }
		    }
		    reader.endObject();
		    return n;
		} finally {
			reader.close();
		}
	}


	List<Receiver> parseReceivers(InputStream in) throws UnsupportedEncodingException, IOException, ParseException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			List<Receiver> recv = new ArrayList<Receiver>();
		    reader.beginArray();
		    while (reader.hasNext()) {
		    	Receiver r = readReceiver(reader);
		    	recv.add(r);
		    }
		    reader.endArray();
		    return recv;
		} finally {
			reader.close();
		}
	}

	Receiver readReceiver(JsonReader reader) throws IOException, ParseException {
		Receiver recv = new Receiver();
		reader.beginObject();
		
		while(reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("receiver_gus")) {
				recv.setId(reader.nextString());
			} else if (name.equals("description")) {
				recv.setDescription(reader.nextString());
			} else if (name.equals("name")) {
				recv.setName(reader.nextString());
				/*
			} else if (name.equals("creation_date")){
				recv.setCreationDate(reader.nextString());
			} else if(name.equals("update_date")){
				recv.setUpdateDate(reader.nextString());
				*/
			} else if(name.equals("can_delete_submission")){
				recv.setCanDeleteSubmission(reader.nextBoolean());
			} else if(name.equals("receiver_level")){
				recv.setReceiverLevel(reader.nextInt());
			} else if (name.equals("contexts")){
				recv.setContexts(readContextsIDs(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return recv;
	}

	Set<String> readContextsIDs(JsonReader reader) throws IOException {
		Set<String> contexts = new HashSet<String>();
		reader.beginArray();
		while(reader.hasNext()){
			contexts.add(reader.nextString());
		}
		reader.endArray();
		return contexts;
	}


	Context readContext(JsonReader reader) throws IOException {
		Context ctx = new Context();
		reader.beginObject();
		
		while(reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("context_gus")) {
				ctx.setId(reader.nextString());
			} else if (name.equals("description")) {
				ctx.setDescription(reader.nextString());
			} else if (name.equals("name")) {
				ctx.setName(reader.nextString());
			} else if (name.equals("fields")) {
				ctx.setFields(readFields(reader));
			} else if (name.equals("receivers")){
				ctx.setReceivers(readReceiversIDs(reader));
			} else if(name.equals("selectable_receiver")) {
				ctx.setSelectableReceiver(reader.nextBoolean());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return ctx;
	}

	Set<String> readReceiversIDs(JsonReader reader) throws IOException {
		Set<String> receivers = new HashSet<String>();
		reader.beginArray();
		while(reader.hasNext()){
			receivers.add(reader.nextString());
		}
		reader.endArray();
		return receivers;
	}

	List<Field> readFields(JsonReader reader) throws IOException {
		List<Field> fields = new ArrayList<Field>();
		reader.beginArray();
		while(reader.hasNext()){
			Field f = readField(reader);
			fields.add(f);
		}
		reader.endArray();
		return fields;
	}

	Field readField(JsonReader reader) throws IOException {
		Field f = new Field();
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("name")){
				f.setName(reader.nextString());
			} else if (name.equals("label")){
				f.setLabel(reader.nextString());
			} else if (name.equals("hint")) {
				f.setHint(reader.nextString());
			} else if (name.equals("required")) {
				f.setRequired(reader.nextBoolean());
			} else if(name.equals("type")) {
				f.setType(Field.getFieldType(reader.nextString()));
			} else if(name.equals("presentation_order")){
				f.setPresentationOrder(reader.nextInt());
			} else if(name.equals("value")){
				f.setValue(reader.nextString());
			}
		}
		reader.endObject();
		return f;
	}

}
