package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.internal.base.cache.base.ConcurrentTripletMap;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Map implementation optimized for ThunderFile
 * internally based on LinkedList for low access times
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ConcurrentBigTripletMap<K, V> extends ConcurrentTripletMap<K, V> {


	public ConcurrentBigTripletMap() {
		super(new BigList<>());
	}


	@Override
	@Contract("-> new")
	public @NotNull List<TripletNode<K, V>> entryList() {
		return new BigList<>(this.localList);
	}
}