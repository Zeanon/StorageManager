package de.zeanon.storagemanagertest.utility.maps;

import de.zeanon.storagemanager.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestDataMap {

	private static final @NotNull DataMap<String, Boolean> testMap = new GapDataMap<>();

	@BeforeAll
	static void setup() {
		TestDataMap.testMap.add("first", true);
		TestDataMap.testMap.add("second", true);
		TestDataMap.testMap.add("first", false);
		TestDataMap.testMap.add("second", true);
		TestDataMap.testMap.put("second", false);
	}


	@Test
	@TestOnly
	void entryList() {
		Assertions.assertAll("EntryList",
							 () -> Assertions.assertSame(4, TestDataMap.testMap.entryList().size()),
							 () -> Assertions.assertEquals("first", TestDataMap.testMap.entryList().get(0).getKey()),
							 () -> Assertions.assertEquals(true, TestDataMap.testMap.entryList().get(0).getValue()),
							 () -> Assertions.assertEquals("second", TestDataMap.testMap.entryList().get(1).getKey()),
							 () -> Assertions.assertEquals(false, TestDataMap.testMap.entryList().get(1).getValue()),
							 () -> Assertions.assertEquals("first", TestDataMap.testMap.entryList().get(2).getKey()),
							 () -> Assertions.assertEquals(false, TestDataMap.testMap.entryList().get(2).getValue()),
							 () -> Assertions.assertEquals("second", TestDataMap.testMap.entryList().get(3).getKey()),
							 () -> Assertions.assertEquals(true, TestDataMap.testMap.entryList().get(3).getValue()));
	}
}