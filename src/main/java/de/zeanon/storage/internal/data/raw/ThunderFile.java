package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.ObjectIsNull;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
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
import java.util.HashSet;
import java.util.List;
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

	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.THUNDER, reloadSetting, commentSetting, dataType);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException | FileParseException e) {
			throw new FileParseException("Error while loading '" + this.getAbsolutePath() + "'", e);
		}
	}


	@Override
	public void reload() {
		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (ObjectIsNull | RuntimeIOException | FileParseException e) {
			throw new FileParseException("Error while reloading '" + this.getAbsolutePath() + "'", e);
		}
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (ObjectIsNull | RuntimeIOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (ObjectIsNull | RuntimeIOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (ObjectIsNull | RuntimeIOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
			}
		}
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		this.getFileData().remove(key);

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (ObjectIsNull | RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String key : keys) {
			this.getFileData().remove(key);
		}

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (ObjectIsNull | RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String tempKey : keys) {
			this.getFileData().remove(key + "." + tempKey);
		}

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (ObjectIsNull | RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e);
		}
	}

	@Override
	public Set<String> keySet() {
		this.update();
		return this.keySet(this.getFileData().toMap());
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		//noinspection unchecked
		return this.getFileData().get(key) instanceof Map ? this.keySet((Map<String, Object>) this.getFileData().get(key)) : null;
	}

	@Override
	public Set<String> blockKeySet() {
		this.update();
		return this.blockKeySet(this.getFileData().toMap());
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		//noinspection unchecked
		return this.getFileData().get(key) instanceof Map ? this.blockKeySet((Map<String, Object>) this.getFileData().get(key)) : null;
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	private Set<String> blockKeySet(final Map<String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(entry.getKey());
			}
		}
		return tempSet;
	}

	private Set<String> keySet(final Map<String, Object> map) {
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


	public enum FileType implements FileTypeBase {

		THUNDER("tf");


		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends ThunderFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
			super(sectionKey, thunderFile);
		}
	}
}