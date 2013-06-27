package org.globaleaks.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.globaleaks.droid.GLApplication;
import org.globaleaks.droid.MainActivity;
import org.globaleaks.model.AuthSession;
import org.globaleaks.model.Context;
import org.globaleaks.model.Credential;
import org.globaleaks.model.ErrorObject;
import org.globaleaks.model.File;
import org.globaleaks.model.Node;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;
import org.globaleaks.model.Tip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;

public class GLClient {

	public static final String DEMO_GLOBALEAKS = "http://demo.globaleaks.org:8082";
	public static final String DEV_GLOBALEAKS = "http://dev.globaleaks.org:8083";
	
	public String baseUrl;
	public Node node;
	public Map<String,Context> contexts   = new HashMap<String, Context>();
	public Map<String,Receiver> receivers = new HashMap<String, Receiver>();
	private Parser parser = new Parser();
	private int cache = 0;
	private static int connection = 0;
	private String sessionId = null;
	private boolean torEnabled = false;
	
	public GLClient(){
		parser.setGLClient(this);
	}
	
	public void setTor(boolean enabled) {
		torEnabled = enabled;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl, android.content.Context context) {
		this.baseUrl = baseUrl;
		/*
		if(!baseUrl.startsWith("http")) {
			this.baseUrl = DEMO_GLOBALEAKS;
		}
		*/
		node = null;
		contexts.clear();
		receivers.clear();
        installCache(context);
	}

    private void installCache(android.content.Context context) {
        try {
            java.io.File httpCacheDir = new java.io.File(context.getExternalCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
          } catch (IOException e) {
            Logger.i("HTTP response cache failed:" + e);
          }
    }

	public void fetchMetadata(){
		node = fetchNode();
		if(node == null) return;
		Logger.i(node.toString());
		List<Context> ctxs = fetchContexts();
		for (Iterator<Context> i = ctxs.iterator(); i.hasNext();) {
			Context context = (Context) i.next();
			contexts.put(context.getId(), context);
			Logger.i(context.toString());
		}
		List<Receiver> recv = fetchReceivers();
		for (Iterator<Receiver> i = recv.iterator(); i.hasNext();) {
			Receiver r = (Receiver) i.next();
			fetchReceiverImage(r);
			receivers.put(r.getId(), r);
			Logger.i(r.toString());
		}
	}
	
	private void fetchReceiverImage(Receiver r) {
		if(r == null) return;
		try {
			HttpURLConnection con = createConnection(baseUrl + "/static/" + r.getId() + "_120.png");
			con.setRequestMethod("GET");
			Bitmap b = BitmapFactory.decodeStream(con.getInputStream());
			r.setImage(b);
			Logger.i(b.getConfig() + " image " + b.getHeight() + " x " + b.getWidth());
		} catch( Exception e) {
			e.printStackTrace();
		}
	}

	private HttpURLConnection createConnection(String url) throws MalformedURLException, IOException {
		URL u = new URL(url);
		Logger.i("URL-" + (++connection) + ": " + u.toString());
		HttpURLConnection con = null;
		if(torEnabled) {
			//Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(MainActivity.PROXY_HOST, MainActivity.PROXY_PORT_SOCKS));
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(MainActivity.PROXY_HOST, MainActivity.PROXY_PORT_HTTP));
			con = (HttpURLConnection) u.openConnection(proxy);
		} else {
			con = (HttpURLConnection) u.openConnection();	
		}
		
		con.setUseCaches(true);
		//con.addRequestProperty("Cache-Control", "only-if-cached");
		
		con.addRequestProperty("Cache-Control", "max-stale=" + cache);
		if(sessionId != null) {
			con.addRequestProperty("X-Session", sessionId);
		}
		return con;
	}

