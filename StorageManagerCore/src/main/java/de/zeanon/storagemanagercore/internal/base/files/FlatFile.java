package de.zeanon.storagemanagercore.internal.base.files;

import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.FileTypeException;
import de.zeanon.storagemanagercore.internal.base.interfaces.*;
import de.zeanon.storagemanagercore.internal.base.settings.Reload;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.storagemanagercore.internal.utility.basic.Pair;
import de.zeanon.storagemanagercore.internal.utility.filelock.ExtendedFileLock;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic foundation for the Data Classes, providing the necessary fields and fundamental methods
 *
 * @author Zeanon
 * @version 2.4.0
 */
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@Accessors(fluent = true, chain = false)
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess", "DefaultAnnotationParam", "unchecked", "rawtypes"})
public abstract class FlatFile<D extends FileData<M, ?, L>, M extends Map, L extends List> implements DataStorage, Comparable<FlatFile> { //NOSONAR


	private final @NotNull File file;
	private final @NotNull D fileData;
	private final @NotNull FileType fileType;
	@Setter
	private volatile long lastLoaded;
	/**
	 * Default: {@link Reload#INTELLIGENT}
	 * -- Setter --
	 * Set the ReloadSetting to be used
	 * -- Getter --
	 * Get the value of the saved ReloadSetting
	 */
	@Setter
	@Accessors(fluent = true, chain = false)
	private @NotNull ReloadSetting reloadSetting;

