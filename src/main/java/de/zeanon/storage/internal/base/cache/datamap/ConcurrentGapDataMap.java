package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.browniescollections.GapList;
import de.zeanon.storage.internal.base.cache.base.ConcurrentDataMap;
import de.zeanon.storage.internal.base.interfaces.DataMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Concurrent Map implementation optimized for ThunderFile
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
public class ConcurrentGapDataMap<K, V> extends ConcurrentDataMap<K, V> {


	public ConcurrentGapDataMap() {
		super(new GapList<>());
	}

	public ConcurrentGapDataMap(final @NotNull Map<K, V> map) {
		super(new GapList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull DataMap<K, V> clone() {
		return new ConcurrentGapDataMap<>(this);
	}
}