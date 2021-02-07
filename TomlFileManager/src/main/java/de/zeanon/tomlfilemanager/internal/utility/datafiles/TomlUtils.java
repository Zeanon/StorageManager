package de.zeanon.tomlfilemanager.internal.utility.datafiles;


import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
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
	public @NotNull List<String> getHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData) { //NOSONAR
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		for (final @NotNull Map.Entry<String, Object> entry : fileData.blockEntryList()) {
			if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
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
	public void setHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @Nullable String[] header) { //NOSONAR
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
	public void setHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] header) { //NOSONAR
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
	public void setHeaderUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] header) { //NOSONAR
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
	public @NotNull List<String> getFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData) { //NOSONAR
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
	public void setFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @Nullable String[] footer) { //NOSONAR
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
	public void setFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] footer) { //NOSONAR
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
	public void setFooterUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] footer) { //NOSONAR
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
	public @Nullable
	List<String> getHeader(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key) { //NOSONAR
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull Map.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryList(key))) {
				if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
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
	public @Nullable
	List<String> getHeaderUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String... key) { //NOSONAR
		if (fileData.getUseArray(key) instanceof Map) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull Map.Entry<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key))) {
				if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
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
	public @NotNull List<String> getFooter(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key) { //NOSONAR
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
	public @NotNull List<String> getFooterUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String... key) { //NOSONAR
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
	public @NotNull List<String> getComments(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final boolean deep) { //NOSONAR
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
	public @NotNull List<String> getComments(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String key, final boolean deep) { //NOSONAR
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
	public @NotNull List<String> getCommentsUseArray(final @NotNull StandardFileData<Map, Map.Entry<String, Object>, List> fileData, final @NotNull String[] key, final boolean deep) { //NOSONAR
		return TomlUtils.internalGetComments(Objects.notNull(deep ? fileData.entryListUseArray(key) : fileData.blockEntryListUseArray(key)), fileData.collectionsProvider());
	}


	// <Internal>
	@Contract("_, _ -> param1")
	private @NotNull List<String> internalGetFooter(final @NotNull List<String> result, final @NotNull List<Map.Entry<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
				result.add(entry.getKey());
			} else {
				Collections.reverse(result);
				return result;
			}
		}
		Collections.reverse(result);
		return result;
	}

	private void internalSetHeader(final @Nullable String[] header, final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull Map<String, Object> returnMap) {
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (final @Nullable String comment : header) {
				if (comment != null) {
					returnMap.put(comment.startsWith("#") ? comment : "# " + comment, TomlUtils.LineType.HEADER);
				}
			}
		}
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			returnMap.put(entry.getKey(), entry.getValue());
		}
	}

	private void internalSetFooter(final @Nullable String[] footer, final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull Map<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
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
					returnMap.put(comment.startsWith("#") ? comment : "# " + comment, TomlUtils.LineType.FOOTER);
				}
			}
		}
	}

	private @NotNull List<String> internalGetComments(final @NotNull List<Map.Entry<String, Object>> entryList, final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, Object> entry : entryList) {
			if (entry.getValue() == TomlUtils.LineType.COMMENT || entry.getValue() == TomlUtils.LineType.HEADER || entry.getValue() == TomlUtils.LineType.FOOTER) {
				result.add(entry.getKey());
			}
		}
		return result;
	}
	// </Internal>

	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE,
		HEADER,
		FOOTER
	}
}