package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.FlatFile;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.section.JsonFileSection;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.JsonUtils;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Class to manage Json-Type Files
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class JsonFile extends FlatFile {

	protected JsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting) {
		super(file, FileType.JSON, reloadSetting);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			final JSONTokener jsonTokener = new JSONTokener(SMFileUtils.createNewInputStream(this.getFile()));
			this.getFileData().loadData(new JSONObject(jsonTokener));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new FileParseException("Error while loading '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	public void reload() {
		try {
			final JSONTokener jsonTokener = new JSONTokener(SMFileUtils.createNewInputStream(this.getFile()));
			this.getFileData().loadData(new JSONObject(jsonTokener));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (RuntimeIOException e) {
			throw new FileParseException("Error while reloading '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Gets a Map by key, also used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 * @return Map
	 */

	@Override
	public Map getMap(final @NotNull String key) {
		if (!this.hasKey(key)) {
			return new HashMap();
		} else {
			return this.getMapWithoutPath(key);
		}
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			try {
				this.write(new JSONObject(this.getFileData().toMap()));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			try {
				this.write(new JSONObject(this.getFileData().toMap()));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			try {
				this.write(new JSONObject(this.getFileData().toMap()));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		this.getFileData().remove(key);

		try {
			this.write(this.getFileData().toJsonObject());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List  must not be null");

		this.update();

		for (String key : keys) {
			this.getFileData().remove(key);
		}

		try {
			this.write(this.getFileData().toJsonObject());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List  must not be null");

		this.update();

		for (String tempKey : keys) {
			this.getFileData().remove(key + "." + tempKey);
		}

		try {
			this.write(this.getFileData().toJsonObject());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public JsonFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	private Map getMapWithoutPath(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");
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
			return (Map<?, ?>) this.getFileData().get(key);
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map);
		} else {
			throw new FileParseException("Json does not contain: '" + key + "'.");
		}
	}

	private void write(final JSONObject object) throws IOException {
		@Cleanup Writer writer = new PrintWriter(new FileWriter(this.getFile().getAbsolutePath()));
		writer.write(object.toString(3));
	}


	public enum FileType implements FileTypeBase {

		JSON("json");


		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends JsonFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}
	}
}
