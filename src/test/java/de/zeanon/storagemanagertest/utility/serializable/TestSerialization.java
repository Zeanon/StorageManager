package de.zeanon.storagemanagertest.utility.serializable;

import de.zeanon.storagemanager.internal.base.cache.datamap.BigDataMap;
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
	void serialize() throws IOException, ClassNotFoundException {
		final @NotNull DataMap<String, Boolean> tempMap = new BigDataMap<>();

		for (int i = 0; i < 5000000; i++) {
			tempMap.add("key" + i, true);
		}

		BaseFileUtils.createFile("src/test/resources/testresults/serialization", "serialization.ser");

		try (final @NotNull ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream(new File("src/test/resources/testresults/serialization", "serialization.ser")))) {
			serialization.writeObject(tempMap);
		}

		final @NotNull DataMap resultMap;
		try (final @NotNull ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream(new File("src/test/resources/testresults/serialization", "serialization.ser")))) {
			resultMap = (DataMap) deserialization.readObject();
		}

		Assertions.assertEquals(tempMap, resultMap);
	}
}