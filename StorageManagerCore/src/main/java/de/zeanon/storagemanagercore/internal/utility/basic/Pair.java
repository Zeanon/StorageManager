package de.zeanon.storagemanagercore.internal.utility.basic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@Setter
@EqualsAndHashCode
public class Pair<K, V> {

	private @NotNull K key;

	private @Nullable V value;


	public Pair(final @NotNull K key, final @Nullable V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}
}