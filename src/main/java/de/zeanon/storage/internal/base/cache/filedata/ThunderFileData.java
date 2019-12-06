package de.zeanon.storage.internal.base.cache.filedata;

import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.DataMap;
import de.zeanon.storage.internal.base.interfaces.FileData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
 * @version 1.5.0
 */
@Getter
@EqualsAndHashCode
@Accessors(fluent = true, chain = false)
@SuppressWarnings({"unused", "DefaultAnnotationParam"})
public class ThunderFileData<M extends DataMap, E extends Map.Entry, L extends List> implements FileData<M, E, L>, Comparable<ThunderFileData> {


	private final @NotNull Provider<M, L> provider;
	/**
	 * internal cache for the contents of the File
	 */
	private @NotNull M dataMap;
	@Setter
	private boolean synchronizedData;


	@Contract(pure = true)
	protected ThunderFileData(final @NotNull Provider<M, L> provider, final boolean synchronize) {
		this.provider = provider;
		this.synchronizedData = synchronize;
		//noinspection unchecked
		this.dataMap = this.synchronizedData ? ((M) Collections.synchronizedMap(this.provider().newMap())) : this.provider().newMap(); //NOSONAR
	}

	/**
	 * Get a List consisting of DataMap.TripletNode objects of the top most layer of the internal DataMap
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
	 * Get a List consisting of DataMap.TripletNode objects whereas values being instances of DataMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("_ -> new")
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
	 * Get a List consisting of DataMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("_ -> new")
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
	 * Get a List consisting of DataMap.TripletNode objects whereas values being instances of DataMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("_ -> new")
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
	 * Get a List consisting of DataMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("_ -> new")
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
	 * Get a List consisting of DataMap.TripletNode objects whereas values being instances of DataMap are also getting parsed to
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
			this.dataMap = this.synchronizedData ? ((M) Collections.synchronizedMap(map)) : map;
		} else {
			//noinspection unchecked
			this.dataMap = this.synchronizedData ? ((M) Collections.synchronizedMap(this.provider().newMap())) : this.provider().newMap(); //NOSONAR
		}
	}

	/**
	 * Map a value to a given key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	@Override
	public void insert(final @NotNull String key, @Nullable Object value) {
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
	public boolean containsKeyUseArray(final @NotNull String... key) {
		return this.internalContainsKey(this.dataMap, key, 0);
	}

	/**
	 * Get the value mapped to a specific key
	 *
	 * @param key the key to look for
	 *
	 * @return the Value mapped to the given key
	 *
	 * @throws ObjectNullException if the given key does not exist
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
	 *
	 * @throws ObjectNullException if the given key does not exist
	 */
	@Override
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
	private void initialInsert(@Nullable Object value, @NotNull String[] parts) {
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			final @Nullable Object tempValue = this.dataMap.get(parts[0]);
			//noinspection unchecked
			final @NotNull DataMap<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof DataMap
					? (DataMap) tempValue
					: this.provider().newMap();
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
					: this.provider().newMap();
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			//noinspection unchecked
			return value instanceof Map && !(value instanceof DataMap) ? this.parseMap((Map) value) : value;
		}
	}

	private void initialRemove(@NotNull String[] parts) {
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
	private @NotNull DataMap internalRemove(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) {
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

	private @NotNull List<DataMap.TripletNode<String, Object>> internalEntryList(final @NotNull DataMap<String, Object> map) {
		final @NotNull List<DataMap.TripletNode<String, Object>> tempList = map.entryList();
		for (@NotNull DataMap.TripletNode<String, Object> entry : tempList) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				entry.setValue(this.internalEntryList((DataMap) entry.getValue()));
			}
		}
		return tempList;
	}

	private boolean internalContainsKey(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final @Nullable Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof DataMap) {
				return this.internalContainsKey((DataMap) tempValue, key, keyIndex + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + key[keyIndex] + "'");
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}

	private @Nullable Object internalGet(final @NotNull DataMap map, final @NotNull String[] key) {
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

	private int internalSize(final @NotNull DataMap<String, Object> map) {
		int size = 0;
		for (@NotNull DataMap.TripletNode entry : map.entryList()) {
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
		final @NotNull DataMap<String, Object> tempMap = this.provider().newMap();
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


	@Override
	public int compareTo(final @NotNull ThunderFileData fileData) {
		return Integer.compare(this.dataMap.size(), fileData.dataMap.size());
	}

	@Override
	public @NotNull String toString() {
		return this.dataMap.toString();
	}
}