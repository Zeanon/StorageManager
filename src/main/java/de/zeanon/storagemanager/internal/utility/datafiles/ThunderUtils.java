package de.zeanon.storagemanager.internal.utility.datafiles;

import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import de.zeanon.storagemanager.internal.utility.editor.ThunderEditor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Different Utility methods for Thunder-Type Files
 *
 * @author Zeanon
 * @version 1.3.0
 */
@UtilityClass
public class ThunderUtils {


	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Header of the FileData
	 */
	public static @NotNull List<String> getHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		for (final @NotNull DataMap.DataNode<String, Object> entry : fileData.blockEntryList()) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				result.add(entry.getKey());
			} else {
				return result;
			}
		}
		return result;
	}

	/**
	 * Set the Header of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param header   the Header to be set
	 */
	public static void setHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @Nullable String[] header) {
		final @NotNull List<DataMap.DataNode<String, Object>> entryList = fileData.blockEntryList();
		//noinspection unchecked
		final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
		ThunderUtils.internalSetHeader(header, entryList, returnMap);
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
	public static void setHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] header) {
		if (fileData.get(key) instanceof DataMap) {
			final @NotNull List<DataMap.DataNode<String, Object>> entryList = Objects.notNull(fileData.blockEntryList(key));
			//noinspection unchecked
			final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
			ThunderUtils.internalSetHeader(header, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
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
	public static void setHeaderUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] header) {
		if (fileData.getUseArray(key) instanceof DataMap) {
			final @NotNull List<DataMap.DataNode<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key));
			//noinspection unchecked
			final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
			ThunderUtils.internalSetHeader(header, entryList, returnMap);
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
	public static @NotNull List<String> getFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		final @NotNull List<DataMap.DataNode<String, Object>> entryList = fileData.blockEntryList();
		return ThunderUtils.internalGetFooter(result, entryList);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param footer   the Footer to be set
	 */
	public static void setFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @Nullable String[] footer) {
		final @NotNull List<DataMap.DataNode<String, Object>> entryList = fileData.blockEntryList();
		//noinspection unchecked
		final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
		ThunderUtils.internalSetFooter(footer, entryList, returnMap);
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
	public static void setFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] footer) {
		if (fileData.get(key) instanceof DataMap) {
			final @NotNull List<DataMap.DataNode<String, Object>> entryList = Objects.notNull(fileData.blockEntryList(key));
			//noinspection unchecked
			final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
			ThunderUtils.internalSetFooter(footer, entryList, returnMap);
			fileData.insert(key, returnMap);
		} else {
			throw new ObjectNullException("ThunderFile does not contain '" + key + "'");
		}
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
	public static void setFooterUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] footer) {
		if (fileData.getUseArray(key) instanceof DataMap) {
			final @NotNull List<DataMap.DataNode<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key));
			//noinspection unchecked
			final @NotNull DataMap<String, Object> returnMap = fileData.collectionsProvider().newMap();
			ThunderUtils.internalSetFooter(footer, entryList, returnMap);
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
	public static @Nullable List<String> getHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key) {
		if (fileData.get(key) instanceof DataMap) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull DataMap.DataNode<String, Object> entry : Objects.notNull(fileData.blockEntryList(key))) {
				if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
					result.add(entry.getKey());
				} else {
					return result;
				}
			}
			return result;
		} else {
			return null;
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
	public static @Nullable List<String> getHeaderUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String... key) {
		if (fileData.getUseArray(key) instanceof DataMap) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull DataMap.DataNode<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key))) {
				if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
					result.add(entry.getKey());
				} else {
					return result;
				}
			}
			return result;
		} else {
			return null;
		}
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
	public static @NotNull List<String> getFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		return ThunderUtils.internalGetFooter(result, Objects.notNull(fileData.entryList(key)));
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
	public static @NotNull List<String> getFooterUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String... key) {
		//noinspection unchecked
		return ThunderUtils.internalGetFooter(fileData.collectionsProvider().newList(), Objects.notNull(fileData.blockEntryListUseArray(key)));
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData the FileDataBase to be used
	 * @param deep     defining, if it should get all comments or only the ones in the top Layer
	 *
	 * @return a List containing the Comments of the FileData
	 */
	public static @NotNull List<String> getComments(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final boolean deep) {
		return ThunderUtils.internalGetComments(deep ? fileData.entryList() : fileData.blockEntryList(), fileData.collectionsProvider());
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
	public static @NotNull List<String> getComments(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final boolean deep) {
		return ThunderUtils.internalGetComments(Objects.notNull(deep ? fileData.entryList(key) : fileData.blockEntryList(key)), fileData.collectionsProvider());
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
	public static @NotNull List<String> getCommentsUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final boolean deep) {
		return ThunderUtils.internalGetComments(Objects.notNull(deep ? fileData.entryListUseArray(key) : fileData.blockEntryListUseArray(key)), fileData.collectionsProvider());
	}


	// <Internal>
	@Contract("_, _ -> param1")
	private static @NotNull List<String> internalGetFooter(final @NotNull List<String> result, final @NotNull List<DataMap.DataNode<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				result.add(entry.getKey());
			} else {
				Collections.reverse(result);
				return result;
			}
		}
		Collections.reverse(result);
		return result;
	}

	private static void internalSetHeader(final @Nullable String[] header, final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull DataMap<String, Object> returnMap) {
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (@Nullable final String comment : header) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "# " + comment, ThunderEditor.LineType.HEADER);
				}
			}
		}
		returnMap.addAll(entryList);
	}

	private static void internalSetFooter(final @Nullable String[] footer, final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull DataMap<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		returnMap.addAll(entryList);
		if (footer != null) {
			for (@Nullable final String comment : footer) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "# " + comment, ThunderEditor.LineType.FOOTER);
				}
			}
		}
	}

	private static @NotNull List<String> internalGetComments(final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				result.add(entry.getKey());
			}
		}
		return result;
	}
	// </Internal>
}