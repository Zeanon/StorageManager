package de.zeanon.storage.internal.base.files;

import de.zeanon.storage.internal.base.exceptions.FileTypeException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.DataStorage;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.FileType;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Reload;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class FlatFile<M extends FileData<?, ?>> implements DataStorage, Comparable<FlatFile> {

	@NotNull
	private final File file;
	@NotNull
	private final FileType fileType;
	@NotNull
	private final M fileData;
	@Setter(AccessLevel.PROTECTED)
	private long lastLoaded;
	/**
	 * Default: {@link Reload#INTELLIGENT}
	 */
	@Setter
	@NotNull
	private ReloadSetting reloadSetting;


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

	@NotNull
	public String getPath() {
		return this.file.getPath();
	}

	@NotNull
	public String getCanonicalPath() {
		try {
			return this.file.getCanonicalPath();
		} catch (@NotNull IOException | SecurityException e) {
			throw new RuntimeIOException("Could not get Canonical Path of '" + this.file.getAbsolutePath() + "'", e.getCause());
		}
	}

	@NotNull
	public String getName() {
		return this.file.getName();
	}

	/**
	 * Set the Contents of the FileData and File from a given InputStream
	 */
	public void setDataFromStream(final @Nullable InputStream inputStream) {
		SMFileUtils.writeToFile(this.file, SMFileUtils.createNewInputStream(inputStream));
		this.reload();
	}

	/**
	 * Delete all Contents of the FileData and File
	 */
	public void clear() {
		SMFileUtils.writeToFile(this.file, null);
		this.reload();
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
	@Synchronized
	public void clearData() {
		this.fileData.clear();
	}

	/**
	 * Set the Contents of the FileData and File from a given File
	 */
	public void setDataFromFile(final @Nullable File file) {
		SMFileUtils.writeToFile(this.file, file == null ? null : SMFileUtils.createNewInputStream(file));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given Resource
	 */
	public void setDataFromResource(final @Nullable String resource) {
		SMFileUtils.writeToFile(this.file, resource == null ? null : SMFileUtils.createNewInputStream(resource));
		this.reload();
	}

	@NotNull
	public String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	/**
	 * Returns if the File has changed since the last check.
	 *
	 * @return true if it has changed.
	 */
	public boolean hasChanged() {
		return SMFileUtils.hasChanged(this.file, this.lastLoaded);
	}

	/**
	 * Reread the content of our flat file
	 */
	public abstract void reload();

	public abstract void save();

	@Override
	public boolean hasKey(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();
		return fileData.containsKey(key);
	}

	@Override
	public boolean hasKeyUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();
		return fileData.containsKeyUseArray(key);
	}

	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	@Synchronized
	public void replaceInFile(final @NotNull CharSequence target, final @NotNull CharSequence replacement) throws IOException {
		Objects.checkNull(target, "Target  must not be null");
		Objects.checkNull(replacement, "Replacement  must not be null");

		final @NotNull Iterator<String> lines = Files.readAllLines(this.file.toPath()).iterator();
		@NotNull @Cleanup PrintWriter writer = new PrintWriter(this.file);
		writer.print((lines.next()).replace(target, replacement));
		lines.forEachRemaining(line -> {
			writer.println();
			writer.print((line).replace(target, replacement));
		});
	}

	@NotNull
	@Override
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");
		update();
		return Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'");
	}

	@NotNull
	@Override
	public Object getUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");
		update();
		return Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'");
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String... keys) {
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String, Object> tempMap = new HashMap<>();
		for (@NotNull String key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (String[] key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String, Object> tempMap = new HashMap<>();
		for (@NotNull String key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.get(key), "File does not contain '" + key + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (String[] key : keys) {
			tempMap.put(key, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys) {
		Objects.checkNull(blockKey, "Key  must not be null");
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(blockKey, Objects.notNull(this.fileData.get(blockKey + "." + tempKey), "File does not contain '" + blockKey + "." + tempKey + "'"));
		}
		return tempMap;
	}

	@NotNull
	@SuppressWarnings("DuplicatedCode")
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		Objects.checkNull(blockKey, "Key  must not be null");
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String[], Object> tempMap = new HashMap<>();
		for (@NotNull String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			tempMap.put(blockKey, Objects.notNull(this.fileData.getUseArray(key), "File does not contain '" + Arrays.toString(key) + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		Objects.checkNull(blockKey, "Key  must not be null");
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(blockKey, Objects.notNull(this.fileData.get(blockKey + "." + tempKey), "File does not contain '" + blockKey + "." + tempKey + "'"));
		}
		return tempMap;
	}

	@NotNull
	@Override
	@SuppressWarnings("DuplicatedCode")
	public Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		Objects.checkNull(blockKey, "Key  must not be null");
		Objects.checkNull(keys, "KeyList  must not be null");
		this.update();

		@NotNull Map<String[], Object> tempMap = new HashMap<>();
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
	@Synchronized
	private boolean insert(final @NotNull String key, final @Nullable Object value) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		this.fileData.insert(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean insertUseArray(final @NotNull String[] key, final @Nullable Object value) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		this.fileData.insertUseArray(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean insertAll(final @NotNull Map<String, Object> map) {
		Objects.checkNull(map, "Map  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData.insert(entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean insertAllUseArray(final @NotNull Map<String[], Object> map) {
		Objects.checkNull(map, "Map  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData.insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean insertAll(final @NotNull String key, final @NotNull Map<String, Object> map) {
		Objects.checkNull(key, "Key  must not be null");
		Objects.checkNull(map, "Map  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData.insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean insertAllUseArray(final @NotNull String[] key, final @NotNull Map<String[], Object> map) {
		Objects.checkNull(map, "Map  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (@NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			@NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData.insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemove(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		this.fileData.remove(key);
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		this.fileData.removeUseArray(key);
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAll(final @NotNull String... keys) {
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAll(final @NotNull Collection<String> keys) {
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAllUseArray(final @NotNull String[]... keys) {
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
			this.fileData.removeUseArray(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAllUseArray(final @NotNull Collection<String[]> keys) {
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
			this.fileData.removeUseArray(tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAll(final @NotNull String blockKey, final @NotNull String... keys) {
		Objects.checkNull(blockKey, "BlockKey  must not be null");
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(blockKey + "." + tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	private boolean internalRemoveAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		Objects.checkNull(blockKey, "BlockKey  must not be null");
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String tempKey : keys) {
			this.fileData.remove(blockKey + "." + tempKey);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	@SuppressWarnings("DuplicatedCode")
	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		Objects.checkNull(blockKey, "BlockKey  must not be null");
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
			@NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.getFileData().removeUseArray(key);
		}
		return !this.fileData.toString().equals(tempData);
	}

	@Synchronized
	@SuppressWarnings("DuplicatedCode")
	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		Objects.checkNull(blockKey, "BlockKey  must not be null");
		Objects.checkNull(keys, "Keys must not be null");
		this.update();

		@NotNull String tempData = this.fileData.toString();
		for (final String[] tempKey : keys) {
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