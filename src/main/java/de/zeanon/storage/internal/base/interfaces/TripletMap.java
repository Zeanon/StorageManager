package de.zeanon.storage.internal.base.interfaces;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface TripletMap<K, V> {

	int size();

	boolean isEmpty();

	boolean containsKey(final @NotNull Object key);

	void put(final @NotNull K key, final @NotNull V value);

	void add(final @NotNull K key, final @NotNull V value);

	void addAll(final @NotNull List<TripletMap.Entry<K, V>> list);

	void remove(final @NotNull Object key);

	@NotNull
	List<TripletMap.Entry<K, V>> entryList();

	@Nullable
	V get(final @NotNull Object key);

	void clear();


	@NotNull
	@Override
	String toString();


	/**
	 * The entries stored in a TripletMap
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 */
	@Getter
	@EqualsAndHashCode
	@AllArgsConstructor
	@Accessors(chain = true)
	class Entry<K, V> implements Comparable<Entry> {

		@Setter
		private int line;
		@Setter
		@NotNull
		private K key;
		@Setter
		@NotNull
		private V value;


		@Override
		public int compareTo(final @NotNull TripletMap.Entry entry) {
			return Integer.compare(line, entry.line);
		}

		@NotNull
		@Override
		public String toString() {
			return "(" + this.line + "," + this.key + "," + this.value + ")";
		}
	}
}