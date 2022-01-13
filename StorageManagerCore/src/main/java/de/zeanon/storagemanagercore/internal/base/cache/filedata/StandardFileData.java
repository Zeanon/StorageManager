package de.zeanon.storagemanagercore.internal.base.cache.filedata;

import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Default FileData, storing the Data in Standard maps
 *
 * @author Zeanon
 * @version 2.7.0
 */
@Getter
@EqualsAndHashCode
@Accessors(fluent = true, chain = false)
@SuppressWarnings({"DefaultAnnotationParam", "unused", "rawtypes"})
public class StandardFileData<M extends Map, E extends Map.Entry, L extends List> implements FileData<M, E, L>, Comparable<StandardFileData>, Serializable { //NOSONAR


	private static final long serialVersionUID = 785462429090408444L;


	/**
	 * The provider to be used to get new Map and List instances
	 * <p>
	 * -- Getter --
	 *
	 * @return the internal Provider to be used
	 */
	private final @NotNull CollectionsProvider<M, L> collectionsProvider;
	/**
	 * Internal cache for the contents of the File
	 * <p>
	 * -- Getter --
	 *
	 * @return the internal DataMap
	 */
	private transient @NotNull M dataMap;


	/**
	 * Initializes a new FileData
	 *
	 * @param collectionsProvider the internal Provider to be used
	 */
	@Contract(pure = true)
	protected StandardFileData(final @NotNull CollectionsProvider<M, L> collectionsProvider) {
		this.collectionsProvider = collectionsProvider;
		this.dataMap = this.collectionsProvider().newMap(); //NOSONAR
	}

	/**
	 * Initializes a new FileData
	 *
	 * @param collectionsProvider the internal Provider to be used
	 */
	@Contract(pure = true)
	protected StandardFileData(final @NotNull CollectionsProvider<M, L> collectionsProvider, final @NotNull M dataMap) {
		this.collectionsProvider = collectionsProvider;
		this.dataMap = dataMap;
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("-> new")
	public @NotNull List<E> entryList() {
		//noinspection unchecked
		return this.internalEntryList(this.dataMap);
	}

	/**
	 * Get a List consisting of Map.Entry objects of the top most layer of the internal DataMap
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("-> new")
	public @NotNull List<E> blockEntryList() {
		//noinspection unchecked
		return this.collectionsProvider.newList(new Class[]{Set.class}, this.dataMap.entrySet());
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<E> entryList(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalEntryList((Map) tempObject);
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of Map.Entry objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<E> blockEntryList(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.collectionsProvider.newList(new Class[]{Set.class}, ((Map) tempObject).entrySet());
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("null -> fail")
	public @Nullable List<E> entryListUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalEntryList((Map) tempObject);
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of Map.Entry objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("null -> fail")
	public @Nullable List<E> blockEntryListUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.collectionsProvider.newList(new Class[]{Set.class}, ((Map) tempObject).entrySet());
		} else {
			return null;
		}
	}


	@Override
	public @NotNull List<String> getKeys() {
		//noinspection unchecked
		return this.internalGetKeys(this.dataMap);
	}

	@Override
	public @NotNull List<String> getBlockKeys() {
		//noinspection unchecked
		return this.collectionsProvider.newList(new Class[]{Set.class}, this.dataMap.keySet());
	}

	@Override
	public @Nullable List<String> getKeys(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalGetKeys((Map) tempObject);
		} else {
			return null;
		}
	}

	@Override
	public @Nullable List<String> getBlockKeys(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.collectionsProvider.newList(new Class[]{Set.class}, ((Map) tempObject).keySet());
		} else {
			return null;
		}
	}

	@Override
	public @Nullable List<String[]> getKeysUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalGetKeysUseArray((Map) tempObject);
		} else {
			return null;
		}
	}

	@Override
	public @Nullable List<String> getBlockKeysUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.collectionsProvider.newList(new Class[]{Set.class}, ((Map) tempObject).keySet());
		} else {
			return null;
		}
	}

	/**
	 * Reload the internal cache
	 *
	 * @param map the values to be loaded
	 */
	@Override
	public void loadData(final @Nullable M map) {
		if (map != null) {
			this.dataMap = map;
		} else {
			this.dataMap = this.collectionsProvider().newMap(); //NOSONAR
		}
	}

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	@Override
	public void insert(final @NotNull String key,
					   final @Nullable Object value) {
		final @NotNull String[] parts = key.split("\\.");
		this.initialInsert(value, parts);
	}

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	@Override
	public void insertUseArray(final @NotNull String[] key,
							   final @Nullable Object value) {
		this.initialInsert(value, key);
	}

	/**
	 * Remove a given key
	 *
	 * @param key the key to be used
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	@Override
	public void remove(final @NotNull String key) {
		final @NotNull String[] parts = key.split("\\.");
		this.initialRemove(parts);
	}

	/**
	 * Remove a given key
	 *
	 * @param key the key to be used
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	@Override
	@Contract("null -> fail")
	public void removeUseArray(final @NotNull String... key) {
		this.initialRemove(key);
	}

	/**
	 * Checks whether the internal cache contains a given key
	 *
	 * @param key the key to be looked for
	 *
	 * @return true if the given key exists, false if not
	 */
	@Override
	public boolean containsKey(final @NotNull String key) {
		final @NotNull String[] parts = key.split("\\.");
		//noinspection unchecked
		return this.internalContainsKey(this.dataMap, parts, 0);
	}

	/**
	 * Checks whether the internal cache contains a given key
	 *
	 * @param key the key to be looked for
	 *
	 * @return true if the given key exists, false if not
	 */
	@Override
	@Contract("null -> fail")
	public boolean containsKeyUseArray(final @NotNull String... key) {
		//noinspection unchecked
		return this.internalContainsKey(this.dataMap, key, 0);
	}

	/**
	 * Get the value mapped to a specific key
	 *
	 * @param key the key to look for
	 *
	 * @return the Value mapped to the given key
	 */
	@Override
	public @Nullable Object get(final @NotNull String key) {
		final @NotNull String[] parts = key.split("\\.");
		return this.internalGet(this.dataMap, parts);
	}

	/**
	 * Get the value mapped to a specific key
	 *
	 * @param key the key to look for
	 *
	 * @return the Value mapped to the given key
	 */
	@Override
	@Contract("null -> fail")
	public @Nullable Object getUseArray(final @NotNull String... key) {
		return this.internalGet(this.dataMap, key);
	}

	/**
	 * @return the size of the top most layer of the internal DataMap
	 */
	@Override
	public int blockSize() {
		return this.dataMap.size();
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the size of the given Block of the internal DataMap
	 */
	@Override
	public int blockSize(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			return ((Map) tempObject).size();
		} else {
			return 0;
		}
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the size of the given Block of the internal DataMap
	 */
	@Override
	@Contract("null -> fail")
	public int blockSizeUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			return ((Map) tempObject).size();
		} else {
			return 0;
		}
	}

