package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.CommentEnabledFile;
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
public class ThunderFile extends CommentEnabledFile<ThunderFile> {

	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.THUNDER, reloadSetting, commentSetting, dataType);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		this.setFileData(new LocalFileData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting())));
		this.setLastLoaded(System.currentTimeMillis());
	}


	@Override
	public ThunderFile reload() {
		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.err.println("Exception while reloading '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public synchronized ThunderFile set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
		return this;
	}

	@Override
	public synchronized ThunderFile setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
		return this;
	}

	@Override
	public synchronized ThunderFile setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			try {
				ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
		return this;
	}

	@Override
	public synchronized ThunderFile remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		this.getFileData().remove(key);

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public synchronized ThunderFile removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String key : keys) {
			this.getFileData().remove(key);
		}

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@SuppressWarnings("DuplicatedCode")
	@Override
	public synchronized ThunderFile removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String tempKey : keys) {
			this.getFileData().remove(key + "." + tempKey);
		}

		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData().toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
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
		for (String key : map.keySet()) {
			if (map.get(key) != ThunderEditor.LineType.COMMENT && map.get(key) != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(key);
			}
		}
		return tempSet;
	}

	@SuppressWarnings("DuplicatedCode")
	private Set<String> keySet(final Map<String, Object> map) {
		Set<String> tempSet = new HashSet<>();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<String, Object>) map.get(key))) {
					tempSet.add(key + "." + tempKey);
				}
			} else if (map.get(key) != ThunderEditor.LineType.COMMENT && map.get(key) != ThunderEditor.LineType.BLANK_LINE) {
				tempSet.add(key);
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