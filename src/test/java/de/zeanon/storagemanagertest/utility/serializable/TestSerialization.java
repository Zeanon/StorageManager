package de.zeanon.storagemanagertest.utility.serializable;

import de.zeanon.storagemanager.internal.base.cache.base.ConcurrentAbstractDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.BigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentBigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagertest.TestStorageManager;
import java.io.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestSerialization {

	@BeforeAll
	static void setup() {
		TestStorageManager.clear("Serialization");
	}

	@Test
	@TestOnly
	void serialize1() throws IOException, ClassNotFoundException {
		final @NotNull DataMap<String, Boolean> tempMap = new GapDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, (i % 2) == 0);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization1.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization1.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull DataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization1.ser")))) {
			resultMap = (GapDataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}

	@Test
	@TestOnly
	void serialize2() throws IOException, ClassNotFoundException {
		final @NotNull DataMap<String, Boolean> tempMap = new BigDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization2.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization2.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull DataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization2.ser")))) {
			resultMap = (BigDataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}

	@Test
	@TestOnly
	void serialize3() throws IOException, ClassNotFoundException {
		final @NotNull DataMap<String, Boolean> tempMap = new GapDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization3.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization3.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull DataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization3.ser")))) {
			resultMap = (DataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}

	@Test
	@TestOnly
	void serialize4() throws IOException, ClassNotFoundException {
		final @NotNull ConcurrentAbstractDataMap<String, Boolean> tempMap = new ConcurrentGapDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization4.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization4.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull ConcurrentAbstractDataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization4.ser")))) {
			resultMap = (ConcurrentGapDataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}

	@Test
	@TestOnly
	void serialize5() throws IOException, ClassNotFoundException {
		final @NotNull ConcurrentAbstractDataMap<String, Boolean> tempMap = new ConcurrentBigDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization5.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization5.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull ConcurrentAbstractDataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization5.ser")))) {
			resultMap = (ConcurrentBigDataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}

	@Test
	@TestOnly
	void serialize6() throws IOException, ClassNotFoundException {
		final @NotNull ConcurrentAbstractDataMap<String, Boolean> tempMap = new ConcurrentGapDataMap<>();

		for (int i = 0; i < 5000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization6.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization6.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull ConcurrentAbstractDataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization6.ser")))) {
			resultMap = (ConcurrentAbstractDataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}
}