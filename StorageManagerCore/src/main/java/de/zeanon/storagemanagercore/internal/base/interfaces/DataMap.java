package de.zeanon.storagemanagercore.internal.base.interfaces;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public interface DataMap<K, V> extends Map<K, V>, Cloneable {

	@Nullable V getFromIndex(final int index);

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	void add(final @NotNull K key, final @Nullable V value);

	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param node mapping to be added to the map
	 */
	void add(final @NotNull DataMap.DataNode<K, V> node);

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	void addAll(final @NotNull List<DataNode<K, V>> nodes);

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodeMap mappings to be stored in this map
	 */
	void addAll(final @NotNull Map<? extends K, ? extends V> nodeMap);

	/**
	 * An application can use this operation to minimize the storage of an instance.
	 */
	void trimToSize();

	/**
	 * Abstract clone method to be overwritten by extending classes
	 */
	@NotNull DataMap<K, V> clone();

	/**
	 * Returns a {@link List} view of the mappings contained in this map.
	 * The list is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the list is in progress (except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The list
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations.  It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	@Contract("-> new")
	@NotNull List<DataNode<K, V>> entryList();


	/**
	 * Basic Interface for DataMap Nodes
	 *
	 * @param <K> the type of keys maintained by this map
	 * @param <V> the type of mapped values
	 */
	@SuppressWarnings("unused")
	interface DataNode<K, V> extends Map.Entry<K, V> {

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
		@NotNull K setKey(final @NotNull K key);

		/**
		 * Returns a String representation of the Node
		 *
		 * @return the Node parsed to a String
		 */
		@Override
		@NotNull String toString();
	}
}