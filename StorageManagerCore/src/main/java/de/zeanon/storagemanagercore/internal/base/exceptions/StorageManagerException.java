package de.zeanon.storagemanagercore.internal.base.exceptions;

/**
 * Basic Exception for the RuntimeExceptions thrown by StorageManager for easier catching
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class StorageManagerException extends RuntimeException {

	private static final long serialVersionUID = 2438245264764832035L;

	protected StorageManagerException() {
		super();
	}

	protected StorageManagerException(final String message) {
		super(message);
	}

	protected StorageManagerException(final Throwable cause) {
		super(cause);
	}

	protected StorageManagerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}