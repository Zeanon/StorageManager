package de.zeanon.storage.internal.data.cache;

import de.zeanon.storage.internal.base.data.DataMap;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ThunderFileData implements FileData<DataMap<String, Object>, DataMap.Entry<String, Object>>, Comparable<ThunderFileData> {

	@Getter(onMethod = @__({@Override}))
	@NotNull
	private DataMap<String, Object> dataMap = new DataMap<>();


	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> entryList() {
		return this.entryList(this.dataMap);
	}

	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> blockEntryList() {
		return this.dataMap.entryList();
	}

	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> entryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.entryList((DataMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> blockEntryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return ((DataMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> entryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.entryList((DataMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	@NotNull
	public final List<DataMap.Entry<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return ((DataMap) tempObject).entryList();
		} else {
			throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
		}
	}

	@Override
	public void loadData(@Nullable DataMap<String, Object> map) {
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
		return this.get(this.dataMap, parts, 0);
	}

	@Override
	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(this.dataMap, key, 0);
	}

	@Override
	public int blockSize() {
		return this.dataMap.size();
	}

	@Override
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		if (tempObject instanceof DataMap) {
			return ((DataMap) tempObject).size();
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			return ((DataMap) tempObject).size();
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
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.size((DataMap) tempObject);
		} else {
			throw new ObjectNullException("File does not contain '" + key + "'");
		}
	}

	@Override
	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		if (tempObject instanceof DataMap) {
			//noinspection unchecked
			return this.size((DataMap) tempObject);
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


	private void initialInsert(@Nullable Object value, @NotNull String[] parts) {
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			final Object tempValue = this.dataMap.get(parts[0]);
			//noinspection unchecked
			final DataMap<String, Object> childMap =
					this.dataMap.containsKey(parts[0])
					&& tempValue instanceof DataMap
					? (DataMap) tempValue
					: new DataMap();
			this.dataMap.put(parts[0], this.insert(childMap, parts, value, 1));
		}
	}

	@NotNull
	private Object insert(final @NotNull DataMap<String, Object> map, final @NotNull String[] key, final @NotNull Object value, final int keyIndex) {
		if (keyIndex < key.length) {
			final Object tempValue = map.get(key[keyIndex]);
			//noinspection unchecked
			final DataMap<String, Object> childMap =
					map.containsKey(key[keyIndex])
					&& tempValue instanceof DataMap
					? (DataMap) tempValue
					: new DataMap();
			map.put(key[keyIndex], this.insert(childMap, key, value, keyIndex + 1));
			return map;
		} else {
			return value;
		}
	}


	// <Internal>
	private void initialRemove(@NotNull String[] parts) {
		if (parts.length == 1) {
			this.dataMap.remove(parts[0]);
		} else {
			final Object tempValue = this.dataMap.get(parts[0]);
			if (tempValue instanceof DataMap) {
				this.dataMap.put(parts[0], this.remove((DataMap) tempValue, parts, 1));
				if (((DataMap) tempValue).isEmpty()) {
					this.dataMap.remove(parts[0]);
				}
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(parts) + "'");
			}
		}
	}

	@NotNull
	private DataMap remove(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof DataMap) {
				//noinspection unchecked
				map.put(key[keyIndex], this.remove((DataMap) tempValue, key, keyIndex + 1));
				if (((DataMap) tempValue
				).isEmpty()) {
					map.remove(key[keyIndex]);
				}
				return map;
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
			}
		} else {
			map.remove(key[keyIndex]);
			return map;
		}
	}


	@NotNull
	private List<DataMap.Entry<String, Object>> entryList(final @NotNull DataMap<String, Object> map) {
		final List<DataMap.Entry<String, Object>> tempList = map.entryList();
		for (DataMap.Entry<String, Object> entry : tempList) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				entry.setValue(this.entryList((DataMap) entry.getValue()));
			}
		}
		return tempList;
	}


	private boolean containsKey(final @NotNull DataMap map, final @NotNull String[] key, final int keyIndex) {
		if (keyIndex < key.length - 1) {
			final Object tempValue = map.get(key[keyIndex]);
			if (tempValue instanceof DataMap) {
				return this.containsKey((DataMap) tempValue, key, keyIndex + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
			}
		} else {
			return map.containsKey(key[keyIndex]);
		}
	}


	@Nullable
	private Object get(final @NotNull DataMap map, final @NotNull String[] key, final int id) {
		if (id < key.length - 1) {
			final Object tempValue = map.get(key[id]);
			if (tempValue instanceof DataMap) {
				return this.get((DataMap) tempValue, key, id + 1);
			} else {
				throw new ObjectNullException("File does not contain '" + Arrays.toString(key) + "'");
			}
		} else {
			return map.get(key[id]);
		}
	}


	private int size(final @NotNull DataMap<String, Object> map) {
		int size = 0;
		for (DataMap.Entry entry : map.entryList()) {
			if (entry.getValue() instanceof DataMap) {
				//noinspection unchecked
				size += this.size((DataMap) entry.getValue());
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