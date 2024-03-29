package de.zeanon.thunderfilemanager.internal.utility.datafiles;

import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.thunderfilemanager.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.thunderfilemanager.internal.utility.parser.ThunderFileParser;
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
@SuppressWarnings("rawtypes")
@UtilityClass
public class ThunderUtils {


	/**
	 * Get the Header from a give FileData
	 *
	 * @param fileData the FileDataBase to be used
	 *
	 * @return a List containing the Header of the FileData
	 */
	public @NotNull List<String> getHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData) { //NOSONAR
		//noinspection unchecked
		final @NotNull List<String> result = fileData.collectionsProvider().newList();
		for (final @NotNull DataMap.DataNode<String, Object> entry : fileData.blockEntryList()) {
			if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
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
	public void setHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @Nullable String[] header) { //NOSONAR
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
	public void setHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] header) { //NOSONAR
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
	public void setHeaderUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] header) { //NOSONAR
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
	public @NotNull List<String> getFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData) { //NOSONAR
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
	public void setFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @Nullable String[] footer) { //NOSONAR
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
	public void setFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final @Nullable String[] footer) { //NOSONAR
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
	public void setFooterUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final @Nullable String[] footer) { //NOSONAR
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
	public @Nullable
	List<String> getHeader(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key) { //NOSONAR
		if (fileData.get(key) instanceof DataMap) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull DataMap.DataNode<String, Object> entry : Objects.notNull(fileData.blockEntryList(key))) {
				if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
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
	List<String> getHeaderUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String... key) { //NOSONAR
		if (fileData.getUseArray(key) instanceof DataMap) {
			//noinspection unchecked
			final @NotNull List<String> result = fileData.collectionsProvider().newList();
			for (final @NotNull DataMap.DataNode<String, Object> entry : Objects.notNull(fileData.blockEntryListUseArray(key))) {
				if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
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
	public @NotNull List<String> getFooter(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key) { //NOSONAR
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
	public @NotNull List<String> getFooterUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String... key) { //NOSONAR
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
	public @NotNull List<String> getComments(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final boolean deep) { //NOSONAR
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
	public @NotNull List<String> getComments(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String key, final boolean deep) { //NOSONAR
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
	public @NotNull List<String> getCommentsUseArray(final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, final @NotNull String[] key, final boolean deep) { //NOSONAR
		return ThunderUtils.internalGetComments(Objects.notNull(deep ? fileData.entryListUseArray(key) : fileData.blockEntryListUseArray(key)), fileData.collectionsProvider());
	}


	// <Internal>
	@Contract("_, _ -> param1")
	private @NotNull List<String> internalGetFooter(final @NotNull List<String> result, final @NotNull List<DataMap.DataNode<String, Object>> entryList) {
		Collections.reverse(entryList);
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
				result.add(entry.getKey());
			} else {
				Collections.reverse(result);
				return result;
			}
		}
		Collections.reverse(result);
		return result;
	}

	private void internalSetHeader(final @Nullable String[] header, final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull DataMap<String, Object> returnMap) {
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		if (header != null) {
			for (final @Nullable String comment : header) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "# " + comment, ThunderFileParser.LineType.COMMENT);
				}
			}
		}
		returnMap.addAll(entryList);
	}

	private void internalSetFooter(final @Nullable String[] footer, final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull DataMap<String, Object> returnMap) {
		Collections.reverse(entryList);
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
				entryList.remove(entry);
			} else {
				break;
			}
		}
		Collections.reverse(entryList);
		returnMap.addAll(entryList);
		if (footer != null) {
			for (final @Nullable String comment : footer) {
				if (comment != null) {
					returnMap.add(comment.startsWith("#") ? comment : "# " + comment, ThunderFileParser.LineType.COMMENT);
				}
			}
		}
	}

	private @NotNull List<String> internalGetComments(final @NotNull List<DataMap.DataNode<String, Object>> entryList, final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull DataMap.DataNode<String, Object> entry : entryList) {
			if (entry.getValue() == ThunderFileParser.LineType.COMMENT) {
				result.add(entry.getKey());
			}
		}
		return result;
	}
	// </Internal>
}