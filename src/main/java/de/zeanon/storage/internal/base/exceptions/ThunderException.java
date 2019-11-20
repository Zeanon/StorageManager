package de.zeanon.storage.internal.base.exceptions;

/**
 * A basic Exception thrown by the ThunderEditor if something went wrong with parsing the File
 *
 * @author Zeanon
 * @version 1.0.0
 */
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