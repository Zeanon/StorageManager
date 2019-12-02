package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.external.lists.IList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic interface for Concurrent TripletMaps
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class ConcurrentTripletMap<K, V> extends TripletMap<K, V> implements ConcurrentMap<K, V> {


	protected ConcurrentTripletMap(@NotNull IList<TripletNode<K, V>> localList) {
		super(localList);
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
	public @Nullable V put(final @NotNull K key, final @Nullable V value) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				if (Objects.equals(tempNode.getValue(), value)) {
					return value;
				} else {
					return tempNode.setValue(value);
				}
			}
		}
		this.add(key, value);
		return null;
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param node mapping to be added to the map
	 */
	@Override
	public void add(final @NotNull TripletMap.TripletNode<K, V> node) {
		synchronized (this.localList) {
			this.localList.add(node);
		}
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	@Override
	public void addAll(final @NotNull List<TripletNode<K, V>> nodes) {
		synchronized (this.localList) {
			this.localList.addAll(nodes);
		}
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
		synchronized (this.localList) {
			while ((tempNode = tempIterator.next()) != null) {
				if (tempNode.getKey().equals(key)) {
					tempIterator.remove();
					return tempNode.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * If the specified key is not already associated
	 * with a value, associate it with the given value.
	 * This is equivalent to
	 * <pre> {@code
	 * if (!map.containsKey(key))
	 *   return map.put(key, value);
	 * else
	 *   return map.get(key);
	 * }</pre>
	 * <p>
	 * except that the action is performed atomically.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 *
	 * @return the previous value associated with the specified key, or
	 * {@code null} if there was no mapping for the key.
	 * (A {@code null} return can also indicate that the map
	 * previously associated {@code null} with the key,
	 * if the implementation supports null values.)
	 *
	 * @throws UnsupportedOperationException if the {@code put} operation
	 *                                       is not supported by this map
	 * @throws ClassCastException            if the class of the specified key or value
	 *                                       prevents it from being stored in this map
	 * @throws NullPointerException          if the specified key or value is null,
	 *                                       and this map does not permit null keys or values
	 * @throws IllegalArgumentException      if some property of the specified key
	 *                                       or value prevents it from being stored in this map
	 * @implNote This implementation intentionally re-abstracts the
	 * inappropriate default provided in {@code Map}.
	 */
	@Override
	public V putIfAbsent(@NotNull K key, V value) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				return tempNode.getValue();
			}
		}
		this.add(key, value);
		return null;
	}

	/**
	 * Removes the entry for a key only if currently mapped to a given value.
	 * This is equivalent to
	 * <pre> {@code
	 * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
	 *   map.remove(key);
	 *   return true;
	 * } else
	 *   return false;
	 * }</pre>
	 * <p>
	 * except that the action is performed atomically.
	 *
	 * @param key   key with which the specified value is associated
	 * @param value value expected to be associated with the specified key
	 *
	 * @return {@code true} if the value was removed
	 *
	 * @throws UnsupportedOperationException if the {@code remove} operation
	 *                                       is not supported by this map
	 * @throws ClassCastException            if the key or value is of an inappropriate
	 *                                       type for this map
	 *                                       (<a href="../Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if the specified key or value is null,
	 *                                       and this map does not permit null keys or values
	 *                                       (<a href="../Collection.html#optional-restrictions">optional</a>)
	 * @implNote This implementation intentionally re-abstracts the
	 * inappropriate default provided in {@code Map}.
	 */
	@Override
	public boolean remove(@NotNull Object key, Object value) {
		final @NotNull Iterator<TripletNode<K, V>> tempIterator = this.localList.iterator();
		TripletNode<K, V> tempNode;
		synchronized (this.localList) {
			while ((tempNode = tempIterator.next()) != null) {
				if (tempNode.getKey().equals(key)) {
					tempIterator.remove();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Replaces the entry for a key only if currently mapped to a given value.
	 * This is equivalent to
	 * <pre> {@code
	 * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
	 *   map.put(key, newValue);
	 *   return true;
	 * } else
	 *   return false;
	 * }</pre>
	 * <p>
	 * except that the action is performed atomically.
	 *
	 * @param key      key with which the specified value is associated
	 * @param oldValue value expected to be associated with the specified key
	 * @param newValue value to be associated with the specified key
	 *
	 * @return {@code true} if the value was replaced
	 *
	 * @throws UnsupportedOperationException if the {@code put} operation
	 *                                       is not supported by this map
	 * @throws ClassCastException            if the class of a specified key or value
	 *                                       prevents it from being stored in this map
	 * @throws NullPointerException          if a specified key or value is null,
	 *                                       and this map does not permit null keys or values
	 * @throws IllegalArgumentException      if some property of a specified key
	 *                                       or value prevents it from being stored in this map
	 * @implNote This implementation intentionally re-abstracts the
	 * inappropriate default provided in {@code Map}.
	 */
	@Override
	public boolean replace(final @NotNull K key, final @Nullable V oldValue, final @Nullable V newValue) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				if (Objects.equals(tempNode.getValue(), oldValue)) {
					if (Objects.equals(tempNode.getValue(), newValue)) {
						return true;
					} else {
						tempNode.setValue(newValue);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Replaces the entry for a key only if currently mapped to some value.
	 * This is equivalent to
	 * <pre> {@code
	 * if (map.containsKey(key)) {
	 *   return map.put(key, value);
	 * } else
	 *   return null;
	 * }</pre>
	 * <p>
	 * except that the action is performed atomically.
	 *
	 * @param key   key with which the specified value is associated
	 * @param value value to be associated with the specified key
	 *
	 * @return the previous value associated with the specified key, or
	 * {@code null} if there was no mapping for the key.
	 * (A {@code null} return can also indicate that the map
	 * previously associated {@code null} with the key,
	 * if the implementation supports null values.)
	 *
	 * @throws UnsupportedOperationException if the {@code put} operation
	 *                                       is not supported by this map
	 * @throws ClassCastException            if the class of the specified key or value
	 *                                       prevents it from being stored in this map
	 * @throws NullPointerException          if the specified key or value is null,
	 *                                       and this map does not permit null keys or values
	 * @throws IllegalArgumentException      if some property of the specified key
	 *                                       or value prevents it from being stored in this map
	 * @implNote This implementation intentionally re-abstracts the
	 * inappropriate default provided in {@code Map}.
	 */
	@Override
	public V replace(final @NotNull K key, final @Nullable V value) {
		for (final @NotNull TripletNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				if (Objects.equals(tempNode.getValue(), value)) {
					return value;
				} else {
					return tempNode.setValue(value);
				}
			}
		}
		return null;
	}

	/**
	 * The concurrent EntryNodes to be stored in a TripletMap
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 *
	 * @author Zeanon
	 * @version 1.3.0
	 */
	@EqualsAndHashCode
	@Getter(onMethod_ = {@Override})
	@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)})
	public static class ConcurrentTripletNode<K, V> implements TripletNode<K, V> {

		/**
		 * -- Getter --
		 * Returns the line corresponding to this entry.
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the line corresponding to this entry
		 */
		private int line;
		/**
		 * -- Getter --
		 * Returns the key corresponding to this entry.
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the key corresponding to this entry
		 */
		private @NotNull K key;
		/**
		 * -- Getter --
		 * Returns the value corresponding to this entry.  If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this entry
		 */
		private @Nullable V value;


		/**
		 * Replaces the line corresponding to this entry with the specified
		 * line (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param line new key to be stored in this entry
		 *
		 * @return old key corresponding to the entry
		 *
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *                                       is not supported by the backing map
		 * @throws ClassCastException            if the class of the specified line
		 *                                       prevents it from being stored in the backing map
		 */
		@Override
		public synchronized int setLine(final int line) {
			try {
				return this.line;
			} finally {
				this.line = line;
			}
		}

		/**
		 * Replaces the key corresponding to this entry with the specified
		 * key (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param key new key to be stored in this entry
		 *
		 * @return old key corresponding to the entry
		 *
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *                                       is not supported by the backing map
		 * @throws ClassCastException            if the class of the specified key
		 *                                       prevents it from being stored in the backing map
		 * @throws NullPointerException          if the backing map does not permit
		 *                                       null keys, and the specified key is null
		 */
		@Override
		public synchronized @NotNull K setKey(final @NotNull K key) {
			try {
				return this.key;
			} finally {
				this.key = key;
			}
		}

		/**
		 * Replaces the value corresponding to this entry with the specified
		 * value (optional operation).  (Writes through to the map.)  The
		 * behavior of this call is undefined if the mapping has already been
		 * removed from the map (by the iterator's <tt>remove</tt> operation).
		 *
		 * @param value new value to be stored in this entry
		 *
		 * @return old value corresponding to the entry
		 *
		 * @throws UnsupportedOperationException if the <tt>put</tt> operation
		 *                                       is not supported by the backing map
		 * @throws ClassCastException            if the class of the specified value
		 *                                       prevents it from being stored in the backing map
		 * @throws NullPointerException          if the backing map does not permit
		 *                                       null values, and the specified value is null
		 * @throws IllegalArgumentException      if some property of this value
		 *                                       prevents it from being stored in the backing map
		 */
		@Override
		public synchronized @Nullable V setValue(final @Nullable V value) {
			try {
				return this.value;
			} finally {
				this.value = value;
			}
		}


		@Override
		public int compareTo(final @NotNull TripletMap.TripletNode entry) {
			return Integer.compare(this.getLine(), entry.getLine());
		}

		@Override
		public @NotNull String toString() {
			return "(" + this.key + "={" + this.value + "," + this.line + "})";
		}
	}
}