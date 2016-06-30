package com.snowstore.pontus.service;

import java.text.MessageFormat;

public class PontusServiceException extends RuntimeException {

	private static final long serialVersionUID = 8053233255835925728L;

	public PontusServiceException() {
		super();
	}

	public PontusServiceException(String pattern, Object... args) {
		super(MessageFormat.format(pattern, args));
	}

	public PontusServiceException(String message) {
		super(message);
	}

	public PontusServiceException(Throwable cause) {
		super(cause);
	}

	public PontusServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
