package de.zeanon.storagemanager.internal.base.cache.base;

import de.zeanon.storagemanager.external.browniescollections.IList;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import java.io.Serializable;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
 * @version 1.4.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class AbstractDataMap<K, V> extends AbstractMap<K, V> implements DataMap<K, V>, Serializable {


	private static final long serialVersionUID = -822922610855254342L;


	/**
	 * List that holds the internal nodes
	 */
	private transient @NotNull IList<DataNode<K, V>> localList;


	/**
	 * Generate a new ConcurrentAbstractDataMap
	 *
	 * @param localList the List to be used to store the internal nodes
	 */
	@Contract(pure = true)
	protected AbstractDataMap(final @NotNull IList<DataNode<K, V>> localList) {
		this.localList = localList;
	}


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
		for (final @NotNull DataMap.DataNode<K, V> TempNode : this.localList) {
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
		for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
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
	public @Nullable
	V put(final @NotNull K key, final @Nullable V value) {
		for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
			if (tempNode.getKey().equals(key)) {
				return tempNode.setValue(value);
			}
		}
		this.add(key, value);
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
	@Override
	public void add(final @NotNull K key, final @Nullable V value) {
		this.add(new Node<>(key, value));
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param node mapping to be added to the map
	 */
	@Override
	public void add(final @NotNull DataMap.DataNode<K, V> node) {
		this.localList.add(node);
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	@Override
	public void addAll(final @NotNull List<DataNode<K, V>> nodes) {
		this.localList.addAll(nodes);
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodeMap mappings to be stored in this map
	 */
	@Override
	public void addAll(final @NotNull Map<? extends K, ? extends V> nodeMap) {
		for (final @NotNull Map.Entry<? extends K, ? extends V> entry : nodeMap.entrySet()) {
			this.add(entry.getKey(), entry.getValue());
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
	public @Nullable
	V get(final @NotNull Object key) {
		for (final @NotNull DataMap.DataNode<K, V> tempNode : this.localList) {
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
	public @Nullable
	V remove(final @NotNull Object key) {
		final @NotNull Iterator<DataNode<K, V>> tempIterator = this.localList.iterator();
		DataNode<K, V> tempNode;
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
		this.localList.trimToSize();
	}

	/**
	 * Trim the backing List to its minimal possible size to minimize Ram usage
	 */
	@Override
	public void trimToSize() {
		this.localList.trimToSize();
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
	public @NotNull Set<Map.Entry<K, V>> entrySet() {
		return new HashSet<>(this.localList);
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
	 * Abstract clone method to be overwritten by extending classes
	 */
	@Override //NOSONAR
	public abstract @NotNull DataMap<K, V> clone(); //NOSONAR

	/**
	 * Method to reinitialize the map on deserialization
	 *
	 * @param localList the List to be used to store the internal nodes
	 */
	protected void reinitialize(final @NotNull IList<DataNode<K, V>> localList) {
		this.localList = localList;
	}

	/**
	 * Returns a String representation of the Map
	 *
	 * @return the Map parsed to a String
	 */
	@Override
	public @NotNull String toString() {
		return this.localList.toString();
	}

	/**
	 * The EntryNodes to be stored in a AbstractDataMap
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
	private static class Node<K, V> implements DataNode<K, V> {

		/**
		 * The key assigned to this Node
		 * <p>
		 * -- Getter --
		 * Returns the key corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the key corresponding to this node
		 * @throws IllegalStateException implementations may, but are not
		 * required to, throw this exception if the entry has been
		 * removed from the backing map.
		 */
		private @NotNull K key;

		/**
		 * The value assigned to this Node
		 * <p>
		 * -- Getter --
		 * Returns the value corresponding to this node. If the mapping
		 * has been removed from the backing map (by the iterator's
		 * <tt>remove</tt> operation), the results of this call are undefined.
		 *
		 * @return the value corresponding to this node
		 * @throws IllegalStateException implementations may, but are not
		 * required to, throw this exception if the entry has been
		 * removed from the backing map.
		 */
		private @Nullable
		V value;

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
		public @Nullable
		V setValue(final @Nullable V value) {
			try {
				return this.value;
			} finally {
				this.value = value;
			}
		}


		/**
		 * Returns a String representation of the Node
		 *
		 * @return the Node parsed to a String
		 */
		@Override
		public @NotNull String toString() {
			return "(" + this.key + "=" + this.value + ")";
		}
	}
}