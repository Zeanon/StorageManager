package de.zeanon.storage.internal.utils.editor;

import de.zeanon.storage.internal.utils.basic.Objects;
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
import org.jetbrains.annotations.NotNull;


/**
 * Class for parsing a Yaml-Type File
 *
 * @author Zeanon
 * @version 1.4.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class YamlEditor {

	@NotNull
	public static List<String> readComments(final @NotNull File file) throws IOException {
		return getCommentsFromLines(read(file));
	}

	public static List<String> read(final @NotNull File file) throws IOException {
		return Files.readAllLines(file.toPath());
	}

	@NotNull
	public static List<String> readFooter(final @NotNull File file) throws IOException {
		return getFooterFromLines(read(file));
	}

	@NotNull
	public static List<String> readHeader(final @NotNull File file) throws IOException {
		return getHeaderFromLines(read(file));
	}

	@NotNull
	public static List<String> readKeys(final @NotNull File file) throws IOException {
		return getKeys(read(file));
	}

	@NotNull
	public static List<String> readPureComments(final @NotNull File file) throws IOException {
		return getPureCommentsFromLines(read(file));
	}

	@NotNull
	public static List<String> readWithoutHeaderAndFooter(final @NotNull File file) throws IOException {
		return getLinesWithoutFooterAndHeaderFromLines(read(file));
	}

	public static void write(final @NotNull File file, final @NotNull List<String> lines) throws IOException {
		@NotNull @Cleanup final PrintWriter writer = new PrintWriter(new FileWriter(file));
		@NotNull final Iterator<String> linesIterator = lines.iterator();
		writer.print(linesIterator.next());
		linesIterator.forEachRemaining(line -> {
			writer.println();
			writer.print(line);
		});
	}

	@NotNull
	public static List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines) {
		@NotNull final List<String> header = getHeaderFromLines(lines);
		@NotNull final List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	@NotNull
	private static List<String> getCommentsFromLines(final @NotNull List<String> lines) {
		@NotNull final List<String> result = new ArrayList<>();
		for (@NotNull final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	@NotNull
	private static List<String> getFooterFromLines(final @NotNull List<String> lines) {
		Objects.checkNull(lines, "Lines must not be null");

		@NotNull final List<String> result = new ArrayList<>();
		Collections.reverse(lines);
		for (@NotNull final String line : lines) {
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

	@NotNull
	private static List<String> getHeaderFromLines(final @NotNull List<String> lines) {
		@NotNull final List<String> result = new ArrayList<>();
		for (@NotNull final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.startsWith("#")) {
				return result;
			} else {
				result.add(line);
			}
		}
		return result;
	}

	@NotNull
	private static List<String> getKeys(final @NotNull List<String> lines) {
		@NotNull final List<String> result = new ArrayList<>();
		for (@NotNull final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	@NotNull
	private static List<String> getPureCommentsFromLines(final @NotNull List<String> lines) {
		@NotNull final List<String> comments = getCommentsFromLines(lines);
		@NotNull final List<String> header = getHeaderFromLines(lines);
		@NotNull final List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}
}