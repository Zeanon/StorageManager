package de.zeanon.storage.internal.utility.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Parsing utilities for Objects Types
 *
 * @author Zeanon
 * @version 1.7.0
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


	/**
	 * Get an Object of the given Class Type
	 *
	 * @param object the Object to be casted
	 * @param def    the Class to be casted to
	 * @param <T>    the Type of def
	 *
	 * @return the passed Object casted to the given Type
	 *
	 * @throws ObjectNullException if a passed value is null
	 */
	@SuppressWarnings("unchecked")
	@Contract("null, _ -> null")
	public static @Nullable <T> T toDef(final @Nullable Object object, final @NotNull Class<T> def) {
		if (object == null) {
			return null;
		} else {
			if (def == int.class || def == Integer.class) {
				return (T) (Object) Objects.toInt(object);
			} else if (def == long.class || def == Long.class) {
				return (T) (Object) Objects.toLong(object);
			} else if (def == double.class || def == Double.class) {
				return (T) (Object) Objects.toDouble(object);
			} else if (def == float.class || def == Float.class) {
				return (T) (Object) Objects.toFloat(object);
			} else if (def == short.class || def == Short.class) {
				return (T) (Object) Objects.toShort(object);
			} else if (def == boolean.class || def == Boolean.class) {
				return (T) (Object) Objects.toBoolean(object);
			} else {
				return (T) object;
			}
		}
	}

	/**
	 * Get an Object of the given Class Type
	 *
	 * @param object the Object to be casted
	 * @param def    the Class to be casted to
	 * @param <T>    the Type of def
	 *
	 * @return the passed Object casted to the given Type
	 *
	 * @throws ObjectNullException if a passed value is null
	 */
	@SuppressWarnings("unchecked")
	@Contract("null, _ -> null")
	public static @Nullable <T> T toDef(final @Nullable Object object, final @NotNull T def) {
		if (object == null) {
			return null;
		} else {
			if (def instanceof Integer) {
				return (T) (Object) Objects.toInt(object);
			} else if (def instanceof Long) {
				return (T) (Object) Objects.toLong(object);
			} else if (def instanceof Double) {
				return (T) (Object) Objects.toDouble(object);
			} else if (def instanceof Float) {
				return (T) (Object) Objects.toFloat(object);
			} else if (def == short.class || def == Short.class) {
				return (T) (Object) Objects.toShort(object);
			} else if (def == boolean.class || def == Boolean.class) {
				return (T) (Object) Objects.toBoolean(object);
			} else {
				return (T) object;
			}
		}
	}

	/**
	 * Castes a given Object to a boolean
	 *
	 * @return the given Object parsed to a boolean or false if the given Object is null
	 */
	@Contract("null -> false")
	public static boolean toBoolean(final @Nullable Object object) {
		if (object == null) {
			return false;
		} else if (object instanceof Boolean) {
			return (boolean) object;
		} else {
			return Objects.toString(object).equalsIgnoreCase("true");
		}
	}

	/**
	 * Castes a given Object to a long
	 *
	 * @return the given Object parsed to a long or 0 if the given Object is null
	 */
	public static long toLong(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Long) {
			return (long) object;
		} else if (object instanceof Number) {
			return ((Number) object).longValue();
		} else {
			return Long.parseLong(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to a double
	 *
	 * @return the given Object parsed to a double or 0 if the given Object is null
	 */
	public static double toDouble(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Double) {
			return (double) object;
		} else if (object instanceof Number) {
			return ((Number) object).longValue();
		} else {
			return Double.parseDouble(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to a float
	 *
	 * @return the given Object parsed to a float or 0 if the given Object is null
	 */
	public static float toFloat(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Float) {
			return (float) object;
		} else if (object instanceof Number) {
			return ((Number) object).floatValue();
		} else {
			return Float.parseFloat(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to an int
	 *
	 * @return the given Object parsed to an int or 0 if the given Object is null
	 */
	public static int toInt(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Integer) {
			return (int) object;
		} else if (object instanceof Number) {
			return ((Number) object).intValue();
		} else {
			return Integer.parseInt(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to a short
	 *
	 * @return the given Object parsed to a short or 0 if the given Object is null
	 */
	public static short toShort(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Short) {
			return (short) object;
		} else if (object instanceof Number) {
			return ((Number) object).shortValue();
		} else {
			return Short.parseShort(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to a byte
	 *
	 * @return the given Object parsed to a byte or 0 if the given Object is null
	 */
	public static byte toByte(final @Nullable Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof Byte) {
			return (byte) object;
		} else if (object instanceof Number) {
			return ((Number) object).byteValue();
		} else {
			return Byte.parseByte(Objects.toString(object));
		}
	}

	/**
	 * Castes a given Object to a String
	 *
	 * @return the given Object parsed to a String or null if the Object is null
	 */
	@Contract("null -> null; !null -> !null")
	public static @Nullable String toString(final @Nullable Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof String) {
			return (String) object;
		} else if (object instanceof char[]) {
			return String.copyValueOf((char[]) object);
		} else {
			return object.toString();
		}
	}
}