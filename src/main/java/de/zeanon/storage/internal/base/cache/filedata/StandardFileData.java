package de.zeanon.storage.internal.base.cache.filedata;

import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.FileData;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Default FileData, storing the Data in Standard maps
 *
 * @author Zeanon
 * @version 2.5.0
 */
@Getter
@EqualsAndHashCode
@SuppressWarnings("unused")
public class StandardFileData<M extends Map, L extends List> implements FileData<M, Map.Entry<String, Object>, L>, Comparable<StandardFileData> {


	@Accessors(fluent = true)
	private final @NotNull Provider<M, L> provider;
	/**
	 * internal cache for the contents of the File
	 */
	private @NotNull M dataMap;


	@Contract(pure = true)
	protected StandardFileData(final @NotNull Provider<M, L> provider) {
		this.provider = provider;
		this.dataMap = this.provider.newMap();
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@Contract("-> new")
	public @NotNull List<Map.Entry<String, Object>> entryList() {
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
	public @NotNull List<Map.Entry<String, Object>> blockEntryList() {
		//noinspection unchecked
		return provider.newList(new Class[]{Set.class}, this.dataMap.entrySet());
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
	@Contract("_ -> new")
	public @Nullable List<Map.Entry<String, Object>> entryList(final @NotNull String key) {
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
	@Contract("_ -> new")
	public @Nullable List<Map.Entry<String, Object>> blockEntryList(final @NotNull String key) {
		final @Nullable Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return provider.newList(new Class[]{Set.class}, ((Map) tempObject).entrySet());
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
	@Contract("_ -> new")
	public @Nullable List<Map.Entry<String, Object>> entryListUseArray(final @NotNull String... key) {
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
	@Contract("_ -> new")
	public @Nullable List<Map.Entry<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		final @Nullable Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return provider.newList(new Class[]{Set.class}, ((Map) tempObject).entrySet());
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
	public void loadData(@Nullable M map) {
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
	public void insertUseArray(final @NotNull String[] key, @Nullable Object value) {
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
	@Synchronized
	private void initialInsert(@Nullable Object value, @NotNull String[] parts) {
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			final Object tempValue = this.dataMap.get(parts[0]);
			//noinspection unchecked
			final @NotNull Map<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof Map
					? (Map) tempValue
					: provider.newMap();
			//noinspection unchecked
			this.dataMap.put(parts[0], this.internalInsert(childMap, parts, value, 1));
		}
	}

	private @NotNull Object internalInsert(final @NotNull Map<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final @NotNull Map<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof Map
					? (Map) tempValue
					: this.provider.newMap();
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			return value;
		}
	}

	@Synchronized
	private void initialRemove(@NotNull String[] parts) {
		if (parts.length == 1) {
			this.dataMap.remove(parts[0]);
		} else {
			final Object tempValue = this.dataMap.get(parts[0]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				this.dataMap.put(parts[0], this.internalRemove((Map) tempValue, parts, 1));
				if (((Map) this.dataMap.get(parts[0])).isEmpty()) {
					this.dataMap.remove(parts[0]);
				}
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(parts) + "' -> could not find '" + parts[0] + "'");
			}
		}
	}

	@Contract("_, _, _ -> param1")
	private @NotNull Map<String, Object> internalRemove(final @NotNull Map<String, Object> map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				map.put(key[keyIndex], this.internalRemove((Map) tempValue, key, keyIndex + 1));
				if (((Map) map.get(key[keyIndex])).isEmpty()) {
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

	private @NotNull List<Map.Entry<String, Object>> internalEntryList(final @NotNull Map<String, Object> map) {
		//noinspection unchecked
		final @NotNull List<Map.Entry<String, Object>> tempList = provider.newList();
		for (@NotNull Map.Entry<String, Object> entry : tempList) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				entry.setValue(this.internalEntryList((Map<String, Object>) entry.getValue()));
			}
		}
		return tempList;
	}

	private boolean internalContainsKey(final @NotNull Map<String, Object> map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof Map) {
				//noinspection unchecked
				return this.internalContainsKey((Map) tempValue, key, keyIndex + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + key[keyIndex] + "'");
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}

	@Synchronized
	private @Nullable Object internalGet(final @NotNull Map map, final @NotNull String[] key) {
		@NotNull Object tempValue = map;
		for (String tempKey : key) {
			try {
				if (tempValue instanceof Map) {
					tempValue = ((Map) tempValue).get(tempKey);
				} else {
					throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
				}
			} catch (NullPointerException e) {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			}
		}
		return tempValue;
	}

	private int internalSize(final @NotNull Map<String, Object> map) {
		int size = 0;
		for (@NotNull Map.Entry entry : map.entrySet()) {
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


	@Override
	public int compareTo(final @NotNull StandardFileData fileData) {
		return Integer.compare(this.dataMap.size(), fileData.dataMap.size());
	}


	@Override
	public @NotNull String toString() {
		return this.dataMap.toString();
	}
}