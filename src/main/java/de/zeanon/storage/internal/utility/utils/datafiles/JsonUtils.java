package de.zeanon.storage.internal.utility.utils.datafiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Different Utility methods for Json-Type Files
 *
 * @author Zeanon
 * @version 1.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings({"unchecked", "unused"})
public class JsonUtils {

	public static @NotNull JSONObject getJsonFromMap(final @NotNull Map<String, Object> map) {
		final @NotNull JSONObject jsonData = new JSONObject();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				value = getJsonFromMap((Map<String, Object>) value);
			}
			jsonData.put(entry.getKey(), value);
		}
		return jsonData;
	}

	public static @NotNull Map<String, Object> jsonToMap(final @NotNull JSONObject json) {
		final @NotNull Map<String, Object> retMap = new HashMap<>();
		if (json != JSONObject.NULL) {
			retMap.putAll(json.toMap());
		}
		return retMap;
	}

	public static @NotNull List<Object> toList(final @NotNull JSONArray array) {
		final @NotNull List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getValue(array.get(i)));
		}
		return list;
	}

	private static @NotNull Object getValue(final @NotNull Object obj) {
		if (obj instanceof JSONArray) {
			return toList((JSONArray) obj);
		} else if (obj instanceof JSONObject) {
			return ((JSONObject) obj).toMap();
		} else {
			return obj;
		}
	}
}