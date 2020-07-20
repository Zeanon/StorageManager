package de.zeanon.storagemanagertest.utility.basic;

import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.files.raw.ThunderFile;
import de.zeanon.storagemanagertest.TestStorageManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestParser {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("Parser");
	}

	@Test
	@TestOnly
	void testResource1() {
		final @NotNull boolean[] result = new boolean[2];

		try {
			ThunderFile testFile = StorageManager.thunderFile("src/test/resources/testresults/parser", "test1")
												 .fromFile("src/test/resources/testsources", "resource.tf")
												 .create();
			result[0] = testFile.getBooleanUseArray("this", "is", "a", "test");
			result[1] = true;
		} catch (FileParseException e) {
			e.printStackTrace();
		}

		Assertions.assertAll("Lock-Results",
							 () -> Assertions.assertTrue(result[0]),
							 () -> Assertions.assertTrue(result[1]));
	}

	@Test
	@TestOnly
	void testResource2() {
		boolean result = false;
		try {
			ThunderFile testFile = StorageManager.thunderFile("src/test/resources/testresults/parser", "test2")
												 .fromFile("src/test/resources/testsources", "resourceBroken.tf")
												 .create();
			testFile.getBooleanUseArray("this", "is", "a", "test");
		} catch (FileParseException e) {
			e.getCause().printStackTrace();
			result = true;
		}

		Assertions.assertTrue(result);
	}
}