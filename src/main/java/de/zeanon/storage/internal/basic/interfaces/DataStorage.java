package de.zeanon.storage.internal.basic.interfaces;

import de.zeanon.storage.internal.data.base.FlatSection;
import de.zeanon.storage.internal.utility.utils.basic.Primitive;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic Interface for the Data Classes, providing different basic getter and setter methods
 *
 * @author Zeanon
 * @version 2.4.0
 */
@SuppressWarnings({"unused", "unchecked", "UnusedReturnValue"})
public interface DataStorage {


	/**
	 * Get an Object from a File
	 *
	 * @param key key to Object in the File
	 *
	 * @return Object from File
	 */
	@NotNull Object get(final @NotNull String key);

	@NotNull Object getUseArray(final @NotNull String... key);

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key key to value in the File
	 * @param def the Class to be casted to
	 * @param <O> ReturnType
	 *
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
	 * @param key key to String in the File
	 *
	 * @return Returns the value
	 */
	@NotNull
	default String getString(final @NotNull String key) {
		return this.get(key) instanceof String ? (String) this.get(key) : this.get(key).toString();
	}

	@NotNull
	default String getStringUseArray(final @NotNull String... key) {
		return this.getUseArray(key) instanceof String ? (String) this.getUseArray(key) : this.getUseArray(key).toString();
	}

	/**
	 * Get String List
	 *
	 * @param key key to String List in the File
	 *
	 * @return List
	 */
	@NotNull
	default List<String> getStringList(final @NotNull String key) {
		return (List<String>) this.get(key);
	}

	@NotNull
	default List<String> getStringListUseArray(final @NotNull String... key) {
		return (List<String>) this.getUseArray(key);
	}

	/**
	 * Get a boolean from a File
	 *
	 * @param key key to boolean in the File
	 *
	 * @return Boolean from File
	 */
	default boolean getBoolean(final @NotNull String key) {
		return Primitive.BOOLEAN.getBoolean(this.get(key));
	}

	default boolean getBooleanUseArray(final @NotNull String... key) {
		return Primitive.BOOLEAN.getBoolean(this.getUseArray(key));
	}

	/**
	 * Get a byte from a File
	 *
	 * @param key key to byte in the File
	 *
	 * @return Byte from File
	 */
	default byte getByte(final @NotNull String key) {
		return Primitive.BYTE.getByte(this.get(key));
	}

	default byte getByteUseArray(final @NotNull String... key) {
		return Primitive.BYTE.getByte(this.getUseArray(key));
	}

	/**
	 * Get a Byte-List from a File
	 *
	 * @param key key to Byte-List in the File
	 *
	 * @return Byte-List
	 */
	@NotNull
	default List<Byte> getByteList(final @NotNull String key) {
		return (List<Byte>) this.get(key);
	}

	@NotNull
	default List<Byte> getByteListUseArray(final @NotNull String... key) {
		return (List<Byte>) this.getUseArray(key);
	}

	/**
	 * Get a double from a File
	 *
	 * @param key key to double in the File
	 *
	 * @return Double from File
	 */
	default double getDouble(final @NotNull String key) {
		return Primitive.DOUBLE.getDouble(this.get(key));
	}

	default double getDoubleUseArray(final @NotNull String... key) {
		return Primitive.DOUBLE.getDouble(this.getUseArray(key));
	}

	/**
	 * Get a Double-List from a File
	 *
	 * @param key key to Double-List int the File
	 *
	 * @return Double-List
	 */
	@NotNull
	default List<Double> getDoubleList(final @NotNull String key) {
		return (List<Double>) this.get(key);
	}

	@NotNull
	default List<Double> getDoubleListUseArray(final @NotNull String... key) {
		return (List<Double>) this.getUseArray(key);
	}

	/**
	 * Get a float from a File
	 *
	 * @param key key to float in the File
	 *
	 * @return Float from File
	 */
	default float getFloat(final @NotNull String key) {
		return Primitive.FLOAT.getFloat(this.get(key));
	}

