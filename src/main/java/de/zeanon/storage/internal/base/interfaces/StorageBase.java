package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.utils.basic.Primitive;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		return Primitive.BOOLEAN.getBoolean(this.get(key));
	}

	default boolean getBooleanUseArray(final @NotNull String... key) {
		return Primitive.BOOLEAN.getBoolean(this.getUseArray(key));
	}

	/**
	 * Get an Object from a File
	 *
	 * @param key key to Object in File
	 * @return Object from File
	 */
	@NotNull Object get(final @NotNull String key);

	@NotNull Object getUseArray(final @NotNull String... key);

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 * @return Map of the give keys and their values
	 */
	@NotNull Map<String, Object> getAll(final @NotNull String... keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull String[]... keys);

	@NotNull Map<String, Object> getAll(final @NotNull Collection<String> keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 * @return Map of the give keys and their values
	 */
	@NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	@NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Get a byte from a File
	 *
	 * @param key key to byte in File
	 * @return Byte from File
	 */
	default byte getByte(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.BYTE.getByte(tempObject);
	}

	default byte getByteUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.BYTE.getByte(tempObject);
	}

	/**
	 * Get a Byte-List from a File
	 *
	 * @param key key to Byte-List from File
	 * @return Byte-List
	 */
	@NotNull
	default List<Byte> getByteList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return (List<Byte>) tempObject;
	}

	@NotNull
	default List<Byte> getByteListUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return (List<Byte>) tempObject;
	}

	/**
	 * Get a double from a File
	 *
	 * @param key key to double in the File
	 * @return Double from File
	 */
	default double getDouble(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.DOUBLE.getDouble(tempObject);
	}

	default double getDoubleUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.DOUBLE.getDouble(tempObject);
	}

	/**
	 * Get a float from a File
	 *
	 * @param key key to float in File
	 * @return Float from File
	 */
	default float getFloat(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.FLOAT.getFloat(tempObject);
	}

	default float getFloatUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.FLOAT.getFloat(tempObject);
	}

	/**
	 * Gets an int from a File
	 *
	 * @param key key to int in File
	 * @return Int from File
	 */
	default int getInt(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.INTEGER.getInt(tempObject);
	}

	default int getIntUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.INTEGER.getInt(tempObject);
	}

	/**
	 * Gets a short from a File
	 *
	 * @param key key to int in File
	 * @return Short from File
	 */
	default short getShort(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.SHORT.getShort(tempObject);
	}

	default short getShortUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.SHORT.getShort(tempObject);
	}

	/**
	 * Get a IntegerList from a File
	 *
	 * @param key key to Integer-List in File
	 * @return Integer-List
	 */
	@NotNull
	default List<Integer> getIntegerList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return (List<Integer>) tempObject;
	}

	@NotNull
	default List<Integer> getIntegerListUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return (List<Integer>) tempObject;
	}

	/**
	 * Get a List from a File
	 *
	 * @param key key to StringList in File
	 * @return List
	 */
	@NotNull
	default List getList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return (List) tempObject;
	}

	@NotNull
	default List getListUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return (List) tempObject;
	}

	/**
	 * Gets a long from a File by key
	 *
	 * @param key key to long in File
	 * @return String from File
	 */
	default long getLong(final @NotNull String key) {
		Object tempObject = this.get(key);
		return Primitive.LONG.getLong(tempObject);
	}

	default long getLongUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return Primitive.LONG.getLong(tempObject);
	}

	/**
	 * Get a Long-List from a File
	 *
	 * @param key key to Long-List in File
	 * @return Long-List
	 */
	@NotNull
	default List<Long> getLongList(final @NotNull String key) {
		return (List<Long>) this.get(key);
	}

	@NotNull
	default List<Long> getLongListUseArray(final @NotNull String... key) {
		return (List<Long>) this.getUseArray(key);
	}

	/**
	 * Gets a Map
	 *
	 * @param key key to Map-List in File
	 * @return Map
	 */
	@NotNull
	default Map getMap(final @NotNull String key) {
		Object tempObject = this.get(key);
		return (Map) tempObject;
	}

	@NotNull
	default Map getMapUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return (Map) tempObject;
	}

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 * @return the value set in the File
	 */
	@NotNull
	@SuppressWarnings("DuplicatedCode")
	default <O> O getOrSetDefault(final @NotNull String key, final @NotNull O value) {
		if (!this.hasKey(key)) {
			this.set(key, value);
			return value;
		} else {
			Object tempObj = this.get(key);
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
			return (O) tempObj;
		}
	}

	@NotNull
	@SuppressWarnings("DuplicatedCode")
	default <O> O getOrSetDefaultUseArray(final @NotNull String[] key, final @NotNull O value) {
		if (!this.hasKeyUseArray(key)) {
			this.setUseArray(key, value);
			return value;
		} else {
			Object tempObj = this.getUseArray(key);
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
			return (O) tempObj;
		}
	}

	/**
	 * Checks whether a key exists in the File
	 *
	 * @param key key to check
	 * @return true if key exists
	 */
	boolean hasKey(final @NotNull String key);

	boolean hasKeyUseArray(final @NotNull String... key);

	/**
	 * Set an Object to your File
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	void set(final @NotNull String key, final @Nullable Object value);

	void setUseArray(final @NotNull String[] key, final @Nullable Object value);

	/**
	 * Set several key, value pairs
	 *
	 * @param dataMap the pairs to be set
	 */
	void setAll(final @NotNull Map<String, Object> dataMap);

	void setAllUseArray(final @NotNull Map<String[], Object> dataMap);

	/**
	 * Set several key, value pairs
	 *
	 * @param blockKey the key of the SubBlock
	 * @param dataMap  the pairs to be set
	 */
	void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap);

	void setAllUseArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap);

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	@NotNull Set<String> keySet();

	@NotNull Set<String[]> keySetUseArray();

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the keySet of all sublayers of the given key or an empty set if the key does not exist (Format: key.subkey).
	 */
	@NotNull Set<String> keySet(final @NotNull String key);

	@NotNull Set<String[]> keySetUseArray(final @NotNull String... key);


	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	@NotNull Set<String> blockKeySet();

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or an empty set if the key does not exist.
	 */
	@NotNull Set<String> blockKeySet(final @NotNull String key);

	@NotNull Set<String> blockKeySetUseArray(final @NotNull String... key);

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key key to value in File
	 * @param def the Class to be casted to
	 * @param <O> ReturnType
	 * @return returns the value of the key casted to def
	 */
	@NotNull
	default <O> O get(final @NotNull String key, final @NotNull Class<O> def) {
		return Primitive.getFromDef(this.get(key), def);
	}

	@NotNull
	default <O> O getUseArray(final @NotNull String[] key, final @NotNull Class<O> def) {
		return Primitive.getFromDef(this.getUseArray(key), def);
	}

	/**
	 * Get a String from a File
	 *
	 * @param key key to String in File
	 * @return Returns the value
	 */
	@NotNull
	default String getString(final @NotNull String key) {
		Object tempObject = this.get(key);
		return tempObject instanceof String ? (String) tempObject : tempObject.toString();
	}

	@NotNull
	default String getStringUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return tempObject instanceof String ? (String) tempObject : tempObject.toString();
	}

	/**
	 * Get String List
	 *
	 * @param key key to String List in File
	 * @return List
	 */
	@NotNull
	default List<String> getStringList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return (List<String>) tempObject;
	}

	@NotNull
	default List<String> getStringListUseArray(final @NotNull String... key) {
		Object tempObject = this.getUseArray(key);
		return (List<String>) tempObject;
	}

	/**
	 * Remove a key from the File
	 *
	 * @param key the key to remove
	 */
	void remove(final @NotNull String key);

	void removeUseArray(final @NotNull String... key);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAll(final @NotNull String... keys);

	void removeAllUseArray(final @NotNull String[]... keys);

	void removeAll(final @NotNull Collection<String> keys);

	void removeAllUseArray(final @NotNull Collection<String[]> keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAll(final @NotNull String blockKey, final @NotNull String... keys);

	void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Sets a value to the File if the File doesn't already contain the value
	 * (Do not mix up with Bukkit addDefault)
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 */
	default void setDefault(final @NotNull String key, final @Nullable Object value) {
		if (!this.hasKey(key)) {
			set(key, value);
		}
	}

	default void setDefaultUseArray(final @NotNull String[] key, final @Nullable Object value) {
		if (!this.hasKeyUseArray(key)) {
			this.setUseArray(key, value);
		}
	}

	@NotNull FlatSection getSection(final @NotNull String sectionKey);

	@NotNull FlatSection getSectionUseArray(final @NotNull String[] sectionKey);
}