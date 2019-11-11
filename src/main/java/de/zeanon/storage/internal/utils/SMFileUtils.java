package de.zeanon.storage.internal.utils;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic utility methods for Files
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class SMFileUtils {

	/**
	 * Creates a given File and, if not existent, it's parents.
	 *
	 * @param file the File to be created.
	 */
	public static synchronized void createFile(final @NotNull File file) {
		Objects.checkNull(file);
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				if (SMFileUtils.failedToCreateParentFile(file.getParentFile())) {
					throw new IOException("Could not create parents of '" + file.getAbsolutePath() + "'");
				}
			}
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new IOException("Could not create '" + file.getAbsolutePath() + "'");
				}
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
	public static BufferedInputStream createNewInputStream(final @Nullable File file) {
		if (file == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(file));
			} catch (IOException e) {
				System.err.println("Error while creating InputStream from '" + file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource.
	 *
	 * @param resource the Path to the resource.
	 * @return BufferedInputStream containing the contents of the resource file.
	 */
	public static BufferedInputStream createNewInputStream(final @Nullable String resource) {
		if (resource == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(Objects.notNull(StorageManager.class.getClassLoader().getResourceAsStream(resource), "Resource does not exist"));
			} catch (IllegalStateException e) {
				System.err.println("Error while creating InputStream from '" + resource + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a given InputStream.
	 *
	 * @param inputStream the InputStream to be converted.
	 * @return null if {@code inputStream} is null or a BufferedInputStream from the given InputStream.
	 */
	public static BufferedInputStream createNewInputStream(final @Nullable InputStream inputStream) {
		if (inputStream == null) {
			return null;
		} else if (inputStream instanceof BufferedInputStream) {
			return (BufferedInputStream) inputStream;
		} else {
			return new BufferedInputStream(inputStream);
		}
	}

	/**
	 * Check if a given File has changed since the given TimeStamp.
	 *
	 * @param file      the File to be checked.
	 * @param timeStamp the TimeStamp to be checked against.
	 * @return true if the File has changed since the {@code timeStamp}.
	 */
	public static boolean hasChanged(final @NotNull File file, final long timeStamp) {
		return Objects.notNull(timeStamp, "TimeStamp must not be null") < Objects.notNull(file, "File must not be null").lastModified();
	}

	/**
	 * Write the contents of a given InputStream to a File.
	 *
	 * @param file        the File to be written to.
	 * @param inputStream the InputStream which shall be written.
	 */
	public static synchronized void writeToFile(final @NotNull File file, final @Nullable BufferedInputStream inputStream) {
		if (!Objects.notNull(file, "File must not be null").exists()) {
			SMFileUtils.createFile(file);
		}
		if (inputStream == null) {
			try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Objects.notNull(file)))) {
				outputStream.write(new byte[0], 0, 0);
			} catch (IOException e) {
				System.err.println("Error while clearing '" + file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		} else {
			try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Objects.notNull(file)))) {
				if (!file.exists()) {
					createFile(file);
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


	private static boolean failedToCreateParentFile(final @NotNull File file) {
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				if (SMFileUtils.failedToCreateParentFile(file.getParentFile())) {
					throw new IOException("Could not create parents of '" + file.getAbsolutePath() + "'");
				}
			}
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new IOException("Could not create '" + file.getAbsolutePath() + "'");
				}
			}
			return false;
		} catch (IOException e) {
			System.err.println("Error while creating File '" + file.getAbsolutePath() + "'.");
			e.printStackTrace();
			return true;
		}
	}
}