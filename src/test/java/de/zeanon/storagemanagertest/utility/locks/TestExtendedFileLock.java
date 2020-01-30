package de.zeanon.storagemanagertest.utility.locks;

import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.filelock.ExtendedFileLock;
import de.zeanon.storagemanagertest.TestStorageManager;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestExtendedFileLock {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("ExtendedFileLock");
	}

	@Test
	@TestOnly
	void equality() throws IOException {
		BaseFileUtils.createFile("src/test/resources/testresults/extendedfilelock", "test.tf");

		try (final @NotNull ExtendedFileLock test1 = new ExtendedFileLock(new File("src/test/resources/testresults/extendedfilelock", "test.tf"));
			 final @NotNull ExtendedFileLock test2 = new ExtendedFileLock(new File("src/test/resources/testresults/extendedfilelock", "test.tf"))) {

			Assertions.assertSame(test1.getRandomAccessFile(), test2.getRandomAccessFile());
		}
	}

	@Test
	@TestOnly
	void locking() throws IOException {
		BaseFileUtils.createFile(new File("src/test/resources/testresults/extendedfilelock", "test.tf"));

		final @NotNull boolean[] result = new boolean[3];

		final @NotNull ExtendedFileLock test1 = new ExtendedFileLock(new File("src/test/resources/testresults/extendedfilelock", "test.tf"));

		try {
			result[0] = test1.writeLock().tryLock();
			result[1] = test1.writeLock().tryLock();

			test1.writeLock().unlock();
		} finally {
			test1.close();
		}

		result[2] = test1.getRandomAccessFile() != new ExtendedFileLock(new File("src/test/resources/testresults/extendedfilelock", "test.tf")).getRandomAccessFile();
		Assertions.assertAll("Lock-Results",
							 () -> Assertions.assertTrue(result[0]),
							 () -> Assertions.assertTrue(result[1]),
							 () -> Assertions.assertTrue(result[2]));
	}
}