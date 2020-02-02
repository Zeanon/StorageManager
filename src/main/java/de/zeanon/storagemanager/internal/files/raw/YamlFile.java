package de.zeanon.storagemanager.internal.files.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.files.CommentEnabledFile;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.files.section.YamlFileSection;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.editor.YamlEditor;
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
public class YamlFile extends CommentEnabledFile<StandardFileData<Map, Map.Entry<String, Object>, List>, Map, List> {


	/**
	 * @param file             the File to be used as a backend
	 * @param inputStream      the FileContent to be set on the creation of the File
	 * @param reloadSetting    the ReloadSetting to be used with this instance
	 * @param commentSetting   the CommentSetting to be used with this instance
	 * @param synchronizedData if the saved data should be synchronized
	 * @param map              the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list             the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final @NotNull CommentSetting commentSetting,
					   final boolean synchronizedData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, FileType.YAML, new LocalFileData(new CollectionsProvider<>(map, list), synchronizedData), reloadSetting, commentSetting);

		BaseFileUtils.writeToFileIfCreated(this.file(), BaseFileUtils.createNewInputStream(inputStream));

		try {
			//noinspection unchecked
			this.fileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.file())).read());
			this.lastLoaded(System.currentTimeMillis());
		} catch (final @NotNull YamlException e) {
			throw new FileParseException("Error while parsing '"
										 + this.file().getAbsolutePath()
										 + "'",
										 e);
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while loading '"
										 + this.file().getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}

	@Override
	public void save() {
		try {
			YamlEditor.writeData(this.file(), this.fileData().dataMap(), this.getCommentSetting(), this.collectionsProvider());
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while writing to "
										 + this.getAbsolutePath()
										 + "'",
										 e.getCause());
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
	public @NotNull YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
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


	public enum FileType implements de.zeanon.storagemanager.internal.base.interfaces.FileType {


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

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}
	}

	private static class LocalFileData extends StandardFileData<Map, Map.Entry<String, Object>, List> {

		private static final long serialVersionUID = 244477712346401950L;

		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider, final boolean synchronize) {
			super(collectionsProvider, synchronize);
		}
	}
}