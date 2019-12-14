package de.zeanon.storage.internal.files.raw;

import de.zeanon.storage.external.browniescollections.BigList;
import de.zeanon.storage.external.browniescollections.GapList;
import de.zeanon.storage.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storage.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.section.JsonFileSection;
import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.datafiles.JsonUtils;
import de.zeanon.storage.internal.utility.locks.ExtendedFileLock;
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
@SuppressWarnings("unused")
public class JsonFile extends FlatFile<StandardFileData<Map, Map.Entry<String, Object>, List>, Map, List> {


	/**
	 * @param file             the File to be used as a backend
	 * @param inputStream      the FileContent to be set on the creation of the File
	 * @param reloadSetting    the ReloadSetting to be used with this instance
	 * @param synchronizedData if the saved data should be synchronized
	 * @param map              the Map implementation to be used, default is HashMap or ConcurrentHashMap if concurrent
	 * @param list             the List implementation to be used, default ist GapList
	 *
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 * @throws RuntimeIOException if the File can not be accessed properly
	 */
	protected JsonFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final boolean synchronizedData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, FileType.JSON, new LocalFileData(new CollectionsProvider<>(map, list), synchronizedData), reloadSetting);

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
	public @Nullable Map getMap(final @NotNull String key) {
		this.update();

		if (!this.hasKey(key)) {
			return this.collectionsProvider().newMap();
		}

		final @Nullable Object map;
		try {
			map = this.get(key);
		} catch (final @NotNull JSONException e) {
			return this.collectionsProvider().newMap();
		}

		if (map instanceof Map) {
			return (Map<?, ?>) this.fileData().get(key);
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map, this.collectionsProvider());
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
	public @Nullable Map getMapUseArray(final @NotNull String... key) {
		this.update();

		if (!this.hasKeyUseArray(key)) {
			return this.collectionsProvider().newMap();
		}

		@Nullable final Object map;
		try {
			map = this.getUseArray(key);
		} catch (final @NotNull JSONException e) {
			return this.collectionsProvider().newMap();
		}

		if (map instanceof Map) {
			return (Map<?, ?>) this.fileData().getUseArray(key);
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map, this.collectionsProvider());
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
	public void bigList(final boolean bigList) {
		this.collectionsProvider().setListType(bigList ? BigList.class : GapList.class);
	}

	@Override
	public void concurrentData(final boolean concurrentData) {
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


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {


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

	private static class LocalFileData extends StandardFileData<Map, Map.Entry<String, Object>, List> {

		private static final long serialVersionUID = -3736783796296434140L;

		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider, final boolean synchronize) {
			super(collectionsProvider, synchronize);
		}
	}
}