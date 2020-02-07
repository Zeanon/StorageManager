package de.zeanon.storagemanager.internal.utility.datafiles;


import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import de.zeanon.storagemanager.internal.utility.editor.ThunderEditor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("rawtypes")
@UtilityClass
public class TomlUtils {

	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Header of the FileData
	 */
	public static @NotNull
	List<String> getHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		for (final @NotNull Map.Entry<String, Object> entry : fileData.blockEntryList()) {
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
	public static void setHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @Nullable String[] header) {
		final @NotNull List<Map.Entry<String, Object>> entryList = fileData.blockEntryList();
		//noinspection unchecked
		final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
		TomlUtils.internalSetHeader(header, entryList, returnMap);
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
	public static void setHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] header) {
		if (fileData.get(key) instanceof Map) {
			final @NotNull List<Map.Entry<String, Object>> entryList = Objects.notNull(fileData.blockEntryList(key));
			//noinspection unchecked
			final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
			TomlUtils.internalSetHeader(header, entryList, returnMap);
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
	public static void setHeaderUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] header) {
		if (fileData.getUseArray(key) instanceof Map) {
			final @NotNull List<Map.Entry<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key));
			//noinspection unchecked
			final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
			TomlUtils.internalSetHeader(header, entryList, returnMap);
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
	public static @NotNull
	List<String> getFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		final @NotNull List<Map.Entry<String, Object>> entryList = fileData.blockEntryList();
		return TomlUtils.internalGetFooter(result, entryList);
	}

	/**
	 * Set the Footer of a FileData
	 *
	 * @param fileData the FileDataBase to be used
	 * @param footer   the Footer to be set
	 */
	public static void setFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @Nullable String[] footer) {
		final @NotNull List<Map.Entry<String, Object>> entryList = fileData.blockEntryList();
		//noinspection unchecked
		final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
		TomlUtils.internalSetFooter(footer, entryList, returnMap);
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
	public static void setFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] footer) {
		if (fileData.get(key) instanceof Map) {
			final @NotNull List<Map.Entry<String, Object>> entryList = Objects.notNull(fileData.blockEntryList(key));
			//noinspection unchecked
			final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
			TomlUtils.internalSetFooter(footer, entryList, returnMap);
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
	public static void setFooterUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] footer) {
		if (fileData.getUseArray(key) instanceof Map) {
			final @NotNull List<Map.Entry<String, Object>> entryList = Objects.notNull(fileData.blockEntryListUseArray(key));
			//noinspection unchecked
			final @NotNull Map<String, Object> returnMap = fileData.collectionsProvider().newMap();
			TomlUtils.internalSetFooter(footer, entryList, returnMap);
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
	public static @Nullable
	List<String> getHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull Map.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryList(key))) {
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
	public static @Nullable
	List<String> getHeaderUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String... key) {
		if (fileData.getUseArray(key) instanceof Map) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull Map.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key))) {
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
	public static @NotNull
	List<String> getFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key) {
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		return TomlUtils.internalGetFooter(result, Objects.notNull(fileData.entryList(key)));
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
	public static @NotNull
	List<String> getFooterUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String... key) {
		//noinspection unchecked
		return TomlUtils.internalGetFooter(fileData.collectionsProvider().newList(), Objects.notNull(fileData.blockEntryListUseArray(key)));
	}

	/**
	 * Get the Comments from a given FileDataBase compatible with ThunderFile
	 *
	 * @param fileData the FileDataBase to be used
	 * @param deep     defining, if it should get all comments or only the ones in the top Layer
	 *
	 * @return a List containing the Comments of the FileData
	 */
	public static @NotNull
	List<String> getComments(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final boolean deep) {
		return TomlUtils.internalGetComments(deep ? fileData.entryList() : fileData.blockEntryList(), fileData.collectionsProvider());
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
	public static @NotNull
	List<String> getComments(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final boolean deep) {
		return TomlUtils.internalGetComments(Objects.notNull(deep ? fileData.entryList(key) : fileData.blockEntryList(key)), fileData.collectionsProvider());
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
	public static @NotNull
	List<String> getCommentsUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final boolean deep) {
		return TomlUtils.internalGetComments(Objects.notNull(deep ? fileData.entryListUseArray(key) : fileData.blockEntryListUseArray(key)), fileData.collectionsProvider());
	}


	// <Internal>
	@Contract("_, _ -> param1")
	private static @NotNull
	List<String> internalGetFooter(final @NotNull List<String> result, final @NotNull List<Map.Entry<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
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

	private static void internalSetHeader(final @Nullable String[] header, final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull Map<String, Object> returnMap) {
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (final @Nullable String comment : header) {
				if (comment != null) {
					returnMap.put(comment.startsWith("#") ? comment : "# " + comment, ThunderEditor.LineType.HEADER);
				}
			}
		}
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			returnMap.put(entry.getKey(), entry.getValue());
		}
	}

	private static void internalSetFooter(final @Nullable String[] footer, final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull Map<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			returnMap.put(entry.getKey(), entry.getValue());
		}
		if (footer != null) {
			for (final @Nullable String comment : footer) {
				if (comment != null) {
					returnMap.put(comment.startsWith("#") ? comment : "# " + comment, ThunderEditor.LineType.FOOTER);
				}
			}
		}
	}

	private static @NotNull
	List<String> internalGetComments(final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderEditor.LineType.COMMENT || entry.getValue() == ThunderEditor.LineType.HEADER || entry.getValue() == ThunderEditor.LineType.FOOTER) {
				result.add(entry.getKey());
			}
		}
		return result;
	}
	// </Internal>
}