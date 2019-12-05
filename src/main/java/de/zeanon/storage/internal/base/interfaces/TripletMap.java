package de.zeanon.storage.internal.base.interfaces;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public interface TripletMap<K, V> extends Map<K, V> {

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
	void add(final @NotNull TripletNode<K, V> node);

	/**
	 * Copies all of the mappings from the specified map to this map.
	 *
	 * @param nodes mappings to be added to the map
	 */
	void addAll(final @NotNull List<TripletNode<K, V>> nodes);

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

	@NotNull TripletMap<K, V> copy();

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
	@NotNull List<TripletNode<K, V>> entryList();


	@SuppressWarnings("UnusedReturnValue")
	interface TripletNode<K, V> extends Map.Entry<K, V> {

		@NotNull K setKey(final @NotNull K key);

		@Override
		@NotNull String toString();
	}
}