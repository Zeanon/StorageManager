package de.zeanon.storage;

import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Reload;
import de.zeanon.storage.internal.files.raw.JsonFile;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.storage.internal.files.raw.TomlFile;
import de.zeanon.storage.internal.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.builder.*;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Builder for StorageManager DataFile Classes
 *
 * @author Zeanon
 * @version 2.0.0
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
public abstract class StorageManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> {


	protected final @NotNull File file;
	protected @NotNull ReloadSetting reloadSetting = Reload.INTELLIGENT;
	protected @Nullable BufferedInputStream inputStream;
	protected @NotNull Class<? extends M> mapType;
	protected @NotNull Class<? extends L> listType;


	@Contract(pure = true)
	protected StorageManager(final @NotNull File file, final @NotNull Class<? extends M> mapType, final @NotNull Class<? extends L> listType) {
		this.file = file;
		this.mapType = mapType;
		this.listType = listType;
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File file) {
		return new LocalJsonFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new LocalJsonFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String name) {
		return new LocalJsonFileBuilder(new File(name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new LocalJsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new LocalJsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new LocalJsonFileBuilder(new File(directory.toFile(), name + "." + JsonFile.FileType.JSON));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new LocalThunderFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new LocalThunderFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new LocalThunderFileBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new LocalThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new LocalThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new LocalThunderFileBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new LocalThunderConfigBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new LocalThunderConfigBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new LocalThunderConfigBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new LocalThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new LocalThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new LocalThunderConfigBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File file) {
		return new LocalTomlFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new LocalTomlFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String name) {
		return new LocalTomlFileBuilder(new File(name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new LocalTomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new LocalTomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new LocalTomlFileBuilder(new File(directory.toFile(), name + "." + TomlFile.FileType.TOML));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File file) {
		return new LocalYamlFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new LocalYamlFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String name) {
		return new LocalYamlFileBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new LocalYamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new LocalYamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new LocalYamlFileBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new LocalYamlConfigBuilder(file);
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new LocalYamlConfigBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new LocalYamlConfigBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new LocalYamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new LocalYamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new LocalYamlConfigBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
	}


	@Contract("_ -> this")
	public final @NotNull B fromInputStream(final @NotNull InputStream inputStream) {
		this.inputStream = BaseFileUtils.createNewInputStream(inputStream);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull File file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull Path file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull String file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull String directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull File directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull Path directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromResource(final @NotNull String resource) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromResource(resource);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B reloadSetting(final @NotNull ReloadSetting reloadSetting) {
		this.reloadSetting = reloadSetting;
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B mapType(final @NotNull Class<? extends M> mapType) {
		this.mapType = mapType;
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B listType(final @NotNull Class<? extends L> listType) {
		this.listType = listType;
		//noinspection unchecked
		return (B) this;
	}

	public abstract @NotNull B bigList(final boolean bigList);

	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Contract("-> new")
	public abstract @NotNull F create();


	private static final class LocalJsonFileBuilder extends JsonFileBuilder {

		public LocalJsonFileBuilder(@NotNull File file) {
			super(file);
		}
	}

	private static final class LocalThunderConfigBuilder extends ThunderConfigBuilder {

		public LocalThunderConfigBuilder(@NotNull File file) {
			super(file);
		}
	}

	private static final class LocalThunderFileBuilder extends ThunderFileBuilder {

		public LocalThunderFileBuilder(@NotNull File file) {
			super(file);
		}
	}

	private static final class LocalTomlFileBuilder extends TomlFileBuilder {

		public LocalTomlFileBuilder(@NotNull File file) {
			super(file);
		}
	}

	private static final class LocalYamlConfigBuilder extends YamlConfigBuilder {

		protected LocalYamlConfigBuilder(@NotNull File file) {
			super(file);
		}
	}

	private static final class LocalYamlFileBuilder extends YamlFileBuilder {

		public LocalYamlFileBuilder(@NotNull File file) {
			super(file);
		}
	}
}