package org.globaleaks.util;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.globaleaks.model.Context;
import org.globaleaks.model.Field;
import org.globaleaks.model.Node;
import org.globaleaks.model.Option;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;

import android.util.JsonReader;

public class Parser {

	public static DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.UK);
	
	private GLClient client;
	
	public List<Context> parseContexts(Reader in) throws UnsupportedEncodingException, IOException {
		JsonReader reader = new JsonReader(in);
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


	public Node parseNode(Reader in) throws UnsupportedEncodingException, IOException {
		JsonReader reader = new JsonReader(in);
		try {
			Node n = new Node();
		    reader.beginObject();
		    while (reader.hasNext()) {
		      String name = reader.nextName();
		      if (name.equals("name")) {
		        n.setName(reader.nextString());
		      } else if (name.equals("description")) {
		        n.setDescription(reader.nextString());
		      } else if (name.equals("email")) {
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


	public List<Receiver> parseReceivers(Reader in) throws UnsupportedEncodingException, IOException, ParseException {
		JsonReader reader = new JsonReader(in);
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
			} else if(name.equals("options")) {
				reader.beginArray();
				ArrayList<Option> opts = new ArrayList<Option>();
				while(reader.hasNext()){
					Option opt = readOption(reader);
					opts.add(opt);
				}
				f.setOptions(opts);
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return f;
	}

	private Option readOption(JsonReader reader) throws IOException {
		Option o = new Option();
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("order")) {
				o.setOrder(reader.nextInt());
			} else if(name.equals("value")) {
				o.setValue(reader.nextString());
			} else if(name.equals("name")) {
				o.setName(reader.nextString());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return o;
	}


	public Submission parseSubmission(Reader in) throws IOException, ParseException {
		JsonReader reader = new JsonReader(in);
		Submission s = new Submission();
		/*
		{"wb_fields": {}, "pertinence": "0", "receivers": [], "access_limit": 42, "receipt": "", "context_gus": "a89b7f94-8d88-4c49-b8cf-9bc5da985792", 
			"creation_date": "Tue Mar  5 21:16:37 2013", "escalation_threshold": "0", "download_limit": 42, 
			"submission_gus": "574ce9a9-1805-49e1-ae60-29c220120a63", "mark": "submission", 
			"id": "574ce9a9-1805-49e1-ae60-29c220120a63", "files": []
				
		}
		*/
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("pertinence")){
				s.setPertinance(reader.nextInt());
			} else if (name.equals("access_limit")){
				s.setAccessLimit(reader.nextInt());
			} else if (name.equals("receipt")) {
				s.setReceipt(reader.nextString());
			} else if (name.equals("context_gus")) {
				s.setCtx(client.contexts.get(reader.nextString()));
			} else if(name.equals("creation_date")) {
				s.setCreationDate(dateFormat.parse(reader.nextString()));
			} else if(name.equals("escalation_threshold")){
				s.setEscalationThreshold(reader.nextInt());
			} else if(name.equals("download_limit")){
				s.setDownloadLimit(reader.nextInt());
			} else if(name.equals("submission_gus")){
				s.setId(reader.nextString());
			} else if(name.equals("id")){
				s.setId(reader.nextString());
			} else if(name.equals("mark")){
				s.setMark(reader.nextString());
				// TODO files
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		return s;
	}


	public void setGLClient(GLClient glClient) {
		this.client = glClient;
	}

}
