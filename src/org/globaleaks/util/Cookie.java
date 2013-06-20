package org.globaleaks.util;

import java.util.HashMap;
import java.util.Iterator;

import org.globaleaks.model.AuthSession;

public class Cookie {

	private HashMap<String, String> values = new HashMap<String, String>();
	private String value;
	
	/*
	 language=en; session_id=HHomClAtRs0LpuLILJU4HxiFNBXokDL9mMRuYMj7PR; role=wb; tip_id=265c00f6-2587-4561-b15a-ac5d431949f7; auth_landing_page=%2F%23%2Fstatus%2F265c00f6-2587-4561-b15a
			    -ac5d431949f7
			    */
	public Cookie(AuthSession session) {
		values.put("session_id", session.getSessionId());
		values.put("role", "wb");
		values.put("tip_id", session.getUserId());
		values.put("auth_landing_page", "%2F%23%2Fstatus%2F" + session.getUserId());
		values.put("language", "en");
		StringBuffer b = new StringBuffer();
		for (Iterator<String> i = values.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			b.append(key);
			b.append("=");
			b.append(values.get(key));
			b.append("; ");
		}
		value = b.toString();
	}
	
	public String toString() {
		return value;
	}
}