	public Submission createSubmission(Context ctx){
		Submission s = new Submission(ctx);
		try {
			HttpURLConnection con = createConnection(baseUrl + "/submission");
			con.setRequestMethod("POST");
			Logger.i("Submission: " + s.toJSON());
			InputStream in = null;
			try {
	            con.getOutputStream().write(s.toJSON().getBytes());
	            in = new BufferedInputStream(con.getInputStream());
            } catch (Exception e) {
            }
			Logger.i("Start pargsing JSON");
			con.connect();
			Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
			return parser.parseSubmission(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Submission updateSubmission(Submission s){
		s.setFinalize(true);
		try {
			HttpURLConnection con = createConnection(baseUrl + "/submission/" + s.getId());
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			con.connect();
			InputStream in = null;
			try {
	            con.getOutputStream().write(s.toJSON().getBytes());
	            Logger.i("Submission: " + s.toJSON());
			    in = new BufferedInputStream(con.getInputStream());
            } catch (Exception e) {
            	e.printStackTrace();
            	in = new BufferedInputStream(con.getErrorStream());
            }
			
			
			Logger.i("Start pargsing JSON");
			//con.connect();
		    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
			return parser.parseSubmission(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void uploadFile(Tip t, File file){
		throw new RuntimeException("Not implemented yet");
	}
	public void uploadFile(GLApplication app, Submission s, File file){
		String boundary = "----" + System.currentTimeMillis()+"----";
		try {
			
            String disposition = "Content-Disposition: form-data; name=\"files[]\"; filename=\"" + file.getName() + "\"";
            String type = "Content-Type: " + file.getMimetype();
            StringBuffer requestBody = new StringBuffer();
            requestBody.append("--");
            requestBody.append(boundary);
            requestBody.append("\r\n");
            requestBody.append(disposition);
            requestBody.append("\r\n");
            requestBody.append(type);
            requestBody.append("\r\n\r\n");
            
            HttpURLConnection con = createConnection(baseUrl + "/submission/" + s.getId() + "/file");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream dataOS = new DataOutputStream(con.getOutputStream());
            dataOS.writeBytes(requestBody.toString());
            InputStream iss = app.getContentResolver().openInputStream(file.getUri());
            BufferedInputStream bis = new BufferedInputStream(iss);
            byte[] buf = new byte[1024];
            while(bis.available() > 0) {
            	int read = bis.read(buf);
            	dataOS.write(buf,0,read);
            }
            bis.close();
            dataOS.write("\r\n--".getBytes());
            dataOS.write(boundary.getBytes());
            dataOS.write("--\r\n".getBytes());
            dataOS.flush();
            dataOS.close();

            Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
            InputStream is = con.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(bytes)) != -1) {
                baos.write(bytes, 0, bytesRead);
            }
            byte[] bytesReceived = baos.toByteArray();
            baos.close();

            is.close();
            String response = new String(bytesReceived);
            Logger.i("uploade file: " + response);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	
	/*
	{"files": [], "pertinence": "0", "im_whistleblower": true, 
	"context_id": "da6f49d3-b641-4578-8c58-7226af77e2de", "access_limit": 42, 
	"expiration_date": "2013-07-04T22:24:32.194926", "context_gus": "da6f49d3-b641-4578-8c58-7226af77e2de", 
	"access_counter": 0, "creation_date": "1 hour", "escalation_threshold": "0", 
	"last_activity": "2013-06-19T23:24:32.194960", "download_limit": 42, "im_receiver": false, 
	"fields": {"Full description": "hbhghg", "Short title": "yhy"}, 
	"mark": "first", "id": "325f94b5-e461-4ad9-adfc-5c91e6f9083d"}
	*/
	public Submission fetchTip() throws GLException {
		try {
			HttpURLConnection con = createConnection(baseUrl + "/tip/5b1ac954-2e4f-5e4d-4336-8bd2fe7acd0d");
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.connect();
			InputStream in = null;
			try {
			    in = new BufferedInputStream(con.getInputStream());
			    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
			    Submission submission = parser.parseSubmission(new InputStreamReader(in, "UTF-8"));
				return submission;
            } catch (Exception e) {
    		    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
    		    in = new BufferedInputStream(con.getErrorStream());
    		    ErrorObject eo = parser.parseError(new InputStreamReader(in, "UTF-8"));
            	throw new GLException(eo);
            } finally {
            	if(in != null) in.close();
            	con.disconnect();
            }
		} catch( Exception e) {
			throw new GLException();
		}
	}
	
	private List<Receiver> fetchReceivers() {
		try {
			HttpURLConnection con = createConnection(baseUrl + "/receivers");
			InputStream in = new BufferedInputStream(con.getInputStream());
			Logger.i("Start pargsing JSON");
			return parser.parseReceivers(new InputStreamReader(in, "UTF-8"));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Node fetchNode() {	    
		Node node = null;
		try {		    
			HttpURLConnection con = createConnection(baseUrl + "/node");
			InputStream in = new BufferedInputStream(con.getInputStream());
			Logger.i("Start pargsing JSON");
			node = parser.parseNode(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		try {
			HttpURLConnection con = createConnection(baseUrl + "/static/globaleaks_logo.png");
			con.setRequestMethod("GET");
			Bitmap b = BitmapFactory.decodeStream(con.getInputStream());
			node.setImage(b);
			Logger.i(b.getConfig() + " image " + b.getHeight() + " x " + b.getWidth());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return node;
	}

	private List<Context> fetchContexts() {
		try {
			HttpURLConnection con = createConnection(baseUrl + "/contexts");
			InputStream in = new BufferedInputStream(con.getInputStream());
			Logger.i("Start pargsing JSON");
			return parser.parseContexts(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public void setCacheExpiration(int cache) {
        this.cache = cache;
    }

    public void eraseCache(android.content.Context context) {
        try {
            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if(cache != null) {
                cache.delete();
            }
            installCache(context);
        } catch (Exception e) {
            Logger.e("Error erasing cache", e);
        }
    }
    
	public AuthSession login(Credential credential) throws GLException {
		try {
			HttpURLConnection con = createConnection(baseUrl + "/authentication");
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.connect();
			InputStream in = null;
			try {
	            con.getOutputStream().write(credential.toJSON().getBytes());
	            Logger.i("Submission: " + credential.toJSON());
			    in = new BufferedInputStream(con.getInputStream());
			    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
				AuthSession session = parser.parseAuthSession(new InputStreamReader(in, "UTF-8"));
				sessionId = session.getSessionId();
				return session;
            } catch (Exception e) {
    		    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
    		    in = new BufferedInputStream(con.getErrorStream());
    		    ErrorObject eo = parser.parseError(new InputStreamReader(in, "UTF-8"));
            	throw new GLException(eo);
            } finally {
            	if(in != null) in.close();
            	con.disconnect();
            }
		} catch( Exception e) {
			throw new GLException();
		}
	}

	public void logout() throws GLException {
		try {
			HttpURLConnection con = createConnection(baseUrl + "/authentication");
			con.setRequestMethod("DELETE");
			con.setDoOutput(false);
			con.connect();
			InputStream in = null;
			try {
			    in = new BufferedInputStream(con.getInputStream());
			    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
			    sessionId = null;
            } catch (Exception e) {
    		    Logger.i("Response: [" + con.getResponseCode() + "] " + con.getResponseMessage());
    		    in = new BufferedInputStream(con.getErrorStream());
    		    ErrorObject eo = parser.parseError(new InputStreamReader(in, "UTF-8"));
            	throw new GLException(eo);
            } finally {
            	if(in != null) in.close();
            	con.disconnect();
            }
		} catch( Exception e) {
			throw new GLException();
		}
	}

}
