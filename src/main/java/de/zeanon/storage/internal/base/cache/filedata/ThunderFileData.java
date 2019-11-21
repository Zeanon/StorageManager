package de.zeanon.storage.internal.base.cache.filedata;

import de.zeanon.storage.internal.base.cache.datamap.HashTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.LinkedTripletMap;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.TripletMap;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Advanced FileData, storing the Data in a TripletMap({@link TripletMap})
 *
 * @author Zeanon
 * @version 1.5.0
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
public class ThunderFileData implements FileData<TripletMap<String, Object>, TripletMap.Entry<String, Object>>, Comparable<ThunderFileData> {

	/**
	 * Defines whether a faster TripleMap implementation shall be used, which in turn requires more memory
	 */
	@Getter
	@Setter
	private boolean fastMap;
	/**
	 * internal cache for the contents of the File
	 */
	@Getter(onMethod = @__({@Override}))
	@NotNull
	private TripletMap<String, Object> dataMap;


	public ThunderFileData(final boolean fastMap) {
		this.fastMap = fastMap;
		this.dataMap = this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>();
	}

	@Override
	@NotNull
	public final List<TripletMap.Entry<String, Object>> blockEntryList() {
		return this.dataMap.entryList();
	}

	@Override
	@NotNull
	public final List<TripletMap.Entry<String, Object>> entryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.entryList((TripletMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	@NotNull
	public final List<TripletMap.Entry<String, Object>> blockEntryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return ((TripletMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	@NotNull
	public final List<TripletMap.Entry<String, Object>> entryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.entryList((TripletMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	@NotNull
	public final List<TripletMap.Entry<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return ((TripletMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	@NotNull
	public List<TripletMap.Entry<String, Object>> entryList() {
		return this.entryList(this.dataMap);
	}

	@Override
	public void loadData(final @Nullable TripletMap<String, Object> map) {
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
	public void insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
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
		return this.containsKey(this.dataMap, parts, 0);
	}

	@Override
	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.containsKey(this.dataMap, key, 0);
	}

	@Override
	@Nullable
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.get(this.dataMap, parts);
	}

	@Override
	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(this.dataMap, key);
	}

	@Override
	public int blockSize() {
		return this.dataMap.size();
	}

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

	@Override
	public int size() {
		return this.size(this.dataMap);
	}

	@Override
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.size((TripletMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof TripletMap) {
			//noinspection unchecked
			return this.size((TripletMap) tempObject);
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
			final TripletMap<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof TripletMap
					? (TripletMap) tempValue
					: (this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>());
			this.dataMap.put(parts[0], this.insert(childMap, parts, value, 1));
		}
	}

	@NotNull
	private Object insert(final @NotNull TripletMap<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final TripletMap<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof TripletMap
					? (TripletMap) tempValue
					: (this.fastMap ? new LinkedTripletMap<>() : new HashTripletMap<>());
			map.put(key[keyIndex], this.insert(childMap, key, value, keyIndex + 1));
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
			if (tempValue instanceof TripletMap) {
				this.dataMap.put(parts[0], this.remove((TripletMap) tempValue, parts, 1));
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
	private TripletMap remove(final @NotNull TripletMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof TripletMap) {
				//noinspection unchecked
				map.put(key[keyIndex], this.remove((TripletMap) tempValue, key, keyIndex + 1));
				if (((TripletMap) tempValue
				).isEmpty()) {
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
	private List<TripletMap.Entry<String, Object>> entryList(final @NotNull TripletMap<String, Object> map) {
		final List<TripletMap.Entry<String, Object>> tempList = map.entryList();
		for (TripletMap.Entry<String, Object> entry : tempList) {
			if (entry.getValue() instanceof TripletMap) {
				//noinspection unchecked
				entry.setValue(this.entryList((TripletMap) entry.getValue()));
			}
		}
		return tempList;
	}

	private boolean containsKey(final @NotNull TripletMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof TripletMap) {
				return this.containsKey((TripletMap) tempValue, key, keyIndex + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + key[keyIndex] + "'");
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}


	@Nullable
	private Object get(final @NotNull TripletMap map, final @NotNull String[] key) {
		Object tempValue = map;
		for (String tempKey : key) {
			if (tempValue instanceof TripletMap) {
				tempValue = ((TripletMap) tempValue).get(tempKey);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "' -> could not find '" + tempKey + "'");
			}
		}
		return tempValue;
	}


	private int size(final @NotNull TripletMap<String, Object> map) {
		int size = 0;
		for (TripletMap.Entry entry : map.entryList()) {
			if (entry.getValue() instanceof TripletMap) {
				//noinspection unchecked
				size += this.size((TripletMap) entry.getValue());
			} else {
				size++;
			}
		}
		return size;
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