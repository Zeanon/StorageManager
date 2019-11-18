package de.zeanon.storage.internal.utils.datafiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class JsonUtils {

	@NotNull
	public static JSONObject getJsonFromMap(final @NotNull Map<String, Object> map) {
		@NotNull final JSONObject jsonData = new JSONObject();
		for (@NotNull final Map.Entry<String, Object> entry : map.entrySet()) {
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
		@NotNull final Map<String, Object> retMap = new HashMap<>();
		if (json != JSONObject.NULL) {
			retMap.putAll(json.toMap());
		}
		return retMap;
	}

	@NotNull
	public static List<Object> toList(final @NotNull JSONArray array) {
		@NotNull final List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getValue(array.get(i)));
		}
		return list;
	}

	@NotNull
	private static Object getValue(final @NotNull Object obj) {
		if (obj instanceof JSONArray) {
			return toList((JSONArray) obj);
		} else if (obj instanceof JSONObject) {
			return ((JSONObject) obj).toMap();
		} else {
			return obj;
		}
	}
}