package de.zeanon.storage.internal.utils.datafiles;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.maps.DataMap;
import de.zeanon.storage.internal.data.cache.ThunderFileData;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	 * @param fileData       the FileDataBase to be used
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Header of the FileData
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull ThunderFileData fileData, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<String> returnList = new LinkedList<>();
		for (@NotNull final DataMap.Entry<String, Object> entry : fileData.blockEntryList()) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param header         the Header to be set
	 * @param commentSetting the CommentSetting to be used
	 */
	@SuppressWarnings("DuplicatedCode")
	public static void setHeader(final @NotNull ThunderFileData fileData, final @Nullable List<String> header, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<DataMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		@NotNull final DataMap<String, Object> returnList = new DataMap<>();
		if (header != null) {
			int commentLine = 0;
			for (@NotNull String comment : header) {
				returnList.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.COMMENT, commentLine);
				commentLine++;
			}
		}
		returnList.addAll(entryList);
		fileData.loadData(returnList);
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param key            the Key of the SubBlock the Header shall be set to
	 * @param header         the Header to be set
	 * @param commentSetting the CommentSetting to be used
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@SuppressWarnings("DuplicatedCode")
	public static void setHeader(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable List<String> header, final @NotNull CommentSettingBase commentSetting) {
		if (fileData.get(key) instanceof DataMap) {
			@NotNull final List<DataMap.Entry<String, Object>> entryList = fileData.blockEntryList(key);
			for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
				if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
					entryList.remove(entry);
				} else {
					break;
				}
			}
			@NotNull final DataMap<String, Object> returnMap = new DataMap<>();
			if (header != null) {
				int commentLine = 0;
				for (@NotNull String comment : header) {
					returnMap.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.COMMENT, commentLine);
					commentLine++;
				}
			}
			returnMap.addAll(entryList);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	/**
	 * Get the Footer from a give FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Footer of the FileData
	 */
	@NotNull
	public static List<String> getFooter(final @NotNull ThunderFileData fileData, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<String> returnList = new LinkedList<>();
		@NotNull final List<DataMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		Collections.reverse(entryList);
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
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
	 * @param fileData       the FileDataBase to be used
	 * @param footer         the Footer to be set
	 * @param commentSetting the CommentSetting to be used
	 */
	@SuppressWarnings("DuplicatedCode")
	public static void setFooter(final @NotNull ThunderFileData fileData, final @Nullable List<String> footer, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<DataMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		Collections.reverse(entryList);
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		@NotNull final DataMap<String, Object> returnList = new DataMap<>();
		if (footer != null) {
			int commentLine = 0;
			for (@NotNull String comment : footer) {
				returnList.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.COMMENT, commentLine);
				commentLine++;
			}
		}
		fileData.loadData(returnList);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param key            the Key of the SubBlock the Footer shall be set to
	 * @param footer         the Header to be set
	 * @param commentSetting the CommentSetting to be used
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@SuppressWarnings("DuplicatedCode")
	public static void setFooter(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable List<String> footer, final @NotNull CommentSettingBase commentSetting) {
		if (fileData.get(key) instanceof DataMap) {
			@NotNull final List<DataMap.Entry<String, Object>> entryList = fileData.blockEntryList(key);
			Collections.reverse(entryList);
			for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
				if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
					entryList.remove(entry);
				} else {
					break;
				}
			}
			@NotNull final DataMap<String, Object> returnList = new DataMap<>();
			if (footer != null) {
				int commentLine = 0;
				for (@NotNull String comment : footer) {
					returnList.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.COMMENT, commentLine);
					commentLine++;
				}
			}
			fileData.insert(key, returnList);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param key            the Key of the SubBlock the Header shall be getted from
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Header of the SubBlock
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull ThunderFileData fileData, final @NotNull String key, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<String> returnList = new LinkedList<>();
		for (@NotNull final DataMap.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'")) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Get the Footer from a give FileData
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param key            the key of the SubBlock the Footer shall be getted from
	 * @param commentSetting the CommentSetting to be used
	 * @return a List containing the Footer of the SubBlock
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@SuppressWarnings("DuplicatedCode")
	@NotNull
	public static List<String> getFooter(final @NotNull ThunderFileData fileData, final @NotNull String key, final @NotNull CommentSettingBase commentSetting) {
		@NotNull final List<String> returnList = new LinkedList<>();
		@NotNull final List<DataMap.Entry<String, Object>> entryList = Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'");
		Collections.reverse(entryList);
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param commentSetting the CommentSetting to be used
	 * @param deep           defining, if it should get all comments or only the ones in the top Layer
	 * @return a List containing the Comments of the FileData
	 */
	@SuppressWarnings("DuplicatedCode")
	@NotNull
	public static List<String> getComments(final @NotNull ThunderFileData fileData, final @NotNull CommentSettingBase commentSetting, final boolean deep) {
		@NotNull final List<String> returnList = new LinkedList<>();
		@NotNull final List<DataMap.Entry<String, Object>> entryList = deep ? fileData.entryList() : fileData.blockEntryList();
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
			}
		}
		return returnList;
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData       the FileDataBase to be used
	 * @param key            the key of the SubBlock the Footer shall be getted from
	 * @param commentSetting the CommentSetting to be used
	 * @param deep           defining, if it should get all comments or only the ones in the given SubBlock
	 * @return a List containing the Comments of the SubBlock
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@SuppressWarnings("DuplicatedCode")
	@NotNull
	public static List<String> getComments(final @NotNull ThunderFileData fileData, final @NotNull String key, final @NotNull CommentSettingBase commentSetting, final boolean deep) {
		@NotNull final List<String> returnList = new LinkedList<>();
		@NotNull final List<DataMap.Entry<String, Object>> entryList = deep ? Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'") : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'");
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT) {
				returnList.add(entry.getKey());
			}
		}
		return returnList;
	}
}