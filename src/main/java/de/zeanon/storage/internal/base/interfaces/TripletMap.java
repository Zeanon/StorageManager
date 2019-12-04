package de.zeanon.storage.internal.base.interfaces;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public interface TripletMap<K, V> extends Map<K, V> {

	@Nullable V put(final @NotNull K key, final @Nullable V value, final int index);

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
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @param index the index to be associated with the specific key
	 */
	void add(final @NotNull K key, final @Nullable V value, final int index);

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

	void trimToSize();

	@Contract("-> new")
	@NotNull List<TripletNode<K, V>> entryList();


	@SuppressWarnings("UnusedReturnValue")
	interface TripletNode<K, V> extends Map.Entry<K, V>, Comparable<TripletNode> {

		int getIndex();

		int setIndex(final int index);

		@NotNull K setKey(final @NotNull K key);
	}
}