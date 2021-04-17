package de.zeanon.thunderfilemanagertest.utility.basic;

import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.thunderfilemanager.ThunderFileManager;
import de.zeanon.thunderfilemanager.internal.files.raw.ThunderFile;
import de.zeanon.thunderfilemanagertest.TestStorageManager;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestParser {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("Parser");
	}

	@Test
	@TestOnly
	void testResource1() {
		final boolean[] result = new boolean[2];

		try {
			ThunderFile testFile = ThunderFileManager.thunderFile("src/test/resources/testresults/parser", "test1")
													 .fromFile("src/test/resources/testsources", "resource.tf")
													 .create();
			result[0] = testFile.getBooleanUseArray("this", "is", "a", "test");
			result[1] = true;
		} catch (FileParseException e) {
			e.printStackTrace();
		}

		Assertions.assertAll("Parse-Results",
							 () -> Assertions.assertTrue(result[0]),
							 () -> Assertions.assertTrue(result[1]));
	}

	@Test
	@TestOnly
	void testResource2() {
		boolean result = false;
		try {
			ThunderFile testFile = ThunderFileManager.thunderFile("src/test/resources/testresults/parser", "test2")
													 .fromFile("src/test/resources/testsources", "resourceBroken.tf")
													 .create();
			testFile.getBooleanUseArray("this", "is", "a", "test");
		} catch (FileParseException e) {
			e.getCause().printStackTrace();
			System.err.println("[IMPORTANT] This error is intended to happen!");
			System.err.println("[IMPORTANT] If here is no error, something went wrong!");
			result = true;
		}

		Assertions.assertTrue(result);
	}
}