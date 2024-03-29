package de.zeanon.storagemanagertest.utility.basic;

import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.storagemanagertest.TestStorageManager;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * test1.tf should have the Contents of resource.tf
 * test2.tf should be empty
 * test3.tf should have the Contents of resource.tf
 */
class TestBaseFileUtils {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("BaseFileUtils");
	}

	@TestOnly
	@AfterAll
	static void listFiles() {
		boolean result;
		try {
			result = Objects.notNull(BaseFileUtils.listFiles(new File("src/test/resources/testresults/basefileutils"))).size() == 3;
		} catch (final IOException e) {
			e.printStackTrace();
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void writeOnCreation11() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFileIfCreated("src/test/resources/testresults/basefileutils", "test1.tf", BaseFileUtils.createNewInputStreamFromFile("src/test/resources/testsources", "resource.tf"));
		} catch (final UncheckedIOException e) {
			e.printStackTrace();
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void writeOnCreation12() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFileIfCreated("src/test/resources/testresults/basefileutils", "test1.tf", null);
		} catch (final UncheckedIOException e) {
			e.printStackTrace();
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void writeOnCreation21() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFileIfCreated("src/test/resources/testresults/basefileutils", "test2.tf", BaseFileUtils.createNewInputStreamFromFile("src/test/resources/testsources", "resource.tf"));
		} catch (final UncheckedIOException e) {
			e.printStackTrace();
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void writeOnCreation22() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFile("src/test/resources/testresults/basefileutils", "test2.tf", null);
		} catch (final UncheckedIOException e) {
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void create() {
		boolean result = true;
		try {
			BaseFileUtils.createFile("src/test/resources/testresults/basefileutils", "test3.tf");
		} catch (final UncheckedIOException e) {
			e.printStackTrace();
			result = false;
		}

		Assertions.assertTrue(result);
	}

	@Test
	@TestOnly
	void writeToFile() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFile("src/test/resources/testresults/basefileutils", "test3.tf", BaseFileUtils.createNewInputStreamFromFile("src/test/resources/testsources", "resource.tf"));
		} catch (final UncheckedIOException e) {
			result = false;
		}

		Assertions.assertTrue(result);
	}
}