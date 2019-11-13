package de.zeanon.storage.internal.utils.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectIsNull;
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
		checkNull(object, "Validated Object  must not be null");
	}

	public static <O> void checkNull(final @Nullable O object, final @NotNull String message) {
		if (object != null) {
			return;
		}
		throw new ObjectIsNull(message);
	}


	/**
	 * Checks if given Object is null
	 *
	 * @return the given object if not null
	 */
	public static <O> O notNull(final @Nullable O object) {
		return notNull(object, "Validated Object  must not be null");
	}


	public static <O> O notNull(final @Nullable O object, final @NotNull String message) {
		if (object != null) {
			return object;
		}
		throw new ObjectIsNull(message);
	}
}