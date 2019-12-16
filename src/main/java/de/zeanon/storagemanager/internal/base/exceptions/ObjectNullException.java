package de.zeanon.storagemanager.internal.base.exceptions;

/**
 * Indicates that a passed object has been null where it NotNull is required
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class ObjectNullException extends NullPointerException {

	private static final long serialVersionUID = 7960773302491731978L;

	public ObjectNullException() {
		super();
	}

	public ObjectNullException(final String message) {
		super(message);
	}
}