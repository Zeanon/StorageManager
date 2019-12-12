package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.browniescollections.GapList;
import de.zeanon.storage.internal.base.cache.base.AbstractDataMap;
import de.zeanon.storage.internal.base.interfaces.DataMap;
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
public class GapDataMap<K, V> extends AbstractDataMap<K, V> {


	public GapDataMap() {
		super(new GapList<>());
	}

	public GapDataMap(final @NotNull Map<K, V> map) {
		super(new GapList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull DataMap<K, V> clone() {
		return new GapDataMap<>(this);
	}
}