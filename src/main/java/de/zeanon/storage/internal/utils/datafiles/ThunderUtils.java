package de.zeanon.storage.internal.utils.datafiles;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.data.FileData;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Adds the utility methods, used by ThunderConfig
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class ThunderUtils {

	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Header of the FileData
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		for (final String tempKey : fileData.blockKeySet()) {
			if (fileData.get(tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param header         the Header to be set
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map with the given Header
	 */
	@NotNull
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @Nullable List<String> header, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final Map<String, Object> tempMap = fileData.toMap();
		for (final Map.Entry<String, Object> entry : tempMap.entrySet()) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				tempMap.remove(entry.getKey());
			} else {
				break;
			}
		}
		final Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, null);
		if (header != null) {
			int commentLine = 0;
			for (String comment : header) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, ThunderEditor.LineType.COMMENT);
				commentLine++;
			}
		}
		finalMap.putAll(tempMap);
		return finalMap;
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param key            the Key of the SubBlock the Header shall be set to
	 * @param header         the Header to be set
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map with the given Header
	 * @throws ObjectNullException if the given FileData does not contain the given key
	 */
	@NotNull
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> header, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			final Map<String, Object> tempMap = (Map<String, Object>) Objects.notNull(fileData.get(key), "ThunderFile does not contain '" + key + "'");
			for (final Map.Entry<String, Object> entry : tempMap.entrySet()) {
				if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
					tempMap.remove(entry.getKey());
				} else {
					break;
				}
			}
			final Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, null);
			if (header != null) {
				int commentLine = 0;
				for (String comment : header) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, ThunderEditor.LineType.COMMENT);
					commentLine++;
				}
			}
			finalMap.putAll(tempMap);
			fileData.insert(key, finalMap);
		}
		return fileData.toMap();
	}

	/**
	 * Get the Footer from a give FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Footer of the FileData
	 */
	@NotNull
	public static List<String> getFooter(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		final List<String> keyList = new ArrayList<>(fileData.blockKeySet());
		Collections.reverse(keyList);
		for (final String tempKey : keyList) {
			if (fileData.get(tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param footer         the Footer to be set
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map with the given Footer
	 */
	@NotNull
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @Nullable List<String> footer, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final Map<String, Object> tempMap = fileData.toMap();
		final List<String> keyList = new ArrayList<>(tempMap.keySet());
		Collections.reverse(keyList);
		for (final String tempKey : keyList) {
			if (tempMap.get(tempKey) == ThunderEditor.LineType.COMMENT) {
				tempMap.remove(tempKey);
			} else {
				break;
			}
		}
		final Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, tempMap);
		if (footer != null) {
			int commentLine = 0;
			for (String comment : footer) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, ThunderEditor.LineType.COMMENT);
				commentLine++;
			}
		}
		return finalMap;
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param key            the Key of the SubBlock the Footer shall be set to
	 * @param footer         the Header to be set
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map with the given Footer
	 * @throws ObjectNullException if the given FileData does not contain the given key
	 */
	@NotNull
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> footer, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			final Map<String, Object> tempMap = (Map<String, Object>) Objects.notNull(fileData.get(key), "ThunderFile does not contain '" + key + "'");
			final List<String> keyList = new ArrayList<>(tempMap.keySet());
			Collections.reverse(keyList);
			for (final String tempKey : keyList) {
				if (tempMap.get(tempKey) == ThunderEditor.LineType.COMMENT) {
					tempMap.remove(tempKey);
				} else {
					break;
				}
			}
			final Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, tempMap);
			if (footer != null) {
				int commentLine = 0;
				for (String comment : footer) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, ThunderEditor.LineType.COMMENT);
					commentLine++;
				}
			}
			finalMap.putAll(tempMap);
			fileData.insert(key, finalMap);
		}
		return fileData.toMap();
	}

	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param key            the Key of the SubBlock the Header shall be getted from
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Header of the SubBlock
	 * @throws ObjectNullException if the given FileData does not contain the given key
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		for (final String tempKey : Objects.notNull(fileData.blockKeySet(key), "ThunderFile does not contain '" + key + "'")) {
			if (fileData.get(key + "." + tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Get the Footer from a give FileData
	 *
	 * @param fileData       the FileData to be used
	 * @param key            the key of the SubBlock the Footer shall be getted from
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Footer of the SubBlock
	 * @throws ObjectNullException if the given FileData does not contain the given key
	 */
	@NotNull
	public static List<String> getFooter(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		final List<String> keyList = new ArrayList<>(Objects.notNull(fileData.blockKeySet(key), "ThunderFile does not contain '" + key + "'"));
		Collections.reverse(keyList);
		for (final String tempKey : keyList) {
			if (fileData.get(key + "." + tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	/**
	 * Get the Comments from a given FileData compatible with ThunderFile
	 *
	 * @param fileData       the FileData to be used
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @param deep           defining, if it should get all comments or only the ones in the top Layer
	 * @return a List containing the Comments of the FileData
	 */
	@NotNull
	public static List<String> getComments(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting, final boolean deep) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		final List<String> keyList = deep ? new ArrayList<>(fileData.keySet()) : new ArrayList<>(fileData.blockKeySet());
		for (final String tempKey : keyList) {
			if (fileData.get(tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			}
		}
		return returnList;
	}

	/**
	 * Get the Comments from a given FileData compatible with ThunderFile
	 *
	 * @param fileData       the FileData to be used
	 * @param key            the key of the SubBlock the Footer shall be getted from
	 * @param dataType       the FileDataType to be used with the given FileData
	 * @param commentSetting the CommentSetting to be used
	 * @param deep           defining, if it should get all comments or only the ones in the given SubBlock
	 * @return a List containing the Comments of the SubBlock
	 * @throws ObjectNullException if the given FileData does not contain the given key
	 */
	@NotNull
	public static List<String> getComments(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting, final boolean deep) {
		final List<String> returnList = dataType.getNewDataList(commentSetting, null);
		final List<String> keyList = deep ? new ArrayList<>(Objects.notNull(fileData.keySet(key), "ThunderFile does not contain '" + key + "'")) : new ArrayList<>(Objects.notNull(fileData.blockKeySet(key), "ThunderFile does not contain '" + key + "'"));
		for (final String tempKey : keyList) {
			if (fileData.get(key + "." + tempKey) == ThunderEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			}
		}
		return returnList;
	}
}