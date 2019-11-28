package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
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
		return (filePath + "." + this.toString());
	}

	default @NotNull Path addExtensionTo(final @NotNull Path filePath) {
		return Paths.get(filePath + "." + this.toString());
	}

	default @NotNull File addExtensionTo(final @NotNull File file) {
		return new File(file.getAbsolutePath() + "." + this.toString());
	}

	default boolean isTypeOf(final @NotNull String filePath) {
		return BaseFileUtils.getExtension(filePath).equalsIgnoreCase(this.toString());
	}

	default boolean isTypeOf(final @NotNull Path filePath) {
		return BaseFileUtils.getExtension(filePath).equalsIgnoreCase(this.toString());
	}

	default boolean isTypeOf(final @NotNull File file) {
		return BaseFileUtils.getExtension(file).equalsIgnoreCase(this.toString());
	}

	@NotNull String toLowerCase();

	@Override
	@NotNull String toString();
}