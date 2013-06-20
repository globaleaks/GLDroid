package org.globaleaks.util;

import org.globaleaks.model.ErrorObject;

public class GLException extends Exception {

	private ErrorObject error;
	
	public GLException(ErrorObject eo) {
		super(eo.toString());
		error = eo;
	}

	public GLException() {
		error = ErrorObject.GENERIC;
	}

	public ErrorObject getErrorObject() {
		return error;
	}

	private static final long serialVersionUID = 810047464714937610L;

}
