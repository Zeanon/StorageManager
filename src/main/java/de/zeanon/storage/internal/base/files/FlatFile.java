package de.zeanon.storage.internal.base.files;

import de.zeanon.storage.internal.base.exceptions.FileTypeException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.DataStorage;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.FileType;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Reload;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic foundation for the Data Classes, providing the necessary fields and basic methods
 *
 * @author Zeanon
 * @version 2.4.0
 */
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
public abstract class FlatFile<M extends FileData<?, ?>> implements DataStorage, Comparable<FlatFile> {

	private final @NotNull File file;
	private final @NotNull FileType fileType;
	private final @NotNull M fileData;
	@Setter(AccessLevel.PROTECTED)
	private long lastLoaded;
	/**
	 * Default: {@link Reload#INTELLIGENT}
	 */
	@Setter
	private @NotNull ReloadSetting reloadSetting;


	protected FlatFile(final @NotNull File file, final @NotNull FileType fileType, final @NotNull M fileData, final @NotNull ReloadSetting reloadSetting) {
		if (fileType.isTypeOf(file)) {
			this.fileType = fileType;
			this.file = file;
			this.fileData = fileData;
			this.reloadSetting = reloadSetting;
		} else {
			throw new FileTypeException("File '" + file.getAbsolutePath() + "' is not of type '" + fileType + "'");
		}
	}

	public @NotNull String getPath() {
		return this.file.getPath();
	}

	public @NotNull String getCanonicalPath() {
		try {
			return this.file.getCanonicalPath();
		} catch (@NotNull IOException | SecurityException e) {
			throw new RuntimeIOException("Could not get Canonical Path of '" + this.file.getAbsolutePath() + "'", e.getCause());
		}
	}

	public @NotNull String getName() {
		return this.file.getName();
	}

	/**
	 * Set the Contents of the FileData and File from a given InputStream
	 */
	public void setDataFromStream(final @Nullable InputStream inputStream) {
		BaseFileUtils.writeToFile(this.file, BaseFileUtils.createNewInputStream(inputStream));
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
		BaseFileUtils.writeToFile(this.file, null);
	}

	/**
	 * Just delete the File
	 */
	@Synchronized
	public void deleteFile() {
		try {
			Files.delete(this.file.toPath());
		} catch (IOException e) {
			throw new RuntimeIOException("Could not delete '" + this.file.getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Clears the contents of the internal FileData.
	 * To get any data, you simply need to reload.
	 */
	public void clearData() {
		this.fileData.clear();
	}

	/**
	 * Set the Contents of the FileData and File from a given File
	 */
	public void setDataFromFile(final @Nullable File file) {
		BaseFileUtils.writeToFile(this.file, BaseFileUtils.createNewInputStream(file));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given Resource
	 */
	public void setDataFromResource(final @Nullable String resource) {
		BaseFileUtils.writeToFile(this.file, BaseFileUtils.createNewInputStream(resource));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given URL
	 */
	public void setDataFromUrl(final @Nullable String url) {
		try {
			BaseFileUtils.writeToFile(this.file, url == null ? null : BaseFileUtils.createNewInputStream(new URL(url)));
			this.reload();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the Contents of the FileData and File from a given URL
	 */
	public void setDataFromUrl(final @Nullable URL url) {
		BaseFileUtils.writeToFile(this.file, BaseFileUtils.createNewInputStream(url));
		this.reload();
	}

	public @NotNull String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	/**
	 * Returns if the File has changed since the last check.
	 *
	 * @return true if it has changed.
	 */
	public boolean hasChanged() {
		return BaseFileUtils.hasChanged(this.file, this.lastLoaded);
	}

	/**
	 * Reread the content of the File into the cache
	 */
	public abstract void reload();

	/**
	 * Save the cached Data to the File
	 */
	public abstract void save();

	@Override
	public boolean hasKey(final @NotNull String key) {
		this.update();
		return this.fileData.containsKey(key);
	}

	@Override
	public boolean hasKeyUseArray(final @NotNull String... key) {
		this.update();
		return this.fileData.containsKeyUseArray(key);
	}

	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	@Synchronized
	public void replaceInFile(final @NotNull CharSequence target, final @NotNull CharSequence replacement) throws IOException {
		final @NotNull Iterator<String> lines = Files.readAllLines(this.file.toPath()).iterator();
		final @NotNull @Cleanup PrintWriter writer = new PrintWriter(this.file);
		writer.print((lines.next()).replace(target, replacement));
		lines.forEachRemaining(line -> {
			writer.println();
			writer.print((line).replace(target, replacement));
		});
	}

	@Override
	public @NotNull Object get(final @NotNull String key) {
		this.update();
		return Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'");
	}

	@Override
	public @NotNull Object getUseArray(final @NotNull String... key) {
		this.update();
		return Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'");
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String... keys) {
		this.update();

		final @NotNull Map<String, Object> tempMap = new HashMap<>();
		for (@NotNull String key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'"));
		}
		return tempMap;
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		this.update();

		final @NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (String[] key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull Map<String, Object> tempMap = new HashMap<>();
		for (@NotNull String key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'"));
		}
		return tempMap;
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (String[] key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys) {
		this.update();

		final @NotNull Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(blockKey, Objects.notNull(this.fileData.get(blockKey + "." + tempKey), "File does not contain '" + blockKey + "." + tempKey + "'"));
		}
		return tempMap;
	}

	@Override
	@SuppressWarnings("DuplicatedCode")
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.update();

		final @NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (@NotNull String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			tempMap.put(blockKey, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(blockKey, Objects.notNull(this.fileData.get(blockKey + "." + tempKey), "File does not contain '" + blockKey + "." + tempKey + "'"));
		}
		return tempMap;
	}

	@Override
	@SuppressWarnings("DuplicatedCode")
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (@NotNull String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			tempMap.put(blockKey, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@Override
	public void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void setUseArray(final @NotNull String[] key, final @Nullable Object value) {
		if (this.insertUseArray(key, value)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void setAllUseArray(final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(dataMap)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(blockKey, dataMap)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void setAllUseArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(blockKey, dataMap)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void remove(final @NotNull String key) {
		if (this.internalRemove(key)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeUseArray(final @NotNull String... key) {
		if (this.internalRemoveUseArray(key)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAll(final @NotNull String... keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAllUseArray(final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAllUseArray(final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull String... keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}


	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
	}

	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.setLastLoaded(System.currentTimeMillis());
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
	protected void update() {
		if (this.shouldReload()) {
			this.reload();
		}
	}

	/**
	 * Insert a key-value-pair to the FileData.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to @param key.
	 *
	 * @return true if the Data contained by FileData contained after adding the key-value-pair.
	 */
	private boolean insert(final @NotNull String key, final @Nullable Object value) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		this.fileData.insert(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	private boolean insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		this.fileData.insertUseArray(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData.insert(entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData.insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull String key, final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData.insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull String[] key, final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			@NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData.insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemove(final @NotNull String key) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		this.fileData.remove(key);
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveUseArray(final @NotNull String... key) {
		this.update();

		@NotNull String tempData = this.fileData.toString();
		this.fileData.removeUseArray(key);
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData.remove(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData.remove(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
			this.fileData.removeUseArray(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
			this.fileData.removeUseArray(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey, final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(blockKey + "." + tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(blockKey + "." + tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@SuppressWarnings("DuplicatedCode")
	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final @NotNull String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.getFileData().removeUseArray(key);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@SuppressWarnings("DuplicatedCode")
	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData.toString();
		for (final @NotNull String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.getFileData().removeUseArray(key);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Override
	public int compareTo(final @NotNull FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}
}