package de.zeanon.storagemanagercore.internal.base.exceptions;

/**
 * Indicates that a File somehow could not be parsed
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class FileParseException extends StorageManagerException {

	private static final long serialVersionUID = 3209172731579069538L;

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