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

	@NotNull
	private Map<@NotNull String, Object> localMap = new HashMap<>();


	/**
	 * Reload the contents of FileData.
	 *
	 * @param map the Contents to be inserted.
	 */
	public void loadData(final @Nullable Map<@NotNull String, Object> map) {
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
			this.localMap.put(key[0],
							  this.localMap.containsKey(key[0])
							  && this.localMap.get(key[0]) instanceof Map
							  ? this.insert((Map) this.localMap.get(key[0]), key, value, 1)
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
		return this.containsKey(this.localMap, parts, 0);
	}

	public boolean containsKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.containsKey(this.localMap, key, 0);
	}

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	@NotNull
	public Set<String> keySet() {
		return this.keySet(this.localMap);
	}

	@NotNull
	public Set<String[]> keySetUseArray() {
		return this.keySetUseArray(this.localMap);
	}

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the Block.
	 * @return the keySet of all sublayers of the given key or null if the key does not exist (Format: key.subkey).
	 */
	@NotNull
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySet((Map<@NotNull String, Object>) tempObject) : new HashSet<>();
	}

	@NotNull
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? this.keySetUseArray((Map<@NotNull String, Object>) tempObject) : new HashSet<>();
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
		return this.get(this.localMap, parts, 0);
	}

	@Nullable
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		return this.get(this.localMap, key, 0);
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	@NotNull
	public Set<String> blockKeySet() {
		return this.localMap.keySet();
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or null if the key does not exist.
	 */
	@NotNull
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		//noinspection unchecked
		return tempObject instanceof Map ? ((Map<@NotNull String, Object>) tempObject).keySet() : new HashSet<>();
	}

	@NotNull
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		//noinspection unchecked
		return tempObject instanceof Map ? ((Map<@NotNull String, Object>) tempObject).keySet() : new HashSet<>();
	}

	/**
	 * Get the size of a single layer of the map.
	 *
	 * @return the size of the top layer of localMap.
	 */
	public int blockSize() {
		return this.localMap.size();
	}

	/**
	 * get the size of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the size of the given layer or 0 if the key does not exist.
	 */
	public int blockSize(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.get(key);
		return tempObject instanceof Map ? ((Map) tempObject).size() : 0;
	}

	public int blockSizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		return tempObject instanceof Map ? ((Map) tempObject).size() : 0;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public int size() {
		return this.size(this.localMap);
	}

	/**
	 * Convert FileData to a JsonObject.
	 *
	 * @return JsonObject from localMap.
	 */
	@NotNull
	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(this.localMap);
	}

	/**
	 * Convert FileData to a nested HashMap.
	 *
	 * @return localMap.
	 */
	@NotNull
	public Map<@NotNull String, Object> toMap() {
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
		Object tempObject = this.get(key);
		return tempObject instanceof Map ? this.size((Map) tempObject) : 0;
	}

	public int sizeUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		Object tempObject = this.getUseArray(key);
		return tempObject instanceof Map ? this.size((Map) tempObject) : 0;
	}

	/**
	 * Clear the contents of this FileData.
	 */
	public void clear() {
		this.localMap = new HashMap<>();
	}


	@Nullable
	private Map<@NotNull String, Object> removeKey(Map<@NotNull String, Object> map, @NotNull String[] key, int id) {
		Map<@NotNull String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				tempMap.remove(key[id]);
				return tempMap;
			} else {
				//noinspection unchecked
				map.put(key[id], this.removeKey((Map<@NotNull String, Object>) map.get(key[id]), key, id + 1));
				//noinspection unchecked
				if (((Map<@NotNull String, Object>) map.get(key[id])).isEmpty()) {
					map.remove(key[id]);
				}
				return map;
			}
		} else {
			return null;
		}
	}

	private boolean containsKey(@NotNull final Map<@NotNull String, Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<@NotNull String, Object> tempMap = (Map<@NotNull String, Object>) map.get(key[id]);
				return this.containsKey(tempMap, key, id + 1);
			} else {
				return false;
			}
		} else {
			return map.containsKey(key[id]);
		}
	}

	@Nullable
	private Object get(@NotNull final Map<@NotNull String, Object> map, @NotNull final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<@NotNull String, Object> tempMap = (Map<@NotNull String, Object>) map.get(key[id]);
				return this.get(tempMap, key, id + 1);
			} else {
				return null;
			}
		} else {
			return map.get(key[id]);
		}
	}

	private Object insert(final Map<@NotNull String, Object> map, @NotNull final String[] key, final Object value, final int id) {
		if (id < key.length) {
			Map<@NotNull String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
			//noinspection unchecked
			Map<@NotNull String, Object> childMap =
					map.containsKey(key[id])
					&& map.get(key[id]) instanceof Map
					? (Map<@NotNull String, Object>) map.get(key[id])
					: this.getMap(map);
			tempMap.put(key[id], this.insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}

	@NotNull
	private Set<String> keySet(@NotNull final Map<@NotNull String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<@NotNull String, Object>) entry.getValue())) {
					tempSet.add(entry.getKey() + "." + tempKey);
				}
			} else {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String[]> keySetUseArray(@NotNull final Map<@NotNull String, Object> map) {
		Set<String[]> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String[] tempKey : this.keySetUseArray((Map<@NotNull String, Object>) entry.getValue())) {
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
	private Map<@NotNull String, Object> getMap(final @NotNull Map<@NotNull String, Object> map) {
		return map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
	}

	@Override
	public int compareTo(final @NotNull FileData fileData) {
		return Integer.compare(this.localMap.size(), Objects.notNull(fileData.localMap).size());
	}

	@Override
	public String toString() {
		return this.localMap.toString();
	}
}