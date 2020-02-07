package de.zeanon.storagemanagertest.utility.maps;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class TestConcurrentMap {


	@Test
	@TestOnly
	void task() throws InterruptedException {
		final @NotNull ExecutorService executor = Executors.newFixedThreadPool(4);
		final @NotNull DataMap<String, Boolean> tempMap = new ConcurrentGapDataMap<>();
		final @NotNull Map<Integer, Boolean> results = new ConcurrentHashMap<>();
		final @NotNull GapList<String> timeList = new GapList<>();

		for (int i = 0; i < 5000000; i++) {
			tempMap.add("key" + i, (i % 2) == 0);
		}

		executor.submit(() -> {
			final boolean result = tempMap.containsKey("key4999999");
			timeList.add("[INFO] Task1 " + System.currentTimeMillis());
			results.put(1, result);
		});

		executor.submit(() -> {
			final boolean result = tempMap.containsKey("key3999999");
			timeList.add("[INFO] Task2 " + System.currentTimeMillis());
			results.put(2, result);
		});

		executor.submit(() -> {
			final boolean result = tempMap.containsKey("key2999999");
			timeList.add("[INFO] Task3 " + System.currentTimeMillis());
			results.put(3, result);
		});

		executor.submit(() -> {
			final boolean result = tempMap.containsKey("key1999999");
			timeList.add("[INFO] Task4 " + System.currentTimeMillis());
			results.put(4, result);
		});

		while (results.size() < 4) {
			Thread.sleep(5); //NOSONAR
		}

		for (String line : timeList) {
			System.out.println(line);
		}

		Assertions.assertAll("Results",
							 () -> Assertions.assertTrue(results.get(1)),
							 () -> Assertions.assertTrue(results.get(2)),
							 () -> Assertions.assertTrue(results.get(3)),
							 () -> Assertions.assertTrue(results.get(4)));
	}
}