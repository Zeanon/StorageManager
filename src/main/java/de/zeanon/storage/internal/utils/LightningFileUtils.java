package de.zeanon.storage.internal.utils;

import de.zeanon.storage.LightningStorage;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Basic utility methods for Files
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightningFileUtils {

	public static void createFile(final @NotNull File file) {
		Objects.checkNull(file);
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				//noinspection ResultOfMethodCallIgnored
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				//noinspection ResultOfMethodCallIgnored
				file.createNewFile();
			}
		} catch (IOException e) {
			System.err.println("Error while creating File '" + file.getAbsolutePath() + "'.");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Create a BufferedInputStream from a File.
	 *
	 * @param file the File to be read.
	 * @return BufferedInputstream containing the contents of the given File.
	 */
	public static BufferedInputStream createNewInputStream(final @NotNull File file) {
		try {
			return new BufferedInputStream(new FileInputStream(Objects.notNull(file, "File must not be null")));
		} catch (IOException e) {
			System.err.println("Error while creating InputStream from '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource.
	 *
	 * @param resource the Path to the resource.
	 * @return BufferedInputStream containing the contents of the resource file.
	 */
	public static BufferedInputStream createNewInputStream(final @NotNull String resource) {
		return new BufferedInputStream(Objects.notNull(LightningStorage.class.getClassLoader().getResourceAsStream(Objects.notNull(resource, "Resource must not be null"))));
	}

	/**
	 * Check if a given File has changed since the given TimeStamp.
	 *
	 * @param file      the File to be checked.
	 * @param timeStamp the TimeStamp to be checked against.
	 * @return true if the File has changed.
	 */
	public static boolean hasChanged(final @NotNull File file, final long timeStamp) {
		return Objects.notNull(timeStamp) < Objects.notNull(file).lastModified();
	}

	/**
	 * Write the contents of a given InputStream to a File.
	 *
	 * @param file        the File to be written to.
	 * @param inputStream the InputStream which shall be written.
	 */
	public static synchronized void writeToFile(final @NotNull File file, final @NotNull InputStream inputStream) {
		Objects.checkNull(inputStream);
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Objects.notNull(file)))) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (IOException e) {
			System.err.println("Error while copying to + '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	/**
	 * Returns the extension of a given File.
	 *
	 * @param file the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull File file) {
		return getExtension(file.getName());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull Path filePath) {
		return getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull String filePath) {
		Objects.checkNull(filePath, "FilePath must not be null");
		char ch;
		int len;
		if ((len = filePath.length()) == 0
			|| (ch = filePath.charAt(len - 1)) == '/'
			|| ch == '\\'
			|| ch == '.') {
			return "";
		}
		int dotInd = filePath.lastIndexOf('.');
		int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return filePath.substring(dotInd + 1).toLowerCase();
		}
	}

	/**
	 * Removes the extension of a given File.
	 *
	 * @param file the File to be checked.
	 * @return the File without it's extension.
	 */
	public static File removeExtension(final @NotNull File file) {
		return new File(removeExtension(file.getAbsolutePath()));
	}

	/**
	 * Removes the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the Path without the extension.
	 */
	public static Path removeExtension(final @NotNull Path filePath) {
		return Paths.get(removeExtension(filePath.toString()));
	}

	/**
	 * Removes the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the Path without the extension.
	 */
	public static String removeExtension(final @NotNull String filePath) {
		Objects.checkNull(filePath, "FilePath must not be null");
		char ch;
		int len;
		if ((len = filePath.length()) == 0
			|| (ch = filePath.charAt(len - 1)) == '/'
			|| ch == '\\'
			|| ch == '.') {
			return "";
		}
		int dotInd = filePath.lastIndexOf('.');
		int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return filePath.substring(0, dotInd).toLowerCase();
		}
	}
}