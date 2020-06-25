package de.zeanon.storagemanager.internal.utility.editor;

import com.esotericsoftware.yamlbeans.YamlWriter;
import de.zeanon.storagemanager.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.utility.datafiles.YamlUtils;
import de.zeanon.storagemanager.internal.utility.filelock.ExtendedFileLock;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Class for parsing a Yaml-Type File
 *
 * @author Zeanon
 * @version 1.4.0
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess", "rawtypes"})
public class YamlEditor {


	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readComments(final @NotNull File file,
							  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getCommentsFromLines(YamlEditor.read(file), collectionsProvider);
	}

	@Contract("null -> fail; _ -> new")
	public @NotNull
	List<String> read(final @NotNull File file) throws IOException {
		return YamlEditor.read(file, 8192);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> read(final @NotNull File file,
					  final @NotNull ReadWriteFileLock tempLock) throws IOException {
		return YamlEditor.read(file, 8192, tempLock);
	}

	@Contract("null, _ -> fail; _, _ -> new")
	public @NotNull
	List<String> read(final @NotNull File file,
					  final int buffer_size) throws IOException {
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file, false).readLock();
			 final @NotNull BufferedReader reader = tempLock.createBufferedReader(buffer_size)) {
			tempLock.lock();
			return reader.lines().collect(Collectors.toList());
		}
	}

	@Contract("null, _, _ -> fail; _, _, null -> fail; _, _, _ -> new")
	public @NotNull
	List<String> read(final @NotNull File file,
					  final int buffer_size,
					  final @NotNull ReadWriteFileLock tempLock) throws IOException {
		try (final @NotNull BufferedReader reader = tempLock.createBufferedReader(buffer_size)) {
			return reader.lines().collect(Collectors.toList());
		}
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readFooter(final @NotNull File file,
							final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getFooterFromLines(YamlEditor.read(file), collectionsProvider);
	}

	@Contract("null, _, _ -> fail; _, null, _ -> fail; _, _, null -> fail; _, _, _ -> new")
	public @NotNull
	List<String> readFooter(final @NotNull File file,
							final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider,
							final @NotNull ReadWriteFileLock tempLock) throws IOException {
		return YamlEditor.getFooterFromLines(YamlEditor.read(file, tempLock), collectionsProvider);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readHeader(final @NotNull File file,
							final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getHeaderFromLines(YamlEditor.read(file), collectionsProvider);
	}

	@Contract("null, _, _ -> fail; _, null, _ -> fail; _, _, null -> fail; _, _, _ -> new")
	public @NotNull
	List<String> readHeader(final @NotNull File file,
							final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider,
							final @NotNull ReadWriteFileLock tempLock) throws IOException {
		return YamlEditor.getHeaderFromLines(YamlEditor.read(file, tempLock), collectionsProvider);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readKeys(final @NotNull File file,
						  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getKeys(YamlEditor.read(file), collectionsProvider);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readPureComments(final @NotNull File file,
								  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getPureCommentsFromLines(YamlEditor.read(file), collectionsProvider);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	public @NotNull
	List<String> readWithoutHeaderAndFooter(final @NotNull File file,
											final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) throws IOException {
		return YamlEditor.getLinesWithoutFooterAndHeaderFromLines(YamlEditor.read(file), collectionsProvider);
	}


	@Contract("null, _ -> fail; _, null -> fail")
	public void write(final @NotNull File file,
					  final @NotNull List<String> lines) throws IOException {
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock();
			 final @NotNull PrintWriter writer = tempLock.createPrintWriter()) {
			tempLock.lock();
			tempLock.truncateChannel(0);
			final @NotNull Iterator<String> lineIterator = lines.iterator();
			writer.print(lineIterator.next());
			lineIterator.forEachRemaining(line -> {
				writer.println();
				writer.print(line);
			});
		}
	}

	@Contract("null, _ -> fail; _, null -> fail")
	public void write(final @NotNull List<String> lines,
					  final @NotNull ReadWriteFileLock tempLock) throws IOException {
		try (final @NotNull PrintWriter writer = tempLock.createPrintWriter()) {
			tempLock.truncateChannel(0);
			final @NotNull Iterator<String> lineIterator = lines.iterator();
			writer.print(lineIterator.next());
			lineIterator.forEachRemaining(line -> {
				writer.println();
				writer.print(line);
			});
		}
	}

	@Contract("null, _, _, _ -> fail; _, null, _, _ -> fail; _, _, null, _ -> fail; _, _, _, null -> fail")
	public void writeData(final @NotNull File file,
						  final @NotNull Map fileData, //NOSONAR
						  final @NotNull CommentSetting commentSetting,
						  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		try (final @NotNull ReadWriteFileLock tempLock = new ExtendedFileLock(file).writeLock()) {
			tempLock.lock();
			if (commentSetting != Comment.PRESERVE) {
				YamlEditor.write(fileData, false, tempLock);
			} else {
				final @NotNull List<String> unEdited = YamlEditor.read(file, tempLock);
				final @NotNull List<String> header = YamlEditor.readHeader(file, collectionsProvider, tempLock);
				final @NotNull List<String> footer = YamlEditor.readFooter(file, collectionsProvider, tempLock);
				YamlEditor.write(fileData, true, tempLock);
				header.addAll(YamlEditor.read(file, tempLock));
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				YamlEditor.write(YamlUtils.parseComments(unEdited, header, collectionsProvider), tempLock);
			}
		} catch (final @NotNull IOException e) {
			throw new RuntimeIOException("Error while writing to "
										 + file.getAbsolutePath()
										 + "'",
										 e.getCause());
		}
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> param1")
	public @NotNull
	List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines,
														 final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		final @NotNull List<String> header = YamlEditor.getHeaderFromLines(lines, collectionsProvider);
		final @NotNull List<String> footer = YamlEditor.getFooterFromLines(lines, collectionsProvider);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	@Contract("null, _, _ -> fail; _, _, null -> fail")
	private void write(final @NotNull Map fileData, //NOSONAR
					   final boolean keepBeanOrder,
					   final @NotNull ReadWriteFileLock tempLock) throws IOException {
		final @NotNull @Cleanup YamlWriter writer = new YamlWriter(tempLock.createWriter());
		writer.getConfig().writeConfig.setKeepBeanPropertyOrder(keepBeanOrder);
		writer.write(fileData);
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	private @NotNull
	List<String> getCommentsFromLines(final @NotNull List<String> lines,
									  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull String line : lines) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	private @NotNull
	List<String> getFooterFromLines(final @NotNull List<String> lines,
									final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		Collections.reverse(lines);
		for (final @NotNull String line : lines) {
			if (!line.startsWith("#")) {
				Collections.reverse(result);
				return result;
			} else {
				result.add(line);
			}
		}
		Collections.reverse(result);
		return result;
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	private @NotNull
	List<String> getHeaderFromLines(final @NotNull List<String> lines,
									final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull String line : lines) {
			if (!line.startsWith("#")) {
				return result;
			} else {
				result.add(line);
			}
		}
		return result;
	}

	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	private @NotNull
	List<String> getKeys(final @NotNull List<String> lines,
						 final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> result = collectionsProvider.newList();
		for (final @NotNull String line : lines) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	@Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
	private @NotNull
	List<String> getPureCommentsFromLines(final @NotNull List<String> lines,
										  final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		final @NotNull List<String> comments = YamlEditor.getCommentsFromLines(lines, collectionsProvider);
		final @NotNull List<String> header = YamlEditor.getHeaderFromLines(lines, collectionsProvider);
		final @NotNull List<String> footer = YamlEditor.getFooterFromLines(lines, collectionsProvider);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}
}