package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
@SuppressWarnings("unused")
public abstract class TripletMap<K, V> extends AbstractMap<K, V> {


	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	@Override
	public int size() {
		return this.getLocalList().size();
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
		for (final @NotNull TripletNode<K, V> TempNode : this.getLocalList()) {
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
		for (final @NotNull TripletNode<K, V> tempNode : this.getLocalList()) {
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
	public V put(final @NotNull K key, final @NotNull V value) {
		for (final @NotNull TripletNode<K, V> TempNode : this.getLocalList()) {
			if (TempNode.getKey().equals(key)) {
				final V tempValue = TempNode.getValue();
				TempNode.setValue(value);
				return tempValue;
			}
		}
		this.getLocalList().add(new TripletNode<>(this.getLocalList().size(), key, value));
		return null;
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for
	 * any of the keys currently in the specified map.
	 *
	 * @param map mappings to be stored in this map
	 *
	 * @throws ObjectNullException if the specified map is null
	 */
	@Override
	public void putAll(final @NotNull Map<? extends K, ? extends V> map) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : Objects.notNull(map.entrySet(), "Map must not be null")) {
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
		return this.getLocalList().isEmpty();
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	public void add(final @NotNull K key, final @NotNull V value) {
		this.getLocalList().add(new TripletNode<>(this.size(), key, value));
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be stored in this map
	 *
	 * @throws ObjectNullException if the specified map is null
	 */
	public void addAll(final @NotNull List<TripletNode<K, V>> nodes) {
		this.getLocalList().addAll(nodes);
	}

	public void addAll(final @NotNull Map<? extends K, ? extends V> map) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.getLocalList().add(new TripletNode<>(this.size(), entry.getKey(), entry.getValue()));
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
	@Nullable
	@Override
	public V get(final @NotNull Object key) {
		for (final @NotNull TripletNode<K, V> tempNode : this.getLocalList()) {
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
	@Nullable
	@Override
	public V remove(final @NotNull Object key) {
		for (final @NotNull TripletNode<K, V> tempNode : this.getLocalList()) {
			if (tempNode.getKey().equals(key)) {
				this.getLocalList().remove(tempNode);
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
		this.getLocalList().clear();
	}

	@NotNull
	public List<TripletNode<K, V>> entryList() {
		return new ArrayList<>(this.getLocalList());
	}

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
	@NotNull
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new HashSet<>(this.getLocalList());
	}

	/**
	 * Returns a {@link Set} view of the keys contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation), the results of
	 * the iteration are undefined.  The set supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
	 * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 *
	 * @return a set view of the keys contained in this map
	 */
	@NotNull
	@Override
	public Set<K> keySet() {
		final @NotNull Set<K> tempCollection = new HashSet<>();
		for (final @NotNull TripletNode<K, V> tempNode : this.getLocalList()) {
			tempCollection.add(tempNode.getKey());
		}
		return tempCollection;
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this map.
	 * The collection is backed by the map, so changes to the map are
	 * reflected in the collection, and vice-versa.  If the map is
	 * modified while an iteration over the collection is in progress
	 * (except through the iterator's own <tt>remove</tt> operation),
	 * the results of the iteration are undefined.  The collection
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
	 * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
	 * support the <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a view of the values contained in this map
	 */
	@NotNull
	@Override
	public Collection<V> values() {
		final @NotNull Collection<V> tempCollection = new HashSet<>();
		for (final @NotNull TripletNode<K, V> tempNode : this.getLocalList()) {
			tempCollection.add(tempNode.getValue());
		}
		return tempCollection;
	}

	@NotNull
	protected abstract List<TripletMap.TripletNode<K, V>> getLocalList();

	@NotNull
	@Override
	public String toString() {
		return this.getLocalList().toString();
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
	@Getter(onMethod_ = @Override)
	@EqualsAndHashCode
	@AllArgsConstructor
	@SuppressWarnings("unused")
	public static class TripletNode<K, V> implements Map.Entry<K, V>, Comparable<TripletNode> {

		@Getter
		private int line;
		private @NotNull K key;
		private @Nullable V value;

		public @NotNull K setKey(final @NotNull K key) {
			final @NotNull K tempKey = this.key;
			this.key = key;
			return tempKey;
		}

		@Override
		public @Nullable V setValue(final @NotNull V value) {
			final V tempValue = this.value;
			this.value = value;
			return tempValue;
		}

		@Override
		public int compareTo(final @NotNull TripletMap.TripletNode entry) {
			return Integer.compare(this.line, entry.line);
		}

		@NotNull
		@Override
		public String toString() {
			return "(" + this.line + "," + this.key + "," + this.value + ")";
		}
	}
}