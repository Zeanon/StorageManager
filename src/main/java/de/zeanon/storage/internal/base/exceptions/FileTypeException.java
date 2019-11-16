package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class FileTypeException extends StorageManagerException {

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