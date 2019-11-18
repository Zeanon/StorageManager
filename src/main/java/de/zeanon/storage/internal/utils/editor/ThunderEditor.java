package de.zeanon.storage.internal.utils.editor;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
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
	public static void writeData(final @NotNull File file, final @NotNull FileData<String, Pair<Integer, String>, Object> fileData, final @NotNull CommentSettingBase commentSetting) {
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
	 * @param dataType       the FileDataType to be used
	 * @param commentSetting the CommentSetting to be used
	 * @return a Map containing the Data of the File
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ThunderException    if the Content of the File can not be parsed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	@NotNull
	public static Map<Pair<Integer, String>, Object> readData(final @NotNull File file, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			return initialReadWithComments(Objects.notNull(file, "File must not be null"), Objects.notNull(dataType, "DataType must not be null"), Objects.notNull(commentSetting, "CommentSetting must not be null"));
		} else {
			return initialReadWithOutComments(Objects.notNull(file, "File must not be null"), Objects.notNull(dataType, "DataType must not be null"), Objects.notNull(commentSetting, "CommentSetting must not be null"));
		}
	}


	// <Read Data>
	// <Read Data with Comments>
	@NotNull
	private static Map<Pair<Integer, String>, Object> initialReadWithComments(final @NotNull File file, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			@NotNull Map<Pair<Integer, String>, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

			@Nullable String tempKey = null;
			int line = 0;
			while (!lines.isEmpty()) {
				@NotNull String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					tempMap.put(new Pair<>(line, ""), LineType.BLANK_LINE);
				} else if (tempLine.startsWith("#")) {
					tempMap.put(new Pair<>(line, tempLine), LineType.COMMENT);
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.put(new Pair<>(line, tempKey), internalReadWithComments(file.getAbsolutePath(), lines, line, dataType, commentSetting));
				} else {
					tempKey = readKey(file.getAbsolutePath(), lines, dataType, tempMap, tempKey, tempLine, commentSetting, line);
				}
				line++;
			}
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ThunderException("Error while parsing content of '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}
	// </Read Data with Comments>

	@NotNull
	private static Map<Pair<Integer, String>, Object> internalReadWithComments(final @NotNull String filePath, final @NotNull List<String> lines, int line, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		@NotNull Map<Pair<Integer, String>, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
		@Nullable String tempKey = null;

		while (!lines.isEmpty()) {
			@NotNull String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new ThunderException("Error at '" + filePath + "' -> Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				tempMap.put(new Pair<>(line, ""), LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(new Pair<>(line, tempLine), LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
				}
				tempMap.put(new Pair<>(line, tempKey), internalReadWithComments(filePath, lines, line, dataType, commentSetting));
			} else {
				tempKey = readKey(filePath, lines, dataType, tempMap, tempKey, tempLine, commentSetting, line);
			}
			line++;
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}

	// <Read Data without Comments>
	@NotNull
	private static Map<Pair<Integer, String>, Object> initialReadWithOutComments(final @NotNull File file, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			@NotNull Map<Pair<Integer, String>, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

			@Nullable String tempKey = null;
			int line = 0;
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
						tempMap.put(new Pair<>(line, tempKey), internalReadWithOutComments(file.getAbsolutePath(), lines, dataType, commentSetting, line));
					} else {
						tempKey = readKey(file.getAbsolutePath(), lines, dataType, tempMap, tempKey, tempLine, commentSetting, line);
					}
				}
				line++;
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
	private static Map<Pair<Integer, String>, Object> internalReadWithOutComments(final @NotNull String filePath, final @NotNull List<String> lines, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting, int line) throws ThunderException {
		@NotNull Map<Pair<Integer, String>, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
		@Nullable String tempKey = null;

		while (!lines.isEmpty()) {
			@NotNull String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + filePath + "' -> Block closed without being opened");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.put(new Pair<>(line, tempKey), internalReadWithOutComments(filePath, lines, dataType, commentSetting, line));
				} else {
					tempKey = readKey(filePath, lines, dataType, tempMap, tempKey, tempLine, commentSetting, line);
				}
			}
			line++;
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}

	private static String readKey(final @NotNull String filePath, final @NotNull List<String> lines, final @NotNull DataTypeBase dataType, final @NotNull Map<Pair<Integer, String>, Object> tempMap, String tempKey, final @NotNull String tempLine, final @NotNull CommentSettingBase commentSetting, final int lineId) throws ThunderException {
		if (tempLine.contains("=")) {
			@NotNull String[] line = split(tempLine, "=");
			if (line.length > 2) {
				throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> illegal amount of delimiters");
			}
			line[0] = line[0].trim();
			line[1] = line[1].trim();
			if (line[1].startsWith("[")) {
				if (line[1].endsWith("]")) {
					@NotNull String[] listArray = split(line[1].substring(1, line[1].length() - 1), ",");
					@NotNull List<String> list = dataType.getNewDataList(commentSetting, null);
					for (@NotNull String value : listArray) {
						list.add(value.trim().replace("\"", ""));
					}
					tempMap.put(new Pair<>(lineId, line[0]), list);
				} else {
					tempMap.put(new Pair<>(lineId, line[0]), readList(filePath, lines, dataType, commentSetting));
				}
			} else {
				line[1] = line[1].replace("\"", "");
				if (line[1].equalsIgnoreCase("true") || line[1].equalsIgnoreCase("false")) {
					tempMap.put(new Pair<>(lineId, line[0]), line[1].equalsIgnoreCase("true"));
				} else {
					tempMap.put(new Pair<>(lineId, line[0]), line[1]);
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
	private static List<String> readList(final String filePath, final @NotNull List<String> lines, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) throws ThunderException {
		@NotNull List<String> tempList = dataType.getNewDataList(commentSetting, null);
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
	private static void initialWriteWithComments(final @NotNull File file, final @NotNull FileData<String, Pair<Integer, String>, Object> fileData) {
		try (@NotNull final PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				@NotNull final Iterator<FileData.Entry<String, Object>> mapIterator = fileData.entrySet().iterator();
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

	private static void topLayerWriteWithComments(final @NotNull PrintWriter writer, final @NotNull FileData.Entry<String, Object> entry) {
		if (entry.getKey().startsWith("#") && entry.getValue() == LineType.COMMENT) {
			writer.print(entry.getKey());
		} else if (entry.getValue() instanceof Map) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			internalWriteWithComments((Map<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println(entry.getKey() + " = [");
			//noinspection unchecked
			writeList((List<String>) entry.getValue(), "  ", writer);
		} else if (!entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}
	// </Write Data with Comments

	private static void internalWriteWithComments(final @NotNull Map<String, Object> map, final String indentationString, final @NotNull PrintWriter writer) {
		for (@NotNull final Map.Entry<String, Object> entry : map.entrySet()) {
			writer.println();
			if (entry.getKey().startsWith("#") && entry.getValue() == LineType.COMMENT) {
				writer.print(indentationString + "  " + entry.getKey());
			} else if (entry.getValue() instanceof Map) {
				writer.print(indentationString + "  " + entry.getKey() + " " + "{");
				//noinspection unchecked
				internalWriteWithComments((Map<String, Object>) entry.getValue(), indentationString + "  ", writer);
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
	private static void initialWriteWithOutComments(final @NotNull File file, final @NotNull FileData<String, Pair<Integer, String>, Object> fileData) {
		try (@NotNull final PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				@NotNull Iterator<FileData.Entry<String, Object>> mapIterator = fileData.entrySet().iterator();
				FileData.Entry<String, Object> initialEntry = mapIterator.next();
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

	private static void topLayerWriteWithOutComments(final @NotNull PrintWriter writer, final @NotNull FileData.Entry<String, Object> entry) {
		if (entry.getValue() instanceof Map) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			internalWriteWithoutComments((Map<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println("  " + entry.getKey() + " = [");
			//noinspection unchecked
			writeList((List<String>) entry.getValue(), "  ", writer);
		} else {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}
	// </Write Data without Comments>

	private static void internalWriteWithoutComments(final @NotNull Map<String, Object> map, final String indentationString, final @NotNull PrintWriter writer) {
		for (@NotNull final Map.Entry<String, Object> entry : map.entrySet()) {
			if (!entry.getKey().startsWith("#") && entry.getValue() != LineType.COMMENT && !entry.getKey().equals("") && entry.getValue() != LineType.BLANK_LINE) {
				writer.println();
				if (entry.getValue() instanceof Map) {
					writer.print(indentationString + "  " + entry.getKey() + " " + "{");
					//noinspection unchecked
					internalWriteWithoutComments((Map<String, Object>) entry.getValue(), indentationString + "  ", writer);
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


	// <Utility>
	@NotNull
	private static String[] split(final @NotNull String string, final @NotNull CharSequence delimiter) {
		@NotNull List<String> tempList = new ArrayList<>();
		@NotNull char[] chars = string.toCharArray();
		int splitStart = 0;
		boolean outOfString = true;
		for (int i = 0; i < string.length(); i++) {
			if (chars[i] == '\"') {
				outOfString = !outOfString;
			} else if (outOfString && chars[i] == delimiter.charAt(0)) {
				boolean split = true;
				for (int i1 = 1; i < delimiter.length(); i1++) {
					if (chars[i + i1] != delimiter.charAt(i1)) {
						split = false;
						break;
					}
				}
				if (split) {
					tempList.add(string.substring(splitStart, i));
					i += delimiter.length();
					splitStart = i;
				}
			}
		}
		if (splitStart != chars.length) {
			tempList.add(string.substring(splitStart, chars.length));
		}
		return tempList.toArray(new String[0]);
	}
	// </Utility>


	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}