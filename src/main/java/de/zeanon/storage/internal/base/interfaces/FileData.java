package de.zeanon.storage.internal.base.interfaces;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic interface for different classes for cashing the Data read from the DataFile
 *
 * @param <M> the Collection implementation to be used, can also be an own version
 * @param <E> the Entry from it, to be returned by the entryList methods
 */
@SuppressWarnings("unused")
public interface FileData<M, E> {

	void loadData(final @Nullable M map);


	/**
	 * Method to assign a value to a key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	void insert(final @NotNull String key, final @Nullable Object value);

	void insertUseArray(final @NotNull String[] key, final @Nullable Object value);

	/**
	 * Remove a key with its assigned value from the map if given key exists
	 *
	 * @param key the key to be removed from the map
	 */
	void remove(final @NotNull String key);

	void removeUseArray(final @NotNull String... key);

	/**
	 * Check whether the map contains a certain key
	 *
	 * @param key the key to be looked for
	 * @return true if the key exists, otherwise false
	 */
	boolean containsKey(final @NotNull String key);

	boolean containsKeyUseArray(final @NotNull String... key);


	/**
	 * Method to get the object assign to a key from a FileData Object
	 *
	 * @param key the key to look for
	 * @return the value assigned to the given key or null if the key does not exist
	 */
	@Nullable
	Object get(final @NotNull String key);

	@Nullable
	Object getUseArray(final @NotNull String... key);

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
	int blockSize(final @NotNull String key);

	int blockSizeUseArray(final @NotNull String... key);

	/**
	 * Get the size of the local map
	 *
	 * @return the size of all layers of localMap combined
	 */
	int size();

	/**
	 * get the size of all sublayers of the given key combined
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist
	 */
	int size(final @NotNull String key);

	int sizeUseArray(final @NotNull String... key);

	/**
	 * Clear the contents of this FileData
	 */
	void clear();

	boolean isEmpty();

	@NotNull
	M getDataMap();

	@NotNull
	List<E> entryList();

	@NotNull
	List<E> blockEntryList();

	@NotNull
	List<E> entryList(final @NotNull String key);

	@NotNull
	List<E> blockEntryList(final @NotNull String key);

	@NotNull
	List<E> entryListUseArray(final @NotNull String... key);

	@NotNull
	List<E> blockEntryListUseArray(final @NotNull String... key);

	@NotNull
	@Override
	String toString();
}