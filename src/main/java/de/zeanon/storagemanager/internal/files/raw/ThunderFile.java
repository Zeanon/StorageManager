package de.zeanon.storagemanager.internal.files.raw;

import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.datamap.BigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentBigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storagemanager.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.exceptions.ThunderException;
import de.zeanon.storagemanager.internal.base.files.CommentEnabledFile;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.files.section.ThunderFileSection;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.editor.ThunderEditor;
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
@SuppressWarnings({"unused", "rawtypes"})
public class ThunderFile extends CommentEnabledFile<ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List>, DataMap, List> {


	private int bufferSize;
	private boolean autoFlush;
	private boolean concurrentData;
	private boolean bigData;


	/**
	 * @param file             the File to be used as a backend
	 * @param inputStream      the FileContent to be set on the creation of the File
	 * @param reloadSetting    the ReloadSetting to be used with this instance
	 * @param commentSetting   the CommentSetting to be used with this instance
	 * @param bufferSize       the bufferSize to be used with the Reader
	 * @param autoFlush        autoFlush parameter of the PrintWriter
	 * @param concurrentData   if the saved data should be concurrent
	 * @param bigData          if BigDataMap optimized for a huge amount of entries should be used
	 * @param synchronizedData if the saved data should be synchronized
	 * @param map              the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list             the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderFile(final @NotNull File file,
						  final @Nullable InputStream inputStream,
						  final @NotNull ReloadSetting reloadSetting,
						  final @NotNull CommentSetting commentSetting,
						  final int bufferSize,
						  final boolean autoFlush,
						  final boolean concurrentData,
						  final boolean bigData,
						  final boolean synchronizedData,
						  final @NotNull Class<? extends DataMap> map,
						  final @NotNull Class<? extends List> list) {
		super(file, FileType.THUNDERFILE, new LocalFileData(new CollectionsProvider<>(map, list), synchronizedData), reloadSetting, commentSetting);
		this.bufferSize = bufferSize;
		this.autoFlush = autoFlush;
		this.concurrentData = concurrentData;
		this.bigData = bigData;

		BaseFileUtils.writeToFileIfCreated(this.file(), BaseFileUtils.createNewInputStream(inputStream));

		try {
			this.fileData().loadData(ThunderEditor.readData(this.file(), this.collectionsProvider(), this.getCommentSetting(), this.bufferSize));
			this.lastLoaded(System.currentTimeMillis());
		} catch (final @NotNull ThunderException e) {
			throw new FileParseException("Error while parsing '"
										 + this.getAbsolutePath()
										 + "'",
										 e);
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '"
										 + this.getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}

	@Override
	public void save() {
		try {
			ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting(), this.getAutoFlush());
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to "
										 + this.getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}

	public void setBigData(final boolean bigData) {
		this.bigData = bigData;
		this.collectionsProvider().setMapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
																  : (this.bigData ? BigDataMap.class : GapDataMap.class));
	}

	@Override
	public void setBigList(final boolean bigList) {
		this.collectionsProvider().setListType(bigList ? BigList.class : GapList.class);
	}

	@Override
	public void setConcurrentData(final boolean concurrentData) {
		this.concurrentData = concurrentData;
		this.collectionsProvider().setMapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
																  : (this.bigData ? BigDataMap.class : GapDataMap.class));
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	@Override
	protected @NotNull DataMap readFile() {
		try {
			return ThunderEditor.readData(this.file(), this.collectionsProvider(), this.getCommentSetting(), this.bufferSize);
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (final @NotNull ThunderException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e);
		}
	}


	public enum FileType implements de.zeanon.storagemanager.internal.base.interfaces.FileType {


		THUNDERFILE();


		private final @NotNull String extension = "tf";


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

	private static class LocalSection extends ThunderFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
			super(sectionKey, thunderFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile thunderFile) {
			super(sectionKey, thunderFile);
		}
	}

	private static class LocalFileData extends ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> {

		private static final long serialVersionUID = -4787829380861376534L;

		private LocalFileData(final @NotNull CollectionsProvider<DataMap, List> collectionsProvider, final boolean synchronize) {
			super(collectionsProvider, synchronize);
		}
	}
}