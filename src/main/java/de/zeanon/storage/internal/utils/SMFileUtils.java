package de.zeanon.storage.internal.utils;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic utility methods for Files
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SMFileUtils {

	/**
	 * Creates a given File and, if not existent, it's parents.
	 *
	 * @param file the File to be created.
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static synchronized boolean createFile(final @NotNull File file) {
		try {
			return createFileInternally(Objects.notNull(file, "File must not be null"));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating '" + file.getAbsolutePath() + "'"
										 + System.lineSeparator() + e.getMessage(), e.getCause());
		}
	}

	/**
	 * Creates the parents of a given File.
	 *
	 * @param file the File to be used.
	 */
	public static synchronized boolean createParents(final @NotNull File file) {
		try {
			if (Objects.notNull(file, "File must not be null").getParentFile() != null) {
				return createFileInternally(file.getParentFile());
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating parents of '" + file.getAbsolutePath() + "'"
										 + System.lineSeparator() + e.getMessage(), e.getCause());
		}
	}


	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 * @return the files of the directory that are folders
	 */
	@NotNull
	public static Collection<File> listFolders(final @NotNull File directory, final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		Collection<File> files = new LinkedList<>();
		if (directory.isDirectory()) {
			File[] fileList = directory.listFiles();
			for (File file : fileList != null ? fileList : new File[0]) {
				if (file.isDirectory()) {
					files.add(file);
					if (deep) {
						files.addAll(listFolders(file, true));
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
	 * @return the files of the directory that are folders
	 */
	@NotNull
	public static Collection<File> listFolders(final @NotNull File directory) {
		return listFolders(directory, false);
	}


	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 * @param deep       also look through subdirectories
	 * @return the files of the given directory with the given extensions
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory, final @NotNull List<String> extensions, final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		Objects.checkNull(extensions, "Extensions must not be null");
		Collection<File> files = new LinkedList<>();
		if (directory.isDirectory()) {
			File[] fileList = directory.listFiles();
			for (File file : fileList != null ? fileList : new File[0]) {
				if (extensions.stream().anyMatch(SMFileUtils.getExtension(file)::equalsIgnoreCase)) {
					files.add(file);
				}
				if (deep) {
					files.addAll(listFiles(file, extensions, true));
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
	 * @return the files of the given directory with the given extensions
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory, final @NotNull String[] extensions, final boolean deep) {
		return listFiles(directory, Arrays.asList(extensions), deep);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 * @return the files of the given directory with the given extensions
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory, final @NotNull List<String> extensions) {
		return listFiles(directory, extensions, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 * @return the files of the given directory with the given extensions
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory, final @NotNull String[] extensions) {
		return listFiles(directory, Arrays.asList(extensions), false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 * @return the files of the given directory
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory, final boolean deep) {
		Objects.checkNull(directory, "Directory must not be null");
		Collection<File> files = new LinkedList<>();
		if (directory.isDirectory()) {
			File[] fileList = directory.listFiles();
			for (File file : fileList != null ? fileList : new File[0]) {
				files.add(file);
				if (deep) {
					files.addAll(listFiles(file, true));
				}
			}
		}
		return files;
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @return the files of the given directory
	 */
	@NotNull
	public static Collection<File> listFiles(final @NotNull File directory) {
		return listFiles(directory, false);
	}


	/**
	 * Create a BufferedInputStream from a File.
	 *
	 * @param file the File to be read.
	 * @return BufferedInputstream containing the contents of the given File.
	 */
	@NotNull
	public static BufferedInputStream createNewInputStream(final @NotNull File file) {
		Objects.checkNull(file, "File must not be null");
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while creating InputStream from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource.
	 *
	 * @param resource the Path to the resource.
	 * @return BufferedInputStream containing the contents of the resource file.
	 */
	@NotNull
	public static BufferedInputStream createNewInputStream(final @NotNull String resource) {
		Objects.checkNull(resource, "Resource must not be null");
		try {
			return new BufferedInputStream(Objects.notNull(StorageManager.class.getClassLoader().getResourceAsStream(resource), "Resource does not exist"));
		} catch (NullPointerException e) {
			throw new RuntimeIOException("Error while creating InputStream from '" + resource + "'", e.getCause());
		}
	}

	/**
	 * Create a BufferedInputStream from a given InputStream.
	 *
	 * @param inputStream the InputStream to be converted.
	 * @return null if {@code inputStream} is null or a BufferedInputStream from the given InputStream.
	 */
	@Nullable
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
		return timeStamp < Objects.notNull(file, "File must not be null").lastModified();
	}

	/**
	 * Write the contents of a given InputStream to a File.
	 *
	 * @param file        the File to be written to.
	 * @param inputStream the InputStream which shall be written.
	 */
	public static synchronized void writeToFile(final @NotNull File file, final @Nullable BufferedInputStream inputStream) {
		SMFileUtils.createFile(Objects.notNull(file, "File must not be null"));
		if (inputStream == null) {
			try (final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(new byte[0], 0, 0);
			} catch (IOException e) {
				throw new RuntimeIOException("Error while clearing '" + file.getAbsolutePath() + "'", e.getCause());
			}
		} else {
			try (final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				createFile(file);
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			} catch (IOException e) {
				throw new RuntimeIOException("Error while copying to + '" + file.getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param file the File to be checked.
	 * @return the extension of the given File.
	 */
	@NotNull
	public static String getExtension(final @NotNull File file) {
		return getExtension(Objects.notNull(file, "File must not be null").getName());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	@NotNull
	public static String getExtension(final @NotNull Path filePath) {
		return getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	@NotNull
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
		final int dotInd = filePath.lastIndexOf('.');
		final int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
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
	@NotNull
	public static File removeExtension(final @NotNull File file) {
		return new File(removeExtension(Objects.notNull(file, "File must not be null").getAbsolutePath()));
	}

	/**
	 * Removes the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the Path without the extension.
	 */
	@NotNull
	public static Path removeExtension(final @NotNull Path filePath) {
		return Paths.get(removeExtension(filePath.toString()));
	}

	/**
	 * Removes the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the Path without the extension.
	 */
	@NotNull
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
		final int dotInd = filePath.lastIndexOf('.');
		final int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return filePath.substring(0, dotInd).toLowerCase();
		}
	}

	private static boolean createFileInternally(@NotNull File file) throws IOException {
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			try {
				if (!createFileInternally(file.getParentFile())) {
					throw new IOException();
				}
			} catch (IOException e) {
				throw new IOException("Could not create parents of '" + file.getAbsolutePath() + "'"
									  + System.lineSeparator() + e.getMessage(), e.getCause());
			}
		}
		if (!file.exists()) {
			try {
				if (file.createNewFile()) {
					return true;
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				throw new IOException("Could not create '" + file.getAbsolutePath() + "'", e.getCause());
			}
		} else {
			return false;
		}
	}
}