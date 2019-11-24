package de.zeanon.storage.internal.files.raw;

import de.zeanon.storage.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.files.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.section.ThunderFileSection;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Thunder-Type Files
 *
 * @author Zeanon
 * @version 2.5.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFile extends CommentEnabledFile<ThunderFileData> {

	@Getter
	@Setter
	private boolean bigMap;


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final boolean bigMap) {
		super(file, ThunderFile.FileType.THUNDER, new LocalFileData(bigMap), reloadSetting, commentSetting);

		this.bigMap = bigMap;

		if (BaseFileUtils.createFile(this.getFile()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.getFile(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getCommentSetting(), this.bigMap));
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
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getCommentSetting(), this.bigMap));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@NotNull
	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

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

	private static class LocalFileData extends ThunderFileData {

		private LocalFileData(final boolean bigMap) {
			super(bigMap);
		}
	}
}