package de.zeanon.storage.internal.files.raw;

import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.cache.datamap.BigDataMap;
import de.zeanon.storage.internal.base.cache.datamap.ConcurrentBigDataMap;
import de.zeanon.storage.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storage.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storage.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.files.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.DataMap;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.section.ThunderFileSection;
import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFile extends CommentEnabledFile<ThunderFileData<DataMap, DataMap.TripletNode<String, Object>, List>, DataMap, List> {


	private int bufferSize;
	private boolean concurrentData;
	private boolean bigData;


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file,
						  final @Nullable InputStream inputStream,
						  final @NotNull ReloadSetting reloadSetting,
						  final @NotNull CommentSetting commentSetting,
						  final int bufferSize,
						  final boolean concurrentData,
						  final boolean bigData,
						  final boolean synchronizedData,
						  final @NotNull Class<? extends DataMap> map,
						  final @NotNull Class<? extends List> list) {
		super(file, FileType.THUNDER, new LocalFileData(new Collections(map, list), synchronizedData), reloadSetting, commentSetting);
		this.bufferSize = bufferSize;
		this.bigData = bigData;
		this.concurrentData = concurrentData;

		BaseFileUtils.writeToFileIfCreated(this.file(), BaseFileUtils.createNewInputStream(inputStream));

		try {
			this.fileData().loadData(ThunderEditor.readData(this.file(), this.provider(), this.getCommentSetting(), this.bufferSize));
			this.lastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e);
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e);
		}
	}

	@Override
	public void save() {
		try {
			ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	public void bigData(final boolean bigData) {
		this.bigData = bigData;
		this.provider().setMapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
													   : (this.bigData ? BigDataMap.class : GapDataMap.class));
	}

	@Override
	public void bigList(final boolean bigList) {
		this.provider().setListType(bigList ? BigList.class : GapList.class);
	}

	@Override
	public void concurrentData(final boolean concurrentData) {
		this.concurrentData = concurrentData;
		this.provider().setMapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
													   : (this.bigData ? BigDataMap.class : GapDataMap.class));
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


	@Override
	protected @NotNull DataMap readFile() {
		try {
			return ThunderEditor.readData(this.file(), this.provider(), this.getCommentSetting(), this.bufferSize);
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e);
		}
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

	private static class Collections extends Provider<DataMap, List> {

		private Collections(final @NotNull Class<? extends DataMap> map, final @NotNull Class<? extends List> list) {
			super(map, list);
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

	private static class LocalFileData extends ThunderFileData<DataMap, DataMap.TripletNode<String, Object>, List> {

		private LocalFileData(final @NotNull Provider<DataMap, List> provider, final boolean synchronize) {
			super(provider, synchronize);
		}
	}
}