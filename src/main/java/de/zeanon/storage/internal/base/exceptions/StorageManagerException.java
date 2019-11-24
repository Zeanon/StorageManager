package de.zeanon.storage.internal.base.exceptions;

/**
 * Basic Exception for the RuntimeExceptions thrown by StorageManager for easier catching
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class StorageManagerException extends RuntimeException {

	public StorageManagerException() {
		super();
	}

	public StorageManagerException(final String message) {
		super(message);
	}

	public StorageManagerException(final Throwable cause) {
		super(cause);
	}

	public StorageManagerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}