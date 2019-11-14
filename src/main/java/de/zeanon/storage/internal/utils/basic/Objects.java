package de.zeanon.storage.internal.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to check validities
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class Objects {

	/**
	 * Checks if given Object is null
	 */
	public static <O> void checkNull(final @Nullable O object) {
		checkNull(object, "Checked Object  must not be null");
	}

	public static <O> void checkNull(final @Nullable O object, final @NotNull String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
	}


	/**
	 * Checks if given Object is null
	 *
	 * @return the given object if not null
	 */
	public static <O> O notNull(final @Nullable O object) {
		return notNull(object, "Checked Object  must not be null");
	}


	public static <O> O notNull(final @Nullable O object, final @NotNull String message) {
		if (object == null) {
			throw new NullPointerException(message);
		} else {
			return object;
		}
	}
}