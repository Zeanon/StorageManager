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

	public static JSONObject getJsonFromMap(final @NotNull Map<String, Object> map) {
		JSONObject jsonData = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				value = getJsonFromMap((Map<String, Object>) value);
			}
			jsonData.put(entry.getKey(), value);
		}
		return jsonData;
	}

	public static Map<String, Object> jsonToMap(final @NotNull JSONObject json) {
		Map<String, Object> retMap = new HashMap<>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static List<Object> toList(final @NotNull JSONArray array) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getValue(array.get(i)));
		}
		return list;
	}

	public static Map<String, Object> toMap(final @NotNull JSONObject object) {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keys();
		keysItr.forEachRemaining(key -> map.put(key, getValue(object.get(key))));
		return map;
	}

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