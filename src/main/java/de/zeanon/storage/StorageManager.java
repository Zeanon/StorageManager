package de.zeanon.storage;

import de.zeanon.storage.internal.files.raw.JsonFile;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.storage.internal.files.raw.TomlFile;
import de.zeanon.storage.internal.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.builder.*;
import de.zeanon.utils.basic.BaseFileUtils;
import de.zeanon.utils.basic.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
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
public abstract class StorageManager<B, F> {

	protected final @NotNull File file;
	protected @Nullable BufferedInputStream inputStream;


	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new ThunderFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new ThunderFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new ThunderConfigBuilder(Objects.notNull(file, "File must not be null"));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new ThunderConfigBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(Objects.notNull(file, "File must not be null"));
	}


	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@Contract("null -> fail; !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}


	@Contract("_ -> this")
	public final @NotNull B fromInputStream(final @Nullable InputStream inputStream) {
		this.inputStream = BaseFileUtils.createNewInputStream(inputStream);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @Nullable File file) {
		this.inputStream = file == null ? null : BaseFileUtils.createNewInputStream(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @Nullable Path file) {
		this.inputStream = file == null ? null : BaseFileUtils.createNewInputStream(file.toFile());
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @Nullable String file) {
		this.inputStream = file == null ? null : BaseFileUtils.createNewInputStream(new File(file));
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @Nullable String directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = BaseFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @Nullable File directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = BaseFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = BaseFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory.toFile(), name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromResource(final @Nullable String resource) {
		this.inputStream = resource == null ? null : BaseFileUtils.createNewInputStream(resource);
		//noinspection unchecked
		return (B) this;
	}


	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Contract("-> new")
	public abstract @NotNull F create();
}