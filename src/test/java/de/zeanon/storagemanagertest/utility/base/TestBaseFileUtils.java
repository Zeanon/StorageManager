package de.zeanon.storagemanagertest.utility.base;

import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagertest.TestStorageManager;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestBaseFileUtils {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("BaseFileUtils");
	}

	@Test
	@TestOnly
	void writeOnCreation11() {
		boolean result = true;
		try {
			BaseFileUtils.writeToFileIfCreated("src/test/resources/testresults/basefileutils", "test1.tf", BaseFileUtils.createNewInputStreamFromFile("src/test/resources/testsources", "resource.tf"));
		} catch (RuntimeIOException e) {
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
		} catch (RuntimeIOException e) {
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
		} catch (RuntimeIOException e) {
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
		} catch (RuntimeIOException e) {
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
		} catch (RuntimeIOException e) {
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
		} catch (RuntimeIOException e) {
			result = false;
		}

		Assertions.assertTrue(result);
	}
}