package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.AbstractTripletMap;
import de.zeanon.storage.internal.base.interfaces.TripletMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Map implementation optimized for ThunderFile
 * Internally based on GapList for low memory usage and fast access
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "WeakerAccess"})
public class GapTripletMap<K, V> extends AbstractTripletMap<K, V> {


	public GapTripletMap() {
		super(new GapList<>());
	}

	public GapTripletMap(final @NotNull Map<K, V> map) {
		super(new GapList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull TripletMap<K, V> clone() {
		return new GapTripletMap<>(this);
	}
}