package de.zeanon.storage.internal.files.raw;

import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.section.JsonFileSection;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import de.zeanon.storage.internal.utility.utils.datafiles.JsonUtils;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.Accessors;
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
@Getter
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class JsonFile extends FlatFile<StandardFileData<Map, List>, Map, List> {


	/**
	 * @param file          the File to be used as a backend
	 * @param inputStream   the FileContent to be set on the creation of the File
	 * @param reloadSetting the ReloadSetting to be used with this instance
	 *
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 * @throws RuntimeIOException if the File can not be accessed properly
	 */
	protected JsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull Class<? extends Map> map, final @NotNull Class<? extends List> list) {
		super(file, FileType.JSON, new LocalFileData(new Collections(map, list)), reloadSetting);

		if (BaseFileUtils.createFile(this.file()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			final @NotNull JSONTokener jsonTokener = new JSONTokener(BaseFileUtils.createNewInputStreamFromFile(this.file()));
			this.fileData().loadData(new JSONObject(jsonTokener).toMap());
			this.lastLoaded(System.currentTimeMillis());
		} catch (JSONException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	@Synchronized
	public void reload() {
		try {
			final @NotNull JSONTokener jsonTokener = new JSONTokener(BaseFileUtils.createNewInputStreamFromFile(this.file()));
			this.fileData().loadData(new JSONObject(jsonTokener).toMap());
			this.lastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new FileParseException("Error while reloading '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			this.write(new JSONObject(this.fileData().getDataMap()));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.file().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public void bigList(final boolean bigList) {
		this.provider().setListType(bigList ? BigList.class : GapList.class);
	}

	/**
	 * Gets a Map by key, also used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 *
	 * @return Map
	 */
	@NotNull
	@Override
	public Map getMap(final @NotNull String key) {
		if (!this.hasKey(key)) {
			return this.provider().newMap();
		} else {
			return this.getMapWithoutPath(key);
		}
	}

	@NotNull
	@Override
	public Map getMapUseArray(final @NotNull String... key) {
		if (!this.hasKeyUseArray(key)) {
			return this.provider().newMap();
		} else {
			return this.getMapWithoutPath(key);
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
	public JsonFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public JsonFileSection getSectionUseArray(@NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	private Map getMapWithoutPath(final @NotNull String key, final @NotNull Provider<? extends Map, ? extends List> provider) {
		this.update();

		if (!this.hasKey(key)) {
			return this.provider().newMap();
		}

		Object map;
		try {
			map = this.get(key);
		} catch (JSONException e) {
			return this.provider().newMap();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) Objects.notNull(this.fileData().get(key), "The File does not contain '" + key + "'");
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map, provider);
		} else {
			throw new ObjectNullException("The File does not contain: '" + key + "'");
		}
	}

	@NotNull
	private Map getMapWithoutPath(final @NotNull String... key) {
		this.update();

		if (!this.hasKeyUseArray(key)) {
			return this.provider().newMap();
		}

		Object map;
		try {
			map = this.getUseArray(key);
		} catch (JSONException e) {
			return this.provider().newMap();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) Objects.notNull(this.fileData().getUseArray(key), "The File does not contain '" + Arrays.toString(key) + "'");
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map, this.provider());
		} else {
			throw new ObjectNullException("The File does not contain: '" + Arrays.toString(key) + "'");
		}
	}

	private void write(final @NotNull JSONObject object) throws IOException {
		@NotNull @Cleanup Writer writer = new PrintWriter(new FileWriter(this.file().getAbsolutePath()));
		writer.write(object.toString(3));
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

		JSON("json");


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

	private static class Collections extends Provider<Map, List> {

		private Collections(Class<? extends Map> map, Class<? extends List> list) {
			super(map, list);
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

	private static class LocalFileData extends StandardFileData<Map, List> {

		private LocalFileData(final @NotNull Provider<Map, List> provider) {
			super(provider);
		}
	}
}