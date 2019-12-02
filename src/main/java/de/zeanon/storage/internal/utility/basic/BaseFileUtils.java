package de.zeanon.storage.internal.utility.basic;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic utility methods for Files
 *
 * @author Zeanon
 * @version 2.2.0
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class BaseFileUtils {


	@Getter
	@Setter
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
	public static boolean createFile(final @NotNull File file) {
		boolean create = !file.exists();
		try (final @NotNull FileChannel localChannel = new RandomAccessFile(file, "rws").getChannel()) {
			return create;
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
	public static boolean createParents(final @NotNull File file) {
		boolean create = !file.exists();
		try (final @NotNull FileChannel localChannel = new RandomAccessFile(file.getParentFile(), "rws").getChannel()) {
			return create;
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
	 *
	 * @return the files of the directory that are folders
	 */
	public static @NotNull Collection<File> listFolders(final @NotNull File directory) {
		return BaseFileUtils.listFolders(directory, false);
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
	 * Create a BufferedInputStream from a File
	 *
	 * @param file the File to be read
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable File file) {
		if (file == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(file));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + file.getAbsolutePath()
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param name the File to be read
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(new File(name)));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + name
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param file the File to be read
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable Path file) {
		if (file == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(file.toFile()));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + file.toAbsolutePath()
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param name      the File to be read
	 * @param directory the directory of the file
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("_, null -> null; _, !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable String directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory, name)));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory + "/" + name)
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param name      the File to be read
	 * @param directory the directory of the file
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("_, null -> null; _, !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable File directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory, name)));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory.getAbsolutePath() + "/" + name)
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a File
	 *
	 * @param name      the File to be read
	 * @param directory the directory of the file
	 *
	 * @return BufferedInputstream containing the contents of the given File
	 */
	@Contract("_, null -> null; _, !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory.toFile(), name)));
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory.toAbsolutePath() + "/" + name)
											 + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource
	 *
	 * @param resource the Path to the resource
	 *
	 * @return BufferedInputStream containing the contents of the resource file
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromResource(final @Nullable String resource) {
		if (resource == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(Objects.notNull(
						BaseFileUtils.class.getClassLoader()
										   .getResourceAsStream(resource),
						"Resource does not exist"));
			} catch (ObjectNullException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + resource + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal URL
	 *
	 * @param url the URL to be read from
	 *
	 * @return BufferedInputStream containing the contents of the resource file
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromUrl(final @Nullable URL url) {
		if (url == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(url.openStream());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + url + "'", e.getCause());
			}
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal URL
	 *
	 * @param url the URL to be read from
	 *
	 * @return BufferedInputStream containing the contents of the resource file
	 */
	@Contract("null -> null; !null -> new")
	public static @Nullable BufferedInputStream createNewInputStreamFromUrl(final @Nullable String url) {
		if (url == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new URL(url).openStream());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + url + "'", e.getCause());
			}
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
	public static void writeToFile(final @NotNull File file,
								   final @Nullable BufferedInputStream inputStream) {
		if (inputStream == null) {
			try (final @NotNull FileChannel localChannel = new RandomAccessFile(file, "rws").getChannel();
				 final @NotNull BufferedOutputStream outputStream = new BufferedOutputStream(Channels.newOutputStream(localChannel))) {
				localChannel.lock();
				outputStream.write(new byte[0], 0, 0);
			} catch (IOException e) {
				throw new RuntimeIOException("Error while clearing '"
											 + file.getAbsolutePath()
											 + "'", e.getCause());
			}
		} else {
			try (final @NotNull FileChannel localChannel = new RandomAccessFile(file, "rws").getChannel();
				 final @NotNull BufferedOutputStream outputStream = new BufferedOutputStream(Channels.newOutputStream(localChannel))) {
				localChannel.lock();
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

	public static void writeToFileIfCreated(final @NotNull File file,
											final @Nullable BufferedInputStream inputStream) {
		if (inputStream == null) {
			try {
				new RandomAccessFile(file, "rws");
			} catch (IOException e) {
				throw new RuntimeIOException("Error while creating '"
											 + file.getAbsolutePath()
											 + "'"
											 + System.lineSeparator()
											 + e.getMessage(), e.getCause());
			}
		} else {
			boolean create = !file.exists();
			try (final @NotNull FileChannel localChannel = new RandomAccessFile(file, "rws").getChannel();
				 final @NotNull BufferedOutputStream outputStream = new BufferedOutputStream(Channels.newOutputStream(localChannel))) {
				if (create) {
					localChannel.lock();
					final @NotNull byte[] data = new byte[bufferSize];
					int count;
					while ((count = inputStream.read(data, 0, bufferSize)) != -1) {
						outputStream.write(data, 0, count);
					}
					outputStream.flush();
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
		return BaseFileUtils.getExtension(file.getName());
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
}
