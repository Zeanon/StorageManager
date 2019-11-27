package de.zeanon.storage.internal.utility.utils.datafiles;

import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.utility.utils.editor.YamlEditor;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Different Utility methods for Yaml-Type Files
 *
 * @author Zeanon
 * @version 1.2.0
 */
@UtilityClass
@SuppressWarnings("unused")
public class YamlUtils {


	@Contract("_, _, _ -> param1")
	public static @NotNull List<String> parseComments(final @NotNull List<String> comments, final @NotNull List<String> updated, final @NotNull Provider<? extends Map, ? extends List> provider) {
		final @NotNull Map<String, List<String>> parsed = assignCommentsToKey(comments, provider);

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

	private static @NotNull Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines, final @NotNull Provider<? extends Map, ? extends List> provider) {
		//noinspection unchecked
		final @NotNull List<String> storage = provider.newList();
		final @NotNull List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines, provider);
		//noinspection unchecked
		final @NotNull Map<String, List<String>> result = provider.newMap();

		Collections.reverse(lines);
		for (final @NotNull String line : lines) {
			if (line.trim().startsWith("#") || line.isEmpty()) {
				storage.add(line);
				continue;
			}
			result.put(line, storage);
			storage.clear();
		}

		//noinspection unchecked
		final @NotNull List<String> keysToRemove = provider.newList();
		for (final @NotNull Map.Entry<String, List<String>> entry : result.entrySet()) {
			if (entry.getValue().equals(provider.newList())) {
				keysToRemove.add(entry.getKey());
			}
		}

		for (final String key : keysToRemove) {
			result.remove(key);
		}
		return result;
	}
}