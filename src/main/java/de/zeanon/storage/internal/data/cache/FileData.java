package de.zeanon.storage.internal.data.cache;

import de.zeanon.storage.internal.base.interfaces.DataList;
import de.zeanon.storage.internal.base.lists.ArrayDataList;
import de.zeanon.storage.internal.base.lists.LinkedDataList;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.*;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileData implements Comparable<FileData> {

	@Getter
	@NotNull
	private DataList<DataList.Entry<String, Object>> dataList = new ArrayDataList<>();

	public void loadData(final @Nullable Map<String, Object> map) {
		if (map != null) {
			this.dataList = this.parseMap(map, 0);
		}
	}

	public void loadData(final @Nullable DataList<DataList.Entry<String, Object>> dataList) {
		if (dataList != null) {
			this.dataList = dataList;
		}
	}


	/**
	 * Method to assign a value to a key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	public void insert(final @NotNull String key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			final Object tempValue = this.get(this.dataList, parts[0]);
			//noinspection unchecked
			this.put(this.dataList, parts[0],
					 this.containsKey(this.dataList, parts[0])
					 && tempValue instanceof DataList
					 ? this.insert((DataList) tempValue, parts, value, 1)
					 : this.insert(this.dataList instanceof LinkedDataList
								   ? new LinkedDataList<>()
								   : new ArrayDataList<>(), parts, value, 1));
		}
	}

	public void insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		if (value == null) {
			this.removeUseArray(key);
		} else {
			final Object tempValue = this.get(this.dataList, key[0]);
			//noinspection unchecked
			this.put(this.dataList, key[0],
					 this.containsKey(this.dataList, key[0])
					 && tempValue instanceof DataList
					 ? this.insert((DataList) tempValue, key, value, 1)
					 : this.insert(this.dataList instanceof LinkedDataList
								   ? new LinkedDataList<>()
								   : new ArrayDataList<>(), key, value, 1));
		}
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists
	 *
	 * @param key the key to be removed from the map
	 */
	public void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKey(key)) {
			final String[] parts = key.split("\\.");
			this.removeKey(this.dataList, parts, 0);
		}
	}

	public void removeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKeyUseArray(key)) {
			this.removeKey(this.dataList, key, 0);
		}
	}

	/**
	 * Check whether the map contains a certain key
	 *
	 * @param key the key to be looked for
	 * @return true if the key exists, otherwise false
	 */
	public boolean containsKey(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.containsKey(this.dataList, parts, 0);
	}

	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.containsKey(this.dataList, key, 0);
	}

	/**
	 * get the keyList of all layers of the map combined
	 *
	 * @return the keyList of all layers of localMap combined (Format: key.subkey)
	 */

	@NotNull
	public List<String> keyList() {
		return this.keyList(this.dataList, true);
	}


	@NotNull
	public List<String[]> keyListUseArray() {
		return this.keyListUseArray(this.dataList);
	}

	/**
	 * get the keyList of all sublayers of the given key combined
	 *
	 * @param key the key of the Block
	 * @return the keyList of all sublayers of the given key or null if the key does not exist (Format: key.subkey)
	 */

	@NotNull
	public List<String> keyList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.keyList((DataList) tempObject, true) : new ArrayList<>();
	}


	@NotNull
	public List<String[]> keyListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.keyListUseArray((DataList) tempObject) : new ArrayList<>();
	}

	/**
	 * Method to get the object assign to a key from a FileData Object
	 *
	 * @param key the key to look for
	 * @return the value assigned to the given key or null if the key does not exist
	 */

	@Nullable
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.get(this.dataList, parts, 0);
	}


	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(this.dataList, key, 0);
	}

	/**
	 * get the keyList of a single layer of the map
	 *
	 * @return the keyList of the top layer of localMap
	 */

	@NotNull
	public List<String> blockKeyList() {
		return this.keyList(dataList, false);
	}

	/**
	 * get the keyList of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the keyList of the given layer
	 */

	@NotNull
	public List<String> blockKeyList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.keyList((DataList) tempObject, false) : new ArrayList<>();
	}


	@NotNull
	public List<String> blockKeyListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.keyList((DataList) tempObject, false) : new ArrayList<>();
	}

	/**
	 * Get the size of a single layer of the map
	 *
	 * @return the size of the top layer of map
	 */
	public int blockSize() {
		return this.dataList.size();
	}

	/**
	 * get the size of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the size of the given layer or 0 if the key does not exist
	 */
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		return tempObject instanceof DataList ? ((Map) tempObject).size() : 0;
	}

	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		return tempObject instanceof DataList ? ((Map) tempObject).size() : 0;
	}

	/**
	 * Get the size of the local map
	 *
	 * @return the size of all layers of localMap combined
	 */
	public int size() {
		return this.size(this.dataList);
	}

	/**
	 * Convert FileData to a nested HashMap
	 */

	@NotNull
	public Map<String, Object> toMap() {
		return this.toMap(this.dataList);
	}

	/**
	 * get the size of all sublayers of the given key combined
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist
	 */
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.size((DataList) tempObject) : 0;
	}

	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof DataList ? this.size((DataList) tempObject) : 0;
	}

	/**
	 * Clear the contents of this FileData
	 */
	public void clear() {
		this.dataList.clear();
	}

	public boolean isEmpty() {
		return this.dataList.isEmpty();
	}

	@NotNull
	public List<DataList.Entry<String, Object>> entryList() {
		return this.entryList(this.dataList, true);
	}

	@NotNull
	public List<DataList.Entry<String, Object>> blockEntryList() {
		return this.entryList(this.dataList, false);
	}

	@NotNull
	public List<DataList.Entry<String, Object>> entryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		return tempObject instanceof DataList ? this.entryList((DataList) tempObject, true) : new ArrayList<>();
	}

	@NotNull
	public List<DataList.Entry<String, Object>> blockEntryList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.get(key);
		return tempObject instanceof DataList ? this.entryList((DataList) tempObject, false) : new ArrayList<>();
	}

	@NotNull
	public List<DataList.Entry<String, Object>> entryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		return tempObject instanceof DataList ? this.entryList((DataList) tempObject, true) : new ArrayList<>();
	}

	@NotNull
	public List<DataList.Entry<String, Object>> blockEntryListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		final Object tempObject = this.getUseArray(key);
		return tempObject instanceof DataList ? this.entryList((DataList) tempObject, false) : new ArrayList<>();
	}

	@NotNull
	public DataList<DataList.Entry<String, Object>> parseMap(final @NotNull Map<String, Object> map) {
		return this.parseMap(map, 0);
	}

	// <Local>
	@NotNull
	private DataList<DataList.Entry<String, Object>> parseMap(final @NotNull Map<String, Object> map, int line) {
		DataList<DataList.Entry<String, Object>> tempList = map instanceof LinkedHashMap ? new LinkedDataList<>() : new ArrayDataList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				tempList.add(new DataList.Entry<>(entry.getKey(), this.parseMap((Map) entry.getValue(), line)));
			} else {
				tempList.add(new DataList.Entry<>(entry.getKey(), entry.getValue()));
			}
			line++;
		}
		return tempList;
	}

	private void put(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String key, final @NotNull Object value) {
		for (final DataList.Entry<String, Object> entry : list) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		list.add(new DataList.Entry<>(key, value, list.size()));
	}

	@NotNull
	private DataList<DataList.Entry<String, Object>> removeKey(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String[] key, final int id) {
		final DataList<DataList.Entry<String, Object>> tempList = list instanceof LinkedDataList ? new LinkedDataList<>() : new ArrayDataList<>();
		if (id < key.length) {
			if (id == key.length - 1) {
				this.remove(tempList, key[id]);
			} else {
				final Object tempValue = this.get(list, key[id]);
				if (tempValue instanceof DataList) {
					//noinspection unchecked
					this.put(list, key[id], this.removeKey((DataList) tempValue, key, id + 1));
					//noinspection unchecked
					if (((Map<Pair<Integer, String>, Object>) tempValue).isEmpty()) {
						this.remove(list, key[id]);
					}
				}
				return list;
			}
		}
		return tempList;
	}

	private boolean containsKey(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String[] key, final int id) {
		if (id < key.length - 1) {
			final Object tempValue = this.get(list, key[id]);
			if (tempValue instanceof DataList) {
				//noinspection unchecked
				return this.containsKey((DataList) tempValue, key, id + 1);
			} else {
				return false;
			}
		} else {
			return this.containsKey(list, key[id]);
		}
	}


	@Nullable
	private Object get(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String[] key, final int id) {
		if (id < key.length - 1) {
			final Object tempValue = this.get(list, key[id]);
			if (tempValue instanceof DataList) {
				//noinspection unchecked
				return this.get((DataList) tempValue, key, id + 1);
			} else {
				return null;
			}
		} else {
			return this.get(list, key[id]);
		}
	}

	private void remove(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String key) {
		list.removeIf(entry -> entry.getKey().equals(key));
	}

	@NotNull
	private Object insert(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String[] key, final @NotNull Object value, final int id) {
		if (id < key.length) {
			final DataList<DataList.Entry<String, Object>> tempList = list instanceof LinkedList ? new LinkedDataList<>(list) : new ArrayDataList<>(list);
			final Object tempValue = this.get(list, key[id]);
			//noinspection unchecked
			final DataList<DataList.Entry<String, Object>> childMap =
					this.containsKey(list, key[id])
					&& tempValue instanceof DataList
					? (DataList) tempValue
					: list;
			this.put(tempList, key[id], this.insert(childMap, key, value, id + 1));
			return tempList;
		} else {
			return value;
		}
	}

	private boolean containsKey(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String key) {
		for (final DataList.Entry<String, Object> entry : list) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}


	@Nullable
	private Object get(final @NotNull DataList<DataList.Entry<String, Object>> list, final @NotNull String key) {
		for (final DataList.Entry<String, Object> entry : list) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@NotNull
	private Map<String, Object> toMap(final @NotNull DataList<DataList.Entry<String, Object>> list) {
		final Map<String, Object> tempMap = new HashMap<>();
		for (DataList.Entry<String, Object> entry : this.dataList) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				tempMap.put(entry.getKey(), this.toMap((DataList) entry.getValue()));
			} else {
				tempMap.put(entry.getKey(), entry.getValue());
			}
		}
		return tempMap;
	}

	private int size(final @NotNull DataList<DataList.Entry<String, Object>> list) {
		int size = 0;
		for (DataList.Entry<String, Object> entry : list) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				size += this.size((DataList) entry.getValue());
			} else {
				size++;
			}
		}
		return size;
	}

	@NotNull
	private List<String> keyList(final @NotNull DataList<DataList.Entry<String, Object>> list, final boolean deep) {
		final List<String> tempList = list instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : list) {
			if (deep && entry.getValue() instanceof DataList) {
				//noinspection unchecked
				for (String tempKey : this.keyList((DataList<DataList.Entry<String, Object>>) entry.getValue(), true)) {
					tempList.add(entry.getKey() + "." + tempKey);
				}
			} else {
				tempList.add(entry.getKey());
			}
		}
		return tempList;
	}

	@NotNull
	private List<String[]> keyListUseArray(final @NotNull DataList<DataList.Entry<String, Object>> list) {
		final List<String[]> tempList = list instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : list) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				for (String[] tempKey : this.keyListUseArray((DataList<DataList.Entry<String, Object>>) entry.getValue())) {
					final String[] key = new String[1 + tempKey.length];
					key[0] = entry.getKey();
					System.arraycopy(tempKey, 0, key, 1, tempKey.length);
					tempList.add(key);
				}
			} else {
				tempList.add(new String[]{
						entry.getKey()
				});
			}
		}
		return tempList;
	}

	@NotNull
	private List<DataList.Entry<String, Object>> entryList(final @NotNull DataList list, final boolean deep) {
		final List<DataList.Entry<String, Object>> tempList = this.dataList instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : this.dataList) {
			if (deep && entry.getValue() instanceof DataList) {
				entry.setValue(this.entryList((DataList) entry.getValue(), true));
			}
			tempList.add(new DataList.Entry<>(entry.getKey(), entry.getValue(), entry.getLine()));
		}
		return tempList;
	}
	// </Local>


	@Override
	public int compareTo(final @NotNull FileData fileData) {
		return Integer.compare(this.dataList.size(), Objects.notNull(fileData.dataList).size());
	}


	@NotNull
	@Override
	public String toString() {
		return this.toMap().toString();
	}
}