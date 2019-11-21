package de.zeanon.storage.internal.data.files.raw;

import de.zeanon.storage.internal.basic.exceptions.FileParseException;
import de.zeanon.storage.internal.basic.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.basic.exceptions.ThunderException;
import de.zeanon.storage.internal.basic.interfaces.CommentSetting;
import de.zeanon.storage.internal.basic.interfaces.ReloadSetting;
import de.zeanon.storage.internal.data.base.CommentEnabledFile;
import de.zeanon.storage.internal.data.cache.ThunderFileData;
import de.zeanon.storage.internal.data.cache.datamap.TripletMap;
import de.zeanon.storage.internal.data.files.section.ThunderFileSection;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSetting reloadSetting, final @Nullable CommentSetting commentSetting) {
		super(file, ThunderFile.FileType.THUNDER, reloadSetting, commentSetting, new LocalFileData());

		if (SMFileUtils.createFile(this.getFile()) && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getCommentSetting()));
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
			this.getFileData().loadData(ThunderEditor.readData(this.getFile(), this.getCommentSetting()));
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
	public Map getMap(final @NotNull String key) {
		@NotNull Object tempValue = this.get(key);
		//noinspection unchecked
		return tempValue instanceof TripletMap ? this.parseToMap((TripletMap) tempValue) : (Map) tempValue;
	}

	@NotNull
	@Override
	public Map getMapUseArray(final @NotNull String... key) {
		@NotNull Object tempValue = this.getUseArray(key);
		//noinspection unchecked
		return tempValue instanceof TripletMap ? this.parseToMap((TripletMap) tempValue) : (Map) tempValue;
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
	public ThunderFileSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	@NotNull
	private Map<String, Object> parseToMap(final @NotNull TripletMap<String, Object> map) {
		final @NotNull Map<String, Object> tempMap = new LinkedHashMap<>();
		for (TripletMap.Entry<String, Object> entry : map.entryList()) {
			if (entry.getValue() instanceof TripletMap) {
				//noinspection unchecked
				tempMap.put(entry.getKey(), this.parseToMap((TripletMap) entry.getValue()));
			} else {
				tempMap.put(entry.getKey(), entry.getValue());
			}
		}
		return tempMap;
	}


	public enum FileType implements de.zeanon.storage.internal.basic.interfaces.FileType {

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

		private LocalFileData() {
			super();
		}
	}
}