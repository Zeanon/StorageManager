package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.internal.base.interfaces.TripletMap;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Custom Map implementation optimized for ThunderFile
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class HashTripletMap<K, V> implements TripletMap<K, V> {

	@NotNull
	private ArrayList<TripletNode<K, V>> localList = new ArrayList<>();


	public HashTripletMap(final @NotNull Map<K, V> map) {
		this.addAll(map);
	}

	@Override
	public int size() {
		return this.localList.size();
	}

	@Override
	@Contract(pure = true)
	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	@Override
	@Contract(pure = true)
	public boolean containsKey(final @NotNull Object key) {
		for (TripletNode<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Contract(pure = true)
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public V put(final @NotNull K key, final @NotNull V value) {
		for (TripletNode<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				V tempValue = entry.getValue();
				entry.setValue(value);
				return tempValue;
			}
		}
		this.localList.add(new TripletNode<>(this.localList.size(), key, value));
		return null;
	}

	@Override
	public void add(final @NotNull K key, final @NotNull V value) {
		this.localList.add(new TripletNode<>(this.localList.size(), key, value));
	}

	@Override
	public void addAll(final @NotNull List<TripletNode<K, V>> nodes) {
		this.localList.addAll(nodes);
	}

	@Override
	public void addAll(final @NotNull Map<K, V> map) {
		for (Map.Entry<K, V> node : map.entrySet()) {
			this.localList.add(new TripletNode<>(this.localList.size(), node.getKey(), node.getValue()));
		}
	}

	@Nullable
	@Override
	public V remove(final @NotNull Object key) {
		for (TripletNode<K, V> entry : this.localList) {
			if (entry.getKey().equals(key)) {
				this.localList.remove(entry);
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@NotNull
	@Override
	public List<TripletNode<K, V>> entryList() {
		return new ArrayList<>(this.localList);
	}

	@Nullable
	@Override
	public V get(final @NotNull Object key) {
		for (TripletNode<K, V> entry : this.localList) {
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
	public Set<K> keySet() {
		Set<K> tempCollection = new HashSet<>();
		for (TripletNode<K, V> entry : this.localList) {
			tempCollection.add(entry.getKey());
		}
		return tempCollection;
	}

	@NotNull
	@Override
	public Collection<V> values() {
		Collection<V> tempCollection = new HashSet<>();
		for (TripletNode<K, V> entry : this.localList) {
			tempCollection.add(entry.getValue());
		}
		return tempCollection;
	}

	@NotNull
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new HashSet<>(this.localList);
	}


	@NotNull
	@Override
	public String toString() {
		return this.localList.toString();
	}
}