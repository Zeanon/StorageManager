package de.zeanon.thunderfilemanagertest.utility.basic;

import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.thunderfilemanager.ThunderFileManager;
import de.zeanon.thunderfilemanager.internal.files.raw.ThunderFile;
import de.zeanon.thunderfilemanagertest.TestThunderFileManager;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestParser {

	@BeforeAll
	static void setup() {
		TestThunderFileManager.clear("Parser");
	}

	@Test
	@TestOnly
	void testResource1() {
		final boolean[] result = new boolean[2];

		try {
			final ThunderFile testFile = ThunderFileManager.thunderFile("src/test/resources/testresults/parser", "test1")
														   .fromFile("src/test/resources/testsources", "resource.tf")
														   .create();
			result[0] = testFile.getBooleanUseArray("this", "is", "a", "test");
			result[1] = true;
		} catch (final FileParseException e) {
			e.printStackTrace();
		}

		Assertions.assertAll("Parse-Results",
							 () -> Assertions.assertTrue(result[0]),
							 () -> Assertions.assertTrue(result[1]));
	}

	@Test
	@TestOnly
	void testResource2() {
		Assertions.assertThrows(FileParseException.class, () -> {
			final ThunderFile testFile = ThunderFileManager.thunderFile("src/test/resources/testresults/parser", "test2")
														   .fromFile("src/test/resources/testsources", "resourceBroken.tf")
														   .create();
			testFile.getBooleanUseArray("this", "is", "a", "test");
		});
	}
}