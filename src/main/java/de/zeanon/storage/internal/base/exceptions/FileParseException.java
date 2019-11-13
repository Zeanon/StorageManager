package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class FileParseException extends RuntimeException {

	public FileParseException() {
		super();
	}

	public FileParseException(final String message) {
		super(message);
	}

	public FileParseException(final Throwable cause) {
		super(cause);
	}

	public FileParseException(final String message, final Throwable cause) {
		super(message, cause);
	}
}