package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic interface for different classes dedicated to cashing the Data read from the DataFile
 *
 * @param <M> the Collection implementation to be used
 * @param <E> the Entry from it, to be returned by the entryList methods
 *
 * @author Zeanon
 * @version 2.1.0
 */
@SuppressWarnings("unused")
public interface FileData<M extends Map, E extends Map.Entry, L extends List> {


	/**
	 * Reload the internal cache
	 *
	 * @param map the values to be loaded
	 */
	void loadData(final @Nullable M map);

	/**
	 *
	 */
	@NotNull Provider<M, L> provider();

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	void insert(final @NotNull String key, final @Nullable Object value);

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	void insertUseArray(final @NotNull String[] key, final @Nullable Object value);

	/**
	 * Remove a given key
	 *
	 * @param key the key to be used
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	void remove(final @NotNull String key);

	/**
	 * Remove a given key
	 *
	 * @param key the key to be used
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	void removeUseArray(final @NotNull String... key);

	/**
	 * Checks whether the internal cache contains a given key
	 *
	 * @param key the key to be looked for
	 *
	 * @return true if the given key exists, false if not
	 */
	boolean containsKey(final @NotNull String key);

	/**
	 * Checks whether the internal cache contains a given key
	 *
	 * @param key the key to be looked for
	 *
	 * @return true if the given key exists, false if not
	 */
	boolean containsKeyUseArray(final @NotNull String... key);

	/**
	 * Get the value mapped to a specific key
	 *
	 * @param key the key to look for
	 *
	 * @return the Value mapped to the given key
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	@Nullable Object get(final @NotNull String key);

	/**
	 * Get the value mapped to a specific key
	 *
	 * @param key the key to look for
	 *
	 * @return the Value mapped to the given key
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	@Nullable Object getUseArray(final @NotNull String... key);

	/**
	 * @return the size of the top most layer of the internal DataMap
	 */
	int blockSize();

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the size of the given Block of the internal DataMap
	 */
	int blockSize(final @NotNull String key);

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the size of the given Block of the internal DataMap
	 */
	int blockSizeUseArray(final @NotNull String... key);

	/**
	 * @return the amount of all Entries from the DataMap combined
	 */
	int size();

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the amount of all Entries from the given Block combined
	 */
	int size(final @NotNull String key);

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the amount of all Entries from the given Block combined
	 */
	int sizeUseArray(final @NotNull String... key);

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	void clear();

	/**
	 * Checks whether the internal DataMap is empty
	 *
	 * @return true if so
	 */
	boolean isEmpty();

	@NotNull M getDataMap();

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @return the entryList of the internal dataMap
	 */
	@NotNull List<E> entryList();

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of the top most layer of the internal DataMap
	 *
	 * @return the entryList of the internal dataMap
	 */
	@NotNull List<E> blockEntryList();

	/**
	 * Get a List consisting of TripletMap.TripletNode objects whereas values being instances of TripletMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Nullable List<E> entryList(final @NotNull String key);

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Nullable List<E> blockEntryList(final @NotNull String key);

	/**
	 * Get a List consisting of TripletMap.TripletNode objects whereas values being instances of TripletMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Nullable List<E> entryListUseArray(final @NotNull String... key);

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Nullable List<E> blockEntryListUseArray(final @NotNull String... key);

	@Override
	@NotNull String toString();
}