package de.zeanon.jsonfilemanager;

import de.zeanon.jsonfilemanager.internal.files.raw.JsonFile;
import de.zeanon.jsonfilemanager.internal.utility.builder.JsonFileBuilder;
import de.zeanon.storagemanagercore.StorageManager;
import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"rawtypes", "SameParameterValue", "unused"})
public abstract class JsonFileManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> extends StorageManager<B, F, M, L> {

	protected JsonFileManager(@NotNull File file, @NotNull Class<? extends M> mapType, @NotNull Class<? extends L> listType) {
		super(file, mapType, listType);
	}


	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File file) {
		return new JsonFileBuilder(file);
	}

	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path file) {
		return new JsonFileBuilder(file.toFile());
	}

	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String name) {
		return new JsonFileBuilder(new File(BaseFileUtils.getExtension(name).equals(JsonFile.FileType.JSON.toString())
											? name
											: name + "." + JsonFile.FileType.JSON));
	}

	public static @NotNull JsonFileBuilder jsonFile(final @NotNull String directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(JsonFile.FileType.JSON.toString())
													   ? name
													   : name + "." + JsonFile.FileType.JSON));
	}

	public static @NotNull JsonFileBuilder jsonFile(final @NotNull File directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory, BaseFileUtils.getExtension(name).equals(JsonFile.FileType.JSON.toString())
													   ? name
													   : name + "." + JsonFile.FileType.JSON));
	}

	public static @NotNull JsonFileBuilder jsonFile(final @NotNull Path directory, final @NotNull String name) {
		return new JsonFileBuilder(new File(directory.toFile(), BaseFileUtils.getExtension(name).equals(JsonFile.FileType.JSON.toString())
																? name
																: name + "." + JsonFile.FileType.JSON));
	}
}