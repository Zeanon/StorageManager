package de.zeanon.storage.internal.data;

import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.JsonUtils;
import java.util.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


/**
 * Class to handle the Nested HashMaps used to cache the Data read from the Files
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public class FileData implements Comparable<FileData> {

	@Nullable
	private Map<String, Object> localMap;


	/**
	 * Reload the contents of FileData.
	 *
	 * @param map the Contents to be inserted.
	 */
	public void loadData(final @Nullable Map<String, Object> map) {
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

	public void loadData(final @Nullable JSONObject jsonObject) {
		if (jsonObject != null) {
			if (jsonObject.toMap() instanceof LinkedHashMap) {
				this.localMap = new LinkedHashMap<>(jsonObject.toMap());
			} else {
				this.localMap = new HashMap<>(jsonObject.toMap());
			}
		} else {
			this.localMap = new HashMap<>();
		}
	}


	/**
	 * Method to assign a value to a key.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to the key.
	 */
	public void insert(final @NotNull String key, final @Nullable Object value) {
		final String[] parts = key.split("\\.");
		this.insertUseArray(parts, value);
	}

	public void insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		if (value == null) {
			this.removeUseArray(key);
		} else {
			//noinspection unchecked
			Objects.notNull(this.localMap).put(key[0],
											   this.localMap.containsKey(key[0])
											   && this.localMap.get(key[0]) instanceof Map
											   ? this.insert((Map<String, Object>) this.localMap.get(key[0]), key, value, 1)
											   : this.insert(new HashMap<>(), key, value, 1));
		}
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists.
	 *
	 * @param key the key to be removed from the map.
	 */
	public void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKey(key)) {
			final String[] parts = key.split("\\.");
			this.removeKey(this.localMap, parts, 0);
		}
	}

	public void removeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		if (this.containsKeyUseArray(key)) {
			this.removeKey(this.localMap, key, 0);
		}
	}

	/**
	 * Check whether the map contains a certain key.
	 *
	 * @param key the key to be looked for.
	 * @return true if the key exists, otherwise false.
	 */
	public boolean containsKey(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		String[] parts = key.split("\\.");
		return this.containsKey(Objects.notNull(this.localMap), parts, 0);
	}

	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.containsKey(Objects.notNull(this.localMap), key, 0);
	}

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	@NotNull
	public Set<String> keySet() {
		return this.keySet(Objects.notNull(this.localMap));
	}

	@NotNull
	public Set<String[]> keySetUseArray() {
		return this.keySetUseArray(Objects.notNull(this.localMap));
	}

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the Block.
	 * @return the keySet of all sublayers of the given key or null if the key does not exist (Format: key.subkey).
	 */
	@Nullable
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.get(key) instanceof Map ? this.keySet((Map<String, Object>) Objects.notNull(this.get(key))) : null;
	}

	@Nullable
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.getUseArray(key) instanceof Map ? this.keySetUseArray((Map<String, Object>) Objects.notNull(this.getUseArray(key))) : null;
	}

	/**
	 * Method to get the object assign to a key from a FileData Object.
	 *
	 * @param key the key to look for.
	 * @return the value assigned to the given key or null if the key does not exist.
	 */
	@Nullable
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		final String[] parts = key.split("\\.");
		return this.get(Objects.notNull(this.localMap), parts, 0);
	}

	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(Objects.notNull(this.localMap), key, 0);
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	@NotNull
	public Set<String> blockKeySet() {
		return Objects.notNull(this.localMap).keySet();
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or null if the key does not exist.
	 */
	@Nullable
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.get(key) instanceof Map ? ((Map<String, Object>) Objects.notNull(this.get(key))).keySet() : null;
	}

	@Nullable
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.getUseArray(key) instanceof Map ? ((Map<String, Object>) Objects.notNull(this.getUseArray(key))).keySet() : null;
	}

	/**
	 * Get the size of a single layer of the map.
	 *
	 * @return the size of the top layer of localMap.
	 */
	public int blockSize() {
		return Objects.notNull(this.localMap).size();
	}

	/**
	 * get the size of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the size of the given layer or 0 if the key does not exist.
	 */
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(key) instanceof Map ? ((Map) Objects.notNull(this.get(key))).size() : 0;
	}

	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.getUseArray(key) instanceof Map ? ((Map) Objects.notNull(this.getUseArray(key))).size() : 0;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public int size() {
		return this.size(Objects.notNull(localMap));
	}

	/**
	 * Convert FileData to a JsonObject.
	 *
	 * @return JsonObject from localMap.
	 */
	@NotNull
	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(Objects.notNull(this.localMap));
	}

	/**
	 * Convert FileData to a nested HashMap.
	 *
	 * @return localMap.
	 */
	@Nullable
	public Map<String, Object> toMap() {
		return this.localMap;
	}

	/**
	 * get the size of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist.
	 */
	public int size(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.containsKey(key) ? this.size((Map<String, Object>) Objects.notNull(this.get(key))) : 0;
	}

	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		//noinspection unchecked
		return this.containsKeyUseArray(key) ? this.size((Map<String, Object>) Objects.notNull(this.getUseArray(key))) : 0;
	}

	/**
	 * Clear the contents of this FileData.
	 */
	public void clear() {
		this.localMap = new HashMap<>();
	}


	@Nullable
	private Map<String, Object> removeKey(Map<String, Object> map, @NotNull String[] key, int id) {
		Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				tempMap.remove(key[id]);
				return tempMap;
			} else {
				//noinspection unchecked
				map.put(key[id], this.removeKey((Map<String, Object>) map.get(key[id]), key, id + 1));
				//noinspection unchecked
				if (((Map<String, Object>) map.get(key[id])).isEmpty()) {
					map.remove(key[id]);
				}
				return map;
			}
		} else {
			return null;
		}
	}

	private boolean containsKey(@NotNull final Map<String, Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
				return this.containsKey(tempMap, key, id + 1);
			} else {
				return false;
			}
		} else {
			return map.containsKey(key[id]);
		}
	}

	@Nullable
	private Object get(@NotNull final Map<String, Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
				return this.get(tempMap, key, id + 1);
			} else {
				return null;
			}
		} else {
			return map.get(key[id]);
		}
	}

	private Object insert(final Map<String, Object> map, @NotNull final String[] key, final Object value, final int id) {
		if (id < key.length) {
			Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
			//noinspection unchecked
			Map<String, Object> childMap =
					map.containsKey(key[id])
					&& map.get(key[id]) instanceof Map
					? (Map<String, Object>) map.get(key[id])
					: this.getMap(map);
			tempMap.put(key[id], this.insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}

	@NotNull
	private Set<String> keySet(@NotNull final Map<String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<String, Object>) entry.getValue())) {
					tempSet.add(entry.getKey() + "." + tempKey);
				}
			} else {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String[]> keySetUseArray(@NotNull final Map<String, Object> map) {
		Set<String[]> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String[] tempKey : this.keySetUseArray((Map<String, Object>) entry.getValue())) {
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

	private int size(@NotNull final Map<String, Object> map) {
		int size = map.size();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				size += this.size((Map<String, Object>) entry.getValue());
			}
		}
		return size;
	}

	@NotNull
	private Map<String, Object> getMap(final @NotNull Map<String, Object> map) {
		return map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
	}

	@Override
	public int compareTo(final @NotNull FileData fileData) {
		return Integer.compare(Objects.notNull(this.localMap).size(), Objects.notNull(fileData.localMap).size());
	}

	@Override
	public String toString() {
		return Objects.notNull(this.localMap).toString();
	}
}