package de.zeanon.storagemanagercore.internal.utility.basic;

import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storagemanagercore.internal.utility.filelock.ExtendedFileLock;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class BaseFileUtils {


	@Getter
	@Setter
	private int bufferSize = 8192;


	/**
	 * Check if a given File has changed since the given TimeStamp
	 *
	 * @param file      the File to be checked
	 * @param timeStamp the TimeStamp to be checked against
	 *
	 * @return true if the File has changed since the {@code timeStamp}
	 */
	public boolean hasChanged(final @NotNull File file,
							  final long timeStamp) {
		return timeStamp < file.lastModified();
	}

	/**
	 * Creates a given File and, if not existent, it's parents
	 *
	 * @param file the File to be created
	 */
	public boolean createFile(final @NotNull File file) {
		return BaseFileUtils.createFileInternally(file, false);
	}

	public boolean createFile(final @NotNull String file) {
		return BaseFileUtils.createFileInternally(new File(file), false);
	}

	public boolean createFile(final @NotNull File parent, final @NotNull String child) {
		return BaseFileUtils.createFileInternally(new File(parent, child), false);
	}

	public boolean createFile(final @NotNull String parent, final @NotNull String child) {
		return BaseFileUtils.createFileInternally(new File(parent, child), false);
	}


	/**
	 * Creates a given Folder and, if not existent, it's parents
	 *
	 * @param file the Folder to be created
	 */
	public boolean createFolder(final @NotNull File file) {
		return BaseFileUtils.createFileInternally(file, true);
	}

	/**
	 * Creates a given Folder and, if not existent, it's parents
	 *
	 * @param file the Folder to be created
	 */
	public boolean createFolder(final @NotNull String file) {
		return BaseFileUtils.createFileInternally(new File(file), true);
	}

	/**
	 * Creates a given Folder and, if not existent, it's parents
	 *
	 * @param parent the parent directory of the file to be created
	 * @param child  the name of the FIle to be created
	 */
	public boolean createFolder(final @NotNull File parent, final @NotNull String child) {
		return BaseFileUtils.createFileInternally(new File(parent, child), true);
	}

	/**
	 * Creates a given Folder and, if not existent, it's parents
	 *
	 * @param parent the parent directory of the file to be created
	 * @param child  the name of the FIle to be created
	 */
	public boolean createFolder(final @NotNull String parent, final @NotNull String child) {
		return BaseFileUtils.createFileInternally(new File(parent, child), true);
	}

	/**
	 * Creates the parents of a given File
	 *
	 * @param file the File to be used
	 */
	public boolean createParents(final @NotNull File file) {
		return BaseFileUtils.createFileInternally(file.getParentFile(), true);
	}

	/**
	 * Creates the parents of a given File
	 *
	 * @param file the File to be used
	 */
	public boolean createParents(final @NotNull String file) {
		return BaseFileUtils.createFileInternally(new File(file).getParentFile(), true);
	}


	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> listFolders(final @NotNull File directory) throws IOException {
		return BaseFileUtils.searchFolders(directory, false, null, false);
	}

	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> listFolders(final @NotNull File directory,
										   final boolean deep) throws IOException {
		return BaseFileUtils.searchFolders(directory, deep, null, false);
	}

	/**
	 * List all folders in a given directory
	 *
	 * @param directory the directory to look into
	 * @param sequence  the String the filename has to contain
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> searchFolders(final @NotNull File directory,
											 final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFolders(directory, false, sequence, false);
	}

	/**
	 * List all folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> searchFolders(final @NotNull File directory,
											 final @Nullable String sequence,
											 final boolean caseSensitive) throws IOException {
		return BaseFileUtils.searchFolders(directory, false, sequence, caseSensitive);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 * @param sequence  the String the filename has to contain (case insensitive)
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> searchFolders(final @NotNull File directory,
											 final boolean deep,
											 final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFolders(directory, deep, sequence, false);
	}

	/**
	 * List all folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the directory that are folders
	 */
	public @NotNull List<File> searchFolders(final @NotNull File directory,
											 final boolean deep,
											 final @Nullable String sequence,
											 final boolean caseSensitive) throws IOException {
		final @NotNull List<File> files = new GapList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null && file.isDirectory()) {
						if (sequence == null
							|| (!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase()))
							|| (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence))) {
							files.add(file);
						}

						if (deep) {
							files.addAll(BaseFileUtils.searchFolders(file, true, sequence, caseSensitive));
						}
					}
				}
			}
			return files;
		} else {
			throw new IOException("File '" + directory.getAbsolutePath() + "' is no directory");
		}
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> listFilesAndFolders(final @NotNull File directory) throws IOException {
		return BaseFileUtils.searchFilesAndFolders(directory, false, null, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> listFilesAndFolders(final @NotNull File directory,
												   final boolean deep) throws IOException {
		return BaseFileUtils.searchFilesAndFolders(directory, deep, null, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param sequence  the String the filename has to contain (case insensitive)
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFilesAndFolders(final @NotNull File directory,
													 final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFilesAndFolders(directory, false, sequence, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFilesAndFolders(final @NotNull File directory,
													 final @Nullable String sequence,
													 final boolean caseSensitive) throws IOException {
		return BaseFileUtils.searchFilesAndFolders(directory, false, sequence, caseSensitive);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 * @param sequence  the String the filename has to contain (case insensitive)
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFilesAndFolders(final @NotNull File directory,
													 final boolean deep,
													 final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFilesAndFolders(directory, deep, sequence, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFilesAndFolders(final @NotNull File directory,
													 final boolean deep,
													 final @Nullable String sequence,
													 final boolean caseSensitive) throws IOException {
		final @NotNull List<File> files = new GapList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (sequence == null
							|| (!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase()))
							|| (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence))) {
							files.add(file);
						}

						if (deep && file.isDirectory()) {
							files.addAll(BaseFileUtils.searchFilesAndFolders(file, true, sequence, caseSensitive));
						}
					}
				}
			}
			return files;
		} else {
			throw new IOException("File '" + directory.getAbsolutePath() + "' is no directory");
		}
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> listFiles(final @NotNull File directory) throws IOException {
		return BaseFileUtils.searchFiles(directory, false, null, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param sequence  the String the filename has to contain (case insensitive)
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFiles(final @NotNull File directory,
										   final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFiles(directory, false, sequence, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFiles(final @NotNull File directory,
										   final @Nullable String sequence,
										   final boolean caseSensitive) throws IOException {
		return BaseFileUtils.searchFiles(directory, false, sequence, caseSensitive);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory the directory to look into
	 * @param deep      also look through subdirectories
	 * @param sequence  the String the filename has to contain (case insensitive)
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFiles(final @NotNull File directory,
										   final boolean deep,
										   final @Nullable String sequence) throws IOException {
		return BaseFileUtils.searchFiles(directory, deep, sequence, false);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 *
	 * @return the files of the given directory
	 */
	public @NotNull List<File> searchFiles(final @NotNull File directory,
										   final boolean deep,
										   final @Nullable String sequence,
										   final boolean caseSensitive) throws IOException {
		final @NotNull List<File> files = new GapList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (file.isFile()
							&& (sequence == null
								|| (!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase()))
								|| (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence)))) {
							files.add(file);
						} else if (deep && file.isDirectory()) {
							files.addAll(BaseFileUtils.searchFiles(file, true, sequence, caseSensitive));
						}
					}
				}
			}
			return files;
		} else {
			throw new IOException("File '" + directory.getAbsolutePath() + "' is no directory");
		}
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> listFilesOfType(final @NotNull File directory,
											   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, null, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> listFilesOfType(final @NotNull File directory,
											   final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, null, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> listFilesOfType(final @NotNull File directory,
											   final boolean deep,
											   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, deep, null, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> listFilesOfType(final @NotNull File directory,
											   final boolean deep,
											   final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, deep, null, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final @Nullable String sequence,
												 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, sequence, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final @Nullable String sequence,
												 final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, sequence, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final @Nullable String sequence,
												 final boolean caseSensitive,
												 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, sequence, caseSensitive, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final @Nullable String sequence,
												 final boolean caseSensitive,
												 final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, false, sequence, caseSensitive, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final boolean deep,
												 final @Nullable String sequence,
												 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, deep, sequence, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param sequence   the String the filename has to contain (case insensitive)
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final boolean deep,
												 final @Nullable String sequence,
												 final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, deep, sequence, false, extensions);
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final boolean deep,
												 final @Nullable String sequence,
												 final boolean caseSensitive,
												 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfType(directory, deep, sequence, caseSensitive, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions
	 */
	public @NotNull List<File> searchFilesOfType(final @NotNull File directory,
												 final boolean deep,
												 final @Nullable String sequence,
												 final boolean caseSensitive,
												 final @NotNull String... extensions) throws IOException {
		final @NotNull List<File> files = new GapList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (file.isFile()
							&& Arrays.stream(extensions).anyMatch(BaseFileUtils.getExtension(file)::equalsIgnoreCase)
							&& (sequence == null
								|| (!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase()))
								|| (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence)))) {
							files.add(file);
						} else if (deep && file.isDirectory()) {
							files.addAll(BaseFileUtils.searchFilesOfType(file, true, sequence, caseSensitive, extensions));
						}
					}
				}
			}
			return files;
		} else {
			throw new IOException("File '" + directory.getAbsolutePath() + "' is no directory");
		}
	}


	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> listFilesOfTypeAndFolders(final @NotNull File directory,
														 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, null, false, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> listFilesOfTypeAndFolders(final @NotNull File directory,
														 final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, null, false, extensions);
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> listFilesOfTypeAndFolders(final @NotNull File directory,
														 final boolean deep,
														 final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, deep, null, false, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> listFilesOfTypeAndFolders(final @NotNull File directory,
														 final boolean deep,
														 final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, deep, null, false, extensions);
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final @Nullable String sequence,
														   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, sequence, false, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param sequence   the String the filename has to contain
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final @Nullable String sequence,
														   final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, sequence, false, extensions);
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final @Nullable String sequence,
														   final boolean caseSensitive,
														   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, sequence, caseSensitive, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final @Nullable String sequence,
														   final boolean caseSensitive,
														   final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, false, sequence, caseSensitive, extensions);
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param sequence   the String the filename has to contain
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final boolean deep,
														   final @Nullable String sequence,
														   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, deep, sequence, false, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory  the directory to look into
	 * @param deep       also look through subdirectories
	 * @param sequence   the String the filename has to contain
	 * @param extensions the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final boolean deep,
														   final @Nullable String sequence,
														   final @NotNull String... extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, deep, sequence, false, extensions);
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final boolean deep,
														   final @Nullable String sequence,
														   final boolean caseSensitive,
														   final @NotNull List<String> extensions) throws IOException {
		return BaseFileUtils.searchFilesOfTypeAndFolders(directory, deep, sequence, caseSensitive, extensions.toArray(new String[0]));
	}

	/**
	 * List all Files and Folders in a given directory
	 *
	 * @param directory     the directory to look into
	 * @param deep          also look through subdirectories
	 * @param sequence      the String the filename has to contain
	 * @param caseSensitive defines, whether @param sequence has to be casesensitive
	 * @param extensions    the file extensions to look for
	 *
	 * @return the files of the given directory with the given extensions and all folders
	 */
	public @NotNull List<File> searchFilesOfTypeAndFolders(final @NotNull File directory,
														   final boolean deep,
														   final @Nullable String sequence,
														   final boolean caseSensitive,
														   final @NotNull String... extensions) throws IOException {
		final @NotNull List<File> files = new GapList<>();
		if (directory.isDirectory()) {
			final @Nullable File[] fileList = directory.listFiles();
			if (fileList != null) {
				for (final @Nullable File file : fileList) {
					if (file != null) {
						if (file.isFile()
							&& Arrays.stream(extensions).anyMatch(BaseFileUtils.getExtension(file)::equalsIgnoreCase)
							&& (sequence == null
								|| (!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase()))
								|| (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence)))) {
							files.add(file);
						} else if (file.isDirectory()) {
							files.add(file);
							if (deep) {
								files.addAll(BaseFileUtils.searchFilesOfTypeAndFolders(file, true, sequence, caseSensitive, extensions));
							}
						}
					}
				}
			}
			return files;
		} else {
			throw new IOException("File '" + directory.getAbsolutePath() + "' is no directory");
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable File file) {
		if (file == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(file));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + file.getAbsolutePath()
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(new File(name)));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + name
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable Path file) {
		if (file == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new FileInputStream(file.toFile()));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + file.toAbsolutePath()
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable String directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory, name)));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory + "/" + name)
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable File directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory, name)));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory.getAbsolutePath() + "/" + name)
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(directory == null ? new FileInputStream(new File(name)) : new FileInputStream(new File(directory.toFile(), name)));
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + (directory == null ? name : directory.toAbsolutePath() + "/" + name)
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromResource(final @Nullable String resource) {
		if (resource == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(Objects.notNull(
						BaseFileUtils.class.getClassLoader()
										   .getResourceAsStream(resource),
						"Resource does not exist"));
			} catch (final @NotNull ObjectNullException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + resource
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromUrl(final @Nullable URL url) {
		if (url == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(url.openStream());
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + url
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStreamFromUrl(final @Nullable String url) {
		if (url == null) {
			return null;
		} else {
			try {
				return new BufferedInputStream(new URL(url).openStream());
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating InputStream from '"
											 + url
											 + "'",
											 e.getCause());
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
	public @Nullable BufferedInputStream createNewInputStream(final @Nullable InputStream inputStream) {
		if (inputStream == null) {
			return null;
		} else if (inputStream instanceof BufferedInputStream) {
			return (BufferedInputStream) inputStream;
		} else {
			return new BufferedInputStream(inputStream);
		}
	}


	public void writeToFile(final @NotNull String file,
							final @Nullable BufferedInputStream inputStream) {
		BaseFileUtils.writeToFile(file, inputStream, BaseFileUtils.getBufferSize());
	}

	public void writeToFile(final @NotNull String parent,
							final @NotNull String child,
							final @Nullable BufferedInputStream inputStream) {
		BaseFileUtils.writeToFile(parent, child, inputStream, BaseFileUtils.getBufferSize());
	}

	public void writeToFile(final @NotNull String file,
							final @Nullable BufferedInputStream inputStream,
							final int bufferSize) {
		BaseFileUtils.writeToFile(new File(file), inputStream, bufferSize);
	}

	public void writeToFile(final @NotNull String parent,
							final @NotNull String child,
							final @Nullable BufferedInputStream inputStream,
							final int bufferSize) {
		BaseFileUtils.writeToFile(new File(parent, child), inputStream, bufferSize);
	}

	/**
	 * Write the contents of a given InputStream to a File
	 *
	 * @param file        the File to be written to
	 * @param inputStream the InputStream which shall be written
	 */
	public void writeToFile(final @NotNull File file,
							final @Nullable BufferedInputStream inputStream) {
		BaseFileUtils.writeToFile(file, inputStream, BaseFileUtils.getBufferSize());
	}

	public void writeToFile(final @NotNull File file,
							final @Nullable BufferedInputStream inputStream,
							final int bufferSize) {
		if (inputStream == null) {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock();
				 final @NotNull BufferedOutputStream outputStream = tempLock.createBufferedOutputStream()) {
				tempLock.lock();
				tempLock.truncateChannel(0);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while clearing '"
											 + file.getAbsolutePath()
											 + "'"
											 + System.lineSeparator()
											 + e.getMessage(),
											 e.getCause());
			}
		} else {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock();
				 final @NotNull BufferedOutputStream outputStream = tempLock.createBufferedOutputStream()) {
				tempLock.lock();
				tempLock.truncateChannel(0);
				final @NotNull byte[] data = new byte[bufferSize];
				int count;
				while ((count = inputStream.read(data, 0, bufferSize)) != -1) {
					outputStream.write(data, 0, count);
				}
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while writing Data to '"
											 + file.getAbsolutePath()
											 + "'"
											 + System.lineSeparator()
											 + e.getMessage(),
											 e.getCause());
			}
		}
	}

	public boolean writeToFileIfCreated(final @NotNull String file,
										final @Nullable BufferedInputStream inputStream) {
		return BaseFileUtils.writeToFileIfCreated(file, inputStream, BaseFileUtils.getBufferSize());
	}

	public boolean writeToFileIfCreated(final @NotNull String parent,
										final @NotNull String child,
										final @Nullable BufferedInputStream inputStream) {
		return BaseFileUtils.writeToFileIfCreated(parent, child, inputStream, BaseFileUtils.getBufferSize());
	}

	public boolean writeToFileIfCreated(final @NotNull String file,
										final @Nullable BufferedInputStream inputStream,
										final int bufferSize) {
		return BaseFileUtils.writeToFileIfCreated(new File(file), inputStream, bufferSize);
	}

	public boolean writeToFileIfCreated(final @NotNull String parent,
										final @NotNull String child,
										final @Nullable BufferedInputStream inputStream,
										final int bufferSize) {
		return BaseFileUtils.writeToFileIfCreated(new File(parent, child), inputStream, bufferSize);
	}

	public boolean writeToFileIfCreated(final @NotNull File file,
										final @Nullable BufferedInputStream inputStream) {
		return BaseFileUtils.writeToFileIfCreated(file, inputStream, BaseFileUtils.getBufferSize());
	}

	public boolean writeToFileIfCreated(final @NotNull File file,
										final @Nullable BufferedInputStream inputStream,
										final int bufferSize) {
		if (!file.exists()) {
			BaseFileUtils.writeToFile(file, inputStream, bufferSize);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param file the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public @NotNull String getExtension(final @NotNull File file) {
		return BaseFileUtils.getExtension(file.getName());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public @NotNull String getExtension(final @NotNull Path filePath) {
		return BaseFileUtils.getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the extension of the given File
	 */
	public @NotNull String getExtension(final @NotNull String filePath) {
		final char ch;
		final int len;
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
			return filePath.substring(dotInd + 1);
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
	public @NotNull File removeExtension(final @NotNull File file) {
		return new File(BaseFileUtils.removeExtension(file.getAbsolutePath()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public @NotNull Path removeExtension(final @NotNull Path filePath) {
		return Paths.get(BaseFileUtils.removeExtension(filePath.toString()));
	}

	/**
	 * Removes the extension of a given File
	 *
	 * @param filePath the Path of the File to be checked
	 *
	 * @return the Path without the extension
	 */
	public @NotNull String removeExtension(final @NotNull String filePath) {
		final char ch;
		final int len;
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
			return filePath.substring(0, dotInd);
		}
	}


	@Contract("null, _ -> false")
	private boolean createFileInternally(final @Nullable File file,
										 final boolean isDirectory) {
		if (file != null && !file.exists() && !isDirectory) {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock()) {
				return true;
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while creating '"
											 + file.getAbsolutePath()
											 + "'"
											 + System.lineSeparator()
											 + e.getMessage(),
											 e.getCause());
			}
		} else if (file != null && !file.exists() && isDirectory) {
			return file.mkdirs();
		} else {
			return false;
		}
	}
}