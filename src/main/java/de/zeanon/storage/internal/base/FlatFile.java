package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.base.interfaces.StorageBase;
import de.zeanon.storage.internal.data.FileData;
import de.zeanon.storage.internal.settings.Reload;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


/**
 * Basic foundation for the Data Classes
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
public abstract class FlatFile implements StorageBase, Comparable<FlatFile> {

	protected final File file;
	private final FileTypeBase fileType;
	protected FileData fileData;
	protected long lastLoaded;
	/**
	 * Default: INTELLIGENT
	 */
	@Setter
	private ReloadSettingBase reloadSetting = Reload.INTELLIGENT;


	protected FlatFile(final @NotNull File file, final @NotNull FileTypeBase fileType, final @Nullable ReloadSettingBase reloadSetting) {
		if (fileType.isTypeOf(file)) {
			this.fileType = fileType;
			this.file = file;

			if (reloadSetting != null) {
				this.reloadSetting(reloadSetting);
			}
		} else {
			throw new IllegalStateException("File '" + file.getAbsolutePath() + "' is not of type '" + fileType + "'");
		}
	}

	public String getPath() {
		return this.file.getPath();
	}

	public String getCanonicalPath() {
		try {
			return this.file.getCanonicalPath();
		} catch (IOException e) {
			System.err.println("Could not get Canonical Path of '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	public String getName() {
		return this.file.getName();
	}

	/**
	 * Set the Contents of the FileData and File from a given InputStream
	 */
	public synchronized void setDataFromStream(final @Nullable InputStream inputStream) {
		SMFileUtils.writeToFile(this.file, SMFileUtils.createNewInputStream(inputStream));
		this.reload();
	}

	/**
	 * Delete all Contents of the FileData and File
	 */
	public synchronized void clear() {
		SMFileUtils.writeToFile(this.file, null);
		this.reload();
	}

	/**
	 * Just delete the File
	 */
	public synchronized void deleteFile() {
		if (!this.file.delete()) {
			System.err.println("Could not delete '" + this.file.getAbsolutePath() + "'");
			throw new IllegalStateException();
		}
	}

	/**
	 * Clears the contents of the internal FileData.
	 * To get any data, you simply need to reload.
	 */
	public synchronized void clearData() {
		this.fileData.clear();
	}

	/**
	 * Set the Contents of the FileData and File from a given File
	 */
	public synchronized void setDataFromFile(final @Nullable File file) {
		SMFileUtils.writeToFile(this.file, SMFileUtils.createNewInputStream(file));
		this.reload();
	}

	/**
	 * Set the Contents of the FileData and File from a given Resource
	 */
	public synchronized void setDataFromResource(final @Nullable String resource) {
		SMFileUtils.writeToFile(this.file, SMFileUtils.createNewInputStream(resource));
		this.reload();
	}

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

	@Override
	public boolean hasKey(final @NotNull String key) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		this.update();
		return fileData.containsKey(key);
	}

	@Override
	public Set<String> keySet() {
		this.update();
		return this.fileData.keySet();
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		this.update();
		return this.fileData.keySet(key);
	}

	@Override
	public Set<String> blockKeySet() {
		this.update();
		return this.fileData.blockKeySet();
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		this.update();
		return this.fileData.blockKeySet(key);
	}

	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	public synchronized void replaceInFile(final @NotNull CharSequence target, final @NotNull CharSequence replacement) throws IOException {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(target, "Target must not be null");
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(replacement, "Replacement must not be null");

		final Iterator lines = Files.readAllLines(this.file.toPath()).iterator();
		PrintWriter writer = new PrintWriter(this.file);
		writer.print(((String) lines.next()).replace(target, replacement));
		//noinspection unchecked
		lines.forEachRemaining(line -> {
			writer.println();
			writer.print(((String) line).replace(target, replacement));
		});
		this.reload();
	}

	@Override
	public Object get(final @NotNull String key) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		update();
		return fileData.get(key);
	}

	@Override
	public Map<String, Object> getAll(final @NotNull List<String> keys) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(keys, "KeyList must not be null");
		this.update();

		Map<String, Object> tempMap = new HashMap<>();
		for (String key : keys) {
			tempMap.put(key, this.fileData.get(key));
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(keys, "KeyList must not be null");
		this.update();

		Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(key, this.fileData.get(key + "." + tempKey));
		}
		return tempMap;
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
	 * @return true if the Data contained by FileData contained after adding the key-value-pair.
	 */
	protected boolean insert(final @NotNull String key, final @Nullable Object value) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		this.update();

		String tempData = this.fileData.toString();
		this.fileData.insert(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	protected boolean insertAll(final @NotNull Map<String, Object> map) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(map, "Map must not be null");
		this.update();

		String tempData = this.fileData.toString();
		for (String key : map.keySet()) {
			this.fileData.insert(key, map.get(key));
		}
		return !this.fileData.toString().equals(tempData);
	}

	protected boolean insertAll(final @NotNull String key, final @NotNull Map<String, Object> map) {
		de.zeanon.storage.internal.utils.basic.Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(map, "Map must not be null");
		this.update();

		String tempData = this.fileData.toString();
		for (String tempKey : map.keySet()) {
			this.fileData.insert(key + "." + tempKey, map.get(tempKey));
		}
		return !this.fileData.toString().equals(tempData);
	}

	/**
	 * Creates an empty file.
	 *
	 * @return true if File was created.
	 */
	protected synchronized boolean create() {
		if (this.file.exists()) {
			return false;
		} else {
			SMFileUtils.createFile(this.file);
			return true;
		}
	}

	@Override
	public int compareTo(final @NotNull FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}


	protected static class LocalFileData extends FileData {

		public LocalFileData(final @Nullable Map<String, Object> map) {
			super(map);
		}

		public LocalFileData(final @NotNull JSONObject jsonObject) {
			super(jsonObject);
		}
	}
}