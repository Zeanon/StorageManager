package de.zeanon.storagemanager.internal.base.exceptions;

/**
 * Indicates that there is some type of conflict based on FileTypes
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class FileTypeException extends StorageManagerException {

	private static final long serialVersionUID = -3209179731679169538L;

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