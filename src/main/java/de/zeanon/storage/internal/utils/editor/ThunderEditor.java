package de.zeanon.storage.internal.utils.editor;

import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Class for parsing a Thunder-Type File
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThunderEditor {

	/**
	 * Write the given Data to a File.
	 *
	 * @param file           the File to be written to.
	 * @param map            a HashMap containing the Data to be written.
	 * @param commentSetting the CommentSetting to be used.
	 */
	public static void writeData(final @NotNull File file, final @NotNull Map<String, Object> map, final @NotNull CommentSettingBase commentSetting) {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			initialWriteWithComments(file, map);
		} else {
			initialWriteWithOutComments(file, map);
		}
	}

	/**
	 * Read the Data of a File.
	 *
	 * @param file           the File to be read from.
	 * @param dataType       the FileDataType to be used.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a Map containing the Data of the File.
	 */
	public static Map<String, Object> readData(final @NotNull File file, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			return initialReadWithComments(file, dataType, commentSetting);
		} else {
			return initialReadWithOutComments(file, dataType, commentSetting);
		}
	}

	// <Read Data>
	// <Read Data with Comments>
	private static Map<String, Object> initialReadWithComments(final @NotNull File file, final @NotNull DataTypeBase dataType, final @NotNull CommentSettingBase commentSetting) {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

			String tempKey = null;
			int blankLine = 0;
			int commentLine = 0;
			while (!lines.isEmpty()) {
				String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (tempLine.contains("}")) {
					throw new FileParseException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
					blankLine++;
				} else if (tempLine.startsWith("#")) {
					tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
					commentLine++;
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new FileParseException("Error at '" + file.getAbsolutePath() + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.put(tempKey, internalReadWithComments(file.getAbsolutePath(), lines, blankLine, commentLine, dataType, commentSetting));
				} else {
					tempKey = readKey(file.getAbsolutePath(), lines, dataType, tempMap, tempKey, tempLine, commentSetting);
				}
			}
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new FileParseException("Error while reading '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static Map<String, Object> internalReadWithComments(final String filePath, final List<String> lines, int blankLine, int commentLine, final DataTypeBase dataType, final CommentSettingBase commentSetting) {
		Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
		String tempKey = null;

		while (!lines.isEmpty()) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new FileParseException("Error at '" + filePath + "' -> Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
				blankLine++;
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
				commentLine++;
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new FileParseException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
				}
				tempMap.put(tempKey, internalReadWithComments(filePath, lines, blankLine, commentLine, dataType, commentSetting));
			} else {
				tempKey = readKey(filePath, lines, dataType, tempMap, tempKey, tempLine, commentSetting);
			}
		}
		throw new FileParseException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read Data with Comments>

	// <Read Data without Comments>
	private static Map<String, Object> initialReadWithOutComments(final File file, final DataTypeBase dataType, final CommentSettingBase commentSetting) {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

			String tempKey = null;
			while (!lines.isEmpty()) {
				String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
					if (tempLine.contains("}")) {
						throw new FileParseException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
					} else if (tempLine.endsWith("{")) {
						if (!tempLine.equals("{")) {
							tempKey = tempLine.replace("{", "").trim();
						} else if (tempKey == null) {
							throw new FileParseException("Error at '" + file.getAbsolutePath() + "' - > '" + tempLine + "' -> Key must not be null");
						}
						tempMap.put(tempKey, internalReadWithOutComments(file.getAbsolutePath(), lines, dataType, commentSetting));
					} else {
						tempKey = readKey(file.getAbsolutePath(), lines, dataType, tempMap, tempKey, tempLine, commentSetting);
					}
				}
			}
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new FileParseException("Error while reading '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static Map<String, Object> internalReadWithOutComments(final String filePath, final List<String> lines, final DataTypeBase dataType, final CommentSettingBase commentSetting) {
		Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
		String tempKey = null;

		while (!lines.isEmpty()) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new FileParseException("Error at '" + filePath + "' -> Block closed without being opened");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new FileParseException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.put(tempKey, internalReadWithOutComments(filePath, lines, dataType, commentSetting));
				} else {
					tempKey = readKey(filePath, lines, dataType, tempMap, tempKey, tempLine, commentSetting);
				}
			}
		}
		throw new FileParseException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read without Comments>

	private static String readKey(final String filePath, final List<String> lines, final DataTypeBase dataType, final Map<String, Object> tempMap, String tempKey, final String tempLine, final CommentSettingBase commentSetting) {
		if (tempLine.contains("=")) {
			String[] line = tempLine.split("=");
			line[0] = line[0].trim();
			line[1] = line[1].trim();
			if (line[1].startsWith("[")) {
				if (line[1].endsWith("]")) {
					String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
					List<String> list = dataType.getNewDataList(commentSetting, null);
					for (String value : listArray) {
						list.add(value.trim());
					}
					tempMap.put(line[0], list);
				} else {
					tempMap.put(line[0], readList(filePath, lines, dataType, commentSetting));
				}
			} else {
				if (line[1].equalsIgnoreCase("true") || line[1].equalsIgnoreCase("false")) {
					tempMap.put(line[0], line[1].equalsIgnoreCase("true"));
				} else {
					tempMap.put(line[0], line[1]);
				}
			}
		} else {
			if (lines.get(1).contains("{")) {
				tempKey = tempLine;
			} else {
				throw new FileParseException("Error at '" + filePath + "' -> '" + tempLine + "' -> does not contain value or subblock");
			}
		}
		return tempKey;
	}

	private static List<String> readList(final String filePath, final List<String> lines, final DataTypeBase dataType, final CommentSettingBase commentSetting) {
		List<String> tempList = dataType.getNewDataList(commentSetting, null);
		while (!lines.isEmpty()) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);
			if (tempLine.startsWith("-")) {
				tempList.add(tempLine.substring(1).trim());
			} else if (tempLine.endsWith("]")) {
				return tempList;
			} else {
				throw new FileParseException("Error at '" + filePath + "' -> List not closed properly");
			}
		}
		throw new FileParseException("Error at '" + filePath + "' -> List not closed properly");
	}
	// </Read Data>


	// <Write Data>
	// <Write Data with Comments>
	private static void initialWriteWithComments(final File file, final Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithComments(writer, map, (String) mapIterator.next());
				//noinspection unchecked
				mapIterator.forEachRemaining(tempKey -> {
					writer.println();
					topLayerWriteWithComments(writer, map, (String) tempKey);
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Could not write to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithComments(final PrintWriter writer, final Map<String, Object> map, final String tempKey) {
		if (tempKey.startsWith("#") && map.get(tempKey) == LineType.COMMENT) {
			writer.print(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
		} else if (map.get(tempKey) instanceof Map) {
			writer.print(tempKey + " " + "{");
			//noinspection unchecked
			internalWriteWithComments((Map<String, Object>) map.get(tempKey), "", writer);
		} else if (map.get(tempKey) instanceof List) {
			writer.println(tempKey + " = [");
			//noinspection unchecked
			writeList((List<String>) map.get(tempKey), "  ", writer);
		} else if (!tempKey.startsWith("{=}emptyline") && map.get(tempKey) != LineType.BLANK_LINE) {
			writer.print(tempKey + " = " + map.get(tempKey));
		}
	}

	private static void internalWriteWithComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			writer.println();
			if (entry.getKey().startsWith("#") && entry.getValue() == LineType.COMMENT) {
				writer.print(indentationString + "  " + entry.getKey().substring(0, entry.getKey().lastIndexOf("{=}")));
			} else if (entry.getValue() instanceof Map) {
				writer.print(indentationString + "  " + entry.getKey() + " " + "{");
				//noinspection unchecked
				internalWriteWithComments((Map<String, Object>) entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof List) {
				writer.println(indentationString + "  " + entry.getKey() + " = [");
				//noinspection unchecked
				writeList((List<String>) entry.getValue(), indentationString + "  ", writer);
			} else if (!entry.getKey().startsWith("{=}emptyline") && entry.getValue() != LineType.BLANK_LINE) {
				writer.print(indentationString + "  " + entry.getKey() + " = " + entry.getValue());
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data with Comments

	// <Write Data without Comments>
	private static void initialWriteWithOutComments(final File file, final Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				String initialKey = (String) mapIterator.next();
				while (initialKey.startsWith("#") || map.get(initialKey) == LineType.COMMENT || initialKey.startsWith("{=}emptyline") || map.get(initialKey) == LineType.BLANK_LINE) {
					initialKey = (String) mapIterator.next();
				}
				topLayerWriteWithOutComments(writer, map, (String) mapIterator.next());
				//noinspection unchecked
				mapIterator.forEachRemaining(tempKey -> {
					if (!((String) tempKey).startsWith("#") && map.get(tempKey) != LineType.COMMENT && !((String) tempKey).startsWith("{=}emptyline") && map.get(tempKey) != LineType.BLANK_LINE) {
						writer.println();
						topLayerWriteWithOutComments(writer, map, (String) tempKey);
					}
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Could not write to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithOutComments(final PrintWriter writer, final Map<String, Object> map, final String tempKey) {
		if (map.get(tempKey) instanceof Map) {
			writer.print(tempKey + " " + "{");
			//noinspection unchecked
			internalWriteWithoutComments((Map<String, Object>) map.get(tempKey), "", writer);
		} else if (map.get(tempKey) instanceof List) {
			writer.println("  " + tempKey + " = [");
			//noinspection unchecked
			writeList((List<String>) map.get(tempKey), "  ", writer);
		} else {
			writer.print(tempKey + " = " + map.get(tempKey));
		}
	}

	private static void internalWriteWithoutComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (!entry.getKey().startsWith("#") && entry.getValue() != LineType.COMMENT && !entry.getKey().startsWith("{=}emptyline") && entry.getValue() != LineType.BLANK_LINE) {
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
	// </Write Data without Comments>

	private static void writeList(final List<String> list, final String indentationString, final PrintWriter writer) {
		for (String line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}
	// </Write Data>


	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}