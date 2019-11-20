package de.zeanon.storage.internal.base.exceptions;

/**
 * Just a basic unchecked version of an IOException
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class RuntimeIOException extends StorageManagerException {

	public RuntimeIOException() {
		super();
	}

	public RuntimeIOException(final String message) {
		super(message);
	}

	public RuntimeIOException(final Throwable cause) {
		super(cause);
	}

	public RuntimeIOException(final String message, final Throwable cause) {
		super(message, cause);
	}
}