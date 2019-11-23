package de.zeanon.storage.internal.utility.utils;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic utility methods for Files
 *
 * @author Zeanon
 * @version 2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public class SMFileUtils {

	@Setter
	@Getter
	private static int bufferSize = 8192;


	/**
	 * Check if a given File has changed since the given TimeStamp
	 *
	 * @param file      the File to be checked
	 * @param timeStamp the TimeStamp to be checked against
	 *
	 * @return true if the File has changed since the {@code timeStamp}
	 */
	public static boolean hasChanged(final @NotNull File file,
									 final long timeStamp) {
		return timeStamp < Objects.notNull(file, "File must not be null").lastModified();
	}

	/**
	 * Creates a given File and, if not existent, it's parents
	 *
	 * @param file the File to be created
	 */
	@SuppressWarnings("UnusedReturnValue")
	@Synchronized
	public static boolean createFile(final @NotNull File file) {
		try {
			return SMFileUtils.createFileInternally(Objects.notNull(file, "File must not be null"), false);
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating '"
										 + file.getAbsolutePath()
										 + "'"
										 + System.lineSeparator()
										 + e.getMessage(), e.getCause());
		}
	}

	/**
	 * Creates the parents of a given File
	 *
	 * @param file the File to be used
	 */
	@Synchronized
	public static boolean createParents(final @NotNull File file) {
		try {
			if (Objects.notNull(file, "File must not be null").getParentFile() != null) {
				return SMFileUtils.createFileInternally(file.getParentFile(), true);
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating parents of '"
										 + file.getAbsolutePath()
										 + "'"
										 + System.lineSeparator()
										 + e.getMessage(), e.getCause());
		}
	}


	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 *
	 * @return the files of the directory that are folders
	 */
	public static @NotNull Collection<File> listFolders(final @NotNull File directory,
														final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		final @NotNull Collection<File> files = new ArrayList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null && file.isDirectory()) {
						files.add(file);
						if (deep) {
							files.addAll(SMFileUtils.listFolders(file, true));
						}
					}
				}
			}
		}
		return files;
	}

	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 *
	 * @return the files of the directory that are folders
	 */
	public static @NotNull Collection<File> listFolders(final @NotNull File directory) {
		return SMFileUtils.listFolders(directory, false);
	}


	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 * @param deep       also look through subdirectories
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory,
													  final @NotNull String[] extensions,
													  final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		Objects.checkNull(extensions, "Extensions must not be null");
		final @NotNull Collection<File> files = new ArrayList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (Arrays.stream(extensions).anyMatch(SMFileUtils.getExtension(file)::equalsIgnoreCase)) {
							files.add(file);
						}
						if (deep) {
							files.addAll(SMFileUtils.listFiles(file, extensions, true));
						}
					}
				}
			}
		}
		return files;
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 * @param deep       also look through subdirectories
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory,
													  final @NotNull List<String> extensions,
													  final boolean deep) {
		return SMFileUtils.listFiles(directory, extensions.toArray(new String[0]), deep);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory,
													  final @NotNull String[] extensions) {
		return SMFileUtils.listFiles(directory, extensions, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory,
													  final @NotNull List<String> extensions) {
		return SMFileUtils.listFiles(directory, extensions.toArray(new String[0]), false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 *
	 * @return the files of the given directory
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory,
													  final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		final @NotNull Collection<File> files = new ArrayList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						files.add(file);
						if (deep) {
							files.addAll(listFiles(file, true));
						}
					}
				}
			}
		}
		return files;
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 *
	 * @return the files of the given directory
	 */
	public static @NotNull Collection<File> listFiles(final @NotNull File directory) {
		return SMFileUtils.listFiles(directory, false);
	}


	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param file the File to be read
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@NotNull
	@Contract("_ -> new")
	public static BufferedInputStream createNewInputStream(final @NotNull File file) {
		Objects.checkNull(file, "File must not be null");
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating InputStream from '"
										 + file.getAbsolutePath()
										 + "'", e.getCause());
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource
	 *
	 * @param resource the Path to the resource
	 *
	 * @return BufferedInputStream containing the contents of the resource file
	 */
	@Contract("_ -> new")
	public static @NotNull BufferedInputStream createNewInputStream(final @NotNull String resource) {
		Objects.checkNull(resource, "Resource must not be null");
		try {
			return new BufferedInputStream(Objects.notNull(
					StorageManager.class.getClassLoader()
										.getResourceAsStream(resource),
					"Resource does not exist"));
		} catch (NullPointerException e) {
			throw new RuntimeIOException("Error while creating InputStream from '"
										 + resource + "'", e.getCause());
		}
	}

	/**
	 * Create a BufferedInputStream from a given InputStream
	 *
	 * @param inputStream the InputStream to be converted
	 *
	 * @return null if {@code inputStream} is null or a BufferedInputStream from the given InputStream
	 */
	@Contract(value = "null -> null", pure = true)
	public static @Nullable BufferedInputStream createNewInputStream(final @Nullable InputStream inputStream) {
		if (inputStream == null) {
			return null;
		} else if (inputStream instanceof BufferedInputStream) {
			return (BufferedInputStream) inputStream;
		} else {
			return new BufferedInputStream(inputStream);
		}
	}

	/**
	 * Write the contents of a given InputStream to a File
	 *
	 * @param file        the File to be written to
	 * @param inputStream the InputStream which shall be written
	 */
	@Synchronized
	public static void writeToFile(final @NotNull File file,
								   final @Nullable BufferedInputStream inputStream) {
		SMFileUtils.createFile(Objects.notNull(file, "File must not be null"));
		if (inputStream == null) {
			try (final @NotNull BufferedOutputStream outputStream =
						 new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(new byte[0], 0, 0);
			} catch (IOException e) {
				throw new RuntimeIOException("Error while clearing '"
											 + file.getAbsolutePath()
											 + "'", e.getCause());
			}
		} else {
			try (final @NotNull BufferedOutputStream outputStream =
						 new BufferedOutputStream(new FileOutputStream(file))) {
				SMFileUtils.createFile(file);
				final @NotNull byte[] data = new byte[bufferSize];
				int count;
				while ((count = inputStream.read(data, 0, bufferSize)) != -1) {
					outputStream.write(data, 0, count);
				}
			} catch (IOException e) {
				throw new RuntimeIOException("Error while writing Data to '"
											 + file.getAbsolutePath()
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param file the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public static @NotNull String getExtension(final @NotNull File file) {
		return SMFileUtils.getExtension(
				Objects.notNull(file, "File must not be null").getName());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public static @NotNull String getExtension(final @NotNull Path filePath) {
		return SMFileUtils.getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public static @NotNull String getExtension(final @NotNull String filePath) {
		Objects.checkNull(filePath, "FilePath must not be null");
		char ch;
		int len;
		if ((len = filePath.length()) == 0
			|| (ch = filePath.charAt(len - 1)) == '/'
			|| ch == '\\'
			|| ch == '.') {
			return "";
		}
		final int dotInd = filePath.lastIndexOf('.');
		final int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return filePath.substring(dotInd + 1).toLowerCase();
		}
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param file the File to be checked
	 *
	 * @return the File without it's extension
	 */
	@Contract("_ -> new")
	public static @NotNull File removeExtension(final @NotNull File file) {
		return new File(SMFileUtils.removeExtension(
				Objects.notNull(file, "File must not be null").getAbsolutePath()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public static @NotNull Path removeExtension(final @NotNull Path filePath) {
		return Paths.get(SMFileUtils.removeExtension(filePath.toString()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public static @NotNull String removeExtension(final @NotNull String filePath) {
		Objects.checkNull(filePath, "FilePath must not be null");
		char ch;
		int len;
		if ((len = filePath.length()) == 0
			|| (ch = filePath.charAt(len - 1)) == '/'
			|| ch == '\\'
			|| ch == '.') {
			return filePath;
		}
		final int dotInd = filePath.lastIndexOf('.');
		final int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return filePath;
		} else {
			return filePath.substring(0, dotInd).toLowerCase();
		}
	}


	private static boolean createFileInternally(final @NotNull File file,
												final boolean isDirectory) throws IOException {
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			try {
				if (!SMFileUtils.createFileInternally(file.getParentFile(), true)) {
					throw new IOException();
				}
			} catch (IOException e) {
				throw new IOException("Could not create parents of '"
									  + file.getAbsolutePath()
									  + "'"
									  + System.lineSeparator()
									  + e.getMessage(), e.getCause());
			}
		}
		if (!file.exists()) {
			try {
				if (isDirectory) {
					if (file.mkdir()) {
						return true;
					} else {
						throw new IOException();
					}
				} else {
					if (file.createNewFile()) {
						return true;
					} else {
						throw new IOException();
					}
				}
			} catch (IOException e) {
				throw new IOException("Could not create '"
									  + file.getAbsolutePath()
									  + "'", e.getCause());
			}
		} else {
			return false;
		}
	}
}