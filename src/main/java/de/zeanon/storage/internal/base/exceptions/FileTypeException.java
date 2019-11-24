package de.zeanon.storage.internal.base.exceptions;

/**
 * Indicates that there is some type of conflict based on FileTypes
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class FileTypeException extends RuntimeException {

	public FileTypeException() {
		super();
	}

	public FileTypeException(final String message) {
		super(message);
	}

	public FileTypeException(final Throwable cause) {
		super(cause);
	}

	public FileTypeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}