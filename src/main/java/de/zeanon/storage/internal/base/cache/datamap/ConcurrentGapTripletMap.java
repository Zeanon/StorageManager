package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.ConcurrentTripletMap;
import java.util.List;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ConcurrentGapTripletMap<K, V> extends ConcurrentTripletMap<K, V> {


	public ConcurrentGapTripletMap() {
		super(new GapList<>());
	}


	@Override
	@Contract("-> new")
	public @NotNull List<TripletNode<K, V>> entryList() {
		return this.entryList(new GapList<>());
	}
}