package de.zeanon.storage.internal.base.maps;

import java.util.LinkedList;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor
@AllArgsConstructor
public class DataMap<K, V> {

	@NotNull
	private LinkedList<DataMap.Entry<K, V>> localList = new LinkedList<>();


// --Commented out by Inspection START (19.11.2019 14:36):
//	public DataMap(final @NotNull DataMap<K, V> list) {
//		this.localList = new LinkedList<>(list.entryList());
//	}
// --Commented out by Inspection STOP (19.11.2019 14:36)

	public int size() {
		return this.localList.size();
	}

	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	public boolean containsKey(Object key) {
		for (DataMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public void put(K key, V value) {
		for (DataMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		this.localList.add(new DataMap.Entry<>(key, value, 0));
	}

// --Commented out by Inspection START (19.11.2019 14:36):
//	public V put(K key, V value, int line) {
//		for (DataMap.Entry<K, V> entry : this.entryList()) {
//			if (entry.getKey().equals(key)) {
//				V tempValue = entry.getValue();
//				entry.setValue(value);
//				entry.setLine(line);
//				return tempValue;
//			}
//		}
//		this.localList.add(new DataMap.Entry<>(key, value, line));
//		return null;
//	}
// --Commented out by Inspection STOP (19.11.2019 14:36)

	public void add(K key, V value, int line) {
		this.localList.add(new Entry<>(key, value, line));
	}

	public void addAll(List<Entry<K, V>> list) {
		this.localList.addAll(list);
	}

	public void remove(Object key) {
		for (DataMap.Entry<K, V> entry : this.entryList()) {
			if (entry.getKey().equals(key)) {
				this.localList.remove(entry);
				return;
			}
		}
	}

	@NotNull
	public List<DataMap.Entry<K, V>> entryList() {
		return this.localList;
	}

	public V get(Object key) {
		for (DataMap.Entry<K, V> entry : this.entryList()) {
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

	@Getter
	@EqualsAndHashCode
	@AllArgsConstructor
	@Accessors(chain = true)
	public static class Entry<K, V> implements Comparable<DataMap.Entry> {

		@Setter
		@NotNull
		private K key;
		@Setter
		@NotNull
		private V value;
		@Setter
		private int line;

		@Override
		public int compareTo(@NotNull DataMap.Entry entry) {
			return Integer.compare(line, entry.line);
		}

		@NotNull
		@Override
		public String toString() {
			return "[" + this.key + "," + this.value + "," + this.line + "]";
		}
	}
}