package de.zeanon.storage.internal.utils.datafiles;

import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unchecked", "WeakerAccess"})
public class JsonUtils {

	@NotNull
	public static JSONObject getJsonFromMap(final @NotNull Map<String, Object> map) {
		final JSONObject jsonData = new JSONObject();
		for (final Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				value = getJsonFromMap((Map<String, Object>) value);
			}
			jsonData.put(entry.getKey(), value);
		}
		return jsonData;
	}

	@NotNull
	public static Map<String, Object> jsonToMap(final @NotNull JSONObject json) {
		final Map<String, Object> retMap = new HashMap<>();
		if (json != JSONObject.NULL) {
			retMap.putAll(toMap(json));
		}
		return retMap;
	}

	@NotNull
	public static List<Object> toList(final @NotNull JSONArray array) {
		final List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getValue(array.get(i)));
		}
		return list;
	}

	@NotNull
	public static Map<String, Object> toMap(final @NotNull JSONObject object) {
		final Map<String, Object> map = new HashMap<>();

		final Iterator<String> keysIterator = object.keys();
		keysIterator.forEachRemaining(key -> map.put(key, getValue(object.get(key))));
		return map;
	}

	@NotNull
	private static Object getValue(final @NotNull Object obj) {
		if (obj instanceof JSONArray) {
			return toList((JSONArray) obj);
		} else if (obj instanceof JSONObject) {
			return toMap((JSONObject) obj);
		} else {
			return obj;
		}
	}
}