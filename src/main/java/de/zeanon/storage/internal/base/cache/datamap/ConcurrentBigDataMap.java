package de.zeanon.storage.internal.base.cache.datamap;

import de.zeanon.storage.external.browniescollections.BigList;
import de.zeanon.storage.external.browniescollections.GapList;
import de.zeanon.storage.internal.base.cache.base.ConcurrentDataMap;
import de.zeanon.storage.internal.base.interfaces.DataMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;


/**
 * Custom Concurrent Map implementation optimized for ThunderFile
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
public class ConcurrentBigDataMap<K, V> extends ConcurrentDataMap<K, V> {


	public ConcurrentBigDataMap() {
		super(new BigList<>());
	}

	public ConcurrentBigDataMap(final @NotNull Map<K, V> map) {
		super(new GapList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull DataMap<K, V> clone() {
		return new ConcurrentBigDataMap<>(this);
	}
}