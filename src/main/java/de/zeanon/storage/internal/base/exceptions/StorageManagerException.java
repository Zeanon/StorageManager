package de.zeanon.storage.internal.base.exceptions;

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