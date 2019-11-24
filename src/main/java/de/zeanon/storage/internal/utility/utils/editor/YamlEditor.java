package de.zeanon.storage.internal.utility.utils.editor;

import de.zeanon.utils.basic.Objects;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Class for parsing a Yaml-Type File
 *
 * @author Zeanon
 * @version 1.4.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public class YamlEditor {

	public static @NotNull List<String> readComments(final @NotNull File file) throws IOException {
		return getCommentsFromLines(read(file));
	}

	@Contract("_ -> new")
	public static @NotNull List<String> read(final @NotNull File file) throws IOException {
		return Files.readAllLines(file.toPath());
	}

	public static @NotNull List<String> readFooter(final @NotNull File file) throws IOException {
		return getFooterFromLines(read(file));
	}

	public static @NotNull List<String> readHeader(final @NotNull File file) throws IOException {
		return getHeaderFromLines(read(file));
	}

	public static @NotNull List<String> readKeys(final @NotNull File file) throws IOException {
		return getKeys(read(file));
	}

	public static @NotNull List<String> readPureComments(final @NotNull File file) throws IOException {
		return getPureCommentsFromLines(read(file));
	}

	public static @NotNull List<String> readWithoutHeaderAndFooter(final @NotNull File file) throws IOException {
		return getLinesWithoutFooterAndHeaderFromLines(read(file));
	}

	@Synchronized
	public static void write(final @NotNull File file, final @NotNull List<String> lines) throws IOException {
		@NotNull @Cleanup final PrintWriter writer = new PrintWriter(new FileWriter(file));
		final @NotNull Iterator<String> linesIterator = lines.iterator();
		writer.print(linesIterator.next());
		linesIterator.forEachRemaining(line -> {
			writer.println();
			writer.print(line);
		});
	}

	@Contract("_ -> param1")
	public static @NotNull List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines) {
		final @NotNull List<String> header = getHeaderFromLines(lines);
		final @NotNull List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	private static @NotNull List<String> getCommentsFromLines(final @NotNull List<String> lines) {
		final @NotNull List<String> result = new ArrayList<>();
		for (final @NotNull String line : Objects.notNull(lines, "Lines must not be null")) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	private static @NotNull List<String> getFooterFromLines(final @NotNull List<String> lines) {
		Objects.checkNull(lines, "Lines must not be null");

		final @NotNull List<String> result = new ArrayList<>();
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

	private static @NotNull List<String> getHeaderFromLines(final @NotNull List<String> lines) {
		final @NotNull List<String> result = new ArrayList<>();
		for (final @NotNull String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.startsWith("#")) {
				return result;
			} else {
				result.add(line);
			}
		}
		return result;
	}

	private static @NotNull List<String> getKeys(final @NotNull List<String> lines) {
		final @NotNull List<String> result = new ArrayList<>();
		for (final @NotNull String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	private static @NotNull List<String> getPureCommentsFromLines(final @NotNull List<String> lines) {
		final @NotNull List<String> comments = getCommentsFromLines(lines);
		final @NotNull List<String> header = getHeaderFromLines(lines);
		final @NotNull List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}
}