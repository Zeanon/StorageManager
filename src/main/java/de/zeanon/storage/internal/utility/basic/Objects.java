package de.zeanon.storage.internal.utility.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to check Objects
 *
 * @author Zeanon
 * @version 1.2.0
 */
@UtilityClass
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