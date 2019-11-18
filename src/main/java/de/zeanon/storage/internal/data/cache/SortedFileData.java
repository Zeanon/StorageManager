package de.zeanon.storage.internal.data.cache;

import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.*;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SortedFileData implements FileData<String, Pair<Integer, String>, Object>, Comparable<SortedFileData> {


	@NotNull
	private Map<Pair<Integer, String>, Object> localMap = new HashMap<>();

	@Override
	public void loadData(final @Nullable Map<Pair<Integer, String>, Object> map) {
		if (map != null) {
			if (map instanceof LinkedHashMap) {
				this.localMap = new LinkedHashMap<>(map);
			} else {
				this.localMap = new HashMap<>(map);
			}
		} else {
			this.localMap = new HashMap<>();
		}
	}


	/**
	 * Method to assign a value to a key
	 *
	 * @param key   the key to be used
	 * @param value the value to be assigned to the key
	 */
	@Override
	public void insert(final @NotNull String key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		if (value == null) {
			this.removeUseArray(parts);
		} else {
			//noinspection unchecked
			this.put(this.localMap, parts[0],
					 this.containsKey(this.localMap, parts[0])
					 && this.get(this.localMap, parts[0]) instanceof Map
					 ? this.insert((Map) this.get(this.localMap, parts[0]), parts, value, 1)
					 : this.insert(new HashMap<>(), parts, value, 1));
		}
	}

	@Override
	public void insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		if (value == null) {
			this.removeUseArray(key);
		} else {
			//noinspection unchecked
			this.put(this.localMap, key[0],
					 this.containsKey(this.localMap, key[0])
					 && this.get(this.localMap, key[0]) instanceof Map
					 ? this.insert((Map) this.get(this.localMap, key[0]), key, value, 1)
					 : this.insert(new HashMap<>(), key, value, 1));
		}
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists
	 *
	 * @param key the key to be removed from the map
	 */
	@Override
	public void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKey(key)) {
			final String[] parts = key.split("\\.");
			this.removeKey(this.localMap, parts, 0);
		}
	}

	@Override
	public void removeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKeyUseArray(key)) {
			this.removeKey(this.localMap, key, 0);
		}
	}

	/**
	 * Check whether the map contains a certain key
	 *
	 * @param key the key to be looked for
	 * @return true if the key exists, otherwise false
	 */
	@Override
	public boolean containsKey(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		String[] parts = key.split("\\.");
		return this.containsKey(this.localMap, parts, 0);
	}

	@Override
	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.containsKey(this.localMap, key, 0);
	}

	/**
	 * get the keySet of all layers of the map combined
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey)
	 */
	@NotNull
	@Override
	public Set<String> keySet() {
		return this.keySet(this.localMap, true);
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray() {
		return this.keySetUseArray(this.localMap);
	}

	/**
	 * get the keySet of all sublayers of the given key combined
	 *
	 * @param key the key of the Block
	 * @return the keySet of all sublayers of the given key or null if the key does not exist (Format: key.subkey)
	 */
	@NotNull
	@Override
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySet((Map<Pair<Integer, String>, Object>) tempObject, true) : new HashSet<>();
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySetUseArray((Map<Pair<Integer, String>, Object>) tempObject) : new HashSet<>();
	}

	/**
	 * Method to get the object assign to a key from a FileData Object
	 *
	 * @param key the key to look for
	 * @return the value assigned to the given key or null if the key does not exist
	 */
	@Nullable
	@Override
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.get(this.localMap, parts, 0);
	}

	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(this.localMap, key, 0);
	}

	/**
	 * get the keySet of a single layer of the map
	 *
	 * @return the keySet of the top layer of localMap
	 */
	@NotNull
	@Override
	public Set<String> blockKeySet() {
		return this.keySet(localMap, false);
	}

	/**
	 * get the keySet of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the keySet of the given layer
	 */
	@NotNull
	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySet((Map<Pair<Integer, String>, Object>) tempObject, false) : new HashSet<>();
	}

	@NotNull
	@Override
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySet((Map<Pair<Integer, String>, Object>) tempObject, false) : new HashSet<>();
	}

	/**
	 * Get the size of a single layer of the map
	 *
	 * @return the size of the top layer of map
	 */
	@Override
	public int blockSize() {
		return this.localMap.size();
	}

	/**
	 * get the size of a single layer of the map
	 *
	 * @param key the key of the layer
	 * @return the size of the given layer or 0 if the key does not exist
	 */
	@Override
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		return tempObject instanceof Map ? ((Map) tempObject).size() : 0;
	}

	@Override
	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		return tempObject instanceof Map ? ((Map) tempObject).size() : 0;
	}

	/**
	 * Get the size of the local map
	 *
	 * @return the size of all layers of localMap combined
	 */
	@Override
	public int size() {
		return this.size(this.localMap);
	}

	/**
	 * Convert FileData to a nested HashMap
	 */
	@NotNull
	@Override
	public Map<String, Object> toMap() {
		return this.toMap(this.localMap);
	}

	@NotNull
	@Override
	public Map<Pair<Integer, String>, Object> toRawMap() {
		return this.localMap;
	}

	/**
	 * get the size of all sublayers of the given key combined
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist
	 */
	@Override
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		return tempObject instanceof Map ? this.size((Map) tempObject) : 0;
	}

	@Override
	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		return tempObject instanceof Map ? this.size((Map) tempObject) : 0;
	}

	/**
	 * Clear the contents of this FileData
	 */
	@Override
	public void clear() {
		this.localMap = new HashMap<>();
	}

	@Override
	public boolean isEmpty() {
		return this.localMap.isEmpty();
	}

	@Override
	@NotNull
	public Set<Entry<String, Object>> entrySet() {
		Set<Entry<String, Object>> tempSet = new HashSet<>();
		for (Map.Entry<Pair<Integer, String>, Object> entry : this.localMap.entrySet()) {
			tempSet.add(new FileData.Entry<>(entry.getKey().getValue(), entry.getValue()));
		}
		return tempSet;
	}

	@Override
	@NotNull
	public Set<Entry<Pair<Integer, String>, Object>> rawEntrySet() {
		Set<Entry<Pair<Integer, String>, Object>> tempSet = new HashSet<>();
		for (Map.Entry<Pair<Integer, String>, Object> entry : this.localMap.entrySet()) {
			tempSet.add(new FileData.Entry<>(entry.getKey(), entry.getValue()));
		}
		return tempSet;
	}

	@NotNull
	@Override
	public String toRawString() {
		return this.toRawMap().toString();
	}

	private void put(@NotNull Map<Pair<Integer, String>, Object> map, @NotNull String key, @NotNull Object value) {
		for (final Map.Entry<Pair<Integer, String>, Object> entry : map.entrySet()) {
			if (entry.getKey().getValue().equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		map.put(new Pair<>(map.size(), key), value);
	}

	@NotNull
	private Map<Pair<Integer, String>, Object> removeKey(final @NotNull Map<Pair<Integer, String>, Object> map, final @NotNull String[] key, final int id) {
		Map<Pair<Integer, String>, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				this.remove(tempMap, key[id]);
			} else {
				//noinspection unchecked
				this.put(map, key[id], this.removeKey((Map) Objects.notNull(this.get(map, key[id])), key, id + 1));
				//noinspection unchecked
				if (((Map<Pair<Integer, String>, Object>) Objects.notNull(this.get(map, key[id]))).isEmpty()) {
					this.remove(map, key[id]);
				}
				return map;
			}
		}
		return tempMap;
	}

	private boolean containsKey(final @NotNull Map<Pair<Integer, String>, Object> map, final @NotNull String[] key, final int id) {
		if (id < key.length - 1) {
			if (this.containsKey(map, key[id]) && this.get(map, key[id]) instanceof Map) {
				//noinspection unchecked
				Map<Pair<Integer, String>, Object> tempMap = (Map<Pair<Integer, String>, Object>) this.get(map, key[id]);
				return this.containsKey(Objects.notNull(tempMap), key, id + 1);
			} else {
				return false;
			}
		} else {
			return this.containsKey(map, key[id]);
		}
	}

	@Nullable
	private Object get(final @NotNull Map<Pair<Integer, String>, Object> map, final @NotNull String[] key, final int id) {
		if (id < key.length - 1) {
			if (this.get(map, key[id]) instanceof Map) {
				//noinspection unchecked
				Map<Pair<Integer, String>, Object> tempMap = (Map<Pair<Integer, String>, Object>) this.get(map, key[id]);
				return this.get(Objects.notNull(tempMap), key, id + 1);
			} else {
				return null;
			}
		} else {
			return this.get(map, key[id]);
		}
	}

	private void remove(final @NotNull Map<Pair<Integer, String>, Object> map, final @NotNull String key) {
		map.entrySet().removeIf(e -> e.getKey().getValue().equals(key));
	}

	private Object insert(final Map<Pair<Integer, String>, Object> map, final @NotNull String[] key, final Object value, final int id) {
		if (id < key.length) {
			final Map<Pair<Integer, String>, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
			//noinspection unchecked
			final Map<Pair<Integer, String>, Object> childMap =
					this.containsKey(map, key[id])
					&& this.get(map, key[id]) instanceof Map
					? (Map<Pair<Integer, String>, Object>) this.get(map, key[id])
					: map;
			this.put(tempMap, key[id], this.insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}

	@NotNull
	private Set<String> keySet(final @NotNull Map<Pair<Integer, String>, Object> map, final boolean deep) {
		Set<String> tempSet = new HashSet<>();
		for (final Map.Entry<Pair<Integer, String>, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map && deep) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<Pair<Integer, String>, Object>) entry.getValue(), true)) {
					tempSet.add(entry.getKey().getValue() + "." + tempKey);
				}
			} else {
				tempSet.add(entry.getKey().getValue());
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String[]> keySetUseArray(final @NotNull Map<Pair<Integer, String>, Object> map) {
		Set<String[]> tempSet = new HashSet<>();
		for (Map.Entry<Pair<Integer, String>, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String[] tempKey : this.keySetUseArray((Map<Pair<Integer, String>, Object>) entry.getValue())) {
					String[] key = new String[1 + tempKey.length];
					key[0] = entry.getKey().getValue();
					System.arraycopy(tempKey, 0, key, 1, tempKey.length);
					tempSet.add(key);
				}
			} else {
				tempSet.add(new String[]{
						entry.getKey().getValue()
				});
			}
		}
		return tempSet;
	}

	private int size(final @NotNull Map<?, ?> map) {
		int size = map.size();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				size += this.size((Map) entry.getValue());
			}
		}
		return size;
	}

	private boolean containsKey(final @NotNull Map<Pair<Integer, String>, Object> map, final @NotNull String key) {
		for (final Pair<Integer, String> entry : map.keySet()) {
			if (entry.getValue().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	private <V> V get(final @NotNull Map<Pair<Integer, String>, V> map, final @NotNull String key) {
		for (final Map.Entry<Pair<Integer, String>, V> entry : map.entrySet()) {
			if (entry.getKey().getValue().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@NotNull
	private Map<String, Object> toMap(final @NotNull Map<Pair<Integer, String>, Object> map) {
		Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>() : new HashMap<>();
		for (Map.Entry<Pair<Integer, String>, Object> entry : map.entrySet()) {
			//noinspection unchecked
			tempMap.put(entry.getKey().getValue(), entry.getValue() instanceof Map ? this.toMap((Map) entry.getValue()) : entry.getValue());
		}
		return tempMap;
	}

	@Override
	public int compareTo(final @NotNull SortedFileData sortedFileData) {
		return Integer.compare(this.localMap.size(), Objects.notNull(sortedFileData.localMap).size());
	}

	@NotNull
	@Override
	public String toString() {
		return this.toMap().toString();
	}
}