	/**
	 * @return the amount of all Entries from the DataMap combined
	 */
	@Override
	public int size() {
		//noinspection unchecked
		return this.internalSize(this.dataMap);
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the amount of all Entries from the given Block combined
	 */
	@Override
	public int size(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalSize((Map) tempObject);
		} else {
			return 0;
		}
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the amount of all Entries from the given Block combined
	 */
	@Override
	@Contract("null -> fail")
	public int sizeUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalSize((Map) tempObject);
		} else {
			return 0;
		}
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	@Override
	public void clear() {
		this.dataMap.clear();
	}

	/**
	 * Checks whether the internal DataMap is empty
	 *
	 * @return true if so
	 */
	@Override
	public boolean isEmpty() {
		return this.dataMap.isEmpty();
	}


	// <Internal>
	private @Nullable Object internalGet(final @NotNull Map map, final @NotNull String... key) { //NOSONAR
		@NotNull Object tempValue = map;
		for (final String tempKey : key) {
			if (tempValue instanceof Map) {
				tempValue = ((Map) tempValue).get(tempKey);
			} else {
				return null;
			}
		}
		return tempValue;
	}

	@Contract("_, null -> fail")
	private void initialInsert(final @Nullable Object value,
							   final @NotNull String... key) {
		if (value == null) {
			this.removeUseArray(key);
		} else {
			final Object tempValue = this.dataMap.get(key[0]);
			//noinspection unchecked
			final @NotNull Map<String, Object> childMap =
					this.dataMap.containsKey(key[0])
					&& tempValue instanceof Map
					? (Map) tempValue
					: this.collectionsProvider().newMap();
			//noinspection unchecked
			this.dataMap.put(key[0], this.internalInsert(childMap, key, value, 1));
		}
	}

