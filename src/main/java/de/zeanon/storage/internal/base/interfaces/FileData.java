package de.zeanon.storage.internal.base.interfaces;

import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface FileData<T, K, V> {


	/**
	 * Reload the contents of the FileData
	 *
	 * @param map the Contents to be inserted
	 */
	void loadData(final @Nullable Map<K, V> map);

	/**
	 * Method to assign a value to a key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	void insert(final T key, final @Nullable Object value);

	void insertUseArray(final T[] key, final @Nullable Object value);

	/**
	 * Remove a key with its assigned value from the map if given key exists
	 *
	 * @param key the key to be removed from the map
	 */
	void remove(final T key);

	void removeUseArray(final T... key);

	/**
	 * Check whether the map contains a certain key
	 *
	 * @param key the key to be looked for
	 * @return true if the key exists, otherwise false
	 */
	boolean containsKey(final T key);

	boolean containsKeyUseArray(final T... key);

	/**
	 * get the keySet of all layers of the map combined
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey)
	 */
	@NotNull
	Set<T> keySet();

	@NotNull
	Set<T[]> keySetUseArray();

	/**
	 * get the keySet of all sublayers of the given key combined
	 *
	 * @param key the key of the Block
	 * @return the keySet of all sublayers of the given key or null if the key does not exist (Format: key.subkey)
	 */
	@NotNull
	Set<T> keySet(final T key);

	@NotNull
	Set<T[]> keySetUseArray(final T... key);

	/**
	 * Method to get the object assign to a key from a FileData Object
	 *
	 * @param key the key to look for
	 * @return the value assigned to the given key or null if the key does not exist
	 */
	@Nullable
	Object get(final T key);

	@Nullable
	Object getUseArray(final T... key);

	/**
	 * get the keySet of a single layer of the map
	 *
	 * @return the keySet of the top layer of localMap
	 */
	@NotNull
	Set<T> blockKeySet();

	/**
	 * get the keySet of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the keySet of the given layer
	 */
	@NotNull
	Set<T> blockKeySet(final T key);

	@NotNull
	Set<T> blockKeySetUseArray(final T... key);

	/**
	 * Get the size of a single layer of the map
	 *
	 * @return the size of the top layer of map
	 */
	int blockSize();

	/**
	 * get the size of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the size of the given layer or 0 if the key does not exist
	 */
	int blockSize(final T key);

	int blockSizeUseArray(final T... key);

	/**
	 * Get the size of the local map
	 *
	 * @return the size of all layers of localMap combined
	 */
	int size();

	/**
	 * Convert FileData to a nested HashMap
	 */
	@NotNull
	Map<T, V> toMap();

	/**
	 * get the size of all sublayers of the given key combined
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist
	 */
	int size(final T key);

	int sizeUseArray(final T... key);

	/**
	 * Clear the contents of this FileData
	 */
	void clear();

	Set<FileData.Entry<T, V>> entrySet();

	@Override
	String toString();

	interface Entry<T, V> {

		T getKey();

		V getValue();

		V setValue(V value);
	}
}