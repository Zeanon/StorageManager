package de.zeanon.storagemanager.internal.utility.lagcatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


/**
 * Utility Class to benchmark
 *
 * @author Zeanon
 * @version 1.0.0
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class LagCatcher {


	private static final @NotNull Map<String, Long> startTimes = new HashMap<>();
	private static final @NotNull Map<String, Long> stopTimes = new HashMap<>();


	public static void runMultipleTimes(final int cycles, final @NotNull Runnable runnable) {
		long nanosTook = 0L;
		for (int i = 0; i < cycles; ++i) {
			final long nanoTime = System.nanoTime();
			runnable.run();
			nanosTook = System.nanoTime() - nanoTime;
		}
		System.out.println((Object) ("Average time: " + TimeUnit.NANOSECONDS.toMicros(nanosTook / cycles)
									 + " micros - " + TimeUnit.NANOSECONDS.toMillis(nanosTook / cycles) + " ms."));
		System.out.println((Object) ("Test took: " + TimeUnit.NANOSECONDS.toMicros(nanosTook) + " micros " +
									 "- " + TimeUnit.NANOSECONDS.toMillis(nanosTook) + " ms"));
	}

	public static void start(final @NotNull String name) {
		if (LagCatcher.startTimes.containsKey(name)) {
			throw new IllegalStateException(("Test is already running for '" + name + '\''));
		}
		final long nanoTime = System.nanoTime();
		LagCatcher.startTimes.put(name, nanoTime);
	}

	public static void stopAndShow(final @NotNull String name) {
		stop(name);
		show(name);
	}

	public static void stop(final @NotNull String name) {
		if (LagCatcher.stopTimes.containsKey(name)) {
			throw new IllegalStateException(("No test running for '" + name + '\''));
		}
		LagCatcher.stopTimes.put(name, System.nanoTime());
	}

	private static void show(final @NotNull String name) {
		if (!LagCatcher.startTimes.containsKey(name) || !LagCatcher.stopTimes.containsKey(name)) {
			throw new IllegalStateException(("No results found for '" + name + '\''));
		}
		final Long value = LagCatcher.startTimes.get(name);
		if (value == null) {
			return;
		}
		final long start = value;
		final Long value2 = LagCatcher.stopTimes.get(name);
		if (value2 != null) {
			final long end = value2;
			final long took = end - start;
			System.out.println((Object) ("Test '" + name + "' took " + TimeUnit.NANOSECONDS.toMicros(took) +
										 " micro-seconds. That's " + TimeUnit.NANOSECONDS.toMillis(took) + " ms."));
			LagCatcher.stopTimes.remove(name);
			LagCatcher.startTimes.remove(name);
		}
	}
}