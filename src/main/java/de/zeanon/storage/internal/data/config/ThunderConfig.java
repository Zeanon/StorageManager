package de.zeanon.storage.internal.data.config;

import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.data.section.ThunderConfigSection;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.ThunderUtils;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended ThunderFile with added methods for Config purposes
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ThunderConfig extends CommentEnabledFile<ThunderConfig> implements ConfigBase<ThunderConfig> {

	protected ThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, ThunderFile.FileType.THUNDER, reloadSetting, commentSetting, dataType);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		this.setFileData(new LocalFileData(ThunderEditor.readData(this.getFile(), this.getDataType(), this.getCommentSetting())));
		this.setLastLoaded(System.currentTimeMillis());
	}


	@Override
	public List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.getFileData(), this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public ThunderConfig setHeader(final @Nullable List<String> header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setHeader(this.getFileData(), header, this.getDataType(), this.getCommentSetting());
			if (!this.getFileData().toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.getFile(), tempMap, this.getCommentSetting());
			}
		}
		return this;
	}

	public List<String> getHeader(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.getFileData(), key, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public ThunderConfig setHeader(final @NotNull String key, final @Nullable String... header) {
		this.setHeader(key, header == null ? null : Arrays.asList(header));
		return this;
	}

	public ThunderConfig setHeader(final @NotNull String key, final @Nullable List<String> header) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setHeader(this.getFileData(), key, header, this.getDataType(), this.getCommentSetting());
			if (!this.getFileData().toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.getFile(), tempMap, this.getCommentSetting());
			}
		}
		return this;
	}


	@Override
	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.getFileData(), this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public ThunderConfig setFooter(final @Nullable List<String> footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setFooter(this.getFileData(), footer, this.getDataType(), this.getCommentSetting());
			if (!this.getFileData().toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.getFile(), tempMap, this.getCommentSetting());
			}
		}
		return this;
	}

	public List<String> getFooter(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.getFileData(), key, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public ThunderConfig setFooter(final @NotNull String key, final @Nullable String... footer) {
		this.setFooter(key, footer == null ? null : Arrays.asList(footer));
		return this;
	}

	public ThunderConfig setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setFooter(this.getFileData(), key, footer, this.getDataType(), this.getCommentSetting());
			if (!this.getFileData().toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.getFile(), tempMap, this.getCommentSetting());
			}
		}
		return this;
	}


	@Override
	public List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), key, this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), this.getDataType(), this.getCommentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), key, this.getDataType(), this.getCommentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@Override
	public ThunderConfig reload() {
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
	public synchronized ThunderConfig set(final @NotNull String key, final @Nullable Object value) {
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
	public synchronized ThunderConfig setAll(final @NotNull Map<String, Object> dataMap) {
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
	public synchronized ThunderConfig setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
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
	public synchronized ThunderConfig remove(final @NotNull String key) {
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
	public synchronized ThunderConfig removeAll(final @NotNull List<String> keys) {
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
	public synchronized ThunderConfig removeAll(final @NotNull String key, final @NotNull List<String> keys) {
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


	private static class LocalSection extends ThunderConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}
	}
}