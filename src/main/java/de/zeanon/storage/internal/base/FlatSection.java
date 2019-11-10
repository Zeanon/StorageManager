package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.StorageBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@SuppressWarnings("unused")
public abstract class FlatSection implements StorageBase, Comparable<FlatSection> {

	private final FlatFile flatFile;
	@Getter
	protected String sectionKey;


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile flatFile) {
		this.sectionKey = Objects.notNull(sectionKey, "Key must not be null");
		this.flatFile = flatFile;
	}

	@Override
	public Object get(final @NotNull String key) {
		return this.flatFile.get(this.sectionKey(key));
	}

	@Override
	public Map<String, Object> getAll(final @NotNull List<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys) {
		return this.flatFile.getAll(this.sectionKey(key), keys);
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.sectionKey(key), value);
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.sectionKey(), dataMap);
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.sectionKey(key), dataMap);
	}

	public synchronized void set(final @Nullable Object value) {
		this.flatFile.set(this.sectionKey(), value);
	}

	public synchronized void remove() {
		this.flatFile.remove(this.sectionKey());
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		this.flatFile.remove(this.sectionKey(key));
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.sectionKey(), keys);
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.sectionKey(key), keys);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.sectionKey(key));
	}

	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(this.sectionKey());
	}

	@Override
	public Set<String> blockKeySet() {
		return this.flatFile.blockKeySet(this.sectionKey());
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		return this.flatFile.keySet(this.sectionKey(key));
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		return this.flatFile.blockKeySet(this.sectionKey(key));
	}

	protected String sectionKey(final @NotNull String key) {
		return (this.sectionKey() == null || this.sectionKey().isEmpty()) ? Objects.notNull(key, "Key must not be null") : this.sectionKey() + "." + Objects.notNull(key, "Key must not be null");
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}