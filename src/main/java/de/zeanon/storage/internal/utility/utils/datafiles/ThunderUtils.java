package de.zeanon.storage.internal.utility.utils.datafiles;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.BigTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.GapTripletMap;
import de.zeanon.storage.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import de.zeanon.storage.internal.utility.utils.editor.ThunderEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Different Utility methods for Thunder-Type Files
 *
 * @author Zeanon
 * @version 1.3.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
public class ThunderUtils {


	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Header of the FileData
	 */
	public static @NotNull List<String> getHeader(final @NotNull ThunderFileData fileData) {
		final @NotNull List<String> returnList = new ArrayList<>();
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : fileData.blockEntryList()) {
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
	public static void setHeader(final @NotNull ThunderFileData fileData, final @Nullable String[] header, final boolean fastMap) {
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryList();
		final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
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
	public static void setHeader(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable String[] header, final boolean fastMap) {
		if (fileData.get(key) instanceof TripletMap) {
			final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryList(key);
			final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
			internalSetHeader(header, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	public static void setHeaderUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final @Nullable String[] header, final boolean fastMap) {
		if (fileData.getUseArray(key) instanceof TripletMap) {
			final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryListUseArray(key);
			final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
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
	public static @NotNull List<String> getFooter(final @NotNull ThunderFileData fileData) {
		final @NotNull List<String> returnList = new ArrayList<>();
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryList();
		return internalGetFooter(returnList, entryList);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param footer   the Footer to be set
	 */
	public static void setFooter(final @NotNull ThunderFileData fileData, final @Nullable String[] footer, final boolean fastMap) {
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryList();
		final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
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
	public static void setFooter(final @NotNull ThunderFileData fileData, final @NotNull String key, final @Nullable String[] footer, final boolean fastMap) {
		if (fileData.get(key) instanceof TripletMap) {
			final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryList(key);
			final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
			internalSetFooter(footer, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
	}

	public static void setFooterUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final @Nullable String[] footer, final boolean fastMap) {
		if (fileData.getUseArray(key) instanceof TripletMap) {
			final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = fileData.blockEntryListUseArray(key);
			final @NotNull TripletMap<String, Object> returnMap = fastMap ? new BigTripletMap<>() : new GapTripletMap<>();
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
	public static @NotNull List<String> getHeader(final @NotNull ThunderFileData fileData, final @NotNull String key) {
		final @NotNull List<String> returnList = new ArrayList<>();
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'")) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				returnList.add(entry.getKey());
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	public static @NotNull List<String> getHeaderUseArray(final @NotNull ThunderFileData fileData, final @NotNull String... key) {
		final @NotNull List<String> returnList = new ArrayList<>();
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'")) {
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
	public static @NotNull List<String> getFooter(final @NotNull ThunderFileData fileData, final @NotNull String key) {
		final @NotNull List<String> returnList = new ArrayList<>();
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'");
		return internalGetFooter(returnList, entryList);
	}

	public static @NotNull List<String> getFooterUseArray(final @NotNull ThunderFileData fileData, final @NotNull String... key) {
		final @NotNull List<String> returnList = new ArrayList<>();
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'");
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
	public static @NotNull List<String> getComments(final @NotNull ThunderFileData fileData, final boolean deep) {
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = deep ? fileData.entryList() : fileData.blockEntryList();
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
	public static @NotNull List<String> getComments(final @NotNull ThunderFileData fileData, final @NotNull String key, final boolean deep) {
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = deep ? Objects.notNull(fileData.entryList(key), "ThunderFile does not contain '" + key + "'") : Objects.notNull(fileData.blockEntryList(key), "ThunderFile does not contain '" + key + "'");
		return internalGetComments(entryList);
	}

	public static @NotNull List<String> getCommentsUseArray(final @NotNull ThunderFileData fileData, final @NotNull String[] key, final boolean deep) {
		final @NotNull List<TripletMap.TripletNode<String, Object>> entryList = deep ? Objects.notNull(fileData.entryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'") : Objects.notNull(fileData.blockEntryListUseArray(key), "ThunderFile does not contain '" + Arrays.toString(key) + "'");
		return internalGetComments(entryList);
	}


	// <Internal>
	@Contract("_, _ -> param1")
	private static @NotNull List<String> internalGetFooter(@NotNull List<String> returnList, @NotNull List<TripletMap.TripletNode<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : entryList) {
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

	@Synchronized
	private static void internalSetHeader(@Nullable String[] header, @NotNull List<TripletMap.TripletNode<String, Object>> entryList, @NotNull TripletMap<String, Object> returnMap) {
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (@Nullable String comment : header) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.HEADER);
				}
			}
		}
		returnMap.addAll(entryList);
	}

	@Synchronized
	private static void internalSetFooter(@Nullable String[] footer, @NotNull List<TripletMap.TripletNode<String, Object>> entryList, @NotNull TripletMap<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		returnMap.addAll(entryList);
		if (footer != null) {
			for (@Nullable String comment : footer) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "#" + comment, ThunderEditor.LineType.FOOTER);
				}
			}
		}
	}

	private static @NotNull List<String> internalGetComments(final @NotNull List<TripletMap.TripletNode<String, Object>> entryList) {
		final @NotNull List<String> returnList = new ArrayList<>();
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				returnList.add(entry.getKey());
			}
		}
		return returnList;
	}
	// </Internal>
}