package de.zeanon.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class ProviderException extends StorageManagerException {

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