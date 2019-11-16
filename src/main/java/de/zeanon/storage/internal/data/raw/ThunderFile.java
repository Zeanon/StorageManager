package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.section.ThunderFileSection;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Thunder-Type Files
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFile extends CommentEnabledFile {


	/**
	 * @param file           the File to be used as a backend.
	 * @param inputStream    the FileContent to be set on the creation of the File.
	 * @param reloadSetting  the ReloadSetting to be used with this instance.
	 * @param commentSetting the CommentSetting to be used with this instance.
	 * @param dataType       the DataType to be used with this instance.
	 * @throws RuntimeIOException if the File can not be accessed properly.
	 * @throws FileParseException if the Content of the File can not be parsed properly.
	 */
	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.THUNDER, reloadSetting, commentSetting, dataType);

		if (SMFileUtils.createFile(this.getFile()) && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	public void reload() {
		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public synchronized void save() {
		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@NotNull
	@Override
	public Set<String> keySet() {
		this.update();
		return this.keySet(this.getFileData().toMap());
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray() {
		this.update();
		return this.keySetUseArray(this.getFileData().toMap());
	}

	@NotNull
	@Override
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		Object tempMap = this.getFileData().get(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof Map ? this.keySet((Map<String, Object>) tempMap) : null, "File does not contain '" + key + "'");
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		Object tempMap = this.getFileData().getUseArray(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof Map ? this.keySetUseArray((Map<String, Object>) tempMap) : null, "File does not contain '" + Arrays.toString(key) + "'");
	}

	@NotNull
	@Override
	public Set<String> blockKeySet() {
		this.update();
		return this.blockKeySet(this.getFileData().toMap());
	}

	@NotNull
	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		Object tempMap = this.getFileData().get(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof Map ? this.blockKeySet((Map<String, Object>) tempMap) : null, "File does not contain '" + key + "'");
	}

	@NotNull
	@Override
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		Object tempMap = this.getFileData().getUseArray(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof Map ? this.blockKeySet((Map<String, Object>) tempMap) : null, "File does not contain '" + Arrays.toString(key) + "'");
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@NotNull
	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public ThunderFileSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	private Set<String> keySet(@NotNull final Map<String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<String, Object>) entry.getValue())) {
					tempSet.add(entry.getKey() + "." + tempKey);
				}
			} else if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String[]> keySetUseArray(@NotNull final Map<String, Object> map) {
		Set<String[]> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				//noinspection unchecked
				for (String[] tempKey : this.keySetUseArray((Map<String, Object>) entry.getValue())) {
					String[] key = new String[1 + tempKey.length];
					key[0] = entry.getKey();
					System.arraycopy(tempKey, 0, key, 1, tempKey.length);
					tempSet.add(key);
				}
			} else if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(new String[]{
						entry.getKey()
				});
			}
		}
		return tempSet;
	}

	@NotNull
	private Set<String> blockKeySet(@NotNull final Map<String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}


	public enum FileType implements FileTypeBase {

		THUNDER("tf");


		@NotNull
		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@NotNull
		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@NotNull
		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends ThunderFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
			super(sectionKey, thunderFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile thunderFile) {
			super(sectionKey, thunderFile);
		}
	}
}