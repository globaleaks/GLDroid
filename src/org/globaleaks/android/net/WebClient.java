package org.globaleaks.android.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.globaleaks.android.TulipActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

public class WebClient {

    private static final String LOG_TAG = "WebClient";
    
    private HttpClient http;
    private String baseUrl;
    
    public WebClient(String baseUrl) {
        http = new DefaultHttpClient();
        this.baseUrl = baseUrl;
    }
    

    /** Form structure for submission:
     
    <form action="" enctype="multipart/form-data" method="post">
        <table><tr><td>Title</td><td>
        <input name="Title" type="text" />
        <textarea cols="40" name="Description" rows="10"></textarea>
        <input class="disabled" name="material1" type="file" />
        <input class="notimplemented" name="metadata" type="checkbox" value="on" />
        <input name="disclaimer" type="checkbox" value="on" />
        <input name="submit" type="submit" />
        <input name="_formkey" type="hidden" value="bfd84fe7-fdc9-4f49-988b-42f55f4d9950" />
        <input name="_formname" type="hidden" value="default" />
    </form>
     * @throws Exception 
    */
    public void submit(Intent data, final Activity ctx) throws Exception {
        final Bundle bundle = data.getExtras();
        final String imgUri = bundle.getString("img");
        final ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Submission");
        dialog.setMessage("Uploading material...");
        dialog.show();
        AsyncTask<Void, Long, Boolean> task = new AsyncTask<Void, Long, Boolean>() {
            private String tulip;

            @Override
            protected void onProgressUpdate(Long... values) {
                if(values == null || values[0] == null) return;
                dialog.setProgress(values[0].intValue());
            }

            @Override
            protected Boolean doInBackground(Void...voids) {
                publishProgress(0L);
                final String formKey = parseFormKey();
                publishProgress(10L);
                if(formKey == null) {
                    Log.e(LOG_TAG, "Error getting formKey");
                    return false;
                }
                tulip = submitLeak(formKey, bundle);
                publishProgress(20L);
                try {
                    if(imgUri == null) {
                        publishProgress(100L);
                        return true;
                    }
                    final long imgSize = getImageSize(imgUri, ctx);
                    InputStream input = ctx.getContentResolver().openInputStream(Uri.parse(imgUri));
                    HttpPost post = new HttpPost("http://"+baseUrl+"/globaleaks/submission/upload?qqfile=image.png");
                    final long offset = 20L;
                    FilterStreamEntity entity = new FilterStreamEntity(input, imgSize, new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            Log.i(LOG_TAG, "Transferred " + num + " bytes");
                            long progress = num * 70 / imgSize;
                            publishProgress(progress + offset);
                        }
                    });
                    post.setEntity(entity);
                    HttpResponse resp = http.execute(post);
                    publishProgress(100L);
                    HttpEntity he = resp.getEntity();
                    System.out.println(convertStreamToString(he.getContent()));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error uploading file", e);
                    return null;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                Intent i = new Intent(ctx, TulipActivity.class);
                i.putExtra("tulip", tulip);
                ctx.startActivity(i);
                dialog.dismiss();
            }
            
        };
        task.execute();
    }


    private String submitLeak(String formKey, Bundle bundle) {
        String title = bundle.getString("title");
        String desc = bundle.getString("description");
        HttpPost post = new HttpPost("http://"+baseUrl+"/submit");
        MultipartEntity me = new MultipartEntity();
        me.addPart("Title", title);
        me.addPart("Description", desc);
        me.addPart("disclaimer", "on");
        me.addPart("_formkey", formKey);
        me.addPart("_formname", "default");
        me.addPart("submit", "send request");
        post.setEntity(me);
        try {
            HttpResponse resp = http.execute(post);
            HttpEntity he = resp.getEntity();
            String tulip = parseTulip(he.getContent());
            if(tulip == null) {
                Log.e(LOG_TAG, "Error getting/parsing tulip");
                return null;
            }
            return tulip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private long getImageSize(String imgUri, Activity ctx) throws Exception {
        /* File upload is done with separate POST request for each file uploaded
         * to POST /globaleaks/submission/upload?qqfile=filename
         */
        Cursor cursor = null;
        long size = -1;
        try {
            String originalImageFilePath = null;
            String[] columnsToSelect = { MediaStore.Images.Media.DATA };
            cursor = ctx.getContentResolver().query(Uri.parse(imgUri), columnsToSelect, null, null, null );
            if ( cursor != null && cursor.getCount() == 1 ) {
                cursor.moveToFirst();
                originalImageFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            File f = new File(originalImageFilePath);
            size = f.length();
        } catch (Exception e) {
            if(cursor != null) cursor.close();
        }
        return size;
    } 


    private String parseTulip(InputStream content) {
        // TODO: use XML parser to extract tulip ID
        String response = convertStreamToString(content);
        String startTag = "class=\"tulip\">";
        int idx = response.indexOf(startTag);
        if(idx == -1) {
            return null;
        }
        int idx2 = response.indexOf("</p>", idx);
        if(idx2 == -1) {
            return null;
        }
        String tulip = response.substring(idx + startTag.length(),idx2).trim();
        return tulip;
    }


    private String parseFormKey() {
        //TODO use XML parser to extract form key
        HttpGet get = new HttpGet("http://"+baseUrl+"/submit");
        try {
            HttpResponse response = http.execute(get);
            HttpEntity entity = response.getEntity();
            String form = convertStreamToString(entity.getContent());
            int idx = form.indexOf("_formkey");
            if(idx == -1) return null;
            String s1 = form.substring(idx);
            idx = s1.indexOf("value=");
            if(idx == -1) return null;
            String s2 = s1.substring(idx + "value=".length());
            String k = s2.substring(1,37);
            return k;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

