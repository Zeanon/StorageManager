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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public abstract class StorageManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> {


	protected final @NotNull File file;
	protected @NotNull ReloadSetting reloadSetting = Reload.INTELLIGENT;
	protected @Nullable BufferedInputStream inputStream;


	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory.toFile(), name + "." + JsonFile.FileType.JSON));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new ThunderFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new ThunderFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new ThunderFileBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new ThunderConfigBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new ThunderConfigBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new ThunderConfigBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory.toFile(), name + "." + TomlFile.FileType.TOML));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(file);
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(file);
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(file.toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
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

	public abstract @NotNull B map(Class<? extends M> map);

	public abstract @NotNull B list(Class<? extends L> list);

	public abstract void bigList(final boolean bigList);


	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Contract("-> new")
	public abstract @NotNull F create();
}