package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.internal.base.cache.base.AbstractTripletMap;
import de.zeanon.storage.internal.base.interfaces.TripletMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Map implementation optimized for ThunderFile
 * internally based on BigList to optimize for huge amounts of entries
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zeanon
 * @version 1.3.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "WeakerAccess"})
public class BigTripletMap<K, V> extends AbstractTripletMap<K, V> {


	public BigTripletMap() {
		super(new BigList<>());
	}

	public BigTripletMap(final @NotNull Map<K, V> map) {
		super(new BigList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull TripletMap<K, V> copy() {
		return new BigTripletMap<>(this);
	}
}