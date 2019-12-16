package de.zeanon.storagemanagertest;

import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TestStorageManager {

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
			}
			System.out.println(result ? "[INFO] Cleared Folder" : "[INFO] Failed to clear Folder");
		} catch (RuntimeIOException e) {
			System.out.println(e.getMessage());
		}
	}
}