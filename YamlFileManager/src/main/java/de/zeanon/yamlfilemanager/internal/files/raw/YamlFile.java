package de.zeanon.yamlfilemanager.internal.files.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import de.zeanon.storagemanagercore.external.browniescollections.BigList;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.base.files.CommentEnabledFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.yamlfilemanager.internal.files.section.YamlFileSection;
import de.zeanon.yamlfilemanager.internal.utility.parser.YamlFileParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Yaml-Type Files
 *
 * @author Zeanon
 * @version 1.3.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class YamlFile extends CommentEnabledFile<StandardFileData<Map, Map.Entry, List>, Map, List> { //NOSONAR


	/**
	 * @param file            the File to be used as a backend
	 * @param inputStream     the FileContent to be set on the creation of the File
	 * @param reloadSetting   the ReloadSetting to be used with this instance
	 * @param commentSetting  the CommentSetting to be used with this instance
	 * @param synchronizeData if the saved data should be synchronized
	 * @param map             the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list            the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final @NotNull CommentSetting commentSetting,
					   final boolean synchronizeData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, inputStream, FileType.YAML, new LocalFileData(new CollectionsProvider<>(map, list, synchronizeData)), reloadSetting, commentSetting);
	}

	@Override
	public void save() {
		try {
			YamlFileParser.writeData(this.file(), this.fileData().dataMap(), this.getCommentSetting(), this.collectionsProvider());
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to "
										 + this.getAbsolutePath()
										 + "'",
										 e);
		}
	}

	@Override
	public void setBigList(final boolean bigList) {
		this.collectionsProvider().setListType(bigList ? BigList.class : GapList.class);
	}

	@Override
	public void setConcurrentData(final boolean concurrentData) {
		this.collectionsProvider().setMapType(concurrentData ? ConcurrentHashMap.class : HashMap.class);
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull YamlFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this, this.fileData());
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this, this.fileData());
	}

	@Override
	public @NotNull YamlFileSection getOrCreateSection(final @NotNull String sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull YamlFileSection getOrCreateSectionUseArray(final @NotNull String... sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull YamlFileSection createSection(final @NotNull String sectionKey) {
		this.set(sectionKey, this.collectionsProvider().newMap());
		return new LocalSection(sectionKey, this, this.fileData());
	}

	@Override
	public @NotNull YamlFileSection createSectionUseArray(final @NotNull String... sectionKey) {
		this.setUseArray(sectionKey, this.collectionsProvider().newMap());
		return new LocalSection(sectionKey, this, this.fileData());
	}


	@Override
	protected @NotNull Map readFile() {
		try (final @NotNull FileReader tempReader = new FileReader(this.file())) {
			//noinspection unchecked
			return (Map<String, Object>) new YamlReader(tempReader).read();
		} catch (final @NotNull YamlException e) {
			throw new FileParseException("Error while parsing '" + this.file().getAbsolutePath() + "'", e);
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e);
		}
	}


	public enum FileType implements de.zeanon.storagemanagercore.internal.base.interfaces.FileType {


		YAML();


		private final @NotNull String extension = "yml";


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

	private static class LocalSection extends YamlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile, final @NotNull FileData<Map, ?, List> fileData) {
			super(sectionKey, yamlFile, fileData);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull YamlFile yamlFile, final @NotNull FileData<Map, ?, List> fileData) {
			super(sectionKey, yamlFile, fileData);
		}
	}

	private static class LocalFileData extends StandardFileData<Map, Map.Entry, List> { //NOSONAR

		private static final long serialVersionUID = 244477712346401950L;

		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider) { //NOSONAR
			super(collectionsProvider);
		}
	}
}