	private @NotNull Object internalInsert(final @NotNull Map<String, Object> map,
										   final @NotNull String[] key,
										   final @NotNull Object value,
										   final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final @NotNull Map<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof Map
					? (Map) tempValue
					: this.collectionsProvider().newMap();
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			return value;
		}
	}

	@Contract("null -> fail")
	private void initialRemove(final @NotNull String... key) {
		if (key.length == 1) {
			this.dataMap.remove(key[0]);
		} else {
			final Object tempValue = this.dataMap.get(key[0]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				this.dataMap.put(key[0], this.internalRemove((Map) tempValue, key, 1));
				if (((Map) this.dataMap.get(key[0])).isEmpty()) {
					this.dataMap.remove(key[0]);
				}
			}
		}
	}

	@Contract("_, _, _ -> param1")
	private @NotNull Map<String, Object> internalRemove(final @NotNull Map<String, Object> map,
														final @NotNull String[] key,
														final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				map.put(key[keyIndex], this.internalRemove((Map) tempValue, key, keyIndex + 1));
				if (((Map) map.get(key[keyIndex])).isEmpty()) {
					map.remove(key[keyIndex]);
				}
			}
		} else {
			map.remove(key[keyIndex]);
		}
		return map;
	}

	private @NotNull List<Map.Entry<String, Object>> internalEntryList(final @NotNull Map<String, Object> map) {
		//noinspection unchecked
		final @NotNull List<Map.Entry<String, Object>> tempList = this.collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				tempList.add(new Node<>(entry.getKey(), this.internalEntryList((Map) entry.getValue())));
			}
		}
		return tempList;
	}

	private @NotNull List<String> internalGetKeys(final @NotNull Map<String, Object> map) {
		//noinspection unchecked
		final @NotNull List<String> tempList = this.collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				for (final @NotNull String key : this.internalGetKeys((DataMap<String, Object>) entry.getValue())) {
					tempList.add(entry.getKey() + "." + key);
				}
			} else {
				tempList.add(entry.getKey());
			}
		}
		return tempList;
	}

	private @NotNull List<String[]> internalGetKeysUseArray(final @NotNull Map<String, Object> map) {
		//noinspection unchecked
		final @NotNull List<String[]> tempList = this.collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				for (final @NotNull String[] key : this.internalGetKeysUseArray((DataMap<String, Object>) entry.getValue())) {
					tempList.add(Objects.addElementInFrontOfArray(entry.getKey(), key));
				}
			} else {
				tempList.add(new String[]{entry.getKey()});
			}
		}
		return tempList;
	}

	private boolean internalContainsKey(final @NotNull Map<String, Object> map,
										final @NotNull String[] key,
										final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final @Nullable Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				return this.internalContainsKey((Map) tempValue, key, keyIndex + 1);
			} else {
				return false;
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}

	private int internalSize(final @NotNull Map<String, Object> map) {
		int size = 0;
		for (final @NotNull Map.Entry entry : map.entrySet()) { //NOSONAR
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				size += this.internalSize((Map) entry.getValue());
			} else {
				size++;
			}
		}
		return size;
	}
	// </Internal>


	// <Serialization>
	private void writeObject(final @NotNull ObjectOutputStream outputStream) throws IOException {
		outputStream.defaultWriteObject();
		outputStream.writeObject(this.dataMap());
	}

	private void readObject(final @NotNull ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		//noinspection unchecked
		this.dataMap = (M) inputStream.readObject();
	}
	// </Serialization>


	/**
	 * Compare this FileData to a given one
	 *
	 * @param fileData the FileData to be compared to
	 */
	@Override
	public int compareTo(final @NotNull StandardFileData fileData) {
		return Integer.compare(this.dataMap.size(), fileData.dataMap.size());
	}


	/**
	 * Returns a String representation of the FileData
	 *
	 * @return the FileData parsed to a String
	 */
	@Override
	public @NotNull String toString() {
		return this.dataMap.toString();
	}


	@EqualsAndHashCode
	@Accessors(fluent = false)
	@Getter(onMethod_ = {@Override})
	@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)})
	@SuppressWarnings({"DefaultAnnotationParam", "unused"})
	private static class Node<K, V> implements Map.Entry<K, V> {

		/**
		 * -- Getter --
		 * Returns the key corresponding to this entry.
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the key corresponding to this entry
		 */
		private final @NotNull K key;

		/**
		 * -- Getter --
		 * Returns the value corresponding to this entry.  If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this entry
		 */
		private @Nullable
		V value;

		/**
		 * Replaces the value corresponding to this entry with the specified
		 * value (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param value new value to be stored in this entry
		 *
		 * @return old value corresponding to the entry
		 *
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *                                       is not supported by the backing map
		 * @throws ClassCastException            if the class of the specified value
		 *                                       prevents it from being stored in the backing map
		 * @throws NullPointerException          if the backing map does not permit
		 *                                       null values, and the specified value is null
		 * @throws IllegalArgumentException      if some property of this value
		 *                                       prevents it from being stored in the backing map
		 */
		@Override
		public @Nullable
		V setValue(final @Nullable V value) {
			final @Nullable V returnValue = this.value;
			this.value = value;
			return returnValue;
		}


		/**
		 * Returns a String representation of the Node
		 *
		 * @return the Node parsed to a String
		 */
		@Override
		public @NotNull String toString() {
			return "(" + this.key + "=" + this.value + ")";
		}
	}
}