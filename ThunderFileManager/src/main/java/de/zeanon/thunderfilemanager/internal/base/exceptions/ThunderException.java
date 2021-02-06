package de.zeanon.thunderfilemanager.internal.base.exceptions;

/**
 * A basic Exception thrown by the ThunderFileParser if something went wrong with parsing the File
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class ThunderException extends Exception {

	private static final long serialVersionUID = 7200179831679569538L;

	public ThunderException() {
		super();
	}

	public ThunderException(final String message) {
		super(message);
	}

	public ThunderException(final Throwable cause) {
		super(cause);
	}

	public ThunderException(final String message, final Throwable cause) {
		super(message, cause);
	}
}