	protected FlatFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull FileType fileType,
					   final @NotNull D fileData,
					   final @NotNull ReloadSetting reloadSetting) {
		if (fileType.isTypeOf(file)) {
			this.fileType = fileType;
			this.file = file;
			this.fileData = fileData;
			this.reloadSetting = reloadSetting;

			BaseFileUtils.writeToFileIfCreated(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		} else {
			throw new FileTypeException("File '"
										+ file.getAbsolutePath()
										+ "' is not of type '"
										+ fileType
										+ "'");
		}
	}


	public @NotNull CollectionsProvider<M, L> collectionsProvider() {
		return this.fileData().collectionsProvider();
	}

	public @NotNull String getPath() {
		return this.file().getPath();
	}

	public @NotNull String getCanonicalPath() {
		try {
			return this.file().getCanonicalPath();
		} catch (final @NotNull IOException e) {
			throw new UncheckedIOException("Could not get Canonical Path of '"
										   + this.file().getAbsolutePath()
										   + "'",
										   e);
		} catch (final @NotNull SecurityException e) {
			throw new SecurityException("Could not get Canonical Path of '"
										+ this.file().getAbsolutePath()
										+ "'",
										e);
		}
	}

	public @NotNull String getName() {
		return this.file().getName();
	}

	/**
	 * Set the Contents of the FileData and File from a given InputStream
	 */
	public void setDataFromStream(final @Nullable InputStream inputStream) {
		BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		this.reload();
	}

	/**
	 * Delete all Contents of the FileData and File
	 */
	public void clear() {
		this.clearFile();
		this.clearData();
	}

	public void clearFile() {
		BaseFileUtils.writeToFile(this.file(), null);
	}

	/**
	 * Just delete the File
	 */
	public void deleteFile() {
		try {
			Files.delete(this.file().toPath());
		} catch (final @NotNull IOException e) {
			throw new UncheckedIOException("Could not delete '"
										   + this.file().getAbsolutePath()
										   + "'",
										   e);
		}
	}

	/**
	 * Clears the contents of the internal FileData.
	 * To get any data, you simply need to reload.
	 */
	public void clearData() {
		this.fileData().clear();
	}

	/**
	 * Set the Contents of the FileData and File from a given File
	 */
	public void setDataFromFile(final @Nullable File file) {
		BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStreamFromFile(file));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given Resource
	 */
	public void setDataFromResource(final @Nullable String resource) {
		BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStreamFromResource(resource));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given URL
	 */
	public void setDataFromUrl(final @Nullable String url) {
		BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStreamFromUrl(url));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given URL
	 */
	public void setDataFromUrl(final @Nullable URL url) {
		BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStreamFromUrl(url));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given FlatFile
	 */
	public void setDataFromFlatFile(final @NotNull FlatFile<D, M, L> flatFile) {
		this.loadDataFromFileData(flatFile.fileData());
		this.reload();
	}

	public @NotNull String getAbsolutePath() {
		return this.file().getAbsolutePath();
	}

	/**
	 * Returns if the File has changed since the last check.
	 *
	 * @return true if it has changed.
	 */
	public boolean hasChanged() {
		return BaseFileUtils.hasChanged(this.file(), this.lastLoaded());
	}

	/**
	 * Reload the content of the File into the cache
	 */
	public void reload() {
		this.fileData().loadData(this.readFile());
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Load the data from a given {@link FileData} into the cache
	 *
	 * @param fileData the Data to be loaded
	 */
	public void loadDataFromFileData(final @NotNull D fileData) {
		this.fileData().loadData(fileData.dataMap());
		this.save();
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Load the data from a given {@link Map} into the cache
	 *
	 * @param map the Data to be loaded
	 */
	public void loadDataFromMap(final @NotNull M map) {
		this.fileData().loadData(map);
		this.save();
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Save the cached Data to the File
	 */
	public abstract void save();

	/**
	 * Set whether a {@link de.zeanon.storagemanagercore.external.browniescollections.BigList} should be used as the List implementation
	 */
	public abstract void setBigList(final boolean bigList);

	/**
	 * Set whether the internal data should be concurrent
	 */
	public abstract void setConcurrentData(final boolean concurrentData);

	/**
	 * Checks whether the FileData contains the given key
	 *
	 * @param key key to check
	 *
	 * @return true if key exists, otherwise false
	 */
	@Override
	public boolean hasKey(final @NotNull String key) {
		this.update();
		return this.fileData().containsKey(key);
	}

	/**
	 * Checks whether the FileData contains the given key
	 *
	 * @param key key to check
	 *
	 * @return true if key exists, otherwise false
	 */
	@Override
	public boolean hasKeyUseArray(final @NotNull String... key) {
		this.update();
		return this.fileData().containsKeyUseArray(key);
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Contract("-> new")
	@Override
	public @NotNull List<?> entryList() {
		return this.fileData().entryList();
	}

	/**
	 * Get a List consisting of Map.Entry objects of the top most layer of the internal DataMap
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Contract("-> new")
	@Override
	public @NotNull List<?> blockEntryList() {
		return this.fileData().blockEntryList();
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<?> entryList(final @NotNull String key) {
		return this.fileData().entryList(key);
	}

	/**
	 * Get a List consisting of Map.Entry objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Override
	public @Nullable List<?> blockEntryList(final @NotNull String key) {
		return this.fileData().blockEntryList(key);
	}

	/**
	 * Get a List consisting of Map.Entry objects whereas values being instances of Map are also getting parsed to
	 * their entryLists
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Contract("null -> fail")
	@Override
	public @Nullable List<?> entryListUseArray(final @NotNull String... key) {
		return this.fileData().entryListUseArray(key);
	}

	/**
	 * Get a List consisting of Map.Entry objects of only the given Block
	 *
	 * @param key the Key to the SubBlock the entryList should be generated from
	 *
	 * @return the entryList of the internal dataMap
	 */
	@Contract("null -> fail")
	@Override
	public @Nullable List<?> blockEntryListUseArray(final @NotNull String... key) {
		return this.fileData().blockEntryListUseArray(key);
	}


	@Override
	public List<String> getKeys() {
		return this.fileData().getKeys();
	}

	@Override
	public List<String> getBlockKeys() {
		return this.fileData().getBlockKeys();
	}

	@Override
	public List<String> getKeys(final @NotNull String key) {
		return this.fileData().getKeys(key);
	}

	@Override
	public List<String> getBlockKeys(final @NotNull String key) {
		return this.fileData().getBlockKeys(key);
	}

	@Override
	public List<String[]> getKeysUseArray(final @NotNull String... key) {
		return this.fileData().getKeysUseArray(key);
	}

	@Override
	public List<String> getBlockKeysUseArray(final @NotNull String... key) {
		return this.fileData().getBlockKeysUseArray(key);
	}


	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	public void replaceInFile(final @NotNull CharSequence target,
							  final @NotNull CharSequence replacement) throws IOException {
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(this.file()).writeLock()) {
			tempLock.lock();
			final @NotNull Iterator<String> lines;
			try (final @NotNull BufferedReader reader = tempLock.createBufferedReader()) {
				lines = reader.lines().collect(Collectors.toList()).iterator();
			}

			tempLock.truncateChannel(0);
			try (final @NotNull PrintWriter writer = tempLock.createPrintWriter()) {
				writer.print((lines.next()).replace(target, replacement));
				lines.forEachRemaining(line -> {
					writer.println();
					writer.print((line).replace(target, replacement));
				});
			}
		}
	}

	/**
	 * Get the value mapped to the given key
	 *
	 * @param key key to Object in the File
	 *
	 * @return the value mapped to the key or null if the key does not exist
	 */
	@Override
	public @Nullable Object get(final @NotNull String key) {
		this.update();
		return this.fileData().get(key);
	}

	/**
	 * Get the value mapped to the given key
	 *
	 * @param key key to Object in the File
	 *
	 * @return the value mapped to the key or null if the key does not exist
	 */
	@Override
	public @Nullable Object getUseArray(final @NotNull String... key) {
		this.update();
		return this.fileData().getUseArray(key);
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String key : keys) {
			tempMap.put(key, this.fileData().get(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String[] key : keys) {
			tempMap.put(key, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String key : keys) {
			tempMap.put(key, this.fileData().get(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String[] key : keys) {
			tempMap.put(key, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey,
											   final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String tempKey : keys) {
			tempMap.put(blockKey, this.fileData().get(blockKey + "." + tempKey));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
														 final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			tempMap.put(blockKey, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
														 final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			tempMap.put(blockKey, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey,
											   final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.fileData().collectionsProvider().newMap();
		for (final @NotNull String tempKey : keys) {
			tempMap.put(blockKey, this.fileData().get(blockKey + "." + tempKey));
		}
		return tempMap;
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void set(final @NotNull String key,
					final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setWithoutCheck(final @NotNull String key,
								final @Nullable Object value) {
		this.insertWithoutCheck(key, value);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setUseArray(final @NotNull String[] key,
							final @Nullable Object value) {
		if (this.insertUseArray(key, value)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setUseArrayWithoutCheck(final @NotNull String[] key,
										final @Nullable Object value) {
		this.insertUseArrayWithoutCheck(key, value);
		this.lastLoaded(System.currentTimeMillis());
	}


	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull Pair<String, Object>... dataPairs) {
		if (this.insertAll(dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull Pair<String[], Object>... dataPairs) {
		if (this.insertAllUseArray(dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull String blockKey,
					   final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(blockKey, dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull String[] blockKey,
							   final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(blockKey, dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull String blockKey,
					   final @NotNull Pair<String, Object>... dataPairs) {
		if (this.insertAll(blockKey, dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull String[] blockKey,
							   final @NotNull Pair<String[], Object>... dataPairs) {
		if (this.insertAllUseArray(blockKey, dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}


	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull Map<String, Object> dataMap) {
		this.insertAllWithoutCheck(dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull Map<String[], Object> dataMap) {
		this.insertAllUseArrayWithoutCheck(dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull Pair<String, Object>... dataPairs) {
		this.insertAllWithoutCheck(dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull Pair<String[], Object>... dataPairs) {
		this.insertAllUseArrayWithoutCheck(dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull String blockKey,
								   final @NotNull Map<String, Object> dataMap) {
		this.insertAllWithoutCheck(blockKey, dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
										   final @NotNull Map<String[], Object> dataMap) {
		this.insertAllUseArrayWithoutCheck(blockKey, dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull String blockKey,
								   final @NotNull Pair<String, Object>... dataPairs) {
		this.insertAllWithoutCheck(blockKey, dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
										   final @NotNull Pair<String[], Object>... dataPairs) {
		this.insertAllUseArrayWithoutCheck(blockKey, dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	public void replace(final @NotNull CharSequence target,
						final @NotNull CharSequence replacement) {
		this.update();

		final @NotNull Stream<String> lines;
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(this.file, true, false).readLock();
			 final @NotNull BufferedReader reader = tempLock.createBufferedReader()) {
			tempLock.lock();
			lines = reader.lines();
		} catch (final @NotNull IOException e) {
			throw new UncheckedIOException("Error while reading content from '" + this.file.getAbsolutePath() + "'", e);
		}

		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(this.file).writeLock();
			 final @NotNull PrintWriter writer = tempLock.createPrintWriter()) {
			tempLock.lock();
			tempLock.truncateChannel(0);

			lines.map(line -> line.replace(target, replacement)).forEach(writer::println);
		} catch (final @NotNull IOException e) {
			throw new UncheckedIOException("Error while writing to '" + this.file.getAbsolutePath() + "'", e);
		}
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void remove(final @NotNull String key) {
		if (this.internalRemove(key)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeUseArray(final @NotNull String... key) {
		if (this.internalRemoveUseArray(key)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeWithoutCheck(final @NotNull String key) {
		this.internalRemoveWithoutCheck(key);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeUseArrayWithoutCheck(final @NotNull String... key) {
		this.internalRemoveUseArrayWithoutCheck(key);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String... keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String blockKey,
						  final @NotNull String... keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String blockKey,
						  final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey,
								  final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey,
								  final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String... keys) {
		this.internalRemoveAllWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull Collection<String> keys) {
		this.internalRemoveAllWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[]... keys) {
		this.internalRemoveAllUseArrayWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull Collection<String[]> keys) {
		this.internalRemoveAllUseArrayWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String blockKey,
									  final @NotNull String... keys) {
		this.internalRemoveAllWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String blockKey,
									  final @NotNull Collection<String> keys) {
		this.internalRemoveAllWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
											  final @NotNull String[]... keys) {
		this.internalRemoveAllUseArrayWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
											  final @NotNull Collection<String[]> keys) {
		this.internalRemoveAllUseArrayWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Checks if the File needs to be reloaded.
	 *
	 * @return true if File should be reloaded due to it's ReloadSetting.
	 */
	public boolean shouldReload() {
		return this.reloadSetting.shouldReload(this);
	}

	/**
	 * Checks if the File needs to be reloaded and does so if true.
	 */
	public void update() {
		if (this.shouldReload()) {
			this.reload();
		}
	}

	/**
	 * Read the Content of the File and parse it
	 */
	protected abstract @NotNull M readFile();

	private boolean insert(final @NotNull String key,
						   final @Nullable Object value) {
		this.update();

		final @Nullable Object currentValue = this.fileData.get(key);
		if ((currentValue == null && value != null) || (currentValue != null && !currentValue.equals(value))) {
			this.fileData().insert(key, value);
			return true;
		} else {
			return false;
		}
	}

	private void insertWithoutCheck(final @NotNull String key,
									final @Nullable Object value) {
		this.update();
		this.fileData().insert(key, value);
	}

	private boolean insertUseArray(final @NotNull String[] key,
								   final @Nullable Object value) {
		this.update();

		final @Nullable Object currentValue = this.fileData().getUseArray(key);
		if ((currentValue == null && value != null) || (currentValue != null && !currentValue.equals(value))) {
			this.fileData().insertUseArray(key, value);
			return true;
		} else {
			return false;
		}
	}

	private void insertUseArrayWithoutCheck(final @NotNull String[] key,
											final @Nullable Object value) {
		this.update();
		this.fileData().insertUseArray(key, value);
	}

	private boolean insertAll(final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull Pair<String, Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(Objects.notNull(entry.getKey()), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String[], Object> entry : pairs) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull String key,
							  final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull String[] key,
									  final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull String key,
							  final @NotNull Pair<String, Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull String[] key,
									  final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String[], Object> entry : pairs) {
			final @NotNull String[] tempKey = new String[key.length + Objects.notNull(entry.getKey()).length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private void insertAllWithoutCheck(final @NotNull Map<String, Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull Map<String[], Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull Pair<String, Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(Objects.notNull(entry.getKey()), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String[], Object> entry : pairs) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull String key,
									   final @NotNull Map<String, Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull String[] key,
											   final @NotNull Map<String[], Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull String key,
									   final @NotNull Pair<String, Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull String[] key,
											   final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String[], Object> entry : pairs) {
			final @NotNull String[] tempKey = new String[key.length + Objects.notNull(entry.getKey()).length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
	}

	private boolean internalRemove(final @NotNull String key) {
		this.update();

		if (this.fileData.containsKey(key)) {
			this.fileData().remove(key);
			return true;
		} else {
			return false;
		}
	}

	private boolean internalRemoveUseArray(final @NotNull String... key) {
		this.update();

		if (this.fileData.containsKeyUseArray(key)) {
			this.fileData().removeUseArray(key);
			return true;
		} else {
			return false;
		}
	}

	private void internalRemoveWithoutCheck(final @NotNull String key) {
		this.update();

		this.fileData().remove(key);
	}

	private void internalRemoveUseArrayWithoutCheck(final @NotNull String... key) {
		this.update();

		this.fileData().removeUseArray(key);
	}

	private boolean internalRemoveAll(final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey,
									  final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey,
									  final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey,
											  final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey,
											  final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String... keys) {
		this.update();

		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull Collection<String> keys) {
		this.update();

		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[]... keys) {
		this.update();

		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull Collection<String[]> keys) {
		this.update();

		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String blockKey,
											   final @NotNull String... keys) {
		this.update();

		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String blockKey,
											   final @NotNull Collection<String> keys) {
		this.update();

		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
													   final @NotNull String[]... keys) {
		this.update();

		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
													   final @NotNull Collection<String[]> keys) {
		this.update();

		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
	}

	/**
	 * Compare this FlatFile to a given one
	 *
	 * @param flatFile the FlatFile to be compared to
	 */
	@Override
	public int compareTo(final @NotNull FlatFile flatFile) {
		return this.file().compareTo(flatFile.file());
	}
}