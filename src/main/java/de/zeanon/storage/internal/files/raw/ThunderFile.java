package de.zeanon.storage.internal.files.raw;

import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.BigTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.GapTripletMap;
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
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Thunder-Type Files
 *
 * @author Zeanon
 * @version 2.5.0
 */
@Getter
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFile extends CommentEnabledFile<ThunderFileData<TripletMap, List>, TripletMap, List> {


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final @NotNull Class<? extends TripletMap> map, final @NotNull Class<? extends List> list) {
		super(file, ThunderFile.FileType.THUNDER, new LocalFileData(new Collections(map, list)), reloadSetting, commentSetting);

		if (BaseFileUtils.createFile(this.file()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.fileData().loadData(ThunderEditor.readData(this.file(), this.getCommentSetting(), this.provider()));
			this.lastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	@Synchronized
	public void reload() {
		try {
			this.fileData().loadData(ThunderEditor.readData(this.file(), this.getCommentSetting(), this.provider()));
			this.lastLoaded(System.currentTimeMillis());
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
			ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	public void bigMap(final boolean bigMap) {
		this.provider().setMapType(bigMap ? BigTripletMap.class : GapTripletMap.class);
	}

	@Override
	public void bigList(final boolean bigList) {
		this.provider().setListType(bigList ? BigList.class : GapList.class);
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


		private final @NotNull String extension;

		@Contract(pure = true)
		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Contract(pure = true)
		@Override
		public @NotNull String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Contract(pure = true)
		@Override
		public @NotNull String toString() {
			return this.extension;
		}
	}

	public static class Collections extends Provider<TripletMap, List> {

		private Collections(Class<? extends TripletMap> map, Class<? extends List> list) {
			super(map, list);
		}

		@Override
		public @NotNull TripletMap<String, Object> newMap() {
			//noinspection unchecked
			return (TripletMap<String, Object>) super.newMap();
		}

		@Override
		public @NotNull List<String> newList() {
			//noinspection unchecked
			return (List<String>) super.newList();
		}

		@Override
		public @NotNull TripletMap<String, Object> newMap(Class<?>[] parameterTypes, Object... parameters) {
			//noinspection unchecked
			return (TripletMap<String, Object>) super.newMap(parameterTypes, parameters);
		}

		@Override
		public @NotNull List<String> newList(Class<?>[] parameterTypes, Object... parameters) {
			//noinspection unchecked
			return (List<String>) super.newList(parameterTypes, parameters);
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

	private static class LocalFileData extends ThunderFileData<TripletMap, List> {

		private LocalFileData(final @NotNull Provider<TripletMap, List> provider) {
			super(provider);
		}
	}
}