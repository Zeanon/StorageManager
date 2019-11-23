package de.zeanon.storage.internal.utility.utils.datafiles;

import de.zeanon.storage.internal.utility.utils.editor.YamlEditor;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Different Utility methods for Yaml-Type Files
 *
 * @author Zeanon
 * @version 1.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public class YamlUtils {


	@Contract("_, _, -> param1")
	public static @NotNull List<String> parseComments(final @NotNull List<String> comments, final @NotNull List<String> updated) {
		final @NotNull Map<String, List<String>> parsed = assignCommentsToKey(comments);

		for (final @NotNull Map.Entry<String, List<String>> entry : parsed.entrySet()) {
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

	private static @NotNull Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines) {
		final @NotNull List<String> storage = new ArrayList<>();
		final @NotNull List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
		final @NotNull Map<String, List<String>> result = new HashMap<>();

		Collections.reverse(lines);
		for (final @NotNull String line : lines) {
			if (line.trim().startsWith("#") || line.isEmpty()) {
				storage.add(line);
				continue;
			}
			result.put(line, storage);
			storage.clear();
		}

		final @NotNull List<String> keysToRemove = new ArrayList<>();
		for (final @NotNull Map.Entry<String, List<String>> entry : result.entrySet()) {
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