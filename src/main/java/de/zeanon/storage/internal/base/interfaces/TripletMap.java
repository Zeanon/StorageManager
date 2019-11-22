package de.zeanon.storage.internal.base.interfaces;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


/**
 * Basic interface for TripletMaps
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@SuppressWarnings("unused")
public interface TripletMap<K, V> extends Map<K, V> {


	void add(final @NotNull K key, final @NotNull V value);

	void addAll(final @NotNull List<TripletNode<K, V>> nodes);

	void addAll(final @NotNull Map<K, V> map);

	@NotNull
	List<TripletNode<K, V>> entryList();

	@NotNull
	@Override
	default Set<K> keySet() {
		Set<K> tempCollection = new HashSet<>();
		for (TripletNode<K, V> entry : this.entryList()) {
			tempCollection.add(entry.getKey());
		}
		return tempCollection;
	}

	@NotNull
	@Override
	default Collection<V> values() {
		Collection<V> tempCollection = new HashSet<>();
		for (TripletNode<K, V> entry : this.entryList()) {
			tempCollection.add(entry.getValue());
		}
		return tempCollection;
	}

	@NotNull
	@Override
	default Set<Map.Entry<K, V>> entrySet() {
		return new HashSet<>(this.entryList());
	}

	@NotNull
	@Override
	String toString();


	/**
	 * The entries stored in a TripletMap
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 *
	 * @author Zeanon
	 * @version 1.3.0
	 */
	@Getter
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor
	@SuppressWarnings("unused")
	class TripletNode<K, V> implements Map.Entry<K, V>, Comparable<TripletNode> {

		private int line;
		@NotNull
		private K key;
		@NotNull
		private V value;

		@NotNull
		public K setKey(final @NotNull K key) {
			final K tempKey = this.key;
			this.key = key;
			return tempKey;
		}

		@NotNull
		public V setValue(final @NotNull V value) {
			final V tempValue = this.value;
			this.value = value;
			return tempValue;
		}


		@Override
		public int compareTo(final @NotNull TripletMap.TripletNode entry) {
			return Integer.compare(line, entry.line);
		}

		@NotNull
		@Override
		public String toString() {
			return "(" + this.line + "," + this.key + "," + this.value + ")";
		}
	}
}