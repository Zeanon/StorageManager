package de.zeanon.storage.internal.utility.utils.datafiles;

import de.zeanon.storage.internal.basic.exceptions.ObjectNullException;
import de.zeanon.storage.internal.data.cache.ThunderFileData;
import de.zeanon.storage.internal.data.cache.datamap.TripletMap;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import de.zeanon.storage.internal.utility.utils.editor.ThunderEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Different Utility methods for Thunder-Type Files
 *
 * @author Zeanon
 * @version 1.3.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThunderUtils {


	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Header of the FileData
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull ThunderFileData fileData) {
		@NotNull final List<String> returnList = new ArrayList<>();
		for (@NotNull final TripletMap.Entry<String, Object> entry : fileData.blockEntryList()) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
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
	 * @param fileData the FileDataBase to be used
	 * @param header   the Header to be set
	 */
	public static void setHeader(final @NotNull ThunderFileData fileData, final @Nullable String[] header) {
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
		internalSetHeader(header, entryList, returnMap);
		fileData.loadData(returnMap);
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param key      the Key of the SubBlock the Header shall be set to
	 * @param header   the Header to be set
	 *
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	public static void setHeader(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable String[] header) {
		if (fileData.get(key) instanceof TripletMap) {
			@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryList(key);
			@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
			internalSetHeader(header, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	public static void setHeaderUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final @Nullable String[] header) {
		if (fileData.getUseArray(key) instanceof TripletMap) {
			@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryListUseArray(key);
			@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
			internalSetHeader(header, entryList, returnMap);
			fileData.insertUseArray(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + Arrays.toString(key) + "'");
		}
	}

	/**
	 * Get the Footer from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Footer of the FileData
	 */
	@NotNull
	public static List<String> getFooter(final @NotNull ThunderFileData fileData) {
		@NotNull final List<String> returnList = new ArrayList<>();
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		return internalGetFooter(returnList, entryList);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param footer   the Footer to be set
	 */
	public static void setFooter(final @NotNull ThunderFileData fileData, final @Nullable String[] footer) {
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryList();
		@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
		internalSetFooter(footer, entryList, returnMap);
		fileData.loadData(returnMap);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param key      the Key of the SubBlock the Footer shall be set to
	 * @param footer   the Header to be set
	 *
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	public static void setFooter(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable String[] footer) {
		if (fileData.get(key) instanceof TripletMap) {
			@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryList(key);
			@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
			internalSetFooter(footer, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	public static void setFooterUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final @Nullable String[] footer) {
		if (fileData.getUseArray(key) instanceof TripletMap) {
			@NotNull final List<TripletMap.Entry<String, Object>> entryList = fileData.blockEntryListUseArray(key);
			@NotNull final TripletMap<String, Object> returnMap = new TripletMap<>();
			internalSetFooter(footer, entryList, returnMap);
			fileData.insertUseArray(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + Arrays.toString(key) + "'");
		}
	}

	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param key      the Key of the SubBlock the Header shall be getted from
	 *
	 * @return a List containing the Header of the SubBlock
	 *
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@NotNull
	public static List<String> getHeader(final @NotNull ThunderFileData fileData, final @NotNull String key) {
		@NotNull final List<String> returnList = new ArrayList<>();
		for (@NotNull final TripletMap.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'")) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				returnList.add(entry.getKey());
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	@NotNull
	public static List<String> getHeaderUseArray(final @NotNull ThunderFileData fileData, final @NotNull String... key) {
		@NotNull final List<String> returnList = new ArrayList<>();
		for (@NotNull final TripletMap.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'")) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
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
	 * @param fileData the FileDataBase to be used
	 * @param key      the key of the SubBlock the Footer shall be getted from
	 *
	 * @return a List containing the Footer of the SubBlock
	 *
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@NotNull
	public static List<String> getFooter(final @NotNull ThunderFileData fileData, final @NotNull String key) {
		@NotNull final List<String> returnList = new ArrayList<>();
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'");
		return internalGetFooter(returnList, entryList);
	}

	@NotNull
	public static List<String> getFooterUseArray(final @NotNull ThunderFileData fileData, final @NotNull String... key) {
		@NotNull final List<String> returnList = new ArrayList<>();
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'");
		return internalGetFooter(returnList, entryList);
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData the FileDataBase to be used
	 * @param deep     defining, if it should get all comments or only the ones in the top Layer
	 *
	 * @return a List containing the Comments of the FileData
	 */
	@NotNull
	public static List<String> getComments(final @NotNull ThunderFileData fileData, final boolean deep) {
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = deep ? fileData.entryList() : fileData.blockEntryList();
		return internalGetComments(entryList);
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData the FileDataBase to be used
	 * @param key      the key of the SubBlock the Footer shall be getted from
	 * @param deep     defining, if it should get all comments or only the ones in the given SubBlock
	 *
	 * @return a List containing the Comments of the SubBlock
	 *
	 * @throws ObjectNullException if the given FileDataBase does not contain the given key
	 */
	@NotNull
	public static List<String> getComments(final @NotNull ThunderFileData fileData, final @NotNull String key, final boolean deep) {
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = deep ? Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'") : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'");
		return internalGetComments(entryList);
	}

	@NotNull
	public static List<String> getCommentsUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final boolean deep) {
		@NotNull final List<TripletMap.Entry<String, Object>> entryList = deep ? Objects.notNull(fileData.entryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'") : Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'");
		return internalGetComments(entryList);
	}


	@NotNull
	@Contract("_, _ -> param1")
	private static List<String> internalGetFooter(@NotNull List<String> returnList, @NotNull List<TripletMap.Entry<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (@NotNull final TripletMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				returnList.add(entry.getKey());
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	private static void internalSetHeader(@Nullable String[] header, @NotNull List<TripletMap.Entry<String, Object>> entryList, @NotNull TripletMap<String, Object> returnMap) {
		for (@NotNull final TripletMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (String comment : header) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.HEADER);
				}
			}
		}
		returnMap.addAll(entryList);
	}

	private static void internalSetFooter(@Nullable String[] footer, @NotNull List<TripletMap.Entry<String, Object>> entryList, @NotNull TripletMap<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (@NotNull final TripletMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		returnMap.addAll(entryList);
		if (footer != null) {
			for (String comment : footer) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.FOOTER);
				}
			}
		}
	}

	@NotNull
	private static List<String> internalGetComments(@NotNull final List<TripletMap.Entry<String, Object>> entryList) {
		@NotNull final List<String> returnList = new ArrayList<>();
		for (@NotNull final TripletMap.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				returnList.add(entry.getKey());
			}
		}
		return returnList;
	}
}