package de.zeanon.thunderfilemanager;

import de.zeanon.storagemanagercore.StorageManager;
import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.thunderfilemanager.internal.files.raw.ThunderFile;
import de.zeanon.thunderfilemanager.internal.utility.builder.ThunderConfigBuilder;
import de.zeanon.thunderfilemanager.internal.utility.builder.ThunderFileBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("rawtypes")
public abstract class ThunderFileManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> extends StorageManager<B, F, M, L> {

	protected ThunderFileManager(@NotNull File file, @NotNull Class<? extends M> mapType, @NotNull Class<? extends L> listType) {
		super(file, mapType, listType);
	}


	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File file) {
		return new ThunderFileBuilder(file);
	}

	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path file) {
		return new ThunderFileBuilder(file.toFile());
	}

	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String name) {
		return new ThunderFileBuilder(new File(name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull String directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull File directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderFileBuilder thunderFile(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderFileBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDERFILE));
	}


	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File file) {
		return new ThunderConfigBuilder(file);
	}

	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path file) {
		return new ThunderConfigBuilder(file.toFile());
	}

	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String name) {
		return new ThunderConfigBuilder(new File(name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull String directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull File directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory, name + "." + ThunderFile.FileType.THUNDERFILE));
	}

	public static @NotNull ThunderConfigBuilder thunderConfig(final @NotNull Path directory, final @NotNull String name) {
		return new ThunderConfigBuilder(new File(directory.toFile(), name + "." + ThunderFile.FileType.THUNDERFILE));
	}
}