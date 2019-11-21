package de.zeanon.storage.internal.base.cache.filedata;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.util.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Default FileData, storing the Data in Maps
 *
 * @author Zeanon
 * @version 2.5.0
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StandardFileData implements FileData<Map<String, Object>, Map.Entry<String, Object>>, Comparable<StandardFileData> {

	/**
	 * internal cache for the contents of the File
	 */
	@Getter(onMethod = @__({@Override}))
	@NotNull
	private Map<String, Object> dataMap = new HashMap<>();


	/**
	 * Get a List consisting of TripletMap.Entry objects (whereas values being TripletMaps are also parsed to entryLists)
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("-> new")
	public final List<Map.Entry<String, Object>> entryList() {
		return this.internalEntryList(this.dataMap);
	}

	/**
	 * Get a List consisting of TripletMap.Entry objects (without internal parsing)
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("-> new")
	public final List<Map.Entry<String, Object>> blockEntryList() {
		return new ArrayList<>(this.dataMap.entrySet());
	}

	/**
	 * Get a List consisting of TripletMap.Entry objects (whereas values being TripletMaps are also parsed to entryLists)
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<Map.Entry<String, Object>> entryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalEntryList((Map) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	/**
	 * Get a List consisting of TripletMap.Entry objects (without internal parsing)
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<Map.Entry<String, Object>> blockEntryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return new ArrayList<>(((Map) tempObject).entrySet());
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<Map.Entry<String, Object>> entryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalEntryList((Map) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	@NotNull
	@Contract("_ -> new")
	public final List<Map.Entry<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return new ArrayList<>(((Map) tempObject).entrySet());
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	public void loadData(@Nullable Map<String, Object> map) {
		if (map != null) {
			this.dataMap = map;
		}
	}

	@Override
	public void insert(final @NotNull String key, @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		this.initialInsert(value, parts);
	}

	@Override
	public void insertUseArray(final @NotNull String[] key, @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		this.initialInsert(value, key);
	}

	@Override
	public void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		this.initialRemove(parts);
	}

	@Override
	public void removeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		this.initialRemove(key);
	}

	@Override
	public boolean containsKey(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.internalContainsKey(this.dataMap, parts, 0);
	}

	@Override
	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.internalContainsKey(this.dataMap, key, 0);
	}

	@Override
	@NotNull
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.internalGet(this.dataMap, parts);
	}

	@Override
	@NotNull
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.internalGet(this.dataMap, key);
	}

	@Override
	public int blockSize() {
		return this.dataMap.size();
	}

	@Override
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			return ((Map) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			return ((Map) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	public int size() {
		return this.internalSize(this.dataMap);
	}

	@Override
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalSize((Map) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof Map) {
			//noinspection unchecked
			return this.internalSize((Map) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	public void clear() {
		this.dataMap.clear();
	}

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
			final Map<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof Map
					? (Map) tempValue
					: (this.dataMap instanceof LinkedHashMap
					   ? new LinkedHashMap<>()
					   : new HashMap<>());
			this.dataMap.put(parts[0], this.internalInsert(childMap, parts, value, 1));
		}
	}

	@NotNull
	private Object internalInsert(final @NotNull Map<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final Map<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof Map
					? (Map) tempValue
					: (map instanceof LinkedHashMap
					   ? new LinkedHashMap<>()
					   : new HashMap<>());
			map.put(key[keyIndex], this.internalInsert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			return value;
		}
	}

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

	@NotNull
	@Contract("_, _, _ -> param1")
	private Map<String, Object> internalRemove(final @NotNull Map<String, Object> map, final @NotNull String[] key, final int keyIndex) {
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

	@NotNull
	private List<Map.Entry<String, Object>> internalEntryList(final @NotNull Map<String, Object> map) {
		final List<Map.Entry<String, Object>> tempList = new ArrayList<>(map.entrySet());
		for (Map.Entry<String, Object> entry : tempList) {
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

	@NotNull
	private Object internalGet(final @NotNull Map map, final @NotNull String[] key) {
		Object tempValue = map;
		for (String tempKey : key) {
			if (tempValue instanceof Map) {
				tempValue = ((Map) tempValue).get(tempKey);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			}
		}
		return tempValue;
	}

	private int internalSize(final @NotNull Map<String, Object> map) {
		int size = 0;
		for (Map.Entry entry : new ArrayList<>(map.entrySet())) {
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
		return Integer.compare(this.dataMap.size(), Objects.notNull(fileData.dataMap).size());
	}


	@NotNull
	@Override
	public String toString() {
		return this.dataMap.toString();
	}
}