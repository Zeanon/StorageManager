package de.zeanon.thunderfilemanagertest;

import java.io.File;
import java.io.UncheckedIOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TestThunderFileManager {

	public static void clear(final @NotNull String className) {
		System.out.println("[INFO] Clearing TestResources Folder to run tests in '" + className + "'");
		boolean result = true;
		try {
			final @NotNull File tempFolder = new File("src/test/resources/testresults/" + className.toLowerCase());
			if (tempFolder.exists() && tempFolder.listFiles() != null) {
				final @Nullable File[] fileList = tempFolder.listFiles();
				if (fileList != null) {
					for (final @Nullable File tempFile : fileList) {
						if (tempFile != null && !tempFile.delete()) {
							result = false;
						}
					}
				}
			} else {
				result = false;
				System.out.println("[INFO] Folder does not exist");
			}
			System.out.println(result ? "[INFO] Cleared Folder" : "[INFO] Failed to clear Folder");
		} catch (final @NotNull UncheckedIOException e) {
			System.out.println(e.getMessage());
		}
	}
}