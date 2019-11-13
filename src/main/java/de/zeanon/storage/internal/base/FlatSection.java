package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.base.interfaces.StorageBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@SuppressWarnings("unused")
public abstract class FlatSection implements StorageBase, Comparable<FlatSection> {

	private final FlatFile flatFile;
	@Getter
	@Setter
	protected String sectionKey;


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile flatFile) {
		this.sectionKey = Objects.notNull(sectionKey, "Key  must not be null");
		this.flatFile = flatFile;
	}


	public void setReloadSetting(final @NotNull ReloadSettingBase reloadSetting) {
		this.flatFile.setReloadSetting(reloadSetting);
	}

	@Override
	public Object get(final @NotNull String key) {
		return this.flatFile.get(this.getFinalKey(key));
	}

	@Override
	public Map<String, Object> getAll(final @NotNull List<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys) {
		return this.flatFile.getAll(this.getFinalKey(key), keys);
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.getFinalKey(key), value);
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getSectionKey(), dataMap);
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getFinalKey(key), dataMap);
	}

	public synchronized void set(final @Nullable Object value) {
		this.flatFile.set(this.getSectionKey(), value);
	}

	public synchronized void remove() {
		this.flatFile.remove(this.getSectionKey());
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		this.flatFile.remove(this.getFinalKey(key));
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.getSectionKey(), keys);
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.getFinalKey(key), keys);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.getFinalKey(key));
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
		return this.flatFile.keySet(this.getFinalKey(key));
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		return this.flatFile.blockKeySet(this.getFinalKey(key));
	}

	protected String getFinalKey(final @NotNull String key) {
		return (this.getSectionKey() == null || this.getSectionKey().isEmpty()) ? Objects.notNull(key, "Key  must not be null") : this.getSectionKey() + "." + Objects.notNull(key, "Key  must not be null");
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}