package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class ThunderException extends Exception {

	public ThunderException() {
		super();
	}

	public ThunderException(final String message) {
		super(message);
	}

	public ThunderException(final Throwable cause) {
		super(cause);
	}

	public ThunderException(final String message, final Throwable cause) {
		super(message, cause);
	}
}