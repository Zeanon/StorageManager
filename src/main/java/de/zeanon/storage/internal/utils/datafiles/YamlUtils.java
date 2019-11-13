package de.zeanon.storage.internal.utils.datafiles;

import de.zeanon.storage.internal.utils.editor.YamlEditor;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Adds several utility Methods for Yaml-Files
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class YamlUtils {


	public static List<String> parseComments(final @NotNull List<String> comments, final @NotNull List<String> updated) {
		final Map<String, List<String>> parsed;
		parsed = assignCommentsToKey(comments);

		for (Map.Entry<String, List<String>> entry : parsed.entrySet()) {
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

	private static Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines) {
		List<String> storage = new ArrayList<>();
		final List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
		final Map<String, List<String>> result = new HashMap<>();

		Collections.reverse(lines);
		for (final String line : lines) {
			if (line.trim().startsWith("#") || line.isEmpty()) {
				storage.add(line);
				continue;
			}
			result.put(line, storage);
			storage = new ArrayList<>();
		}

		final List<String> keysToRemove = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : result.entrySet()) {
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