	default float getFloatUseArray(final @NotNull String... key) {
		return Primitive.FLOAT.getFloat(this.getUseArray(key));
	}

	/**
	 * Get a Float-List from a File
	 *
	 * @param key key to float in the File
	 *
	 * @return Float-List
	 */
	@NotNull
	default List<Float> getFloatList(final @NotNull String key) {
		return (List<Float>) this.get(key);
	}

	@NotNull
	default List<Float> getFloatListUseArray(final @NotNull String... key) {
		return (List<Float>) this.getUseArray(key);
	}

	/**
	 * Gets an int from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Int from File
	 */
	default int getInt(final @NotNull String key) {
		return Primitive.INTEGER.getInt(this.get(key));
	}

	default int getIntUseArray(final @NotNull String... key) {
		return Primitive.INTEGER.getInt(this.getUseArray(key));
	}

	/**
	 * Get a Integer-List from a File
	 *
	 * @param key key to Integer-List in the File
	 *
	 * @return Integer-List
	 */
	@NotNull
	default List<Integer> getIntegerList(final @NotNull String key) {
		return (List<Integer>) this.get(key);
	}

	@NotNull
	default List<Integer> getIntegerListUseArray(final @NotNull String... key) {
		return (List<Integer>) this.getUseArray(key);
	}

	/**
	 * Gets a short from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Short from File
	 */
	default short getShort(final @NotNull String key) {
		return Primitive.SHORT.getShort(this.get(key));
	}

	default short getShortUseArray(final @NotNull String... key) {
		return Primitive.SHORT.getShort(this.getUseArray(key));
	}

	/**
	 * Gets a Short-List from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Short-List
	 */
	@NotNull
	default List<Short> getShortList(final @NotNull String key) {
		return (List<Short>) this.get(key);
	}

	@NotNull
	default List<Short> getShortListUseArray(final @NotNull String... key) {
		return (List<Short>) this.getUseArray(key);
	}

	/**
	 * Gets a long from a File by key
	 *
	 * @param key key to long in the File
	 *
	 * @return Long from File
	 */
	default long getLong(final @NotNull String key) {
		return Primitive.LONG.getLong(this.get(key));
	}

	default long getLongUseArray(final @NotNull String... key) {
		return Primitive.LONG.getLong(this.getUseArray(key));
	}

	/**
	 * Get a Long-List from a File
	 *
	 * @param key key to Long-List in the File
	 *
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
	 * Get a List from a File
	 *
	 * @param key key to List in the File
	 *
	 * @return List
	 */
	@NotNull
	default List getList(final @NotNull String key) {
		return (List) this.get(key);
	}

	@NotNull
	default List getListUseArray(final @NotNull String... key) {
		return (List) this.getUseArray(key);
	}

	/**
	 * Get a Map from a File
	 *
	 * @param key key to Map in the File
	 *
	 * @return Map
	 */
	@NotNull
	default Map getMap(final @NotNull String key) {
		return (Map) this.get(key);
	}

	@NotNull
	default Map getMapUseArray(final @NotNull String... key) {
		return (Map) this.getUseArray(key);
	}

	/**
	 * Get a Pair from a File
	 *
	 * @param key the key to the Pair in the File
	 * @param <K> the Key-Type of the Pair
	 * @param <V> the ValueType of the Pair
	 *
	 * @return a Pair with a key of type K and a value of type V
	 */
	@NotNull
	default <K, V> Pair<K, V> getPair(final @NotNull String key) {
		return (Pair<K, V>) this.get(key);
	}

	@NotNull
	default <K, V> Pair<K, V> getPairUseArray(final @NotNull String... key) {
		return (Pair<K, V>) this.getUseArray(key);
	}

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 *
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
	 *
	 * @return Map of the give keys and their values
	 */
	@NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	@NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	@NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 *
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
	 *
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


	@NotNull FlatSection getSection(final @NotNull String sectionKey);

	@NotNull FlatSection getSectionUseArray(final @NotNull String[] sectionKey);
}