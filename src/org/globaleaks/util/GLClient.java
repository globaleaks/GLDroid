package org.globaleaks.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.globaleaks.model.Context;
import org.globaleaks.model.Node;
import org.globaleaks.model.Receiver;

import android.util.Log;

public class GLClient {


	private String baseUrl;
	private Node node;
	private Map<String,Context> contexts   = new HashMap<String, Context>();
	private Map<String,Receiver> receivers = new HashMap<String, Receiver>();
	private Parser parser = new Parser();
	
	public GLClient(String baseUrl){
		this.baseUrl = baseUrl;
	}
	
	public void fetchMetadata(){
		node = getNode();
		Log.i("GL", node.toString());
		List<Context> ctxs = getContexts();
		for (Iterator<Context> i = ctxs.iterator(); i.hasNext();) {
			Context context = (Context) i.next();
			contexts.put(context.getId(), context);
			Log.i("GL", context.toString());
		}
		List<Receiver> recv = getReceivers();
		for (Iterator<Receiver> i = recv.iterator(); i.hasNext();) {
			Receiver r = (Receiver) i.next();
			receivers.put(r.getId(), r);
			Log.i("GL", r.toString());
		}
		
	}
	
	private List<Receiver> getReceivers() {
		URL url;
		try {
			url = new URL(baseUrl + "/receivers");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			return parser.parseReceivers(in);
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Node getNode() {
		URL url;
		try {
			url = new URL(baseUrl + "/node");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			return parser.parseNode(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Context> getContexts() {
		URL url;
		try {
			url = new URL(baseUrl + "/contexts");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			return parser.parseContexts(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
