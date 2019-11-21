package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.internal.base.interfaces.TripletMap;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class HashTripletMap<K, V> implements TripletMap<K, V> {

	@NotNull
	private ArrayList<Entry<K, V>> localList = new ArrayList<>();


	@Override
	public int size() {
		return this.localList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	@Override
	public boolean containsKey(final @NotNull Object key) {
		for (HashTripletMap.Entry<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void put(final @NotNull K key, final @NotNull V value) {
		for (HashTripletMap.Entry<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		this.localList.add(new HashTripletMap.Entry<>(this.localList.size(), key, value));
	}

	@Override
	public void add(final @NotNull K key, final @NotNull V value) {
		this.localList.add(new Entry<>(this.localList.size(), key, value));
	}

	@Override
	public void addAll(final @NotNull List<Entry<K, V>> list) {
		this.localList.addAll(list);
	}

	@Override
	public void remove(final @NotNull Object key) {
		for (HashTripletMap.Entry<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				this.localList.remove(entry);
				return;
			}
		}
	}

	@NotNull
	@Override
	public List<HashTripletMap.Entry<K, V>> entryList() {
		return new ArrayList<>(this.localList);
	}

	@Nullable
	@Override
	public V get(final @NotNull Object key) {
		for (HashTripletMap.Entry<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public void clear() {
		this.localList.clear();
	}


	@NotNull
	@Override
	public String toString() {
		return this.localList.toString();
	}
}