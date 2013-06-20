package org.globaleaks.model;

public class AuthSession {

	private String userId;
	private String sessionId;
	
	@Override
	public String toString() {
		return "AuthSession [userId=" + userId + ", sessionId=" + sessionId
				+ "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
