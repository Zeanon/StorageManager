package de.zeanon.storage.internal.basic.exceptions;

/**
 * The base for all custom Exceptions thrown by StorageManager for more precise catching
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public abstract class StorageManagerException extends RuntimeException {

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