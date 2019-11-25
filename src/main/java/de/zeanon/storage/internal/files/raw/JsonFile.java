package de.zeanon.storage.internal.files.raw;

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
import java.util.HashMap;
import java.util.Map;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;
import lombok.ToString;
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
public class JsonFile extends FlatFile<StandardFileData> {


	/**
	 * @param file          the File to be used as a backend
	 * @param inputStream   the FileContent to be set on the creation of the File
	 * @param reloadSetting the ReloadSetting to be used with this instance
	 *
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 * @throws RuntimeIOException if the File can not be accessed properly
	 */
	protected JsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting) {
		super(file, JsonFile.FileType.JSON, new LocalFileData(), reloadSetting);

		if (BaseFileUtils.createFile(this.getFile()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.getFile(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			final @NotNull JSONTokener jsonTokener = new JSONTokener(BaseFileUtils.createNewInputStream(this.getFile()));
			this.getFileData().loadData(new JSONObject(jsonTokener).toMap());
			this.setLastLoaded(System.currentTimeMillis());
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
			final @NotNull JSONTokener jsonTokener = new JSONTokener(BaseFileUtils.createNewInputStream(this.getFile()));
			this.getFileData().loadData(new JSONObject(jsonTokener).toMap());
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new FileParseException("Error while reloading '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			this.write(new JSONObject(this.getFileData().getDataMap()));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
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
			return new HashMap();
		} else {
			return this.getMapWithoutPath(key);
		}
	}

	@NotNull
	@Override
	public Map getMapUseArray(final @NotNull String... key) {
		if (!this.hasKeyUseArray(key)) {
			return new HashMap();
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
	private Map getMapWithoutPath(final @NotNull String key) {
		this.update();

		if (!this.hasKey(key)) {
			return new HashMap<>();
		}

		Object map;
		try {
			map = this.get(key);
		} catch (JSONException e) {
			return new HashMap<>();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) Objects.notNull(this.getFileData().get(key), "The File does not contain '" + key + "'");
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map);
		} else {
			throw new ObjectNullException("The File does not contain: '" + key + "'");
		}
	}

	@NotNull
	private Map getMapWithoutPath(final @NotNull String... key) {
		this.update();

		if (!this.hasKeyUseArray(key)) {
			return new HashMap<>();
		}

		Object map;
		try {
			map = this.getUseArray(key);
		} catch (JSONException e) {
			return new HashMap<>();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) Objects.notNull(this.getFileData().getUseArray(key), "The File does not contain '" + Arrays.toString(key) + "'");
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map);
		} else {
			throw new ObjectNullException("The File does not contain: '" + Arrays.toString(key) + "'");
		}
	}

	private void write(final @NotNull JSONObject object) throws IOException {
		@NotNull @Cleanup Writer writer = new PrintWriter(new FileWriter(this.getFile().getAbsolutePath()));
		writer.write(object.toString(3));
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

		JSON("json");


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


	private static class LocalSection extends JsonFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}
	}

	private static class LocalFileData extends StandardFileData {

		private LocalFileData() {
			super();
		}
	}
}
