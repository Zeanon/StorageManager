package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
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
@Accessors(chain = true)
@SuppressWarnings("unused")
public abstract class FlatSection<C extends FlatSection, F extends FlatFile> implements StorageBase<C>, Comparable<FlatSection> {

	private final FlatFile<F> flatFile;
	@Getter
	protected String sectionKey;


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile<F> flatFile) {
		this.sectionKey = Objects.notNull(sectionKey, "Key must not be null");
		this.flatFile = flatFile;
	}


	public C setReloadSetting(final @NotNull ReloadSettingBase reloadSetting) {
		this.flatFile.setReloadSetting(reloadSetting);
		//noinspection unchecked
		return (C) this;
	}

	public C setSectionKey(final @NotNull String sectionKey) {
		this.sectionKey = sectionKey;
		//noinspection unchecked
		return (C) this;
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
	public synchronized C set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.sectionKey(key), value);
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public synchronized C setAll(final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getSectionKey(), dataMap);
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public synchronized C setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.sectionKey(key), dataMap);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C set(final @Nullable Object value) {
		this.flatFile.set(this.getSectionKey(), value);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C remove() {
		this.flatFile.remove(this.getSectionKey());
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public synchronized C remove(final @NotNull String key) {
		this.flatFile.remove(this.sectionKey(key));
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public synchronized C removeAll(final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.getSectionKey(), keys);
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public synchronized C removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.sectionKey(key), keys);
		//noinspection unchecked
		return (C) this;
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.sectionKey(key));
	}

	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(this.getSectionKey());
	}

	@Override
	public Set<String> blockKeySet() {
		return this.flatFile.blockKeySet(this.getSectionKey());
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
		return (this.getSectionKey() == null || this.getSectionKey().isEmpty()) ? Objects.notNull(key, "Key must not be null") : this.getSectionKey() + "." + Objects.notNull(key, "Key must not be null");
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}