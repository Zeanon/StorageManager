package de.zeanon.tomlfilemanager.internal.files.raw;

import com.electronwill.toml.Toml;
import com.electronwill.toml.TomlException;
import de.zeanon.storagemanagercore.external.browniescollections.BigList;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.tomlfilemanager.internal.files.section.TomlFileSection;
import java.io.File;
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
 * Class to manage Toml-Type Files
 *
 * @author Zeanon
 * @version 1.2.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class TomlFile extends FlatFile<StandardFileData<Map, Map.Entry<String, Object>, List>, Map, List> { //NOSONAR


	/**
	 * @param file            the File to be used as a backend
	 * @param inputStream     the FileContent to be set on the creation of the File
	 * @param reloadSetting   the ReloadSetting to be used with this instance
	 * @param synchronizeData if the saved data should be synchronized
	 * @param map             the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list            the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected TomlFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final boolean synchronizeData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, inputStream, FileType.TOML, new LocalFileData(new CollectionsProvider<>(map, list, synchronizeData)), reloadSetting, true);
	}

	@Override
	public void save() {
		try {
			//noinspection unchecked
			Toml.write(this.fileData().dataMap(), this.file());
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.file().getAbsolutePath() + "'", e);
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
	public @NotNull TomlFileSection getSection(final @NotNull String sectionKey) {
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
	public @NotNull TomlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this, this.fileData());
	}

	@Override
	public @NotNull TomlFileSection getOrCreateSection(final @NotNull String sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull TomlFileSection getOrCreateSectionUseArray(final @NotNull String... sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull TomlFileSection createSection(final @NotNull String sectionKey) {
		this.set(sectionKey, this.collectionsProvider().newMap());
		return new LocalSection(sectionKey, this, this.fileData());
	}

	@Override
	public @NotNull TomlFileSection createSectionUseArray(final @NotNull String... sectionKey) {
		this.setUseArray(sectionKey, this.collectionsProvider().newMap());
		return new LocalSection(sectionKey, this, this.fileData());
	}


	@Override
	protected @NotNull Map<String, Object> readFile() {
		try {
			return Toml.read(this.file());
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e);
		} catch (final @NotNull TomlException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e);
		}
	}


	public enum FileType implements de.zeanon.storagemanagercore.internal.base.interfaces.FileType {


		TOML();


		private final @NotNull String extension = "toml";


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

	private static class LocalSection extends TomlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile, final @NotNull FileData<Map, ?, List> fileData) {
			super(sectionKey, tomlFile, fileData);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull TomlFile tomlFile, final @NotNull FileData<Map, ?, List> fileData) {
			super(sectionKey, tomlFile, fileData);
		}
	}

	private static class LocalFileData extends StandardFileData<Map, Map.Entry<String, Object>, List> { //NOSONAR

		private static final long serialVersionUID = -9141241112197467155L;

		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider) { //NOSONAR
			super(collectionsProvider);
		}
	}
}