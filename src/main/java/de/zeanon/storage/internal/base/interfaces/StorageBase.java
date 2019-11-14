package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.utils.basic.Primitive;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic Interface for the Data Classes
 */
@SuppressWarnings({"unused", "unchecked", "UnusedReturnValue"})
public interface StorageBase {

	/**
	 * Get a boolean from a File
	 *
	 * @param key key to boolean in File
	 * @return Boolean from File
	 */
	default boolean getBoolean(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.BOOLEAN.getBoolean(get(key));
		}
	}

	default boolean getBooleanFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.BOOLEAN.getBoolean(this.getFromArray(key));
		}
	}

	/**
	 * Get an Object from a File
	 *
	 * @param key key to Object in File
	 * @return Object from File
	 */
	Object get(final @NotNull String key);

	Object getFromArray(final @NotNull String... key);

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 * @return Map of the give keys and their values
	 */
	Map<String, Object> getAll(final @NotNull String... keys);

	Map<String[], Object> getAllFromArray(final @NotNull String[]... keys);

	Map<String, Object> getAll(final @NotNull Collection<String> keys);

	Map<String[], Object> getAllFromArray(final @NotNull Collection<String[]> keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 * @return Map of the give keys and their values
	 */
	Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys);

	Map<String[], Object> getAllFromArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	Map<String[], Object> getAllFromArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Get a byte from a File
	 *
	 * @param key key to byte in File
	 * @return Byte from File
	 */
	default byte getByte(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.BYTE.getByte(get(key));
		}
	}

	default byte getByteFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.BYTE.getByte(this.getFromArray(key));
		}
	}

	/**
	 * Get a Byte-List from a File
	 *
	 * @param key key to Byte-List from File
	 * @return Byte-List
	 */
	default List<Byte> getByteList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return (List<Byte>) tempObject;
		}
	}

	default List<Byte> getByteListFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return (List<Byte>) tempObject;
		}
	}

	/**
	 * Get a double from a File
	 *
	 * @param key key to double in the File
	 * @return Double from File
	 */
	default double getDouble(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.DOUBLE.getDouble(get(key));
		}
	}

	default double getDoubleFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.DOUBLE.getDouble(this.getFromArray(key));
		}
	}

	/**
	 * Get a float from a File
	 *
	 * @param key key to float in File
	 * @return Float from File
	 */
	default float getFloat(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.FLOAT.getFloat(get(key));
		}
	}

	default float getFloatFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.FLOAT.getFloat(this.getFromArray(key));
		}
	}

	/**
	 * Gets an int from a File
	 *
	 * @param key key to int in File
	 * @return Int from File
	 */
	default int getInt(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.INTEGER.getInt(get(key));
		}
	}

	default int getIntFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.INTEGER.getInt(this.getFromArray(key));
		}
	}

	/**
	 * Gets a short from a File
	 *
	 * @param key key to int in File
	 * @return Short from File
	 */
	default short getShort(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.SHORT.getShort(get(key));
		}
	}

	default short getShortFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.SHORT.getShort(this.getFromArray(key));
		}
	}

	/**
	 * Get a IntegerList from a File
	 *
	 * @param key key to Integer-List in File
	 * @return Integer-List
	 */
	default List<Integer> getIntegerList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return (List<Integer>) tempObject;
		}
	}

	default List<Integer> getIntegerListFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return (List<Integer>) tempObject;
		}
	}

	/**
	 * Get a List from a File
	 *
	 * @param key key to StringList in File
	 * @return List
	 */
	default List getList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return (List) tempObject;
		}
	}

	default List getListFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return (List) tempObject;
		}
	}

	/**
	 * Gets a long from a File by key
	 *
	 * @param key key to long in File
	 * @return String from File
	 */
	default long getLong(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return Primitive.LONG.getLong(get(key));
		}
	}

	default long getLongFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return Primitive.LONG.getLong(this.getFromArray(key));
		}
	}

	/**
	 * Get a Long-List from a File
	 *
	 * @param key key to Long-List in File
	 * @return Long-List
	 */
	default List<Long> getLongList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return tempObject != null ? (List<Long>) get(key) : null;
	}

	default List<Long> getLongListFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		return tempObject != null ? (List<Long>) tempObject : null;
	}

	/**
	 * Gets a Map
	 *
	 * @param key key to Map-List in File
	 * @return Map
	 */
	default Map getMap(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return (Map) tempObject;
		}
	}

	default Map getMapFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return (Map) tempObject;
		}
	}

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   key to set the value
	 * @param value Value to set
	 * @return the value set in the File
	 */
	@SuppressWarnings("DuplicatedCode")
	default <T> T getOrSetDefault(final @NotNull String key, final @NotNull T value) {
		if (!this.hasKey(key)) {
			this.set(key, value);
			return value;
		} else {
			Object tempObj = get(key);
			if (tempObj instanceof String && value instanceof Integer) {
				tempObj = Integer.parseInt((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Long) {
				tempObj = Long.parseLong((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Double) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Float) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Short) {
				tempObj = Short.parseShort((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Primitive.BOOLEAN) {
				tempObj = ((String) tempObj).equalsIgnoreCase("true");
			}
			return (T) tempObj;
		}
	}

	@SuppressWarnings("DuplicatedCode")
	default <T> T getOrSetDefaultFromArray(final @NotNull String[] key, final @NotNull T value) {
		if (!this.arrayHasKey(key)) {
			this.setFromArray(key, value);
			return value;
		} else {
			Object tempObj = this.getFromArray(key);
			if (tempObj instanceof String && value instanceof Integer) {
				tempObj = Integer.parseInt((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Long) {
				tempObj = Long.parseLong((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Double) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Float) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Short) {
				tempObj = Short.parseShort((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Primitive.BOOLEAN) {
				tempObj = ((String) tempObj).equalsIgnoreCase("true");
			}
			return (T) tempObj;
		}
	}

	/**
	 * Checks whether a key exists in the File
	 *
	 * @param key key to check
	 * @return true if key exists
	 */
	boolean hasKey(final @NotNull String key);

	boolean arrayHasKey(final @NotNull String... key);

	/**
	 * Set an Object to your File
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your File
	 */
	void set(final @NotNull String key, final @Nullable Object value);

	void setFromArray(final @NotNull String[] key, final @Nullable Object value);

	/**
	 * Set several key, value pairs
	 *
	 * @param dataMap the pairs to be set
	 */
	void setAll(final @NotNull Map<String, Object> dataMap);

	void setAllFromArray(final @NotNull Map<String[], Object> dataMap);

	/**
	 * Set several key, value pairs
	 *
	 * @param blockKey the key of the SubBlock
	 * @param dataMap  the pairs to be set
	 */
	void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap);

	void setAllFromArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap);

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	Set<String> keySet();

	Set<String[]> keySetFromArray();

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the keySet of all sublayers of the given key or an empty set if the key does not exist (Format: key.subkey).
	 */
	Set<String> keySet(final @NotNull String key);

	Set<String[]> keySetFromArray(final @NotNull String... key);


	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	Set<String> blockKeySet();

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or an empty set if the key does not exist.
	 */
	Set<String> blockKeySet(final @NotNull String key);

	Set<String> blockKeySetFromArray(final @NotNull String... key);

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key key to value in File
	 * @param def the Class to be casted to
	 * @param <T> returnType
	 * @return returns the value of the key casted to def
	 */
	default <T> T get(final @NotNull String key, final @NotNull Class<T> def) {
		return Primitive.getFromDef(get(key), def);
	}

	default <T> T getFromArray(final @NotNull String[] key, final @NotNull Class<T> def) {
		return Primitive.getFromDef(this.getFromArray(key), def);
	}

	/**
	 * Get a String from a File
	 *
	 * @param key key to String in File
	 * @return Returns the value
	 */
	default String getString(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return tempObject instanceof String ? (String) tempObject : tempObject.toString();
		}
	}

	default String getStringFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return tempObject instanceof String ? (String) tempObject : tempObject.toString();
		}
	}

	/**
	 * Get String List
	 *
	 * @param key key to String List in File
	 * @return List
	 */
	default List<String> getStringList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + key + "' does not exist");
		} else {
			return (List<String>) tempObject;
		}
	}

	default List<String> getStringListFromArray(final @NotNull String... key) {
		Object tempObject = this.getFromArray(key);
		if (tempObject == null) {
			throw new NullPointerException("key '" + Arrays.toString(key) + "' does not exist");
		} else {
			return (List<String>) tempObject;
		}
	}

	/**
	 * Remove a key from the File
	 *
	 * @param key the key to remove
	 */
	void remove(final @NotNull String key);

	void removeFromArray(final @NotNull String... key);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAll(final @NotNull String... keys);

	void removeAllFromArray(final @NotNull String[]... keys);

	void removeAll(final @NotNull Collection<String> keys);

	void removeAllFromArray(final @NotNull Collection<String[]> keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAll(final @NotNull String blockKey, final @NotNull String... keys);

	void removeAllFromArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	void removeAllFromArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Sets a value to the File if the File doesn't already contain the value
	 * (Do not mix up with Bukkit addDefault)
	 *
	 * @param key   key to set the value
	 * @param value Value to set
	 */
	default void setDefault(final @NotNull String key, final @Nullable Object value) {
		if (!this.hasKey(key)) {
			set(key, value);
		}
	}

	default void setDefaultFromArray(final @NotNull String[] key, final @Nullable Object value) {
		if (!this.arrayHasKey(key)) {
			this.setFromArray(key, value);
		}
	}

	FlatSection getSection(final @NotNull String sectionKey);

	FlatSection getSectionFromArray(final @NotNull String[] sectionKey);
}