package de.zeanon.storage.internal.utility.utils.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


/**
 * Parsing utilities for Primitive Types
 *
 * @author Zeanon
 * @version 1.7.0
 */
@UtilityClass
@SuppressWarnings("unused")
public class Primitive {


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
	public static <T> @NotNull T getFromDef(final @NotNull Object object, final @NotNull Class<T> def) {
		Object tempObj = Objects.notNull(object, "Object must not be null");
		if (object instanceof String && def == Integer.class) {
			tempObj = Integer.parseInt((String) object);
		} else if (object instanceof String && def == Long.class) {
			tempObj = Long.parseLong((String) object);
		} else if (object instanceof String && def == Double.class) {
			tempObj = Double.parseDouble((String) object);
		} else if (object instanceof String && def == Float.class) {
			tempObj = Double.parseDouble((String) object);
		} else if (object instanceof String && def == Short.class) {
			tempObj = Short.parseShort((String) object);
		} else if (object instanceof String && def == Boolean.class) {
			tempObj = ((String) object).equalsIgnoreCase("true");
		}
		//noinspection unchecked
		return (T) tempObj;
	}


	@UtilityClass
	public static class BOOLEAN {

		/**
		 * Castes a given Object to a boolean
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static boolean getBoolean(final @NotNull Object object) {
			if (object instanceof Boolean) {
				return (boolean) object;
			} else if (object instanceof String) {
				return ((String) object).equalsIgnoreCase("true");
			} else {
				return object.toString().equalsIgnoreCase("true");
			}
		}
	}


	@UtilityClass
	public static class LONG {

		/**
		 * Castes a given Object to a long
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static long getLong(final @NotNull Object object) {
			if (object instanceof Long) {
				return (long) object;
			} else if (object instanceof Number) {
				return ((Number) object).longValue();
			} else if (object instanceof String) {
				return Long.parseLong((String) object);
			} else {
				return Long.parseLong(object.toString());
			}
		}
	}


	@UtilityClass
	public static class DOUBLE {

		/**
		 * Castes a given Object to a double
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static double getDouble(final @NotNull Object object) {
			if (object instanceof Double) {
				return (double) object;
			} else if (object instanceof Number) {
				return ((Number) object).longValue();
			} else if (object instanceof String) {
				return Double.parseDouble((String) object);
			} else {
				return Double.parseDouble(object.toString());
			}
		}
	}


	@UtilityClass
	public static class FLOAT {

		/**
		 * Castes a given Object to a float
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static float getFloat(final @NotNull Object object) {
			if (object instanceof Float) {
				return (float) object;
			} else if (object instanceof Number) {
				return ((Number) object).floatValue();
			} else if (object instanceof String) {
				return Float.parseFloat((String) object);
			} else {
				return Float.parseFloat(object.toString());
			}
		}
	}


	@UtilityClass
	public static class INTEGER {

		/**
		 * Castes a given Object to an int
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static int getInt(final @NotNull Object object) {
			if (object instanceof Integer) {
				return (int) object;
			} else if (object instanceof Number) {
				return ((Number) object).intValue();
			} else if (object instanceof String) {
				return Integer.parseInt((String) object);
			} else {
				return Integer.parseInt(object.toString());
			}
		}
	}


	@UtilityClass
	public static class SHORT {

		/**
		 * Castes a given Object to a short
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static short getShort(final @NotNull Object object) {
			if (object instanceof Short) {
				return (short) object;
			} else if (object instanceof Number) {
				return ((Number) object).shortValue();
			} else if (object instanceof String) {
				return Short.parseShort((String) object);
			} else {
				return Short.parseShort(object.toString());
			}
		}
	}


	@UtilityClass
	public static class BYTE {

		/**
		 * Castes a given Object to a byte
		 *
		 * @throws ObjectNullException if the passed value is null
		 */
		public static byte getByte(final @NotNull Object object) {
			if (object instanceof Byte) {
				return (byte) object;
			} else if (object instanceof Number) {
				return ((Number) object).byteValue();
			} else if (object instanceof String) {
				return Byte.parseByte((String) object);
			} else {
				return Byte.parseByte(object.toString());
			}
		}
	}
}