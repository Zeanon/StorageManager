package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.interfaces.*;
import de.zeanon.storage.internal.base.lists.LinkedDataList;
import de.zeanon.storage.internal.data.cache.FileData;
import de.zeanon.storage.internal.data.section.ThunderFileSection;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 * @param dataType       the DataType to be used with this instance
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.THUNDER, reloadSetting, commentSetting, dataType, new LocalFileData());

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
			ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@NotNull
	@Override
	public List<String> keyList() {
		this.update();
		return this.keyList(this.getFileData().getDataList());
	}

	@NotNull
	@Override
	public List<String[]> keyListUseArray() {
		this.update();
		return this.keyListUseArray(this.getFileData().getDataList());
	}

	@NotNull
	@Override
	public List<String> keyList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		@Nullable Object tempMap = this.getFileData().get(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof DataList ? this.keyList((DataList) tempMap) : null, "File does not contain '" + key + "'");
	}

	@NotNull
	@Override
	public List<String[]> keyListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		@Nullable Object tempMap = this.getFileData().getUseArray(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof DataList ? this.keyListUseArray((DataList) tempMap) : null, "File does not contain '" + Arrays.toString(key) + "'");
	}

	@NotNull
	@Override
	public List<String> blockKeyList() {
		this.update();
		return this.blockKeyList(this.getFileData().getDataList());
	}

	@NotNull
	@Override
	public List<String> blockKeyList(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		@Nullable Object tempMap = this.getFileData().get(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof DataList ? this.blockKeyList((DataList) tempMap) : null, "File does not contain '" + key + "'");
	}

	@NotNull
	@Override
	public List<String> blockKeyListUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		@Nullable Object tempMap = this.getFileData().getUseArray(key);
		//noinspection unchecked
		return Objects.notNull(tempMap instanceof DataList ? this.blockKeyList((DataList) tempMap) : null, "File does not contain '" + Arrays.toString(key) + "'");
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
	private List<String> keyList(@NotNull final DataList<DataList.Entry<String, Object>> list) {
		final @NotNull List<String> tempList = list instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : list) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				for (String tempKey : this.keyList((DataList<DataList.Entry<String, Object>>) entry.getValue())) {
					tempList.add(entry.getKey() + "." + tempKey);
				}
			} else if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempList.add(entry.getKey());
			}
		}
		return tempList;
	}

	@NotNull
	private List<String[]> keyListUseArray(@NotNull final DataList<DataList.Entry<String, Object>> list) {
		final @NotNull List<String[]> tempList = list instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : list) {
			if (entry.getValue() instanceof DataList) {
				//noinspection unchecked
				for (String[] tempKey : this.keyListUseArray((DataList<DataList.Entry<String, Object>>) entry.getValue())) {
					@NotNull String[] key = new String[1 + tempKey.length];
					key[0] = entry.getKey();
					System.arraycopy(tempKey, 0, key, 1, tempKey.length);
					tempList.add(key);
				}
			} else if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempList.add(new String[]{
						entry.getKey()
				});
			}
		}
		return tempList;
	}

	@NotNull
	private List<String> blockKeyList(@NotNull final DataList<DataList.Entry<String, Object>> list) {
		final @NotNull List<String> tempList = list instanceof LinkedDataList ? new LinkedList<>() : new ArrayList<>();
		for (DataList.Entry<String, Object> entry : list) {
			if (entry.getValue() != ThunderEditor.LineType.COMMENT && entry.getValue() != ThunderEditor.LineType.BLANK_LINE) {
				tempList.add(entry.getKey());
			}
		}
		return tempList;
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

	private static class LocalFileData extends FileData {

		private LocalFileData() {
			super();
		}
	}
}