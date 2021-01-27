package de.zeanon.storagemanager.internal.files.raw;

import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.files.FlatFile;
import de.zeanon.storagemanager.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.files.section.JsonFileSection;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.datafiles.JsonUtils;
import de.zeanon.storagemanager.internal.utility.filelock.ExtendedFileLock;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Class to manage Json-Type Files
 *
 * @author Zeanon
 * @version 2.0.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class JsonFile extends FlatFile<StandardFileData<Map, Map.Entry<String, Object>, List>, Map, List> { //NOSONAR


	/**
	 * @param file            the File to be used as a backend
	 * @param inputStream     the FileContent to be set on the creation of the File
	 * @param reloadSetting   the ReloadSetting to be used with this instance
	 * @param synchronizeData if the saved data should be synchronized
	 * @param map             the Map implementation to be used, default is HashMap or ConcurrentHashMap if concurrent
	 * @param list            the List implementation to be used, default ist GapList
	 *
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 * @throws RuntimeIOException if the File can not be accessed properly
	 */
	protected JsonFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final boolean synchronizeData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, FileType.JSON, new LocalFileData(new CollectionsProvider<>(map, list), synchronizeData), reloadSetting);

		BaseFileUtils.writeToFileIfCreated(this.file(), BaseFileUtils.createNewInputStream(inputStream));

		try {
			final @NotNull JSONTokener jsonTokener = new JSONTokener(
					BaseFileUtils.createNewInputStreamFromFile(this.file()));
			this.fileData().loadData(new JSONObject(jsonTokener).toMap());
			this.lastLoaded(System.currentTimeMillis());
		} catch (final @NotNull JSONException e) {
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


	/**
	 * Gets a Map by key, also used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 *
	 * @return Map
	 */
	@Override
	public @Nullable <K, V> Map<K, V> getMap(final @NotNull String key) {
		this.update();

		if (!this.hasKey(key)) {
			//noinspection unchecked
			return this.collectionsProvider().newMap();
		}

		final @Nullable Object map;
		try {
			map = this.get(key);
		} catch (final @NotNull JSONException e) {
			//noinspection unchecked
			return this.collectionsProvider().newMap();
		}

		if (map instanceof Map) {
			//noinspection unchecked
			return (Map<K, V>) this.fileData().get(key);
		} else if (map instanceof JSONObject) {
			//noinspection unchecked
			return (Map<K, V>) JsonUtils.jsonToMap((JSONObject) map, this.collectionsProvider());
		} else {
			return null;
		}
	}

	/**
	 * Gets a Map by key, also used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 *
	 * @return Map
	 */
	@Override
	public @Nullable <K, V> Map<K, V> getMapUseArray(final @NotNull String... key) {
		this.update();

		if (!this.hasKeyUseArray(key)) {
			//noinspection unchecked
			return this.collectionsProvider().newMap();
		}

		final @Nullable Object map;
		try {
			map = this.getUseArray(key);
		} catch (final @NotNull JSONException e) {
			//noinspection unchecked
			return this.collectionsProvider().newMap();
		}

		if (map instanceof Map) {
			//noinspection unchecked
			return (Map<K, V>) this.fileData().getUseArray(key);
		} else if (map instanceof JSONObject) {
			//noinspection unchecked
			return (Map<K, V>) JsonUtils.jsonToMap((JSONObject) map, this.collectionsProvider());
		} else {
			return null;
		}
	}

	@Override
	public void save() {
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(this.file()).writeLock();
			 final @NotNull Writer writer = tempLock.createPrintWriter()) {
			tempLock.lock();
			tempLock.truncateChannel(0);
			writer.write(new JSONObject(this.fileData().dataMap()).toString(3));
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while writing to "
										 + this.file().getAbsolutePath()
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
	@NotNull
	@Override
	public JsonFileSection getSection(final @NotNull String sectionKey) {
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
	public @NotNull JsonFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	@Override
	protected @NotNull Map readFile() {
		try {
			return new JSONObject(new JSONTokener(
					BaseFileUtils.createNewInputStreamFromFile(this.file()))).toMap();
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '"
										 + this.getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}


	public enum FileType implements de.zeanon.storagemanager.internal.base.interfaces.FileType {


		JSON();


		private final @NotNull String extension = "json";


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

	private static class LocalSection extends JsonFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}
	}

	private static class LocalFileData extends StandardFileData<Map, Map.Entry<String, Object>, List> { //NOSONAR

		private static final long serialVersionUID = -3736783796296434140L;

		@SuppressWarnings("rawtypes")
		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider, final boolean synchronize) { //NOSONAR
			super(collectionsProvider, synchronize);
		}
	}
}