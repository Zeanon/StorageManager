package de.zeanon.storagemanager.internal.base.interfaces;

import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
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

	/**
	 * Add the extension corresponding to the FileType to the given FilePath
	 *
	 * @return the given FilePath with the added extension
	 */
	default @NotNull
	String addExtensionTo(final @NotNull String filePath) {
		return (filePath + "." + this.toString());
	}

	/**
	 * Add the extension corresponding to the FileType to the given FilePath
	 *
	 * @return the given FilePath with the added extension
	 */
	default @NotNull
	Path addExtensionTo(final @NotNull Path filePath) {
		return Paths.get(filePath + "." + this.toString());
	}

	/**
	 * Add the extension corresponding to the FileType to the given File's path
	 *
	 * @return the given File with the added extension
	 */
	default @NotNull
	File addExtensionTo(final @NotNull File file) {
		return new File(file.getAbsolutePath() + "." + this.toString());
	}

	/**
	 * Check whether the given File is of this FileType
	 */
	default boolean isTypeOf(final @NotNull String filePath) {
		return BaseFileUtils.getExtension(filePath).equalsIgnoreCase(this.toString());
	}

	/**
	 * Check whether the given File is of this FileType
	 */
	default boolean isTypeOf(final @NotNull Path filePath) {
		return BaseFileUtils.getExtension(filePath).equalsIgnoreCase(this.toString());
	}

	/**
	 * Check whether the given File is of this FileType
	 */
	default boolean isTypeOf(final @NotNull File file) {
		return BaseFileUtils.getExtension(file).equalsIgnoreCase(this.toString());
	}

	/**
	 * Get then extension of this FileType in LowerCase
	 */
	@NotNull
	String toLowerCase();

	/**
	 * Get the extension of this FileType
	 */
	@Override
	@NotNull
	String toString();
}