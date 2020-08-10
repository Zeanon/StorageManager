package de.zeanon.storagemanager.internal.utility.parser;

import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.exceptions.ThunderException;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import de.zeanon.storagemanager.internal.utility.filelock.ExtendedFileLock;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class for parsing a Thunder-Type File
 *
 * @author Zeanon
 * @version 2.8.0
 */
@UtilityClass
@SuppressWarnings({"unused", "rawtypes"})
public class ThunderFileParser {


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
	public void writeData(final @NotNull File file,
						  final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, //NOSONAR
						  final @NotNull CommentSetting commentSetting,
						  final boolean autoFlush) {
		if (commentSetting == Comment.PRESERVE) {
			ThunderFileParser.initialWriteWithComments(file, fileData, autoFlush);
		} else if (commentSetting == Comment.SKIP) {
			ThunderFileParser.initialWriteWithOutComments(file, fileData, autoFlush);
		} else {
			throw new IllegalArgumentException("Illegal CommentSetting");
		}
	}

	/**
	 * Read the Data of a File
	 *
	 * @param file                the File to be read from
	 * @param commentSetting      the CommentSetting to be used
	 * @param collectionsProvider the Provider to be used to get the Map and List implementations
	 * @param buffer_size         the buffer size to be used with the Reader
	 *
	 * @return a Map containing the Data of the File
	 *
	 * @throws RuntimeIOException  if the File can not be accessed properly
	 * @throws ThunderException    if the Content of the File can not be parsed properly
	 * @throws ObjectNullException if a passed value is null
	 */
	public @NotNull DataMap<String, Object> readData(final @NotNull File file,
													 final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider,
													 final @NotNull CommentSetting commentSetting,
													 final int buffer_size) throws ThunderException {
		try {
			if (commentSetting == Comment.PRESERVE) {
				return ThunderFileParser.initialReadWithComments(file, collectionsProvider, buffer_size);
			} else if (commentSetting == Comment.SKIP) {
				return ThunderFileParser.initialReadWithOutComments(file, collectionsProvider, buffer_size);
			} else {
				throw new IllegalArgumentException("Illegal CommentSetting");
			}
		} catch (ThunderParseException e) {
			throw new ThunderException("Error at '" + file.getAbsolutePath() + "' - > " + e.getMessage(), e.getCause());
		}
	}


