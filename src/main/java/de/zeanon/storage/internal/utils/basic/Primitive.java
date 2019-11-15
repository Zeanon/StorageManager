package de.zeanon.storage.internal.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Parsing utilities for Primitive Types
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class Primitive {

	@NotNull
	public static <T> T getFromDef(final @NotNull Object object, final @NotNull Class<T> def) {
		Objects.checkNull(def, "Definition must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BOOLEAN {

		public static boolean getBoolean(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
			if (object instanceof Boolean) {
				return (boolean) object;
			} else if (object instanceof String) {
				return ((String) object).equalsIgnoreCase("true");
			} else {
				return object.toString().equalsIgnoreCase("true");
			}
		}
	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class LONG {

		public static long getLong(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class DOUBLE {

		public static double getDouble(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class FLOAT {

		public static float getFloat(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class INTEGER {

		public static int getInt(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class SHORT {

		public static short getShort(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BYTE {

		public static byte getByte(final @NotNull Object object) {
			Objects.checkNull(object, "Object must not be null");
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