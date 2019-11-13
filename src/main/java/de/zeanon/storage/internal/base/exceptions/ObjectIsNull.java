package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class ObjectIsNull extends RuntimeException {

	public ObjectIsNull() {
		super();
	}

	public ObjectIsNull(final String message) {
		super(message);
	}

	public ObjectIsNull(final Throwable cause) {
		super(cause);
	}

	public ObjectIsNull(final String message, final Throwable cause) {
		super(message, cause);
	}
}