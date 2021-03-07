package de.zeanon.storagemanagercore.internal.utility.basic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
@EqualsAndHashCode
public class Pair<K, V> {

	private K key;

	private V value;


	public Pair(final @NotNull K key, final @NotNull V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}
}