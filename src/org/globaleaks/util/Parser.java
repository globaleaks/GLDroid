package org.globaleaks.util;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.globaleaks.model.AuthSession;
import org.globaleaks.model.Context;
import org.globaleaks.model.ErrorObject;
import org.globaleaks.model.Field;
import org.globaleaks.model.Node;
import org.globaleaks.model.Option;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;

import android.util.JsonReader;

public class Parser {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",Locale.UK);
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
			} else if(name.equals("receipt_description")){
			    ctx.setReceiptDescription(reader.nextString());
			} else if(name.equals("submission_introduction")) {
			    ctx.setSubmissionIntroduction(reader.nextString());
			} else if(name.equals("submission_disclaimer")){
			    ctx.setSubmissinoDisclaimer(reader.nextString());
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
		try {
			Submission s = new Submission();
			/*
			{"wb_fields": {"Full description": "ijhubb", "Short title": "jjhjjv"}, "pertinence": "0", 
			"receivers": ["24845644-3c6a-488d-b748-5aa15933a0e5", "1e61def9-f031-451b-be68-f177d9f477f6", "fb61111d-1259-47d2-ad0a-69b2d23f801c"], 
			"expiration_date": "2013-06-24T23:39:58.218079", "access_limit": 42, "receipt": "8030426621", "context_gus": "281ba696-c414-4782-8b51-43c709fb1efc", 
			"creation_date": "2013-06-10T00:39:58.218114", "escalation_threshold": "0", "download_limit": 42, "submission_gus": "7f4e24fd-51f2-4fe7-bedc-4f8e77e424f1", 
			"mark": "finalize", "id": "7f4e24fd-51f2-4fe7-bedc-4f8e77e424f1", "files": ["cc0e6ca9-ae69-4957-9983-9c96ca20518a"]}		
			*/
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if(name.equals("error_message")) {
					throw new ParseException(reader.nextString(), 0);
				} else if(name.equals("pertinence")){
					s.setPertinance(reader.nextInt());
				} else if (name.equals("access_limit")){
					s.setAccessLimit(reader.nextInt());
				} else if (name.equals("receipt")) {
					s.setReceipt(reader.nextString());
				} else if (name.equals("context_gus")) {
					s.setCtx(client.contexts.get(reader.nextString()));
				} else if(name.equals("creation_date")) {
					s.setCreationDate(parseDate(reader.nextString()));
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
				} else if(name.equals("receivers")) {
					reader.beginArray();
					while(reader.hasNext()) {
						String id = reader.nextString();
						s.addReceiver(client.receivers.get(id));
					}
					reader.endArray();
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			return s;
		} finally {
			reader.close();
		}
	}

	/**
		{"error_message": "Authentication Failed", "error_code": 29}
	 */
	public ErrorObject parseError(Reader in) throws IOException, ParseException {
		JsonReader reader = new JsonReader(in);
		try {
			ErrorObject error = new ErrorObject();
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if(name.equals("error_message")) {
					error.setMessage(reader.nextString());
				} else if(name.equals("error_code")){
					error.setCode(reader.nextInt());
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			return error;
		} finally {
			reader.close();
		}
	}


	/**
		{"error_message": "Authentication Failed", "error_code": 29}
	 */
	public AuthSession parseAuthSession(Reader in) throws IOException  {
		JsonReader reader = new JsonReader(in);
		try {
			AuthSession session = new AuthSession();
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if(name.equals("user_id")) {
					session.setUserId(reader.nextString());
				} else if(name.equals("session_id")){
					session.setSessionId(reader.nextString());
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			return session;
		} finally {
			reader.close();
		}
	}

	public void setGLClient(GLClient glClient) {
		this.client = glClient;
	}


	public static Date parseDate(String date) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			Logger.e("Wrong datetime format: " + date);
			return null;
		}
	}

}
