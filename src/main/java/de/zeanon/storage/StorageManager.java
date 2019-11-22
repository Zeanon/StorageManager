package de.zeanon.storage;

import de.zeanon.storage.internal.files.raw.JsonFile;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.storage.internal.files.raw.TomlFile;
import de.zeanon.storage.internal.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.builder.*;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
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
@RequiredArgsConstructor(onConstructor_ = @Contract(pure = true))
@SuppressWarnings("unused")
public abstract class StorageManager<B, F> {

	@NotNull
	protected final File file;
	@Nullable
	protected BufferedInputStream inputStream;


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + JsonFile.FileType.JSON));
	}


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new ThunderFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new ThunderFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new ThunderConfigBuilder(Objects.notNull(file, "File must not be null"));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new ThunderConfigBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + ThunderFile.FileType.THUNDER));
	}


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + TomlFile.FileType.TOML));
	}


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(Objects.notNull(file, "File must not be null"));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(Objects.notNull(file, "File must not be null"));
	}


	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(Objects.notNull(file, "Path must not be null").toFile());
	}

	@NotNull
	@Contract("null -> fail; !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	@Contract("null, null -> fail; null, !null -> fail; !null, null -> fail; !null, !null -> new")
	public static YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null") + "." + YamlFile.FileType.YAML));
	}


	@NotNull
	@Contract("_ -> this")
	public final B fromInputStream(final @Nullable InputStream inputStream) {
		this.inputStream = SMFileUtils.createNewInputStream(inputStream);
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_ -> this")
	public final B fromFile(final @Nullable File file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(file);
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_ -> this")
	public final B fromFile(final @Nullable Path file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(file.toFile());
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_ -> this")
	public final B fromFile(final @Nullable String file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(new File(file));
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_, _ -> this")
	public final B fromFile(final @Nullable String directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_, _ -> this")
	public final B fromFile(final @Nullable File directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_, _ -> this")
	public final B fromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory.toFile(), name));
		}
		//noinspection unchecked
		return (B) this;
	}

	@NotNull
	@Contract("_ -> this")
	public final B fromResource(final @Nullable String resource) {
		this.inputStream = resource == null ? null : SMFileUtils.createNewInputStream(resource);
		//noinspection unchecked
		return (B) this;
	}


	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Nullable
	@Contract("-> new")
	public abstract F create();
}