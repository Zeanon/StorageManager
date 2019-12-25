package de.zeanon.storagemanager.internal.base.cache.datamap;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.base.AbstractDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import java.io.*;
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
 * @version 1.5.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class GapDataMap<K, V> extends AbstractDataMap<K, V> implements Serializable {


	private static final long serialVersionUID = 521676275044874030L;


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
		this.reinitialize(new GapList<>());
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