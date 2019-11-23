package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;


/**
 * Base interface for FileType enums, providing different methods to be implemented
 *
 * @author Zeanon
 * @version 1.3.0
 */
@SuppressWarnings("unused")
public interface FileType {


	default @NotNull String addExtensionTo(final @NotNull String filePath) {
		return (Objects.notNull(filePath, "FilePath  must not be null") + "." + this.toString());
	}

	default @NotNull Path addExtensionTo(final @NotNull Path filePath) {
		return Paths.get(Objects.notNull(filePath, "FilePath  must not be null") + "." + this.toString());
	}

	default @NotNull File addExtensionTo(final @NotNull File file) {
		return new File(Objects.notNull(file, "File  must not be null").getAbsolutePath() + "." + this.toString());
	}

	default boolean isTypeOf(final @NotNull String filePath) {
		return SMFileUtils.getExtension(Objects.notNull(filePath, "FilePath  must not be null")).equalsIgnoreCase(this.toString());
	}

	default boolean isTypeOf(final @NotNull Path filePath) {
		return SMFileUtils.getExtension(Objects.notNull(filePath, "FilePath  must not be null")).equalsIgnoreCase(this.toString());
	}

	default boolean isTypeOf(final @NotNull File file) {
		return SMFileUtils.getExtension(Objects.notNull(file, "File  must not be null")).equalsIgnoreCase(this.toString());
	}

	@NotNull String toLowerCase();

	@Override
	@NotNull String toString();
}