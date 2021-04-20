package de.zeanon.storagemanagercore.internal.base.cache.filedata;

import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Optimized FileData for ThunderFile, storing the Data in a ({@link DataMap})
 *
 * @author Zeanon
 * @version 1.7.0
 */
@Getter
@EqualsAndHashCode
@Accessors(fluent = true, chain = false)
@SuppressWarnings({"unused", "DefaultAnnotationParam", "rawtypes"})
public class ThunderFileData<M extends DataMap, E extends Map.Entry, L extends List> implements FileData<M, E, L>, Comparable<ThunderFileData>, Serializable { //NOSONAR


	private static final long serialVersionUID = 746673400911930709L;


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
	 * Defines whether the internal Map should be wrapped by {@link Collections#synchronizedMap(Map)}
	 * <p>
	 * -- Getter --
	 *
	 * @return if the Map is synchronized
	 * <p>
	 * -- Setter --
	 * Define if the Map should be synchronized
	 */
	@Setter
	private boolean synchronizeData;


	/**
	 * Initializes a new FileData
	 *
	 * @param collectionsProvider the internal Provider to be used
	 * @param synchronizeData     whether the internal Map should be synchronized
	 */
	@Contract(pure = true)
	protected ThunderFileData(final @NotNull CollectionsProvider<M, L> collectionsProvider, final boolean synchronizeData) {
		this.collectionsProvider = collectionsProvider;
		this.synchronizeData = synchronizeData;
		//noinspection unchecked
		this.dataMap = this.synchronizeData ? ((M) Collections.synchronizedMap(this.collectionsProvider().newMap())) : this.collectionsProvider().newMap(); //NOSONAR
	}

	/**
	 * Initializes a new FileData
	 *
	 * @param collectionsProvider the internal Provider to be used
	 * @param synchronizeData     whether the internal Map should be synchronized
	 */
	@Contract(pure = true)
	protected ThunderFileData(final @NotNull CollectionsProvider<M, L> collectionsProvider, final boolean synchronizeData, final @NotNull M dataMap) {
		this.collectionsProvider = collectionsProvider;
		this.synchronizeData = synchronizeData;
		this.dataMap = dataMap;
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects of the top most layer of the internal DataMap
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("-> new")
	public @NotNull List<E> blockEntryList() {
		//noinspection unchecked
		return this.dataMap.entryList();
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects whereas values being instances of DataMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<E> entryList(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.internalEntryList((DataMap) tempObject);
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<E> blockEntryList(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return ((DataMap) tempObject).entryList();
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects whereas values being instances of DataMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<E> entryListUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.internalEntryList((DataMap) tempObject);
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("null -> fail")
	public @Nullable List<E> blockEntryListUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return ((DataMap) tempObject).entryList();
		} else {
			return null;
		}
	}

	/**
	 * Get a List consisting of DataMap.DataNode objects whereas values being instances of DataMap are also getting parsed to
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
	 * Reload the internal cache
	 *
	 * @param map the values to be loaded
	 */
	@Override
	public void loadData(final @Nullable M map) {
		if (map != null) {
			//noinspection unchecked
			this.dataMap = this.synchronizeData ? ((M) Collections.synchronizedMap(map)) : map;
		} else {
			//noinspection unchecked
			this.dataMap = this.synchronizeData ? ((M) Collections.synchronizedMap(this.collectionsProvider().newMap())) : this.collectionsProvider().newMap(); //NOSONAR
		}
	}

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	@Override
	public void insert(final @NotNull String key, final @Nullable Object value) {
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
	@Contract("null, _ -> fail")
	public void insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
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
	 * @return the size of the given Block of the internal DataMap or 0 if the given block does not exist
	 */
	@Override
	public int blockSize(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			return ((DataMap) tempObject).size();
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
		if (tempObject instanceof DataMap) {
			return ((DataMap) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
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
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.internalSize((DataMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
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
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.internalSize((DataMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
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
	private @Nullable Object internalGet(final @NotNull DataMap map, final @NotNull String... key) { //NOSONAR
		@Nullable Object tempValue = map;
		for (final @NotNull String tempKey : key) {
			if (tempValue instanceof DataMap) {
				tempValue = ((DataMap) tempValue).get(tempKey);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			}
		}
		return tempValue;
	}

	@Contract("_, null -> fail")
	private void initialInsert(final @Nullable Object value, final @NotNull String... parts) {
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			final @Nullable Object tempValue = this.dataMap.get(parts[0]);
			//noinspection unchecked
			final @NotNull DataMap<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof DataMap
					? (DataMap) tempValue
					: this.collectionsProvider().newMap();
			//noinspection unchecked
			this.dataMap.put(parts[0], this.internalInsert(childMap, parts, value, 1));
		}
	}

	private @NotNull Object internalInsert(final @NotNull DataMap<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final @Nullable Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final @NotNull DataMap<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof DataMap
					? (DataMap) tempValue
					: this.collectionsProvider().newMap();
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			//noinspection unchecked
			return value instanceof Map && !(value instanceof DataMap) ? this.parseMap((Map) value) : value;
		}
	}

	@Contract("null -> fail")
	private void initialRemove(final @NotNull String... parts) {
		if (parts.length == 1) {
			this.dataMap.remove(parts[0]);
		} else {
			final @Nullable Object tempValue = this.dataMap.get(parts[0]);
			if (tempValue instanceof DataMap) {
				//noinspection unchecked
				this.dataMap.put(parts[0], this.internalRemove((DataMap) tempValue, parts, 1));
				if (((DataMap) tempValue).isEmpty()) {
					this.dataMap.remove(parts[0]);
				}
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(parts) + "' -> could not find '" + parts[0] + "'");
			}
		}
	}

	@Contract("_, _, _ -> param1")
	private @NotNull DataMap internalRemove(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) { //NOSONAR
		if (keyIndex < key.length - 1) {
			final @Nullable Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof DataMap) {
				//noinspection unchecked
				map.put(key[keyIndex], this.internalRemove((DataMap) tempValue, key, keyIndex + 1));
				if (((DataMap) tempValue).isEmpty()) {
					map.remove(key[keyIndex]);
				}
				return map;
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + key[keyIndex] + "'");
			}
		} else {
			map.remove(key[keyIndex]);
			return map;
		}
	}

	private @NotNull List<DataMap.DataNode<String, Object>> internalEntryList(final @NotNull DataMap<String, Object> map) {
		//noinspection unchecked
		final @NotNull List<DataMap.DataNode<String, Object>> tempList = this.collectionsProvider.newList();
		for (final @NotNull DataMap.DataNode<String, Object> entry : map.entryList()) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				tempList.add(new Node<>(entry.getKey(), this.internalEntryList((DataMap) entry.getValue())));
			}
		}
		return tempList;
	}

	private boolean internalContainsKey(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) { //NOSONAR
		if (keyIndex < key.length - 1) {
			final @Nullable Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof DataMap) {
				return this.internalContainsKey((DataMap) tempValue, key, keyIndex + 1);
			} else {
				return false;
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}

	private int internalSize(final @NotNull DataMap<String, Object> map) {
		int size = 0;
		for (final @NotNull DataMap.DataNode entry : map.entryList()) { //NOSONAR
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				size += this.internalSize((DataMap) entry.getValue());
			} else {
				size++;
			}
		}
		return size;
	}

	@Contract("_ -> new")
	private @NotNull DataMap<String, Object> parseMap(final @NotNull Map<String, Object> map) {
		//noinspection unchecked
		final @NotNull DataMap<String, Object> tempMap = this.collectionsProvider().newMap();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map && !(entry.getValue() instanceof DataMap)) {
				//noinspection unchecked
				tempMap.add(entry.getKey(), this.parseMap((Map) entry.getValue()));
			} else {
				tempMap.add(entry.getKey(), entry.getValue());
			}
		}
		return tempMap;
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
	public int compareTo(final @NotNull ThunderFileData fileData) {
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
	private static class Node<K, V> implements DataMap.DataNode<K, V> {

		/**
		 * The key assigned to this Node
		 * <p>
		 * -- Getter --
		 * Returns the key corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the key corresponding to this node
		 * @throws IllegalStateException implementations may, but are not
		 * required to, throw this exception if the entry has been
		 * removed from the backing map.
		 */
		private @NotNull K key;

		/**
		 * The value assigned to this Node
		 * <p>
		 * -- Getter --
		 * Returns the value corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this node
		 * @throws IllegalStateException implementations may, but are not
		 * required to, throw this exception if the entry has been
		 * removed from the backing map.
		 */
		private @Nullable
		V value;

		/**
		 * Replaces the key corresponding to this entry with the specified
		 * key (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param key new key to be stored in this entry
		 *
		 * @return old key corresponding to the entry
		 *
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *                                       is not supported by the backing map
		 * @throws ClassCastException            if the class of the specified key
		 *                                       prevents it from being stored in the backing map
		 * @throws NullPointerException          if the backing map does not permit
		 *                                       null keys, and the specified key is null
		 */
		@Override
		public @NotNull K setKey(final @NotNull K key) {
			try {
				return this.key;
			} finally {
				this.key = key;
			}
		}

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