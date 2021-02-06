package de.zeanon.tomlfilemanager;

import de.zeanon.storagemanagercore.StorageManager;
import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.tomlfilemanager.internal.files.raw.TomlFile;
import de.zeanon.tomlfilemanager.internal.utility.builder.TomlConfigBuilder;
import de.zeanon.tomlfilemanager.internal.utility.builder.TomlFileBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("rawtypes")
public abstract class TomlFileManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> extends StorageManager<B, F, M, L> {

	protected TomlFileManager(@NotNull File file, @NotNull Class<? extends M> mapType, @NotNull Class<? extends L> listType) {
		super(file, mapType, listType);
	}


	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File file) {
		return new TomlFileBuilder(file);
	}

	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path file) {
		return new TomlFileBuilder(file.toFile());
	}

	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String name) {
		return new TomlFileBuilder(new File(name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlFileBuilder tomlFile(final @NotNull String directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlFileBuilder tomlFile(final @NotNull File directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlFileBuilder tomlFile(final @NotNull Path directory, final @NotNull String name) {
		return new TomlFileBuilder(new File(directory.toFile(), name + "." + TomlFile.FileType.TOML));
	}


	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull File file) {
		return new TomlConfigBuilder(file);
	}

	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull Path file) {
		return new TomlConfigBuilder(file.toFile());
	}

	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull String name) {
		return new TomlConfigBuilder(new File(name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull String directory, final @NotNull String name) {
		return new TomlConfigBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull File directory, final @NotNull String name) {
		return new TomlConfigBuilder(new File(directory, name + "." + TomlFile.FileType.TOML));
	}

	public static @NotNull TomlConfigBuilder tomlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new TomlConfigBuilder(new File(directory.toFile(), name + "." + TomlFile.FileType.TOML));
	}
}