package de.zeanon.storage.internal.utils.datafiles;

import de.zeanon.storage.internal.utils.editor.YamlEditor;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Different Utility methods for Yaml-Type Files
 *
 * @author Zeanon
 * @version 1.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class YamlUtils {


	@NotNull
	public static List<String> parseComments(final @NotNull List<String> comments, final @NotNull List<String> updated) {
		@NotNull final Map<String, List<String>> parsed = assignCommentsToKey(comments);

		for (@NotNull final Map.Entry<String, List<String>> entry : parsed.entrySet()) {
			int i = 0;
			for (final String line : entry.getValue()) {
				if (updated.contains(entry.getKey() + " ")) {
					updated.add(updated.indexOf(entry.getKey() + " ") + i, line);
				} else if (updated.contains(" " + entry.getKey())) {
					updated.add(updated.indexOf(" " + entry.getKey()) + i, line);
				}
			}
		}
		return updated;
	}

	@NotNull
	private static Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines) {
		@NotNull final List<String> storage = new ArrayList<>();
		@NotNull final List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
		@NotNull final Map<String, List<String>> result = new HashMap<>();

		Collections.reverse(lines);
		for (@NotNull final String line : lines) {
			if (line.trim().startsWith("#") || line.isEmpty()) {
				storage.add(line);
				continue;
			}
			result.put(line, storage);
			storage.clear();
		}

		@NotNull final List<String> keysToRemove = new ArrayList<>();
		for (@NotNull final Map.Entry<String, List<String>> entry : result.entrySet()) {
			if (entry.getValue().equals(new ArrayList<>())) {
				keysToRemove.add(entry.getKey());
			}
		}

		for (final String key : keysToRemove) {
			result.remove(key);
		}
		return result;
	}
}