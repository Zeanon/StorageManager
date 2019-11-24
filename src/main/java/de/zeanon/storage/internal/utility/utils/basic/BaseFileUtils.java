package de.zeanon.storage.internal.utility.utils.basic;

import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
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
@SuppressWarnings({"unused", "WeakerAccess"})
public class BaseFileUtils {

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
		return timeStamp < file.lastModified();
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
			return BaseFileUtils.createFileInternally(file, false);
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
			if (file.getParentFile() != null) {
				return BaseFileUtils.createFileInternally(file.getParentFile(), true);
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
		final @NotNull Collection<File> files = new ArrayList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null && file.isDirectory()) {
						files.add(file);
						if (deep) {
							files.addAll(BaseFileUtils.listFolders(file, true));
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
		return BaseFileUtils.listFolders(directory, false);
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
		final @NotNull Collection<File> files = new ArrayList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (Arrays.stream(extensions).anyMatch(BaseFileUtils.getExtension(file)::equalsIgnoreCase)) {
							files.add(file);
						}
						if (deep) {
							files.addAll(BaseFileUtils.listFiles(file, extensions, true));
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
		return BaseFileUtils.listFiles(directory, extensions.toArray(new String[0]), deep);
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
		return BaseFileUtils.listFiles(directory, extensions, false);
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
		return BaseFileUtils.listFiles(directory, extensions.toArray(new String[0]), false);
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
		return BaseFileUtils.listFiles(directory, false);
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
		try {
			return new BufferedInputStream(Objects.notNull(
					BaseFileUtils.class.getClassLoader()
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
		BaseFileUtils.createFile(file);
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
				BaseFileUtils.createFile(file);
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
		return BaseFileUtils.getExtension(
				file.getName());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public static @NotNull String getExtension(final @NotNull Path filePath) {
		return BaseFileUtils.getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public static @NotNull String getExtension(final @NotNull String filePath) {
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
		return new File(BaseFileUtils.removeExtension(
				file.getAbsolutePath()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public static @NotNull Path removeExtension(final @NotNull Path filePath) {
		return Paths.get(BaseFileUtils.removeExtension(filePath.toString()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public static @NotNull String removeExtension(final @NotNull String filePath) {
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
				if (!BaseFileUtils.createFileInternally(file.getParentFile(), true)) {
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