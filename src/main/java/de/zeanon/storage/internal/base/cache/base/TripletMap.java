package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.external.lists.IList;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic interface for TripletMaps
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)}, access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public abstract class TripletMap<K, V> extends AbstractMap<K, V> {

	protected @NotNull IList<TripletNode<K, V>> localList;


	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	@Override
	public int size() {
		return this.localList.size();
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the
	 * specified key.
	 *
	 * @param key The key whose presence in this map is to be tested
	 *
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 * key.
	 */
	@Override
	@Contract(pure = true)
	public boolean containsKey(final @NotNull Object key) {
		for (final @NotNull TripletNode<K, V> TempNode : this.localList) {
			if (TempNode.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the
	 * specified value.
	 *
	 * @param value value whose presence in this map is to be tested
	 *
	 * @return <tt>true</tt> if this map maps one or more keys to the
	 * specified value
	 */
	@Override
	@Contract(pure = true)
	public boolean containsValue(final @NotNull Object value) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (value.equals(tempNode.getValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old
	 * value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 *
	 * @return the previous value associated with <tt>key</tt>, or
	 * <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 * (A <tt>null</tt> return can also indicate that the map
	 * previously associated <tt>null</tt> with <tt>key</tt>.)
	 */
	@Override
	public @Nullable V put(final @NotNull K key, final @NotNull V value) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				return tempNode.setValue(value);
			}
		}
		this.localList.add(new TripletNode<>(this.localList.size(), key, value));
		return null;
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for
	 * any of the keys currently in the specified map.
	 *
	 * @param map mappings to be stored in this map
	 */
	@Override
	public void putAll(final @NotNull Map<? extends K, ? extends V> map) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	@Override
	@Contract(pure = true)
	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	public void add(final @NotNull K key, final @NotNull V value) {
		this.localList.add(new TripletNode<>(this.size(), key, value));
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param node mapping to be added to the map
	 */
	public void add(final @NotNull TripletMap.TripletNode<K, V> node) {
		this.localList.add(node);
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	public void addAll(final @NotNull List<TripletNode<K, V>> nodes) {
		this.localList.addAll(nodes);
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodeMap mappings to be stored in this map
	 */
	public void addAll(final @NotNull Map<? extends K, ? extends V> nodeMap) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : nodeMap.entrySet()) {
			this.localList.add(new TripletNode<>(this.size(), entry.getKey(), entry.getValue()));
		}
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 *
	 * <p>More formally, if this map contains a mapping from a key
	 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
	 * key.equals(k))}, then this method returns {@code v}; otherwise
	 * it returns {@code null}.  (There can be at most one such mapping.)
	 *
	 * <p>A return value of {@code null} does not <i>necessarily</i>
	 * indicate that the map contains no mapping for the key; it's also
	 * possible that the map explicitly maps the key to {@code null}.
	 * The {@link #containsKey containsKey} operation may be used to
	 * distinguish these two cases.
	 *
	 * @see #put(Object, Object)
	 */
	@Override
	public @Nullable V get(final @NotNull Object key) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				return tempNode.getValue();
			}
		}
		return null;
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 *
	 * @param key key whose mapping is to be removed from the map
	 *
	 * @return the previous value associated with <tt>key</tt>, or
	 * <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 * (A <tt>null</tt> return can also indicate that the map
	 * previously associated <tt>null</tt> with <tt>key</tt>.)
	 */
	@Override
	public @Nullable V remove(final @NotNull Object key) {
		final @NotNull Iterator<TripletNode<K, V>> tempIterator = this.localList.iterator();
		TripletNode<K, V> tempNode;
		while ((tempNode = tempIterator.next()) != null) {
			if (tempNode.getKey().equals(key)) {
				tempIterator.remove();
				return tempNode.getValue();
			}
		}
		return null;
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	@Override
	public void clear() {
		this.localList.clear();
	}

	public void trimToSize() {
		this.localList.trimToSize();
	}

	@Contract("-> new")
	public abstract @NotNull List<TripletNode<K, V>> entryList();

	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The set
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations.  It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	@Override
	@Contract("-> new")
	public @NotNull Set<Map.Entry<K, V>> entrySet() {
		return new LinkedHashSet<>(this.localList);
	}

	@Override
	public @NotNull String toString() {
		return this.localList.toString();
	}


	/**
	 * The entries stored in a TripletMap
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 *
	 * @author Zeanon
	 * @version 1.3.0
	 */
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)})
	@SuppressWarnings("unused")
	public static class TripletNode<K, V> implements Map.Entry<K, V>, Comparable<TripletNode> {

		@Getter
		private int line;
		@Getter(onMethod_ = {@Override})
		private @NotNull K key;
		@Getter(onMethod_ = {@Override})
		private @Nullable V value;

		public @NotNull K setKey(final @NotNull K key) {
			try {
				return this.key;
			} finally {
				this.key = key;
			}
		}

		@Override
		public @Nullable V setValue(final @NotNull V value) {
			try {
				return this.value;
			} finally {
				this.value = value;
			}
		}

		@Override
		public int compareTo(final @NotNull TripletMap.TripletNode entry) {
			return Integer.compare(this.line, entry.line);
		}

		@Override
		public @NotNull String toString() {
			return "(" + this.line + "," + this.key + "," + this.value + ")";
		}
	}
}