package de.zeanon.yamlfilemanager;

import de.zeanon.storagemanagercore.StorageManager;
import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.yamlfilemanager.internal.files.raw.YamlFile;
import de.zeanon.yamlfilemanager.internal.utility.builder.YamlConfigBuilder;
import de.zeanon.yamlfilemanager.internal.utility.builder.YamlFileBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"rawtypes", "SameParameterValue", "unused"})
public abstract class YamlFileManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> extends StorageManager<B, F, M, L> {

	protected YamlFileManager(@NotNull File file, @NotNull Class<? extends M> mapType, @NotNull Class<? extends L> listType) {
		super(file, mapType, listType);
	}


	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File file) {
		return new YamlFileBuilder(file);
	}

	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path file) {
		return new YamlFileBuilder(file.toFile());
	}

	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String name) {
		return new YamlFileBuilder(new File(BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
											? name
											: name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlFileBuilder yamlFile(final @NotNull String directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
													   ? name
													   : name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlFileBuilder yamlFile(final @NotNull File directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
													   ? name
													   : name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlFileBuilder yamlFile(final @NotNull Path directory, final @NotNull String name) {
		return new YamlFileBuilder(new File(directory.toFile(), BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
																? name
																: name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File file) {
		return new YamlConfigBuilder(file);
	}


	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path file) {
		return new YamlConfigBuilder(file.toFile());
	}

	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String name) {
		return new YamlConfigBuilder(new File(BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
											  ? name
											  : name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull String directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
														 ? name
														 : name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull File directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
														 ? name
														 : name + "." + YamlFile.FileType.YAML));
	}

	public static @NotNull YamlConfigBuilder yamlConfig(final @NotNull Path directory, final @NotNull String name) {
		return new YamlConfigBuilder(new File(directory.toFile(), BaseFileUtils.getExtension(name).equals(YamlFile.FileType.YAML.toString())
																  ? name
																  : name + "." + YamlFile.FileType.YAML));
	}
}