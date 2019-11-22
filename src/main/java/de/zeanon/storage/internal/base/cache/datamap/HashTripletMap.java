package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Map implementation optimized for ThunderFile
 * Internally based on ArrayList for low memory usage
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@NoArgsConstructor(onConstructor_ = @Contract(pure = true))
@AllArgsConstructor(onConstructor_ = @Contract(pure = true))
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class HashTripletMap<K, V> extends TripletMap<K, V> {

	/**
	 * Internal List storing the DataNodes
	 */
	@NotNull
	@Getter(onMethod_ = @Override, value = AccessLevel.PROTECTED)
	private List<TripletNode<K, V>> localList = new ArrayList<>();
}