package de.zeanon.storagemanagercore.internal.base.cache.base;

import de.zeanon.storagemanagercore.external.browniescollections.IList;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.StampedLock;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
 * @version 1.4.0
 */
@SuppressWarnings("ALL")
@EqualsAndHashCode(callSuper = true)
public abstract class ConcurrentAbstractDataMap<K, V> extends AbstractDataMap<K, V> implements DataMap<K, V>, ConcurrentMap<K, V>, Serializable {


	private static final long serialVersionUID = -622922610455274342L;


	/**
	 * Local lock to ensure internal concurrency
	 */
	private transient @NotNull StampedLock localLock = new StampedLock();
	private transient @NotNull StampedLock modificationLock = new StampedLock();


	/**
	 * Generate a new ConcurrentAbstractDataMap
	 *
	 * @param localList the List to be used to store the internal nodes
	 */
	protected ConcurrentAbstractDataMap(final @NotNull IList<DataNode<K, V>> localList) {
		super(localList);
	}


	/**
	 * If the specified key is not already associated
	 * with a value, associate it with the given value.
	 * This is equivalent to
	 * {@code
	 * if (!map.containsKey(key))
	 * return map.put(key, value);
	 * else
	 * return map.get(key);
	 * }
	 * 
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
	public V putIfAbsent(final @NotNull K key, final @Nullable V value) {
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (tempNode.getKey().equals(key)) {
					return tempNode.getValue();
				}
			}
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
		this.add(key, value);
		return null;
	}

	/**
	 * Removes the entry for a key only if currently mapped to a given value.
	 * This is equivalent to
	 * {@code
	 * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
	 * map.remove(key);
	 * return true;
	 * } else
	 * return false;
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
	public boolean remove(final @NotNull Object key, final @Nullable Object value) {
		final @NotNull Iterator<DataNode<K, V>> tempIterator = this.localList.iterator();
		@NotNull DataNode<K, V> tempNode;
		long lockStamp = this.localLock.readLock();
		try {
			while ((tempNode = tempIterator.next()) != null) {
				if (tempNode.getKey().equals(key) && tempNode.getValue().equals(value)) {
					final long tempLock = this.localLock.tryConvertToWriteLock(lockStamp);
					if (this.localLock.validate(tempLock)) {
						lockStamp = tempLock;
					} else {
						final long modifyLock = this.modificationLock.writeLock();
						try {
							this.localLock.unlockRead(lockStamp);
							lockStamp = this.localLock.writeLock();
						} finally {
							this.modificationLock.unlockWrite(modifyLock);
						}
					}
					if (tempNode.getKey().equals(key) && tempNode.getValue().equals(value)) {
						tempIterator.remove();
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		} finally {
			this.localLock.unlock(lockStamp);
		}
	}

	/**
	 * Replaces the entry for a key only if currently mapped to a given value.
	 * This is equivalent to
	 * {@code
	 * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
	 * map.put(key, newValue);
	 * return true;
	 * } else
	 * return false;
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
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (tempNode.getKey().equals(key) && Objects.equals(tempNode.getValue(), oldValue)) {
					if (Objects.equals(tempNode.getValue(), newValue)) {
						return true;
					} else {
						tempNode.setValue(newValue);
						return true;
					}
				}
			}
			return false;
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	/**
	 * Replaces the entry for a key only if currently mapped to some value.
	 * This is equivalent to
	 * {@code
	 * if (map.containsKey(key)) {
	 * return map.put(key, value);
	 * } else
	 * return null;
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
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (tempNode.getKey().equals(key)) {
					if (Objects.equals(tempNode.getValue(), value)) {
						return value;
					} else {
						return tempNode.setValue(value);
					}
				}
			}
			return null;
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
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
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> TempNode : this.localList) {
				if (TempNode.getKey().equals(key)) {
					return true;
				}
			}
			return false;
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
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
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (value.equals(tempNode.getValue())) {
					return true;
				}
			}
			return false;
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
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
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (tempNode.getKey().equals(key)) {
					if (Objects.equals(tempNode.getValue(), value)) {
						return value;
					} else {
						return tempNode.setValue(value);
					}
				}
			}
		} finally {
			this.localLock.unlockRead(lockStamp);
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
	public void add(final @NotNull DataMap.DataNode<K, V> node) {
		final long lockStamp = this.localLock.writeLock();
		final long modifyLock = this.modificationLock.writeLock();
		try {
			this.localList.add(node);
		} finally {
			this.localLock.unlockWrite(lockStamp);
			this.modificationLock.unlockWrite(modifyLock);
		}
	}

	@Override
	public void add(final @NotNull K key, final @Nullable V value) {
		this.add(new ConcurrentNode<>(key, value));
	}

	@Override
	public void addAll(final @NotNull Map<? extends K, ? extends V> nodeMap) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : nodeMap.entrySet()) {
			this.add(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	@Override
	public void addAll(final @NotNull List<DataNode<K, V>> nodes) {
		final long lockStamp = this.localLock.writeLock();
		final long modifyLock = this.modificationLock.writeLock();
		try {
			this.localList.addAll(nodes);
		} finally {
			this.localLock.unlockWrite(lockStamp);
			this.modificationLock.unlockWrite(modifyLock);
		}
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 * <p>
	 * More formally, if this map contains a mapping from a key
	 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
	 * key.equals(k))}, then this method returns {@code v}; otherwise
	 * it returns {@code null}.  (There can be at most one such mapping.)
	 * <p>
	 * A return value of {@code null} does not <i>necessarily</i>
	 * indicate that the map contains no mapping for the key; it's also
	 * possible that the map explicitly maps the key to {@code null}.
	 * The {@link #containsKey containsKey} operation may be used to
	 * distinguish these two cases.
	 *
	 * @see #put(Object, Object)
	 */
	@Override
	public @Nullable V get(final @NotNull Object key) {
		final long lockStamp = this.localLock.readLock();
		try {
			for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
				if (tempNode.getKey().equals(key)) {
					return tempNode.getValue();
				}
			}
			return null;
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	@Override
	public @Nullable V getFromIndex(final int index) {
		final long lockStamp = this.localLock.readLock();
		try {
			return this.localList.get(index).getValue();
		} finally {
			this.localLock.unlockRead(lockStamp);
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
		long lockStamp = this.localLock.readLock();
		try {
			final @NotNull Iterator<DataNode<K, V>> tempIterator = this.localList.iterator();
			DataNode<K, V> tempNode;
			while ((tempNode = tempIterator.next()) != null) {
				if (tempNode.getKey().equals(key)) {
					final long tempLock = this.localLock.tryConvertToWriteLock(lockStamp);
					if (this.localLock.validate(tempLock)) {
						lockStamp = tempLock;
					} else {
						final long modifyLock = this.modificationLock.writeLock();
						try {
							this.localLock.unlockRead(lockStamp);
							lockStamp = this.localLock.writeLock();
						} finally {
							this.modificationLock.unlockWrite(modifyLock);
						}
					}
					if (tempNode.getKey().equals(key)) {
						tempIterator.remove();
						return tempNode.getValue();
					} else {
						return null;
					}
				}
			}
			return null;
		} finally {
			this.localLock.unlock(lockStamp);
		}
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	@Override
	public void clear() {
		final long lockStamp = this.localLock.writeLock();
		final long modifyLock = this.modificationLock.writeLock();
		try {
			this.localList.clear();
			this.localList.trimToSize();
		} finally {
			this.localLock.unlockWrite(lockStamp);
			this.modificationLock.unlockWrite(modifyLock);
		}
	}

	/**
	 * Trim the backing List to its minimal possible size to minimize Ram usage
	 */
	@Override
	public void trimToSize() {
		final long lockStamp = this.localLock.writeLock();
		final long modifyLock = this.modificationLock.writeLock();
		try {
			this.localList.trimToSize();
		} finally {
			this.localLock.unlockWrite(lockStamp);
			this.modificationLock.unlockWrite(modifyLock);
		}
	}

	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * The set is not backed by the map, so changes to the map are not
	 * reflected in the set, and vice-versa.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	@Override
	@Contract("-> new")
	public @NotNull Set<Entry<K, V>> entrySet() {
		final long lockStamp = this.localLock.readLock();
		try {
			return new HashSet<>(this.localList);
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	/**
	 * Returns a {@link List} view of the mappings contained in this map.
	 * The list is backed by the map, so changes to the map are
	 * reflected in the list, and vice-versa.  If the map is modified
	 * while an iteration over the list is in progress (except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The list
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>List.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations.  It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a list view of the mappings contained in this map
	 */
	@Override
	public @NotNull List<DataNode<K, V>> entryList() {
		return this.localList;
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 */
	@Override
	public int size() {
		final long lockStamp = this.localLock.readLock();
		try {
			return this.localList.size();
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 */
	@Override
	@Contract(pure = true)
	public boolean isEmpty() {
		final long lockStamp = this.localLock.readLock();
		try {
			return this.localList.isEmpty();
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	/**
	 * Abstract clone method to be overwritten by extending classes
	 */
	@Override //NOSONAR
	public abstract @NotNull DataMap<K, V> clone(); //NOSONAR

	/**
	 * Method to reinitialize the map on deserialization
	 *
	 * @param localList the List to be used to store the internal nodes
	 */
	@Override
	protected void reinitialize(final @NotNull IList<DataNode<K, V>> localList) {
		this.localList = localList;
		this.localLock = new StampedLock();
		this.modificationLock = new StampedLock();
	}

	/**
	 * Returns a String representation of the Map
	 *
	 * @return the Map parsed to a String
	 */
	@Override
	public @NotNull String toString() {
		final long lockStamp = this.localLock.readLock();
		try {
			return this.localList.toString();
		} finally {
			this.localLock.unlockRead(lockStamp);
		}
	}

	/**
	 * The concurrent EntryNodes to be stored in a ConcurrentAbstractDataMap
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 *
	 * @author Zeanon
	 * @version 1.3.0
	 */
	@EqualsAndHashCode
	@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)})
	private static class ConcurrentNode<K, V> implements DataNode<K, V> {


		/**
		 * Local lock to ensure internal concurrency
		 */
		private final @NotNull StampedLock localLock = new StampedLock();


		/**
		 * The key assigned to this Node
		 */
		private @NotNull K key;

		/**
		 * The value assigned to this Node
		 */
		private @Nullable V value;

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
		public @NotNull K setKey(final @NotNull K key) {
			final long lockStamp = this.localLock.writeLock();
			try {
				return this.key;
			} finally {
				this.key = key;
				this.localLock.unlockWrite(lockStamp);
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
		public @Nullable V setValue(final @Nullable V value) {
			final long lockStamp = this.localLock.writeLock();
			try {
				return this.value;
			} finally {
				this.value = value;
				this.localLock.unlockWrite(lockStamp);
			}
		}

		/**
		 * Returns the key corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the key corresponding to this node
		 *
		 * @throws IllegalStateException implementations may, but are not
		 *                               required to, throw this exception if the entry has been
		 *                               removed from the backing map.
		 */
		@Override
		public @NotNull K getKey() {
			final long lockStamp = this.localLock.readLock();
			try {
				return this.key;
			} finally {
				this.localLock.unlockRead(lockStamp);
			}
		}

		/**
		 * Returns the value corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this node
		 *
		 * @throws IllegalStateException implementations may, but are not
		 *                               required to, throw this exception if the entry has been
		 *                               removed from the backing map.
		 */
		@Override
		public @Nullable V getValue() {
			final long lockStamp = this.localLock.readLock();
			try {
				return this.value;
			} finally {
				this.localLock.unlockRead(lockStamp);
			}
		}


		/**
		 * Returns a String representation of the Node
		 *
		 * @return the Node parsed to a String
		 */
		@Override
		public @NotNull String toString() {
			final long lockStamp = this.localLock.readLock();
			try {
				return "(" + this.key + "=" + this.value + ")";
			} finally {
				this.localLock.unlockRead(lockStamp);
			}
		}
	}
}