	// <Internal>
	// <Write Data>
	// <Write Data with Comments>
	private void initialWriteWithComments(final @NotNull File file,
										  final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, //NOSONAR
										  final boolean autoFlush) {
		if (!fileData.isEmpty()) {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock();
				 final @NotNull PrintWriter writer = tempLock.createPrintWriter(autoFlush)) {
				tempLock.lock();
				tempLock.truncateChannel(0);
				final @NotNull Iterator<DataMap.DataNode<String, Object>> mapIterator = fileData.blockEntryList().iterator();
				ThunderFileParser.topLayerWriteWithComments(writer, mapIterator.next());
				mapIterator.forEachRemaining(entry -> {
					writer.println();
					ThunderFileParser.topLayerWriteWithComments(writer, entry);
				});
				writer.flush();
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
			}
		} else {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock()) {
				tempLock.lock();
				tempLock.truncateChannel(0);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	private void topLayerWriteWithComments(final @NotNull PrintWriter writer,
										   final @NotNull DataMap.DataNode<String, Object> entry) {
		if (entry.getValue() == LineType.COMMENT || entry.getValue() == LineType.HEADER || entry.getValue() == LineType.FOOTER) {
			writer.print(entry.getKey().startsWith("#") ? entry.getKey() : ("#" + entry.getKey()));
		} else if (entry.getValue() instanceof DataMap) {
			writer.print(entry.getKey()
						 + " {");
			//noinspection unchecked
			ThunderFileParser.internalWriteWithComments((DataMap<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof Collection) {
			writer.println(entry.getKey()
						   + " = [");
			//noinspection unchecked
			ThunderFileParser.writeCollection((List<String>) entry.getValue(), "  ", writer);
		} else if (Objects.isArray(entry.getValue())) {
			writer.println(entry.getKey()
						   + " = [");
			ThunderFileParser.writeArray(entry.getValue(), "  ", writer);
		} else if (entry.getValue() instanceof Pair) {
			writer.print(entry.getKey()
						 + " = ["
						 + (((Pair) entry.getValue()).getKey() == null ? ":" : (((Pair) entry.getValue()).getKey() + " :"))
						 + (((Pair) entry.getValue()).getValue() == null ? "]" : (" " + ((Pair) entry.getValue()).getValue() + "]")));
		} else if (entry.getValue() != LineType.BLANK_LINE) {
			writer.print(entry.getKey()
						 + (entry.getValue() == null ? " =" : (" = " + entry.getValue())));
		}
	}

	private void internalWriteWithComments(final @NotNull DataMap<String, Object> map,
										   final @NotNull String indentationString,
										   final @NotNull PrintWriter writer) {
		for (final @NotNull DataMap.DataNode<String, Object> entry : map.entryList()) {
			writer.println();
			if (entry.getValue() == LineType.COMMENT || entry.getValue() == LineType.HEADER || entry.getValue() == LineType.FOOTER) {
				writer.print(indentationString
							 + "  "
							 + (entry.getKey().startsWith("#") ? entry.getKey() : ("#" + entry.getKey())));
			} else if (entry.getValue() instanceof DataMap) {
				writer.print(indentationString
							 + "  "
							 + entry.getKey()
							 + " {");
				//noinspection unchecked
				ThunderFileParser.internalWriteWithComments((DataMap<String, Object>) entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof Collection) {
				writer.println(indentationString
							   + "  "
							   + entry.getKey()
							   + " = [");
				ThunderFileParser.writeCollection((Collection) entry.getValue(), indentationString + "  ", writer);
			} else if (Objects.isArray(entry.getValue())) {
				writer.println(indentationString
							   + "  "
							   + entry.getKey()
							   + " = [");
				ThunderFileParser.writeArray(entry.getValue(), indentationString + "  ", writer);
			} else if (entry.getValue() instanceof Pair) {
				writer.print(indentationString
							 + "  "
							 + entry.getKey()
							 + " = ["
							 + (((Pair) entry.getValue()).getKey() == null ? ":" : (((Pair) entry.getValue()).getKey() + " :"))
							 + (((Pair) entry.getValue()).getValue() == null ? "]" : (" " + ((Pair) entry.getValue()).getValue() + "]")));
			} else if (entry.getValue() != LineType.BLANK_LINE) {
				writer.print(indentationString
							 + "  "
							 + entry.getKey()
							 + (entry.getValue() == null ? " =" : (" = " + entry.getValue())));
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data with Comments>

	// <Write Data without Comments>
	private void initialWriteWithOutComments(final @NotNull File file,
											 final @NotNull ThunderFileData<DataMap, DataMap.DataNode<String, Object>, List> fileData, //NOSONAR
											 final boolean autoFlush) {
		if (!fileData.isEmpty()) {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock();
				 final @NotNull PrintWriter writer = tempLock.createPrintWriter(autoFlush)) {
				tempLock.lock();
				tempLock.truncateChannel(0);
				final @NotNull Iterator<DataMap.DataNode<String, Object>> mapIterator = fileData.blockEntryList().iterator();
				@NotNull DataMap.DataNode<String, Object> initialEntry = mapIterator.next();
				while (initialEntry.getValue() == LineType.COMMENT || initialEntry.getValue() == LineType.HEADER || initialEntry.getValue() == LineType.FOOTER || initialEntry.getValue() == LineType.BLANK_LINE) {
					initialEntry = mapIterator.next();
				}
				ThunderFileParser.topLayerWriteWithOutComments(writer, initialEntry);
				mapIterator.forEachRemaining(entry -> {
					if (entry.getValue() != LineType.COMMENT && entry.getValue() != LineType.HEADER && entry.getValue() != LineType.FOOTER && entry.getValue() != LineType.BLANK_LINE) {
						writer.println();
						ThunderFileParser.topLayerWriteWithOutComments(writer, entry);
					}
				});
				writer.flush();
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
			}
		} else {
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock()) {
				tempLock.lock();
				tempLock.truncateChannel(0);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while writing to '" + file.getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	private void topLayerWriteWithOutComments(final @NotNull PrintWriter writer,
											  final @NotNull DataMap.DataNode<String, Object> entry) {
		if (entry.getValue() instanceof DataMap) {
			writer.print(entry.getKey()
						 + " {");
			//noinspection unchecked
			ThunderFileParser.internalWriteWithoutComments((DataMap<String, Object>) entry.getValue(), "", writer);
		} else if (entry.getValue() instanceof Collection) {
			writer.println(entry.getKey()
						   + " = [");
			//noinspection unchecked
			ThunderFileParser.writeCollection((List<String>) entry.getValue(), "  ", writer);
		} else if (Objects.isArray(entry.getValue())) {
			writer.println(entry.getKey()
						   + " = [");
			ThunderFileParser.writeArray(entry.getValue(), "  ", writer);
		} else if (entry.getValue() instanceof Pair) {
			writer.print(entry.getKey()
						 + " = ["
						 + (((Pair) entry.getValue()).getKey() == null ? ":" : (((Pair) entry.getValue()).getKey() + " :"))
						 + (((Pair) entry.getValue()).getValue() == null ? "]" : (" " + ((Pair) entry.getValue()).getValue() + "]")));
		} else {
			writer.print(entry.getKey()
						 + (entry.getValue() == null ? " =" : (" = " + entry.getValue())));
		}
	}

	private void internalWriteWithoutComments(final @NotNull DataMap<String, Object> map,
											  final @NotNull String indentationString,
											  final @NotNull PrintWriter writer) {
		for (final @NotNull DataMap.DataNode<String, Object> entry : map.entryList()) {
			if (entry.getValue() != LineType.COMMENT && entry.getValue() != LineType.HEADER && entry.getValue() != LineType.FOOTER && entry.getValue() != LineType.BLANK_LINE) {
				writer.println();
				if (entry.getValue() instanceof DataMap) {
					writer.print(indentationString
								 + "  "
								 + entry.getKey()
								 + " {");
					//noinspection unchecked
					ThunderFileParser.internalWriteWithoutComments((DataMap<String, Object>) entry.getValue(), indentationString + "  ", writer);
				} else if (entry.getValue() instanceof Collection) {
					writer.println(indentationString
								   + "  "
								   + entry.getKey()
								   + " = [");
					ThunderFileParser.writeCollection((Collection) entry.getValue(), indentationString + "  ", writer);
				} else if (Objects.isArray(entry.getValue())) {
					writer.println(indentationString
								   + "  "
								   + entry.getKey()
								   + " = [");
					ThunderFileParser.writeArray(entry.getValue(), indentationString + "  ", writer);
				} else if (entry.getValue() instanceof Pair) {
					writer.print(indentationString
								 + "  "
								 + entry.getKey()
								 + " = ["
								 + (((Pair) entry.getValue()).getKey() == null ? ":" : (((Pair) entry.getValue()).getKey() + " :"))
								 + (((Pair) entry.getValue()).getValue() == null ? "]" : (" " + ((Pair) entry.getValue()).getValue() + "]")));
				} else {
					writer.print(indentationString
								 + "  "
								 + entry.getKey()
								 + (entry.getValue() == null ? " =" : (" = " + entry.getValue())));
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data without Comments>

	// <Utilities>
	private void writeCollection(final @NotNull Collection list, //NOSONAR
								 final @NotNull String indentationString,
								 final @NotNull PrintWriter writer) {
		for (final @Nullable Object line : list) {
			writer.println(indentationString
						   + (line == null ? "  -" : ("  - " + line)));
		}
		writer.print(indentationString + "]");
	}

	private void writeArray(final @NotNull Object array,
							final @NotNull String indentationString,
							final @NotNull PrintWriter writer) {
		if (array instanceof Object[]) {
			for (final @Nullable Object line : (Object[]) array) {
				writer.println(indentationString
							   + (line == null ? "  -" : ("  - " + line)));
			}
		} else if (array instanceof boolean[]) {
			for (final boolean line : (boolean[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof byte[]) {
			for (final byte line : (byte[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof short[]) {
			for (final short line : (short[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof char[]) {
			for (final char line : (char[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof int[]) {
			for (final int line : (int[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof long[]) {
			for (final long line : (long[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof float[]) {
			for (final float line : (float[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		} else if (array instanceof double[]) {
			for (final double line : (double[]) array) {
				writer.println(indentationString
							   + "  - "
							   + line);
			}
		}
		writer.print(indentationString + "]");
	}
	// </Utilities>
	// </Write Data>


	// <Read Data>
	// <Read Data with Comments>
	private @NotNull DataMap<String, Object> initialReadWithComments(final @NotNull File file,
																	 final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider,
																	 final int buffer_size) throws ThunderParseException {
		try {
			final @NotNull ListIterator<String> lines;
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file, false).readLock();
				 final @NotNull BufferedReader reader = tempLock.createBufferedReader(buffer_size)) {
				tempLock.lock();
				lines = reader.lines().collect(Collectors.toList()).listIterator();
			}

			//noinspection unchecked
			final @NotNull DataMap<String, Object> tempMap = collectionsProvider.newMap();

			@NotNull String tempLine;
			@Nullable String tempKey = null;
			while (lines.hasNext()) {
				tempLine = lines.next().trim();

				if (tempLine.contains("}")) {
					throw new ThunderParseException("Syntax Error at line '" + lines.previousIndex() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					tempMap.add(tempLine, LineType.BLANK_LINE);
				} else if (tempLine.startsWith("#")) {
					tempMap.add(tempLine, LineType.COMMENT);
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
					} else if (tempKey == null) {
						throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") -> Key must not be null");
					}
					tempMap.add(tempKey, ThunderFileParser.internalReadWithComments(lines, collectionsProvider));
				} else {
					tempKey = ThunderFileParser.readKey(lines, tempMap, tempLine, collectionsProvider);
				}
			}
			tempMap.trimToSize();
			return tempMap;
		} catch (final @NotNull IndexOutOfBoundsException e) {
			throw new ThunderParseException("Could not parse content", e);
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private @NotNull DataMap<String, Object> internalReadWithComments(final @NotNull ListIterator<String> lines,
																	  final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) throws ThunderParseException {
		//noinspection unchecked
		final @NotNull DataMap<String, Object> tempMap = collectionsProvider.newMap();

		@NotNull String tempLine;
		@Nullable String tempKey = null;
		while (lines.hasNext()) {
			tempLine = lines.next().trim();

			if (tempLine.equals("}")) {
				tempMap.trimToSize();
				return tempMap;
			} else if (tempLine.endsWith("}")) {
				ThunderFileParser.readKey(filePath, lines, tempMap, tempLine.substring(0, tempLine.length() - 1), collectionsProvider, tempKey);
				tempMap.trimToSize();
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new ThunderParseException("Syntax Error at line '" + lines.previousIndex() + "' -> Illegal Character placement: '}' only allowed as a single Character in line to close blocks");
			} else if (tempLine.isEmpty()) {
				tempMap.add(tempLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.add(tempLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
				} else if (tempKey == null) {
					throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") -> Key must not be null");
				}
				tempMap.add(tempKey, ThunderFileParser.internalReadWithComments(lines, collectionsProvider));
			} else {
				tempKey = ThunderFileParser.readKey(lines, tempMap, tempLine, collectionsProvider);
			}
		}
		throw new ThunderParseException("Block does not close");
	}
	// </Read Data with Comments>

	// <Read Data without Comments>
	private @NotNull DataMap<String, Object> initialReadWithOutComments(final @NotNull File file,
																		final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider,
																		final int buffer_size) throws ThunderParseException {
		try {
			final @NotNull ListIterator<String> lines;
			try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file, false).readLock();
				 final @NotNull BufferedReader reader = tempLock.createBufferedReader(buffer_size)) {
				tempLock.lock();
				lines = reader.lines().collect(Collectors.toList()).listIterator();
			}

			//noinspection unchecked
			final @NotNull DataMap<String, Object> tempMap = collectionsProvider.newMap();

			@NotNull String tempLine;
			@Nullable String tempKey = null;
			while (lines.hasNext()) {
				tempLine = lines.next().trim();

				if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
					if (tempLine.contains("}")) {
						throw new ThunderParseException("Syntax Error at line '" + lines.previousIndex() + "' -> Block closed without being opened");
					} else if (tempLine.endsWith("{")) {
						if (!tempLine.equals("{")) {
							tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
						} else if (tempKey == null) {
							throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") -> Key must not be null");
						}
						tempMap.add(tempKey, ThunderFileParser.internalReadWithOutComments(lines, collectionsProvider));
					} else {
						tempKey = ThunderFileParser.readKey(lines, tempMap, tempLine, collectionsProvider);
					}
				}
			}
			tempMap.trimToSize();
			return tempMap;
		} catch (final @NotNull IndexOutOfBoundsException e) {
			throw new ThunderParseException("Could not parse content", e);
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while reading content from '" + file.getAbsolutePath() + "'", e.getCause());
		}
	}

	private @NotNull DataMap<String, Object> internalReadWithOutComments(final @NotNull ListIterator<String> lines,
																		 final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) throws ThunderParseException {
		//noinspection unchecked
		final @NotNull DataMap<String, Object> tempMap = collectionsProvider.newMap();

		@NotNull String tempLine;
		@Nullable String tempKey = null;
		while (lines.hasNext()) {
			tempLine = lines.next().trim();

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					tempMap.trimToSize();
					return tempMap;
				} else if (tempLine.endsWith("}")) {
					ThunderFileParser.readKey(filePath, lines, tempMap, tempLine.substring(0, tempLine.length() - 1), collectionsProvider, tempKey);
					tempMap.trimToSize();
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new ThunderParseException("Syntax Error at line '" + lines.previousIndex() + "' -> Illegal Character placement: '}' only allowed as a single Character in line to close blocks");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.substring(0, tempLine.length() - 1).trim();
					} else if (tempKey == null) {
						throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") -> Key must not be null");
					}
					tempMap.add(tempKey, ThunderFileParser.internalReadWithOutComments(lines, collectionsProvider));
				} else {
					tempKey = ThunderFileParser.readKey(lines, tempMap, tempLine, collectionsProvider);
				}
			}
		}
		throw new ThunderParseException("Block does not close");
	}
	// </Read without Comments>

	private @Nullable
	String readKey(final @NotNull ListIterator<String> lines,
				   final @NotNull DataMap<String, Object> tempMap,
				   final @NotNull String tempLine,
				   final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) throws ThunderParseException {
		if (tempLine.contains("=")) {
			final @NotNull String[] line = tempLine.split("=", 2);
			line[0] = line[0].trim();
			line[1] = line[1].trim();
			if (line[1].startsWith("[")) {
				if (line[1].endsWith("]")) {
					if (line[1].startsWith("[") && line[1].endsWith("]") && line[1].contains(":") && !line[1].replaceFirst(":", "").contains(":")) {
						final @NotNull String[] pair = line[1].substring(1, line[1].length() - 1).split(":");
						if (pair.length > 2) {
							throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") ->  Illegal Object(Pairs may only have two values");
						} else if (pair.length < 2) {
							throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") ->  Illegal Object(Pairs need two values");
						} else {
							tempMap.add(line[0], new Pair<>(pair[0].trim(), pair[1].trim()));
							return null;
						}
					} else {
						final @NotNull String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
						//noinspection unchecked
						final @NotNull List<String> list = collectionsProvider.newList();
						for (final @NotNull String value : listArray) {
							list.add(value.trim());
						}
						tempMap.add(line[0], list);
						return null;
					}
				} else {
					tempMap.add(line[0], ThunderFileParser.readList(lines, collectionsProvider));
					return null;
				}
			} else {
				if (line[1].equalsIgnoreCase("true") || line[1].equalsIgnoreCase("false")) {
					tempMap.add(line[0], line[1].equalsIgnoreCase("true"));
				} else {
					tempMap.add(line[0], line[1]);
				}
				return null;
			}
		} else {
			if (lines.next().contains("{")) {
				lines.previous();
				return tempLine;
			} else {
				throw new ThunderParseException("'" + tempLine + "' (line: " + lines.previousIndex() + ") -> Line does not contain value or subblock");
			}
		}
	}

	private @NotNull List<String> readList(final @NotNull ListIterator<String> lines,
										   final @NotNull CollectionsProvider<? extends DataMap, ? extends List> collectionsProvider) throws ThunderParseException {
		@NotNull String tempLine;
		//noinspection unchecked
		final @NotNull List<String> tempList = collectionsProvider.newList();
		while (lines.hasNext()) {
			tempLine = lines.next().trim();
			if (tempLine.startsWith("-")) {
				if (tempLine.endsWith("]")) {
					tempList.add(tempLine.substring(1, tempLine.length() - 1).trim());
					return tempList;
				} else {
					tempList.add(tempLine.substring(1).trim());
				}
			} else if (tempLine.endsWith("]")) {
				return tempList;
			} else {
				throw new ThunderParseException("Syntax Error at '" + tempLine + "' (line: " + lines.previousIndex() + ") -> missing '-'");
			}
		}
		throw new ThunderParseException("Syntax Error at line '" + lines.previousIndex() + "' -> List not closed properly");
	}
	// </Read Data>
	// </Internal>


	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE,
		HEADER,
		FOOTER
	}

	private class ThunderParseException extends Exception {

		private static final long serialVersionUID = -5477666037332663814L;

		public ThunderParseException() {
			super();
		}

		public ThunderParseException(final String message) {
			super(message);
		}

		public ThunderParseException(final Throwable cause) {
			super(cause);
		}

		public ThunderParseException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}
}