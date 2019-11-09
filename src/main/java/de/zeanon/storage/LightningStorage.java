package de.zeanon.storage;

import de.zeanon.storage.internal.base.FlatFile;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.config.LightningConfig;
import de.zeanon.storage.internal.data.config.YamlConfig;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.data.raw.LightningFile;
import de.zeanon.storage.internal.data.raw.TomlFile;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.utils.LightningFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public abstract class LightningStorage {

	private final File file;
	private BufferedInputStream inputStream;
	private ReloadSettingBase reloadSetting;

	LightningStorage(final @NotNull File file) {
		this.file = file;
	}

	public static JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(file);
	}

	public static JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(file.toFile());
	}

	public static JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(name));
	}

	public static JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name));
	}

	public static JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name));
	}

	public static JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory.toFile(), name));
	}


	public static LightningFileBuilder lightningFile(final @NotNull File file) {
		return new LightningFileBuilder(file);
	}

	public static LightningFileBuilder lightningFile(final @NotNull Path file) {
		return new LightningFileBuilder(file.toFile());
	}

	public static LightningFileBuilder lightningFile(final @NotNull String name) {
		return new LightningFileBuilder(new File(name));
	}

	public static LightningFileBuilder lightningFile(final @NotNull String directory, final @NotNull String name) {
		return new LightningFileBuilder(new File(directory, name));
	}

	public static LightningFileBuilder lightningFile(final @NotNull File directory, final @NotNull String name) {
		return new LightningFileBuilder(new File(directory, name));
	}

	public static LightningFileBuilder lightningFile(final @NotNull Path directory, final @NotNull String name) {
		return new LightningFileBuilder(new File(directory.toFile(), name));
	}


	public static LightningConfigBuilder lightningConfig(final @NotNull File file) {
		return new LightningConfigBuilder(file);
	}

	public static LightningConfigBuilder lightningConfig(final @NotNull Path file) {
		return new LightningConfigBuilder(file.toFile());
	}

	public static LightningConfigBuilder lightningConfig(final @NotNull String name) {
		return new LightningConfigBuilder(new File(name));
	}

	public static LightningConfigBuilder lightningConfig(final @NotNull String directory, final @NotNull String name) {
		return new LightningConfigBuilder(new File(directory, name));
	}

	public static LightningConfigBuilder lightningConfig(final @NotNull File directory, final @NotNull String name) {
		return new LightningConfigBuilder(new File(directory, name));
	}

	public static LightningConfigBuilder lightningConfig(final @NotNull Path directory, final @NotNull String name) {
		return new LightningConfigBuilder(new File(directory.toFile(), name));
	}


	public static TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(file);
	}

	public static TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(file.toFile());
	}

	public static TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(name));
	}

	public static TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name));
	}

	public static TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name));
	}

	public static TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory.toFile(), name));
	}


	public static YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(file);
	}

	public static YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(file.toFile());
	}

	public static YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(name));
	}

	public static YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name));
	}

	public static YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name));
	}

	public static YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory.toFile(), name));
	}

	public static YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(file);
	}


	public static YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(file.toFile());
	}

	public static YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(name));
	}

	public static YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name));
	}

	public static YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name));
	}

	public static YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory.toFile(), name));
	}


	public abstract LightningStorage fromInputStream(final @Nullable BufferedInputStream inputStream);

	public abstract LightningStorage fromFile(final @Nullable File file);

	public abstract LightningStorage fromFile(final @Nullable Path file);

	public abstract LightningStorage fromFile(final @Nullable String directory, final @Nullable String name);

	public abstract LightningStorage fromFile(final @Nullable File directory, final @Nullable String name);

	public abstract LightningStorage fromFile(final @Nullable Path directory, final @Nullable String name);

	public abstract LightningStorage fromResource(final @Nullable String resource);

	public abstract LightningStorage reloadSetting(final @Nullable ReloadSettingBase reloadSetting);

	public abstract FlatFile create();

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param inputStream the Data to be imported.
	 */
	private void baseFromInputStream(final @Nullable BufferedInputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param file the File to be imported baseFrom.
	 */
	private void baseFromFile(final @Nullable File file) {
		this.inputStream = file == null ? null : LightningFileUtils.createNewInputStream(file);
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param file the File to be imported baseFrom.
	 */
	private void baseFromFile(final @Nullable Path file) {
		this.inputStream = file == null ? null : LightningFileUtils.createNewInputStream(file.toFile());
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported baseFrom.
	 * @param name      the name of the File to be imported baseFrom.
	 */
	private void baseFromFile(final @Nullable String directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = LightningFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported baseFrom.
	 * @param name      the name of the File to be imported baseFrom.
	 */
	private void baseFromFile(final @Nullable File directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = LightningFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported baseFrom.
	 * @param name      the name of the File to be imported baseFrom.
	 */
	private void baseFromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = LightningFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory.toFile(), name));
		}
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param resource the internal resource to be imported baseFrom.
	 */
	private void baseFromResource(final @Nullable String resource) {
		this.inputStream = resource == null ? null : LightningFileUtils.createNewInputStream(resource);
	}

	/**
	 * Set the ReloadSetting for the File.
	 *
	 * @param reloadSetting the ReloadSetting to be set(default is INTELLIGENT)
	 */
	private void baseReloadSetting(final @Nullable ReloadSettingBase reloadSetting) {
		this.reloadSetting = reloadSetting;
	}

	public static final class JsonFileBuilder extends LightningStorage {

		private JsonFileBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final JsonFileBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final JsonFileBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final JsonFileBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final JsonFileBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final JsonFileBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final JsonFileBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final JsonFileBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final JsonFileBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}


		@Override
		public final JsonFile create() {
			return new LocalJsonFile(super.file, super.inputStream, super.reloadSetting);
		}


		private static final class LocalJsonFile extends JsonFile {

			private LocalJsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting) {
				super(file, inputStream, reloadSetting);
			}
		}
	}

	public static final class LightningFileBuilder extends LightningStorage {

		private CommentSettingBase commentSetting;
		private DataTypeBase dataType;


		private LightningFileBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final LightningFileBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final LightningFileBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final LightningFileBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final LightningFileBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningFileBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningFileBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningFileBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final LightningFileBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}

		/**
		 * Set the CommentSetting for the File.
		 *
		 * @param commentSetting the CommentSetting to be set(Default for Configs is true, otherwise it's false)
		 */
		public final LightningFileBuilder commentSetting(final CommentSettingBase commentSetting) {
			this.commentSetting = commentSetting;
			return this;
		}

		/**
		 * Set the way the Data is stored.
		 *
		 * @param dataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
		 */
		public final LightningFileBuilder dataType(final @Nullable DataTypeBase dataType) {
			this.dataType = dataType;
			return this;
		}


		@Override
		public final LightningFile create() {
			return new LocalLightningFile(super.file, super.inputStream, super.reloadSetting, this.commentSetting, this.dataType);
		}


		private static final class LocalLightningFile extends LightningFile {

			private LocalLightningFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
				super(file, inputStream, reloadSetting, commentSetting, dataType);
			}
		}
	}

	public static final class LightningConfigBuilder extends LightningStorage {

		private CommentSettingBase commentSetting;
		private DataTypeBase dataType;


		private LightningConfigBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final LightningConfigBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final LightningConfigBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final LightningConfigBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}

		/**
		 * Set the CommentSetting for the File.
		 *
		 * @param commentSetting the CommentSetting to be set(Default for Configs is true, otherwise it's false)
		 */
		public final LightningConfigBuilder commentSetting(final CommentSettingBase commentSetting) {
			this.commentSetting = commentSetting;
			return this;
		}

		/**
		 * Set the way the Data is stored.
		 *
		 * @param dataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
		 */
		public final LightningConfigBuilder dataType(final @Nullable DataTypeBase dataType) {
			this.dataType = dataType;
			return this;
		}


		@Override
		public final LightningConfig create() {
			return new LocalLightningConfig(super.file, super.inputStream, super.reloadSetting, this.commentSetting, this.dataType);
		}


		private static final class LocalLightningConfig extends LightningConfig {

			private LocalLightningConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
				super(file, inputStream, reloadSetting, commentSetting, dataType);
			}
		}
	}

	public static final class TomlFileBuilder extends LightningStorage {

		private TomlFileBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final TomlFileBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final TomlFileBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final TomlFileBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final TomlFileBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final TomlFileBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final TomlFileBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final TomlFileBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final TomlFileBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}


		@Override
		public final FlatFile create() {
			return new LocalTomlFile(super.file, super.inputStream, super.reloadSetting);
		}


		private static final class LocalTomlFile extends TomlFile {

			private LocalTomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting) {
				super(file, inputStream, reloadSetting);
			}
		}
	}

	public static final class YamlFileBuilder extends LightningStorage {

		private CommentSettingBase commentSetting;
		private DataTypeBase dataType;


		private YamlFileBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final YamlFileBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final YamlFileBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final YamlFileBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final YamlFileBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlFileBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlFileBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlFileBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final YamlFileBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}

		/**
		 * Set the CommentSetting for the File.
		 *
		 * @param commentSetting the CommentSetting to be set(Default for Configs is true, otherwise it's false)
		 */
		public final YamlFileBuilder commentSetting(final CommentSettingBase commentSetting) {
			this.commentSetting = commentSetting;
			return this;
		}

		/**
		 * Set the way the Data is stored.
		 *
		 * @param dataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
		 */
		public final YamlFileBuilder dataType(final @Nullable DataTypeBase dataType) {
			this.dataType = dataType;
			return this;
		}


		@Override
		public final YamlFile create() {
			return new LocalYamlFile(super.file, super.inputStream, super.reloadSetting, this.commentSetting, this.dataType);
		}


		private static final class LocalYamlFile extends YamlFile {

			private LocalYamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
				super(file, inputStream, reloadSetting, commentSetting, dataType);
			}
		}
	}

	public static final class YamlConfigBuilder extends LightningStorage {

		private CommentSettingBase commentSetting;
		private DataTypeBase dataType;


		private YamlConfigBuilder(final @NotNull File file) {
			super(file);
		}


		@Override
		public final YamlConfigBuilder fromInputStream(@Nullable BufferedInputStream inputStream) {
			super.baseFromInputStream(inputStream);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromFile(@Nullable File file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromFile(@Nullable Path file) {
			super.baseFromFile(file);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromFile(@Nullable String directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromFile(@Nullable File directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromFile(@Nullable Path directory, @Nullable String name) {
			super.baseFromFile(directory, name);
			return this;
		}

		@Override
		public final YamlConfigBuilder fromResource(@Nullable String resource) {
			super.baseFromResource(resource);
			return this;
		}

		@Override
		public final YamlConfigBuilder reloadSetting(@Nullable ReloadSettingBase reloadSetting) {
			super.baseReloadSetting(reloadSetting);
			return this;
		}

		/**
		 * Set the CommentSetting for the File.
		 *
		 * @param commentSetting the CommentSetting to be set(Default for Configs is true, otherwise it's false)
		 */
		public final YamlConfigBuilder commentSetting(final CommentSettingBase commentSetting) {
			this.commentSetting = commentSetting;
			return this;
		}

		/**
		 * Set the way the Data is stored.
		 *
		 * @param dataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
		 */
		public final YamlConfigBuilder dataType(final @Nullable DataTypeBase dataType) {
			this.dataType = dataType;
			return this;
		}


		@Override
		public final YamlConfig create() {
			return new LocalYamlConfig(super.file, super.inputStream, super.reloadSetting, this.commentSetting, this.dataType);
		}


		private static final class LocalYamlConfig extends YamlConfig {

			private LocalYamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
				super(file, inputStream, reloadSetting, commentSetting, dataType);
			}
		}
	}
}