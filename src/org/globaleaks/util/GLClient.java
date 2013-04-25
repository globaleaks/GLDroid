package org.globaleaks.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.globaleaks.model.Context;
import org.globaleaks.model.Node;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GLClient {

	public static final String DEMO_GLOBALEAKS = "http://demo.globaleaks.org:8083";
	public static final String DEV_GLOBALEAKS = "http://dev.globaleaks.org:8082";
	
	public String baseUrl;
	public Node node;
	public Map<String,Context> contexts   = new HashMap<String, Context>();
	public Map<String,Receiver> receivers = new HashMap<String, Receiver>();
	private Parser parser = new Parser();
	
	public GLClient(){
		parser.setGLClient(this);
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		if(!baseUrl.startsWith("http")) {
			this.baseUrl = DEMO_GLOBALEAKS;
		}
		node = null;
		contexts.clear();
		receivers.clear();
	}

	public void fetchMetadata(GLClientCallback callback){
		node = getNode();
		if(node == null) return;
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
			fetchReceiverImage(r);
			receivers.put(r.getId(), r);
			Log.i("GL", r.toString());
		}
		callback.onCompleteMetadata();
	}
	
	private void fetchReceiverImage(Receiver r) {
		if(r == null) return;
		URL url;
		try {
			url = new URL(baseUrl + "/static/" + r.getId() + "_120.png");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			Bitmap b = BitmapFactory.decodeStream(con.getInputStream());
			r.setImage(b);
			Log.i("GL", b.getConfig() + " image " + b.getHeight() + " x " + b.getWidth());
		} catch( Exception e) {
			e.printStackTrace();
		}
	}

	public Submission createSubmission(Context ctx){
		Submission s = new Submission(ctx);
		URL url;
		try {
			url = new URL(baseUrl + "/submission");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.getOutputStream().write(s.toJSON().getBytes());
			Log.i("GL","Submission: " + s.toJSON());
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			con.connect();
			return parser.parseSubmission(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Submission updateSubmission(Submission s){
		URL url;
		s.setFinalize(true);
		try {
			url = new URL(baseUrl + "/submission/" + s.getId());
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.getOutputStream().write(s.toJSON().getBytes());
			Log.i("GL","Submission: " + s.toJSON());
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			con.connect();
			return parser.parseSubmission(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public void uploadFile(){
		throw new RuntimeException("Not implemented yet");
	}
	
	public void fetchTip(String tipId) {
		throw new RuntimeException("Not implemented yet");
	}
	
	private List<Receiver> getReceivers() {
		URL url;
		try {
			url = new URL(baseUrl + "/receivers");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			return parser.parseReceivers(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Node getNode() {
		Node node = null;
		URL url;
		try {
			url = new URL(baseUrl + "/node");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			node = parser.parseNode(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		try {
			url = new URL(baseUrl + "/static/globaleaks_logo.png");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			Bitmap b = BitmapFactory.decodeStream(con.getInputStream());
			node.setImage(b);
			Log.i("GL", b.getConfig() + " image " + b.getHeight() + " x " + b.getWidth());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return node;
	}

	private List<Context> getContexts() {
		URL url;
		try {
			url = new URL(baseUrl + "/contexts");
			Log.i("GL", url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(con.getInputStream());
			Log.i("GL","Start pargsing JSON");
			return parser.parseContexts(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
