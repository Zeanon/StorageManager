package de.zeanon.storagemanager.internal.base.exceptions;

/**
 * Basic Exception for the RuntimeExceptions thrown by StorageManager for easier catching
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class StorageManagerException extends RuntimeException {

	private static final long serialVersionUID = 2438245264764832035L;

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