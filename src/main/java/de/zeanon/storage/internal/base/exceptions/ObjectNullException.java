package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class ObjectNullException extends NullPointerException {

	public ObjectNullException() {
		super();
	}

	public ObjectNullException(final String message) {
		super(message);
	}
}