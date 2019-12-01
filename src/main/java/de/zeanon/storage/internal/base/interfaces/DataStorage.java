package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.base.sections.FlatSection;
import de.zeanon.storage.internal.utility.basic.Objects;
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
	@Nullable Object get(final @NotNull String key);

	/**
	 * Get an Object from a File
	 *
	 * @param key key to Object in the File
	 *
	 * @return Object from File
	 */
	@Nullable Object getUseArray(final @NotNull String... key);

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key key to value in the File
	 * @param def the Class to be casted to
	 * @param <O> ReturnType
	 *
	 * @return returns the value of the key casted to def
	 */
	default @Nullable <O> O get(final @NotNull String key, final @NotNull Class<O> def) {
		return Objects.toDef(this.get(key), def);
	}

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key key to value in the File
	 * @param def the Class to be casted to
	 * @param <O> ReturnType
	 *
	 * @return returns the value of the key casted to def
	 */
	default @Nullable <O> O getUseArray(final @NotNull String[] key, final @NotNull Class<O> def) {
		return Objects.toDef(this.getUseArray(key), def);
	}

	/**
	 * Get a String from a File
	 *
	 * @param key key to String in the File
	 *
	 * @return Returns the value
	 */
	default @Nullable String getString(final @NotNull String key) {
		return Objects.toString(this.get(key));
	}

	/**
	 * Get a String from a File
	 *
	 * @param key key to String in the File
	 *
	 * @return Returns the value
	 */
	default @Nullable String getStringUseArray(final @NotNull String... key) {
		return Objects.toString(this.getUseArray(key));
	}

	/**
	 * Get String List
	 *
	 * @param key key to String List in the File
	 *
	 * @return List
	 */
	default @Nullable List<String> getStringList(final @NotNull String key) {
		return (List<String>) this.get(key);
	}

	/**
	 * Get String List
	 *
	 * @param key key to String List in the File
	 *
	 * @return List
	 */
	default @Nullable List<String> getStringListUseArray(final @NotNull String... key) {
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
		return Objects.toBoolean(this.get(key));
	}

	/**
	 * Get a boolean from a File
	 *
	 * @param key key to boolean in the File
	 *
	 * @return Boolean from File
	 */
	default boolean getBooleanUseArray(final @NotNull String... key) {
		return Objects.toBoolean(this.getUseArray(key));
	}

	/**
	 * Get a byte from a File
	 *
	 * @param key key to byte in the File
	 *
	 * @return Byte from File
	 */
	default byte getByte(final @NotNull String key) {
		return Objects.toByte(this.get(key));
	}

	/**
	 * Get a byte from a File
	 *
	 * @param key key to byte in the File
	 *
	 * @return Byte from File
	 */
	default byte getByteUseArray(final @NotNull String... key) {
		return Objects.toByte(this.getUseArray(key));
	}

	/**
	 * Get a Byte-List from a File
	 *
	 * @param key key to Byte-List in the File
	 *
	 * @return Byte-List
	 */
	default @Nullable List<Byte> getByteList(final @NotNull String key) {
		return (List<Byte>) this.get(key);
	}

	/**
	 * Get a Byte-List from a File
	 *
	 * @param key key to Byte-List in the File
	 *
	 * @return Byte-List
	 */
	default @Nullable List<Byte> getByteListUseArray(final @NotNull String... key) {
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
		return Objects.toDouble(this.get(key));
	}

	/**
	 * Get a double from a File
	 *
	 * @param key key to double in the File
	 *
	 * @return Double from File
	 */
	default double getDoubleUseArray(final @NotNull String... key) {
		return Objects.toDouble(this.getUseArray(key));
	}

	/**
	 * Get a Double-List from a File
	 *
	 * @param key key to Double-List int the File
	 *
	 * @return Double-List
	 */
	default @Nullable List<Double> getDoubleList(final @NotNull String key) {
		return (List<Double>) this.get(key);
	}

	/**
	 * Get a Double-List from a File
	 *
	 * @param key key to Double-List int the File
	 *
	 * @return Double-List
	 */
	default @Nullable List<Double> getDoubleListUseArray(final @NotNull String... key) {
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
		return Objects.toFloat(this.get(key));
	}

	/**
	 * Get a float from a File
	 *
	 * @param key key to float in the File
	 *
	 * @return Float from File
	 */
	default float getFloatUseArray(final @NotNull String... key) {
		return Objects.toFloat(this.getUseArray(key));
	}

	/**
	 * Get a Float-List from a File
	 *
	 * @param key key to float in the File
	 *
	 * @return Float-List
	 */
	default @Nullable List<Float> getFloatList(final @NotNull String key) {
		return (List<Float>) this.get(key);
	}

	/**
	 * Get a Float-List from a File
	 *
	 * @param key key to float in the File
	 *
	 * @return Float-List
	 */
	default @Nullable List<Float> getFloatListUseArray(final @NotNull String... key) {
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
		return Objects.toInt(this.get(key));
	}

	/**
	 * Gets an int from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Int from File
	 */
	default int getIntUseArray(final @NotNull String... key) {
		return Objects.toInt(this.getUseArray(key));
	}

	/**
	 * Get a Integer-List from a File
	 *
	 * @param key key to Integer-List in the File
	 *
	 * @return Integer-List
	 */
	default @Nullable List<Integer> getIntegerList(final @NotNull String key) {
		return (List<Integer>) this.get(key);
	}

	/**
	 * Get a Integer-List from a File
	 *
	 * @param key key to Integer-List in the File
	 *
	 * @return Integer-List
	 */
	default @Nullable List<Integer> getIntegerListUseArray(final @NotNull String... key) {
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
		return Objects.toShort(this.get(key));
	}

	/**
	 * Gets a short from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Short from File
	 */
	default short getShortUseArray(final @NotNull String... key) {
		return Objects.toShort(this.getUseArray(key));
	}

	/**
	 * Gets a Short-List from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Short-List
	 */
	default @Nullable List<Short> getShortList(final @NotNull String key) {
		return (List<Short>) this.get(key);
	}

	/**
	 * Gets a Short-List from a File
	 *
	 * @param key key to int in the File
	 *
	 * @return Short-List
	 */
	default @Nullable List<Short> getShortListUseArray(final @NotNull String... key) {
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
		return Objects.toLong(this.get(key));
	}

	/**
	 * Gets a long from a File by key
	 *
	 * @param key key to long in the File
	 *
	 * @return Long from File
	 */
	default long getLongUseArray(final @NotNull String... key) {
		return Objects.toLong(this.getUseArray(key));
	}

	/**
	 * Get a Long-List from a File
	 *
	 * @param key key to Long-List in the File
	 *
	 * @return Long-List
	 */
	default @Nullable List<Long> getLongList(final @NotNull String key) {
		return (List<Long>) this.get(key);
	}

	/**
	 * Get a Long-List from a File
	 *
	 * @param key key to Long-List in the File
	 *
	 * @return Long-List
	 */
	default @Nullable List<Long> getLongListUseArray(final @NotNull String... key) {
		return (List<Long>) this.getUseArray(key);
	}

	/**
	 * Get a List from a File
	 *
	 * @param key key to List in the File
	 *
	 * @return List
	 */
	default @Nullable List getList(final @NotNull String key) {
		return (List) this.get(key);
	}

	/**
	 * Get a List from a File
	 *
	 * @param key key to List in the File
	 *
	 * @return List
	 */
	default @Nullable List getListUseArray(final @NotNull String... key) {
		return (List) this.getUseArray(key);
	}

	/**
	 * Get a Map from a File
	 *
	 * @param key key to Map in the File
	 *
	 * @return Map
	 */
	default @Nullable Map getMap(final @NotNull String key) {
		return (Map) this.get(key);
	}

	/**
	 * Get a Map from a File
	 *
	 * @param key key to Map in the File
	 *
	 * @return Map
	 */
	default @Nullable Map getMapUseArray(final @NotNull String... key) {
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
	default @Nullable <K, V> Pair<K, V> getPair(final @NotNull String key) {
		return (Pair<K, V>) this.get(key);
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
	default @Nullable <K, V> Pair<K, V> getPairUseArray(final @NotNull String... key) {
		return (Pair<K, V>) this.getUseArray(key);
	}

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String, Object> getAll(final @NotNull String... keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String[], Object> getAllUseArray(final @NotNull String[]... keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String, Object> getAll(final @NotNull Collection<String> keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	/**
	 * Returns all values of the given keys
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to get from
	 *
	 * @return Map of the give keys and their values
	 */
	@Nullable Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 *
	 * @return the value set in the File
	 */
	default @Nullable <O> O getOrSetDefault(final @NotNull String key, final @NotNull O value) {
		if (!this.hasKey(key)) {
			this.set(key, value);
			return value;
		} else {
			return Objects.toDef(this.get(key), value);
		}
	}

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 *
	 * @return the value set in the File
	 */
	default @Nullable <O> O getOrSetDefaultUseArray(final @NotNull String[] key, final @NotNull O value) {
		if (!this.hasKeyUseArray(key)) {
			this.setUseArray(key, value);
			return value;
		} else {
			return Objects.toDef(this.getUseArray(key), value);
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

	/**
	 * Checks whether a key exists in the File
	 *
	 * @param key key to check
	 *
	 * @return true if key exists
	 */
	boolean hasKeyUseArray(final @NotNull String... key);


	/**
	 * Set an Object to your File
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	void set(final @NotNull String key, final @Nullable Object value);

	/**
	 * Set an Object to your File
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
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

	/**
	 * Set several key, value pairs
	 *
	 * @param blockKey the key of the SubBlock
	 * @param dataMap  the pairs to be set
	 */
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

	/**
	 * Sets a value to the File if the File doesn't already contain the value
	 * (Do not mix up with Bukkit addDefault)
	 *
	 * @param key   key to set the value
	 * @param value value to set
	 */

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

	/**
	 * Remove a key from the File
	 *
	 * @param key the key to remove
	 */
	void removeUseArray(final @NotNull String... key);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAll(final @NotNull String... keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAllUseArray(final @NotNull String[]... keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAll(final @NotNull Collection<String> keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param keys the keys to remove
	 */
	void removeAllUseArray(final @NotNull Collection<String[]> keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAll(final @NotNull String blockKey, final @NotNull String... keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys);

	/**
	 * Remove given keys from a File
	 *
	 * @param blockKey the key of the targeted SubBlock
	 * @param keys     the keys to remove
	 */
	void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys);


	/**
	 * Get a Section of the DataStorage
	 *
	 * @param sectionKey the key to the Section
	 *
	 * @return the Section corresponding to the given key
	 */
	@NotNull FlatSection getSection(final @NotNull String sectionKey);

	/**
	 * Get a Section of the DataStorage
	 *
	 * @param sectionKey the key to the Section
	 *
	 * @return the Section corresponding to the given key
	 */
	@NotNull FlatSection getSectionUseArray(final @NotNull String... sectionKey);
}