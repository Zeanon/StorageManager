package de.zeanon.storage;

import de.zeanon.storage.internal.builders.*;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.data.raw.TomlFile;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.utils.SMFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
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
@RequiredArgsConstructor
@Accessors(fluent = true)
@SuppressWarnings("unused")
public abstract class StorageManager<C, F> {

	@NotNull
	protected final File file;
	@Nullable
	protected BufferedInputStream inputStream;


	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(file);
	}

	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(file.toFile());
	}

	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(name + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, name + "." + JsonFile.FileType.JSON));
	}

	@NotNull
	public static JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory.toFile(), name + "." + JsonFile.FileType.JSON));
	}


	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new ThunderFileBuilder(file);
	}

	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new ThunderFileBuilder(file.toFile());
	}

	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new ThunderFileBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new ThunderConfigBuilder(file);
	}

	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new ThunderConfigBuilder(file.toFile());
	}

	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new ThunderConfigBuilder(new File(name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDER));
	}

	@NotNull
	public static ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDER));
	}


	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(file);
	}

	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(file.toFile());
	}

	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(name + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	@NotNull
	public static TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory.toFile(), name + "." + TomlFile.FileType.TOML));
	}


	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(file);
	}

	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(file.toFile());
	}

	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(file);
	}


	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(file.toFile());
	}

	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, name + "." + YamlFile.FileType.YAML));
	}

	@NotNull
	public static YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory.toFile(), name + "." + YamlFile.FileType.YAML));
	}


	@NotNull
	public final C fromInputStream(final @Nullable InputStream inputStream) {
		this.inputStream = SMFileUtils.createNewInputStream(inputStream);
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable File file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(file);
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable Path file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(file.toFile());
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable String file) {
		this.inputStream = file == null ? null : SMFileUtils.createNewInputStream(new File(file));
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable String directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable File directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = SMFileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory.toFile(), name));
		}
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	public final C fromResource(final @Nullable String resource) {
		this.inputStream = resource == null ? null : SMFileUtils.createNewInputStream(resource);
		//noinspection unchecked
		return (C) this;
	}


	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Nullable
	public abstract F create();
}