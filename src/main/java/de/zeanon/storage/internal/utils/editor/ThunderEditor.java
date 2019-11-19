package de.zeanon.storage.internal.utils.editor;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.maps.DataMap;
import de.zeanon.storage.internal.data.cache.ThunderFileData;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class for parsing a Thunder-Type File
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThunderEditor {

	/**
	 * Write the given Data to a File
	 *
	 * @param file           the File to be written to
	 * @param fileData       the FileData containing the Data to be written
	 * @param commentSetting the CommentSetting to be used
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	public static void writeData(final @NotNull File file, final @NotNull ThunderFileData fileData, final @NotNull CommentSettingBase commentSetting) {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			initialWriteWithComments(Objects.notNull(file, "File must not be null"), Objects.notNull(fileData, "Map must not be null"));
		} else {
			initialWriteWithOutComments(Objects.notNull(file, "File must not be null"), Objects.notNull(fileData, "Map must not be null"));
		}
	}

	/**
	 * Read the Data of a File
	 *
	 * @param file           the File to be read from
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map containing the Data of the File
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ThunderException    if the Content of the File can not be parsed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	@NotNull
	public static DataMap<String, Object> readData(final @NotNull File file, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			return initialReadWithComments(Objects.notNull(file, "File must not be null"));
		} else {
			return initialReadWithOutComments(Objects.notNull(file, "File must not be null"));
		}
	}


	// <Read Data>
	// <Read Data with Comments>
	@NotNull
	private static DataMap<String, Object> initialReadWithComments(final @NotNull File file) throws ThunderException {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			@NotNull DataMap<String, Object> tempMap = new DataMap<>();

			@Nullable String tempKey = null;
			while (!lines.isEmpty()) {
				@NotNull String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					tempMap.add(tempLine, LineType.BLANK_LINE);
				} else if (tempLine.startsWith("#")) {
					tempMap.add(tempLine, LineType.COMMENT);
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.add(tempKey, internalReadWithComments(file.getAbsolutePath(), lines));
				} else {
					tempKey = readKey(file.getAbsolutePath(), lines, tempMap, tempKey, tempLine);
				}
			}
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ThunderException("Error while parsing content of '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	@NotNull
	private static DataMap<String, Object> internalReadWithComments(final @NotNull String filePath, final @NotNull List<String> lines) throws ThunderException {
		@NotNull DataMap<String, Object> tempMap = new DataMap<>();
		@Nullable String tempKey = null;

		while (!lines.isEmpty()) {
			@NotNull String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new ThunderException("Error at '" + filePath + "' -> " +
										   "Illegal Character placement: '}' only allowed as a single Character in line to close blocks");
			} else if (tempLine.isEmpty()) {
				tempMap.add(tempLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.add(tempLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
				}
				tempMap.add(tempKey, internalReadWithComments(filePath, lines));
			} else {
				tempKey = readKey(filePath, lines, tempMap, tempKey, tempLine);
			}
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read Data with Comments>

	// <Read Data without Comments>
	@NotNull
	private static DataMap<String, Object> initialReadWithOutComments(final @NotNull File file) throws ThunderException {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			@NotNull DataMap<String, Object> tempMap = new DataMap<>();

			@Nullable String tempKey = null;
			while (!lines.isEmpty()) {
				@NotNull String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
					if (tempLine.contains("}")) {
						throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
					} else if (tempLine.endsWith("{")) {
						if (!tempLine.equals("{")) {
							tempKey = tempLine.replace("{", "").trim();
						} else if (tempKey == null) {
							throw new ThunderException("Error at '" + file.getAbsolutePath() + "' - > '" + tempLine + "' -> Key must not be null");
						}
						tempMap.add(tempKey, internalReadWithOutComments(file.getAbsolutePath(), lines));
					} else {
						tempKey = readKey(file.getAbsolutePath(), lines, tempMap, tempKey, tempLine);
					}
				}
			}
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ThunderException("Error while parsing content of '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}
	// </Read without Comments>

	@NotNull
	private static DataMap<String, Object> internalReadWithOutComments(final @NotNull String filePath, final @NotNull List<String> lines) throws ThunderException {
		@NotNull DataMap<String, Object> tempMap = new DataMap<>();
		@Nullable String tempKey = null;

		while (!lines.isEmpty()) {
			@NotNull String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + filePath + "' -> " +
											   "Illegal Character placement: '}' only allowed as a single Character in line to close blocks");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.add(tempKey, internalReadWithOutComments(filePath, lines));
				} else {
					tempKey = readKey(filePath, lines, tempMap, tempKey, tempLine);
				}
			}
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}

	@Nullable
	private static String readKey(final @NotNull String filePath,
								  final @NotNull List<String> lines,
								  final @NotNull DataMap<String, Object> tempMap,
								  @Nullable String tempKey,
								  final @NotNull String tempLine) throws ThunderException {
		if (tempLine.contains("=")) {
			@NotNull String[] splitLine = tempLine.split("=", 2);
			splitLine[0] = splitLine[0].trim();
			splitLine[1] = splitLine[1].trim();
			if (splitLine[1].startsWith("[")) {
				if (splitLine[1].endsWith("]")) {
					@NotNull String[] listArray = splitLine[1].substring(1, splitLine[1].length() - 1).split(",");
					@NotNull List<String> list = new LinkedList<>();
					for (@NotNull String value : listArray) {
						list.add(value.trim());
					}
					tempMap.add(splitLine[0], list);
				} else {
					tempMap.add(splitLine[0], readList(filePath, lines));
				}
			} else {
				if (splitLine[1].equalsIgnoreCase("true") || splitLine[1].equalsIgnoreCase("false")) {
					tempMap.add(splitLine[0], splitLine[1].equalsIgnoreCase("true"));
				} else {
					tempMap.add(splitLine[0], splitLine[1]);
				}
			}
		} else {
			if (lines.get(1).contains("{")) {
				tempKey = tempLine;
			} else {
				throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> does not contain value or subblock");
			}
		}
		return tempKey;
	}
	// </Read Data>

	@NotNull
	private static List<String> readList(final String filePath, final @NotNull List<String> lines) throws ThunderException {
		@NotNull List<String> tempList = new LinkedList<>();
		while (!lines.isEmpty()) {
			@NotNull String tempLine = lines.get(0).trim();
			lines.remove(0);
			if (tempLine.startsWith("-")) {
				tempList.add(tempLine.substring(1).trim().replace("\"", ""));
			} else if (tempLine.endsWith("]")) {
				return tempList;
			} else {
				throw new ThunderException("Error at '" + filePath + "' -> List not closed properly");
			}
		}
		throw new ThunderException("Error at '" + filePath + "' -> List not closed properly");
	}

	// <Write Data>
	// <Write Data with Comments>
	private static void initialWriteWithComments(final @NotNull File file, final @NotNull ThunderFileData fileData) {
		try (@NotNull final PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				@NotNull final Iterator<DataMap.Entry<String, Object>> mapIterator = fileData.entryList().iterator();
				topLayerWriteWithComments(writer, mapIterator.next());
				mapIterator.forEachRemaining(entry -> {
					writer.println();
					topLayerWriteWithComments(writer, entry);
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Could not write to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithComments(final @NotNull PrintWriter writer, final @NotNull DataMap.Entry<String, Object> entry) {
		if (entry.getKey().startsWith("#") && entry.getValue() == LineType.COMMENT) {
			writer.print(entry.getKey());
		} else if (entry.getValue() instanceof DataMap) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			internalWriteWithComments((List<DataMap.Entry<String, Object>>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println(entry.getKey() + " = [");
			//noinspection unchecked
			writeList((List<String>) entry.getValue(), "  ", writer);
		} else if (!entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}
	// </Write Data with Comments

	private static void internalWriteWithComments(final @NotNull List<DataMap.Entry<String, Object>> entryList, final String indentationString, final @NotNull PrintWriter writer) {
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			writer.println();
			if (entry.getKey().startsWith("#") && entry.getValue() == LineType.COMMENT) {
				writer.print(indentationString + "  " + entry.getKey());
			} else if (entry.getValue() instanceof DataMap) {
				writer.print(indentationString + "  " + entry.getKey() + " " + "{");
				//noinspection unchecked
				internalWriteWithComments((List<DataMap.Entry<String, Object>>) entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof List) {
				writer.println(indentationString + "  " + entry.getKey() + " = [");
				//noinspection unchecked
				writeList((List<String>) entry.getValue(), indentationString + "  ", writer);
			} else if (!entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
				writer.print(indentationString + "  " + entry.getKey() + " = " + entry.getValue());
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}

	// <Write Data without Comments>
	private static void initialWriteWithOutComments(final @NotNull File file, final @NotNull ThunderFileData fileData) {
		try (@NotNull final PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				@NotNull Iterator<DataMap.Entry<String, Object>> mapIterator = fileData.entryList().iterator();
				DataMap.Entry<String, Object> initialEntry = mapIterator.next();
				while (initialEntry.getKey().startsWith("#") || initialEntry.getValue() == LineType.COMMENT || initialEntry.getKey().equals("") || initialEntry.getValue() == LineType.BLANK_LINE) {
					initialEntry = mapIterator.next();
				}
				topLayerWriteWithOutComments(writer, initialEntry);
				mapIterator.forEachRemaining(entry -> {
					if (!entry.getKey().startsWith("#") && entry.getValue() != LineType.COMMENT && !entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
						writer.println();
						topLayerWriteWithOutComments(writer, entry);
					}
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Could not write to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithOutComments(final @NotNull PrintWriter writer, final @NotNull DataMap.Entry<String, Object> entry) {
		if (entry.getValue() instanceof DataMap) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			internalWriteWithoutComments((List<DataMap.Entry<String, Object>>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println("  " + entry.getKey() + " = [");
			//noinspection unchecked
			writeList((List<String>) entry.getValue(), "  ", writer);
		} else {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}
	// </Write Data without Comments>

	private static void internalWriteWithoutComments(final @NotNull List<DataMap.Entry<String, Object>> entryList, final String indentationString, final @NotNull PrintWriter writer) {
		for (@NotNull final DataMap.Entry<String, Object> entry : entryList) {
			if (!entry.getKey().startsWith("#") && entry.getValue() != LineType.COMMENT && !entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
				writer.println();
				if (entry.getValue() instanceof DataMap) {
					writer.print(indentationString + "  " + entry.getKey() + " " + "{");
					//noinspection unchecked
					internalWriteWithoutComments((List<DataMap.Entry<String, Object>>) entry.getValue(), indentationString + "  ", writer);
				} else if (entry.getValue() instanceof List) {
					writer.println(indentationString + "  " + entry.getKey() + " = [");
					//noinspection unchecked
					writeList((List<String>) entry.getValue(), indentationString + "  ", writer);
				} else {
					writer.print(indentationString + "  " + entry.getKey() + " = " + entry.getValue());
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data>

	private static void writeList(final @NotNull List<String> list, final String indentationString, final @NotNull PrintWriter writer) {
		for (final String line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}


	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}
