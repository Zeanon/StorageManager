package de.zeanon.storage.internal.utility.utils.editor;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.BigTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.GapTripletMap;
import de.zeanon.storage.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.exceptions.ThunderException;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class for parsing a Thunder-Type File
 *
 * @author Zeanon
 * @version 2.8.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
public class ThunderEditor {

	/**
	 * Write the given Data to a File
	 *
	 * @param file           the File to be written to
	 * @param fileData       the FileData containing the Data to be written
	 * @param commentSetting the CommentSetting to be used
	 *
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	public static void writeData(final @NotNull File file, final @NotNull ThunderFileData fileData, final @NotNull CommentSetting commentSetting) {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			ThunderEditor.initialWriteWithComments(Objects.notNull(file, "File must not be null"), Objects.notNull(fileData, "Map must not be null"));
		} else {
			ThunderEditor.initialWriteWithOutComments(Objects.notNull(file, "File must not be null"), Objects.notNull(fileData, "Map must not be null"));
		}
	}

	/**
	 * Read the Data of a File
	 *
	 * @param file           the File to be read from
	 * @param commentSetting the CommentSetting to be used
	 *
	 * @return a Map containing the Data of the File
	 *
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ThunderException    if the Content of the File can not be parsed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	public static @NotNull TripletMap<String, Object> readData(final @NotNull File file, final @NotNull CommentSetting commentSetting, final boolean bigMap) throws ThunderException {
		if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
			return ThunderEditor.initialReadWithComments(Objects.notNull(file, "File must not be null"), bigMap);
		} else {
			return ThunderEditor.initialReadWithOutComments(Objects.notNull(file, "File must not be null"), bigMap);
		}
	}


	// <Internal>
	// <Write Data>
	// <Write Data with Comments>
	@Synchronized
	private static void initialWriteWithComments(final @NotNull File file, final @NotNull ThunderFileData fileData) {
		try (final @NotNull PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				final @NotNull Iterator<TripletMap.TripletNode<String, Object>> mapIterator = fileData.blockEntryList().iterator();
				ThunderEditor.topLayerWriteWithComments(writer, mapIterator.next());
				mapIterator.forEachRemaining(entry -> {
					writer.println();
					ThunderEditor.topLayerWriteWithComments(writer, entry);
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithComments(final @NotNull PrintWriter writer,
												  final @NotNull TripletMap.TripletNode<String, Object> entry) {
		if (entry.getValue() == LineType.COMMENT || entry.getValue() == LineType.HEADER || entry.getValue() == LineType.FOOTER) {
			writer.print((entry.getKey().startsWith("#") ? entry.getKey() : "#" + entry.getKey()));
		} else if (entry.getValue() instanceof TripletMap) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			ThunderEditor.internalWriteWithComments((TripletMap<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println(entry.getKey() + " = [");
			//noinspection unchecked
			ThunderEditor.writeList((List<String>) entry.getValue(), "  ", writer);
		} else if (entry.getValue() instanceof Pair) {
			writer.print(entry.getKey() + " = [" + ((Pair) entry.getValue()).getKey() + " : " + ((Pair) entry.getValue()).getValue() + "]");
		} else if (entry.getValue() != LineType.BLANK_LINE) {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}

	private static void internalWriteWithComments(final @NotNull TripletMap<String, Object> map,
												  final @NotNull String indentationString,
												  final @NotNull PrintWriter writer) {
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : map.entryList()) {
			writer.println();
			if (entry.getValue() == LineType.COMMENT || entry.getValue() == LineType.HEADER || entry.getValue() == LineType.FOOTER) {
				writer.print(indentationString + "  " + (entry.getKey().startsWith("#") ? entry.getKey() : "#" + entry.getKey()));
			} else if (entry.getValue() instanceof TripletMap) {
				writer.print(indentationString + "  " + entry.getKey() + " " + "{");
				//noinspection unchecked
				ThunderEditor.internalWriteWithComments((TripletMap<String, Object>) entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof List) {
				writer.println(indentationString + "  " + entry.getKey() + " = [");
				//noinspection unchecked
				ThunderEditor.writeList((List<String>) entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof Pair) {
				writer.print(indentationString + "  " + entry.getKey() + " = [" + ((Pair) entry.getValue()).getKey() + " : " + ((Pair) entry.getValue()).getValue() + "]");
			} else if (entry.getValue() != LineType.BLANK_LINE) {
				writer.print(indentationString + "  " + entry.getKey() + " = " + entry.getValue());
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data with Comments>

	// <Write Data without Comments>
	@Synchronized
	private static void initialWriteWithOutComments(final @NotNull File file,
													final @NotNull ThunderFileData fileData) {
		try (final @NotNull PrintWriter writer = new PrintWriter(file)) {
			if (!fileData.isEmpty()) {
				final @NotNull Iterator<TripletMap.TripletNode<String, Object>> mapIterator = fileData.blockEntryList().iterator();
				@NotNull TripletMap.TripletNode<String, Object> initialEntry = mapIterator.next();
				while (initialEntry.getValue() == LineType.COMMENT || initialEntry.getValue() == LineType.HEADER || initialEntry.getValue() == LineType.FOOTER || initialEntry.getValue() == LineType.BLANK_LINE) {
					initialEntry = mapIterator.next();
				}
				ThunderEditor.topLayerWriteWithOutComments(writer, initialEntry);
				mapIterator.forEachRemaining(entry -> {
					if (entry.getValue() != LineType.COMMENT && entry.getValue() != LineType.HEADER && entry.getValue() != LineType.FOOTER && entry.getValue() != LineType.BLANK_LINE) {
						writer.println();
						ThunderEditor.topLayerWriteWithOutComments(writer, entry);
					}
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static void topLayerWriteWithOutComments(final @NotNull PrintWriter writer,
													 final @NotNull TripletMap.TripletNode<String, Object> entry) {
		if (entry.getValue() instanceof TripletMap) {
			writer.print(entry.getKey() + " " + "{");
			//noinspection unchecked
			ThunderEditor.internalWriteWithoutComments((TripletMap<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof List) {
			writer.println(entry.getKey() + " = [");
			//noinspection unchecked
			ThunderEditor.writeList((List<String>) entry.getValue(), "  ", writer);
		} else if (entry.getValue() instanceof Pair) {
			writer.print(entry.getKey() + " = [" + ((Pair) entry.getValue()).getKey() + " : " + ((Pair) entry.getValue()).getValue() + "]");
		} else {
			writer.print(entry.getKey() + " = " + entry.getValue());
		}
	}

	private static void internalWriteWithoutComments(final @NotNull TripletMap<String, Object> map,
													 final @NotNull String indentationString,
													 final @NotNull PrintWriter writer) {
		for (final @NotNull TripletMap.TripletNode<String, Object> entry : map.entryList()) {
			if (entry.getValue() != LineType.COMMENT && entry.getValue() != LineType.HEADER && entry.getValue() != LineType.FOOTER && entry.getValue() != LineType.BLANK_LINE) {
				writer.println();
				if (entry.getValue() instanceof TripletMap) {
					writer.print(indentationString + "  " + entry.getKey() + " " + "{");
					//noinspection unchecked
					ThunderEditor.internalWriteWithoutComments((TripletMap<String, Object>) entry.getValue(), indentationString + "  ", writer);
				} else if (entry.getValue() instanceof List) {
					writer.println(indentationString + "  " + entry.getKey() + " = [");
					//noinspection unchecked
					ThunderEditor.writeList((List<String>) entry.getValue(), indentationString + "  ", writer);
				} else if (entry.getValue() instanceof Pair) {
					writer.print(indentationString + "  " + entry.getKey() + " = [" + ((Pair) entry.getValue()).getKey() + " : " + ((Pair) entry.getValue()).getValue() + "]");
				} else {
					writer.print(indentationString + "  " + entry.getKey() + " = " + entry.getValue());
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data without Comments>

	private static <E> void writeList(final @NotNull List<E> list,
									  final @NotNull String indentationString,
									  final @NotNull PrintWriter writer) {
		for (final @NotNull E line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}
	// </Write Data>


	// <Read Data>
	// <Read Data with Comments>
	private static @NotNull TripletMap<String, Object> initialReadWithComments(final @NotNull File file,
																			   final boolean bigMap) throws ThunderException {
		try {
			final @NotNull List<String> lines = Files.readAllLines(file.toPath());
			final @NotNull TripletMap<String, Object> tempMap = bigMap ? new BigTripletMap<>() : new GapTripletMap<>();

			@NotNull String tempLine;
			@Nullable String tempKey = null;
			while (!lines.isEmpty()) {
				tempLine = lines.get(0).trim();
				lines.remove(0);

				if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					tempMap.add(tempLine, LineType.BLANK_LINE);
				} else if (tempLine.startsWith("#")) {
					tempMap.add(tempLine, LineType.COMMENT);
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.add(tempKey, ThunderEditor.internalReadWithComments(file.getAbsolutePath(), lines, bigMap));
				} else {
					tempKey = ThunderEditor.readKey(file.getAbsolutePath(), lines, tempMap, tempLine, bigMap, tempKey);
				}
			}
			tempMap.trimToSize();
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ThunderException("Error while parsing content of '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static @NotNull TripletMap<String, Object> internalReadWithComments(final @NotNull String filePath,
																				final @NotNull List<String> lines,
																				final boolean bigMap) throws ThunderException {
		final @NotNull TripletMap<String, Object> tempMap = bigMap ? new BigTripletMap<>() : new GapTripletMap<>();

		@NotNull String tempLine;
		@Nullable String tempKey = null;
		while (!lines.isEmpty()) {
			tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				tempMap.trimToSize();
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
					tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
				} else if (tempKey == null) {
					throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
				}
				tempMap.add(tempKey, ThunderEditor.internalReadWithComments(filePath, lines, bigMap));
			} else {
				tempKey = ThunderEditor.readKey(filePath, lines, tempMap, tempLine, bigMap, tempKey);
			}
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read Data with Comments>

	// <Read Data without Comments>
	private static @NotNull TripletMap<String, Object> initialReadWithOutComments(final @NotNull File file,
																				  final boolean bigMap) throws ThunderException {
		try {
			final @NotNull List<String> lines = Files.readAllLines(file.toPath());
			final @NotNull TripletMap<String, Object> tempMap = bigMap ? new BigTripletMap<>() : new GapTripletMap<>();

			@NotNull String tempLine;
			@Nullable String tempKey = null;
			while (!lines.isEmpty()) {
				tempLine = lines.get(0).trim();
				lines.remove(0);

				if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
					if (tempLine.contains("}")) {
						throw new ThunderException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
					} else if (tempLine.endsWith("{")) {
						if (!tempLine.equals("{")) {
							tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
						} else if (tempKey == null) {
							throw new ThunderException("Error at '" + file.getAbsolutePath() + "' - > '" + tempLine + "' -> Key must not be null");
						}
						tempMap.add(tempKey, ThunderEditor.internalReadWithOutComments(file.getAbsolutePath(), lines, bigMap));
					} else {
						tempKey = ThunderEditor.readKey(file.getAbsolutePath(), lines, tempMap, tempLine, bigMap, tempKey);
					}
				}
			}
			tempMap.trimToSize();
			return tempMap;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ThunderException("Error while parsing content of '" + file.getAbsolutePath() + "'", e.getCause());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private static @NotNull TripletMap<String, Object> internalReadWithOutComments(final @NotNull String filePath,
																				   final @NotNull List<String> lines,
																				   final boolean bigMap) throws ThunderException {
		final @NotNull TripletMap<String, Object> tempMap = bigMap ? new BigTripletMap<>() : new GapTripletMap<>();

		@NotNull String tempLine;
		@Nullable String tempKey = null;
		while (!lines.isEmpty()) {
			tempLine = lines.get(0).trim();
			lines.remove(0);

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					tempMap.trimToSize();
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new ThunderException("Error at '" + filePath + "' -> " +
											   "Illegal Character placement: '}' only allowed as a single Character in line to close blocks");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
					} else if (tempKey == null) {
						throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' -> Key must not be null");
					}
					tempMap.add(tempKey, ThunderEditor.internalReadWithOutComments(filePath, lines, bigMap));
				} else {
					tempKey = ThunderEditor.readKey(filePath, lines, tempMap, tempLine, bigMap, tempKey);
				}
			}
		}
		throw new ThunderException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read without Comments>

	private static @Nullable String readKey(final @NotNull String filePath,
											final @NotNull List<String> lines,
											final @NotNull TripletMap<String, Object> tempMap,
											final @NotNull String tempLine,
											final boolean bigMap,
											@Nullable String tempKey) throws ThunderException {
		if (tempLine.contains("=")) {
			final @NotNull String[] line = tempLine.split("=", 2);
			line[0] = line[0].trim();
			line[1] = line[1].trim();
			if (line[1].startsWith("[")) {
				if (line[1].endsWith("]")) {
					if (line[1].startsWith("[") && line[1].endsWith("]") && line[1].contains(":")) {
						final @NotNull String[] pair = line[1].substring(1, line[1].length() - 1).split(":");
						if (pair.length > 2) {
							throw new ThunderException("Error at '" + filePath + "' -> '" + tempLine + "' ->  Illegal Object(Pairs may only have two values");
						} else {
							tempMap.add(line[0], new Pair<>(pair[0].trim(), pair[1].trim()));
						}
					} else {
						final @NotNull String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
						final @NotNull List<String> list = new ArrayList<>();
						for (final @NotNull String value : listArray) {
							list.add(value.trim());
						}
						tempMap.add(line[0], list);
					}
				} else {
					tempMap.add(line[0], ThunderEditor.readList(filePath, lines, bigMap));
				}
			} else {
				if (line[1].equalsIgnoreCase("true") || line[1].equalsIgnoreCase("false")) {
					tempMap.add(line[0], line[1].equalsIgnoreCase("true"));
				} else {
					tempMap.add(line[0], line[1]);
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

	private static @NotNull List<String> readList(final @NotNull String filePath,
												  final @NotNull List<String> lines,
												  final boolean bigMap) throws ThunderException {
		@NotNull String tempLine;
		final @NotNull List<String> tempList = bigMap ? new LinkedList<>() : new ArrayList<>();
		while (!lines.isEmpty()) {
			tempLine = lines.get(0).trim();
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
	// </Read Data>
	// </Internal>


	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE,
		HEADER,
		FOOTER
	}
}