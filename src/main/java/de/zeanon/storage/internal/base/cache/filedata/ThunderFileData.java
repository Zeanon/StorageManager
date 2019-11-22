package de.zeanon.storage.internal.base.cache.filedata;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.HashTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.LinkedTripletMap;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Optimized FileData for ThunderFile, storing the Data in a ({@link TripletMap})
 *
 * @author Zeanon
 * @version 1.5.0
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
public class ThunderFileData implements FileData<TripletMap<String, Object>, TripletMap.TripletNode<String, Object>>, Comparable<ThunderFileData> {

	/**
	 * Defines whether a faster TripleMap implementation shall be used, which in turn requires more memory
	 */
	@Getter
	@Setter
	private boolean fastMap;
	/**
	 * internal cache for the contents of the File
	 */
	@Getter
	@NotNull
	private TripletMap<String, Object> dataMap;


	@Contract(pure = true)
	public ThunderFileData(final boolean fastMap) {
		this.fastMap = fastMap;
		this.dataMap = this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>();
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of the top most layer of the internal DataMap
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("-> new")
	public final List<TripletMap.TripletNode<String, Object>> blockEntryList() {
		return this.dataMap.entryList();
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects whereas values being instances of TripletMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<TripletMap.TripletNode<String, Object>> entryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.internalEntryList((TripletMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<TripletMap.TripletNode<String, Object>> blockEntryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return ((TripletMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects whereas values being instances of TripletMap are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<TripletMap.TripletNode<String, Object>> entryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.internalEntryList((TripletMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<TripletMap.TripletNode<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return ((TripletMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	/**
	 * Get a List consisting of TripletMap.TripletNode objects whereas values being instances of TripletMap are also getting parsed to
	 * their entryLists
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("-> new")
	public List<TripletMap.TripletNode<String, Object>> entryList() {
		return this.internalEntryList(this.dataMap);
	}

	/**
	 * Reload the internal cache
	 *
	 * @param map the values to be loaded
	 */
	@Override
	public void loadData(final @Nullable TripletMap<String, Object> map) {
		if (map != null) {
			this.dataMap = map;
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
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
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
		Objects.checkNull(key, "Key must not be null");
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
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
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
		Objects.checkNull(key, "Key must not be null");
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
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
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
		Objects.checkNull(key, "Key must not be null");
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
	@NotNull
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
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
	@NotNull
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
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
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			return ((TripletMap) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the size of the given Block of the internal DataMap
	 */
	@Override
	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			return ((TripletMap) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	/**
	 * @return the amount of all Entries from the DataMap combined
	 */
	@Override
	public int size() {
		return this.internalSize(this.dataMap);
	}

	/**
	 * @param key the Key to the SubBlock the size should be computed of
	 *
	 * @return the amount of all Entries from the given Block combined
	 */
	@Override
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.internalSize((TripletMap) tempObject);
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
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.internalSize((TripletMap) tempObject);
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
			final Object tempValue = this.dataMap.get(parts[0]);
			//noinspection unchecked
			final TripletMap<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof TripletMap
					? (TripletMap) tempValue
					: (this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>());
			this.dataMap.put(parts[0], this.internalInsert(childMap, parts, value, 1));
		}
	}

	@NotNull
	private Object internalInsert(final @NotNull TripletMap<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final TripletMap<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof TripletMap
					? (TripletMap) tempValue
					: (this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>());
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			//noinspection unchecked
			return value instanceof Map && !(value instanceof TripletMap) ? this.parseMap((Map) value, this.fastMap) : value;
		}
	}

	private void initialRemove(@NotNull String[] parts) {
		if (parts.length == 1) {
			this.dataMap.remove(parts[0]);
		} else {
			final Object tempValue = this.dataMap.get(parts[0]);
			if (tempValue instanceof TripletMap) {
				this.dataMap.put(parts[0], this.internalRemove((TripletMap) tempValue, parts, 1));
				if (((TripletMap) tempValue).isEmpty()) {
					this.dataMap.remove(parts[0]);
				}
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(parts) + "' -> could not find '" + parts[0] + "'");
			}
		}
	}

	@NotNull
	@Contract("_, _, _ -> param1")
	private TripletMap internalRemove(final @NotNull TripletMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof TripletMap) {
				//noinspection unchecked
				map.put(key[keyIndex], this.internalRemove((TripletMap) tempValue, key, keyIndex + 1));
				if (((TripletMap) tempValue).isEmpty()) {
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

	@NotNull
	private List<TripletMap.TripletNode<String, Object>> internalEntryList(final @NotNull TripletMap<String, Object> map) {
		final List<TripletMap.TripletNode<String, Object>> tempList = map.entryList();
		for (TripletMap.TripletNode<String, Object> entry : tempList) {
			if (entry.getValue() instanceof TripletMap) {
				//noinspection unchecked
				entry.setValue(this.internalEntryList((TripletMap) entry.getValue()));
			}
		}
		return tempList;
	}

	private boolean internalContainsKey(final @NotNull TripletMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof TripletMap) {
				return this.internalContainsKey((TripletMap) tempValue, key, keyIndex + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + key[keyIndex] + "'");
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}

	@NotNull
	private Object internalGet(final @NotNull TripletMap map, final @NotNull String[] key) {
		@NotNull Object tempValue = map;
		for (String tempKey : key) {
			if (tempValue instanceof TripletMap) {
				tempValue = Objects.notNull(((TripletMap) tempValue).get(tempKey), "File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			}
		}
		return tempValue;
	}

	private int internalSize(final @NotNull TripletMap<String, Object> map) {
		int size = 0;
		for (TripletMap.TripletNode entry : map.entryList()) {
			if (entry.getValue() instanceof TripletMap) {
				//noinspection unchecked
				size += this.internalSize((TripletMap) entry.getValue());
			} else {
				size++;
			}
		}
		return size;
	}

	@NotNull
	@Contract("_, _ -> new")
	private TripletMap<String, Object> parseMap(final @NotNull Map<String, Object> map, final boolean fastMap) {
		Objects.checkNull(map, "Map must not be null");
		final TripletMap<String, Object> tempMap = fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map && !(entry.getValue() instanceof TripletMap)) {
				//noinspection unchecked
				tempMap.add(entry.getKey(), this.parseMap((Map) entry.getValue(), fastMap));
			} else {
				tempMap.add(entry.getKey(), entry.getValue());
			}
		}
		return tempMap;
	}
	// </Internal>


	@Override
	public int compareTo(final @NotNull ThunderFileData fileData) {
		return Integer.compare(this.dataMap.size(), Objects.notNull(fileData.dataMap).size());
	}

	@NotNull
	@Override
	public String toString() {
		return this.dataMap.toString();
	}
}