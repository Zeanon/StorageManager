package de.zeanon.storage.internal.utility.utils.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to check Objects
 *
 * @author Zeanon
 * @version 1.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public class Objects {


	/**
	 * Checks if given Object is null
	 *
	 * @return the given object if not null
	 */
	@NotNull
	@Contract("null -> fail; !null -> param1")
	public static <O> O notNull(final @Nullable O object) {
		return notNull(object, "Checked Object  must not be null");
	}


	@Contract("null, _ -> fail; !null, _ -> param1")
	public static @NotNull <O> O notNull(final @Nullable O object, final @NotNull String message) {
		if (object == null) {
			throw new ObjectNullException(message);
		} else {
			return object;
		}
	}
}