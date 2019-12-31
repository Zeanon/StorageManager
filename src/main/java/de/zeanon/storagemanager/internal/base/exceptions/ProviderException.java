package de.zeanon.storagemanager.internal.base.exceptions;

/**
 * Exception indicating that there is something wrong with the Classes saved in the provider
 *
 * @author Zeanon
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class ProviderException extends StorageManagerException {

	private static final long serialVersionUID = -9209479739679169538L;

	public ProviderException() {
		super();
	}

	public ProviderException(final String message) {
		super(message);
	}

	public ProviderException(final Throwable cause) {
		super(cause);
	}

	public ProviderException(final String message, final Throwable cause) {
		super(message, cause);
	}
}