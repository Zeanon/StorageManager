package de.zeanon.storagemanager.internal.base.cache.datamap;

import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.internal.base.cache.base.ConcurrentDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import java.io.*;
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
 * @version 1.5.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ConcurrentBigDataMap<K, V> extends ConcurrentDataMap<K, V> implements Serializable {


	private static final long serialVersionUID = 757613140243598365L;


	public ConcurrentBigDataMap() {
		super(new BigList<>());
	}

	public ConcurrentBigDataMap(final @NotNull Map<K, V> map) {
		super(new BigList<>());
		this.addAll(map);
	}


	@Override
	public @NotNull DataMap<K, V> clone() {
		return new ConcurrentBigDataMap<>(this);
	}


	private void writeObject(final @NotNull ObjectOutputStream outputStream) throws IOException {
		outputStream.defaultWriteObject();
		outputStream.writeInt(this.size());
		this.writeNodes(outputStream);
	}

	private void writeNodes(final @NotNull ObjectOutputStream outputStream) throws IOException {
		for (final @NotNull DataMap.DataNode<K, V> entry : this.entryList()) {
			outputStream.writeObject(entry.getKey());
			outputStream.writeObject(entry.getValue());
		}
	}

	private void readObject(final @NotNull ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		this.reinitialize(new BigList<>());
		this.clear();
		final int mappings = inputStream.readInt();
		if (mappings < 0) {
			throw new InvalidObjectException("Illegal mappings count: "
											 + mappings);
		} else if (mappings > 0) {
			for (int i = 0; i < mappings; i++) {
				//noinspection unchecked
				this.add((K) inputStream.readObject(), (V) inputStream.readObject());
			}
		}
	}
}