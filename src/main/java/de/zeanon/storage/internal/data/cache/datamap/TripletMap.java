package de.zeanon.storage.internal.data.cache.datamap;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Custom Map implementation optimized for ThunderFile
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
@NoArgsConstructor
@AllArgsConstructor
public class TripletMap<K, V> {

	@NotNull
	private ArrayList<Entry<K, V>> localList = new ArrayList<>();


	public int size() {
		return this.localList.size();
	}

	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	public boolean containsKey(final @NotNull Object key) {
		for (TripletMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public void put(final @NotNull K key, final @NotNull V value) {
		for (TripletMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		this.localList.add(new TripletMap.Entry<>(this.localList.size(), key, value));
	}

	public void add(final @NotNull K key, final @NotNull V value) {
		this.localList.add(new Entry<>(this.localList.size(), key, value));
	}

	public void addAll(final @NotNull List<Entry<K, V>> list) {
		this.localList.addAll(list);
	}

	public void remove(final @NotNull Object key) {
		for (TripletMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				this.localList.remove(entry);
				return;
			}
		}
	}

	@NotNull
	public List<TripletMap.Entry<K, V>> entryList() {
		return this.localList;
	}

	@Nullable
	public V get(final @NotNull Object key) {
		for (TripletMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void clear() {
		this.localList.clear();
	}


	@NotNull
	@Override
	public String toString() {
		return this.localList.toString();
	}

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
	public static class Entry<K, V> implements Comparable<TripletMap.Entry> {

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