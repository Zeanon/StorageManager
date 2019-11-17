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
import org.json.JSONObject;


@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SortedFileData implements FileData<String, Pair<Integer, String>, Object>, Comparable<SortedFileData> {


	@NotNull
	private Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> localMap = new HashMap<>();

	@Override
	public void loadData(final @Nullable Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map) {
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
			this.removeUseArray(key);
		} else {
			//noinspection unchecked
			this.localMap.put(parts[0],
							  this.localMap.containsKey(parts[0])
							  && this.localMap.get(parts[0]) instanceof Map
							  ? this.insert((Map) this.localMap.get(parts[0]), parts, value, 1)
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
			this.localMap.put(key[0],
							  this.localMap.containsKey(key[0])
							  && this.localMap.get(key[0]) instanceof Map
							  ? this.insert((Map) this.localMap.get(key[0]), key, value, 1)
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
		return this.keySet(this.localMap);
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
		return tempObject instanceof Map ? this.keySet((Map<@NotNull String, @NotNull Object>) tempObject) : new HashSet<>();
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySetUseArray((Map<@NotNull String, @NotNull Object>) tempObject) : new HashSet<>();
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
		return this.localMap.keySet();
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
		return tempObject instanceof Map ? ((Map<@NotNull String, @NotNull Object>) tempObject).keySet() : new HashSet<>();
	}

	@NotNull
	@Override
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? ((Map<@NotNull String, @NotNull Object>) tempObject).keySet() : new HashSet<>();
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
	 * Convert FileData to a JsonObject
	 *
	 * @return JsonObject from localMap
	 */
	@NotNull
	@Override
	public JSONObject toJsonObject() {
		throw new UnsupportedOperationException("This method is not supported by SortedFileData");
	}

	/**
	 * Convert FileData to a nested HashMap
	 */
	@NotNull
	@Override
	public Map<@NotNull String, @NotNull Object> toMap() {
		throw new UnsupportedOperationException("This method is not supported by SortedFileData");
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
	public Set<Entry<String, Object>> entrySet() {
		return null;
	}


	@SuppressWarnings("DuplicatedCode")
	@NotNull
	private Map<@NotNull String, @NotNull Object> removeKey(Map<@NotNull String, @NotNull Object> map, @NotNull String[] key, int id) {
		Map<@NotNull String, @NotNull Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				tempMap.remove(key[id]);
			} else {
				//noinspection unchecked
				map.put(key[id], this.removeKey((Map<@NotNull String, @NotNull Object>) map.get(key[id]), key, id + 1));
				//noinspection unchecked
				if (((Map<@NotNull String, @NotNull Object>) map.get(key[id])).isEmpty()) {
					map.remove(key[id]);
				}
				return map;
			}
		}
		return tempMap;
	}

	private boolean containsKey(@NotNull final Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (this.containsKey(map, key[id]) && this.get(map, key[id]) instanceof Map) {
				//noinspection unchecked
				Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> tempMap = (Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object>) this.get(map, key[id]);
				return this.containsKey(Objects.notNull(tempMap), key, id + 1);
			} else {
				return false;
			}
		} else {
			return this.containsKey(map, key[id]);
		}
	}

	@Nullable
	private Object get(@NotNull final Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (this.get(map, key[id]) instanceof Map) {
				//noinspection unchecked
				Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> tempMap = (Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object>) this.get(map, key[id]);
				return this.get(Objects.notNull(tempMap), key, id + 1);
			} else {
				return null;
			}
		} else {
			return this.get(map, key[id]);
		}
	}

	private Object insert(final Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map, @NotNull final String[] key, final Object value, final int id) {
		if (id < key.length) {
			Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
			//noinspection unchecked
			Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> childMap =
					this.containsKey(map, key[id])
					&& this.get(map, key[id]) instanceof Map
					? (Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object>) this.get(map, key[id])
					: map;
			tempMap.put(key[id], this.insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}

	@NotNull
	private Set<String> keySet(@NotNull final Map<@NotNull String, @NotNull Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<@NotNull String, @NotNull Object>) entry.getValue())) {
					tempSet.add(entry.getKey() + "." + tempKey);
				}
			} else {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String[]> keySetUseArray(@NotNull final Map<@NotNull String, @NotNull Object> map) {
		Set<String[]> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String[] tempKey : this.keySetUseArray((Map<@NotNull String, @NotNull Object>) entry.getValue())) {
					String[] key = new String[1 + tempKey.length];
					key[0] = entry.getKey();
					System.arraycopy(tempKey, 0, key, 1, tempKey.length);
					tempSet.add(key);
				}
			} else {
				tempSet.add(new String[]{
						entry.getKey()
				});
			}
		}
		return tempSet;
	}

	private int size(@NotNull final Map<?, ?> map) {
		int size = map.size();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				size += this.size((Map) entry.getValue());
			}
		}
		return size;
	}

	@NotNull
	private Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> parseMap(final @NotNull Map<@NotNull String, @NotNull Object> map) {
		final Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>() : new HashMap<>();
		int i = 0;
		for (Map.Entry<@NotNull String, @NotNull Object> entry : map.entrySet()) {
			tempMap.put(new Pair<>(i, entry.getKey()), entry.getValue());
			i++;
		}
		return tempMap;
	}

	@NotNull
	private Map<@NotNull String, @NotNull Object> parseMapBackWards(final @NotNull Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map) {
		final Map<@NotNull String, @NotNull Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>() : new HashMap<>();
		for (Map.Entry<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> entry : map.entrySet()) {
			tempMap.put(entry.getKey().getValue(), entry.getValue());
		}
		return tempMap;
	}

	private boolean containsKey(final @NotNull Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull Object> map, final @NotNull String key) {
		for (final Pair<@NotNull Integer, @NotNull String> entry : map.keySet()) {
			if (entry.getValue().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	private <V> V get(final @NotNull Map<@NotNull Pair<@NotNull Integer, @NotNull String>, @NotNull V> map, final @NotNull String key) {
		for (final Map.Entry<Pair<@NotNull Integer, @NotNull String>, @NotNull V> entry : map.entrySet()) {
			if (entry.getKey().getValue().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public int compareTo(final @NotNull SortedFileData sortedFileData) {
		return Integer.compare(this.localMap.size(), Objects.notNull(sortedFileData.localMap).size());
	}

	@Override
	public String toString() {
		return this.localMap.toString();
	}
}