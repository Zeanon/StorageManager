package de.zeanon.storagemanagercore.internal.utility.basic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("UnusedReturnValue")
@Getter
@EqualsAndHashCode
public class Pair<K, V> {

	private @NotNull K key;

	private @Nullable V value;


	public Pair(final @NotNull K key, final @Nullable V value) {
		this.key = key;
		this.value = value;
	}

	public @NotNull K setKey(final @NotNull K key) {
		final @NotNull K currentKey = this.key;
		this.key = key;
		return currentKey;
	}

	public @Nullable V setValue(final @Nullable V value) {
		final @Nullable V currentValue = this.value;
		this.value = value;
		return currentValue;
	}

	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}
}