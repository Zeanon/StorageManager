package de.zeanon.storagemanager.internal.base.files;

import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.exceptions.FileTypeException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.interfaces.*;
import de.zeanon.storagemanager.internal.base.settings.Reload;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.filelock.ExtendedFileLock;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;
import lombok.*;
import lombok.experimental.Accessors;
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


	private final @NotNull
	File file;
	private final @NotNull
	D fileData;
	private final @NotNull
	FileType fileType;
	private final @NotNull
	CollectionsProvider<M, L> collectionsProvider;
	@Setter(AccessLevel.PROTECTED)
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
	private @NotNull
	ReloadSetting reloadSetting;


	protected FlatFile(final @NotNull File file,
					   final @NotNull FileType fileType,
					   final @NotNull D fileData,
					   final @NotNull ReloadSetting reloadSetting) {
		if (fileType.isTypeOf(file)) {
			this.fileType = fileType;
			this.file = file;
			this.fileData = fileData;
			this.reloadSetting = reloadSetting;
			this.collectionsProvider = this.fileData().collectionsProvider();
		} else {
			throw new FileTypeException("File '"
										+ file.getAbsolutePath()
										+ "' is not of type '"
										+ fileType
										+ "'");
		}
	}


	public @NotNull
	String getPath() {
		return this.file().getPath();
	}

	public @NotNull
	String getCanonicalPath() {
		try {
			return this.file().getCanonicalPath();
		} catch (final @NotNull IOException | SecurityException e) {
			throw new RuntimeIOException("Could not get Canonical Path of '"
										 + this.file().getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}

	public @NotNull
	String getName() {
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
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(this.file()).writeLock()) {
			tempLock.lock();
			Files.delete(this.file().toPath());
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Could not delete '"
										 + this.file().getAbsolutePath()
										 + "'",
										 e.getCause());
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

	public @NotNull
	String getAbsolutePath() {
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
	 * Set whether a {@link de.zeanon.storagemanager.external.browniescollections.BigList} should be used as the List implementation
	 */
	public abstract void setBigList(final boolean bigList);

	/**
	 * Set whether the internal data should be concurrent
	 */
	public abstract void setConcurrentData(final boolean concurrentData);

	/**
	 * Set whether the internal data should be synchronized
	 */
	public void synchronizeData(final boolean synchronize) {
		this.fileData().synchronizedData(synchronize);
	}

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
	public @Nullable
	Object get(final @NotNull String key) {
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
	public @Nullable
	Object getUseArray(final @NotNull String... key) {
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
	public @NotNull
	Map<String, Object> getAll(final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.collectionsProvider().newMap();
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
	public @NotNull
	Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.collectionsProvider().newMap();
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
	public @NotNull
	Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.collectionsProvider().newMap();
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
	public @NotNull
	Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.collectionsProvider().newMap();
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
	public @NotNull
	Map<String, Object> getAll(final @NotNull String blockKey,
							   final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.collectionsProvider.newMap();
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
	public @NotNull
	Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
										 final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.collectionsProvider.newMap();
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
	public @NotNull
	Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
										 final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.collectionsProvider.newMap();
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
	public @NotNull
	Map<String, Object> getAll(final @NotNull String blockKey,
							   final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.collectionsProvider.newMap();
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
	public void setUseArray(final @NotNull String[] key,
							final @Nullable Object value) {
		if (this.insertUseArray(key, value)) {
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
	 * Checks if the File needs to be reloaded.
	 *
	 * @return true if File should be reloaded due to it's ReloadSetting.
	 */
	public boolean shouldReload() {
		return this.reloadSetting.shouldReload(this);
	}

	/**
	 * Read the Content of the File and parse it
	 */
	protected abstract @NotNull
	M readFile();

	/**
	 * Checks if the File needs to be reloaded and does so if true.
	 */
	protected void update() {
		if (this.shouldReload()) {
			this.reload();
		}
	}

	private boolean insert(final @NotNull String key,
						   final @Nullable Object value) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		this.fileData().insert(key, value);
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertUseArray(final @NotNull String[] key,
								   final @Nullable Object value) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		this.fileData().insertUseArray(key, value);
		return !this.fileData().toString().equals(tempData);
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
			this.fileData().insert(entry.getKey(), entry.getValue());
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
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemove(final @NotNull String key) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		this.fileData().remove(key);
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveUseArray(final @NotNull String... key) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		this.fileData().removeUseArray(key);
		return !this.fileData().toString().equals(tempData);
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