package de.zeanon.storage.internal.utility.datafiles;

import de.zeanon.storage.internal.base.cache.base.CollectionsProvider;
import de.zeanon.storage.internal.utility.editor.YamlEditor;
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
	public static @NotNull List<String> parseComments(final @NotNull List<String> comments, final @NotNull List<String> updated, final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		final @NotNull Map<String, List<String>> parsed = YamlUtils.assignCommentsToKey(comments, collectionsProvider);

		for (final @NotNull Map.Entry<String, List<String>> entry : parsed.entrySet()) {
			final int i = 0;
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

	private static @NotNull Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines, final @NotNull CollectionsProvider<? extends Map, ? extends List> collectionsProvider) {
		//noinspection unchecked
		final @NotNull List<String> storage = collectionsProvider.newList();
		final @NotNull List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines, collectionsProvider);
		//noinspection unchecked
		final @NotNull Map<String, List<String>> result = collectionsProvider.newMap();

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
		final @NotNull List<String> keysToRemove = collectionsProvider.newList();
		for (final @NotNull Map.Entry<String, List<String>> entry : result.entrySet()) {
			if (entry.getValue().equals(collectionsProvider.newList())) {
				keysToRemove.add(entry.getKey());
			}
		}

		for (final String key : keysToRemove) {
			result.remove(key);
		}
		return result;
	}
}