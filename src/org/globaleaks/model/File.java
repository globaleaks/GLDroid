package org.globaleaks.model;

import android.net.Uri;

public class File {
    
    private static final String DEFAULT_TYPE = "application/octet-stream";
    
    private Uri uri;
    private String name;

	private String mimetype = DEFAULT_TYPE;
    
    public File(Uri uri) {
    	this.uri = uri;
    	name = uri.getLastPathSegment();
    }

	public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
		return name;
	}

    public String getMimetype() {
        return mimetype;
    }
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        File other = (File) obj;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "File [uri=" + uri + ", mimetype=" + mimetype + "]";
    }
    

}
