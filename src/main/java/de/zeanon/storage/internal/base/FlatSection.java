package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.base.interfaces.StorageBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.Collection;
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
	@Getter
	@Setter
	protected String[] arraySectionKey;


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile flatFile) {
		this.sectionKey = Objects.notNull(sectionKey, "Key  must not be null");
		this.arraySectionKey = Objects.notNull(sectionKey, "Key  must not be null").split("\\.");
		this.flatFile = flatFile;
	}

	protected FlatSection(final @NotNull String[] sectionKey, final @NotNull FlatFile flatFile) {
		StringBuilder tempKey = new StringBuilder(sectionKey[0]);
		for (int i = 1; i < sectionKey.length; i++) {
			tempKey.append(".").append(sectionKey[i]);
		}
		this.sectionKey = tempKey.toString();
		this.arraySectionKey = Objects.notNull(sectionKey, "Key  must not be null");
		this.flatFile = flatFile;
	}


	public void setReloadSetting(final @NotNull ReloadSettingBase reloadSetting) {
		this.flatFile.setReloadSetting(reloadSetting);
	}

	@Override
	public synchronized Object get(final @NotNull String key) {
		return this.flatFile.get(this.getFinalKey(key));
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String... keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys) {
		return this.flatFile.getAll(this.getFinalKey(blockKey), keys);
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
	public synchronized void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getFinalKey(blockKey), dataMap);
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
	public synchronized void removeAll(final @NotNull String... keys) {
		this.flatFile.removeAll(this.getSectionKey(), keys);
	}

	@Override
	public synchronized void removeAll(final @NotNull String blockKey, final @NotNull String... keys) {
		this.flatFile.removeAll(this.getFinalKey(blockKey), keys);
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

	@Override
	public synchronized Object getFromArray(final @NotNull String... key) {
		return this.flatFile.getFromArray(this.getFinalArrayKey(key));
	}

	@Override
	public synchronized Map<String[], Object> getAllFromArray(final @NotNull String[]... keys) {
		return this.flatFile.getAllFromArray(this.arraySectionKey, keys);
	}

	@Override
	public synchronized Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public synchronized Map<String[], Object> getAllFromArray(final @NotNull Collection<String[]> keys) {
		return this.flatFile.getAllFromArray(this.arraySectionKey, keys);
	}

	@Override
	public synchronized Map<String[], Object> getAllFromArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		return this.flatFile.getAllFromArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public synchronized Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public synchronized Map<String[], Object> getAllFromArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		return this.flatFile.getAllFromArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public boolean arrayHasKey(final @NotNull String... key) {
		return this.flatFile.arrayHasKey(this.getFinalArrayKey(key));
	}

	@Override
	public void setFromArray(final @NotNull String[] key, final @Nullable Object value) {
		this.flatFile.setFromArray(this.getFinalArrayKey(key), value);
	}

	@Override
	public void setAllFromArray(final @NotNull Map<String[], Object> dataMap) {
		this.flatFile.setAllFromArray(this.getArraySectionKey(), dataMap);
	}

	@Override
	public void setAllFromArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		this.flatFile.setAllFromArray(this.getFinalArrayKey(blockKey), dataMap);
	}

	@Override
	public Set<String[]> keySetFromArray() {
		return this.flatFile.keySetFromArray(this.arraySectionKey);
	}

	@Override
	public Set<String[]> keySetFromArray(final @NotNull String... key) {
		return this.flatFile.keySetFromArray(this.getFinalArrayKey(key));
	}

	@Override
	public Set<String> blockKeySetFromArray(final @NotNull String... key) {
		return this.flatFile.blockKeySetFromArray(this.getFinalArrayKey(key));
	}

	@Override
	public void removeFromArray(final @NotNull String... key) {
		this.flatFile.removeFromArray(this.getFinalArrayKey(key));
	}

	@Override
	public void removeAllFromArray(final @NotNull String[]... keys) {
		this.flatFile.removeAllFromArray(this.arraySectionKey, keys);
	}

	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		this.flatFile.removeAll(this.sectionKey, keys);
	}

	@Override
	public void removeAllFromArray(final @NotNull Collection<String[]> keys) {
		this.flatFile.removeAllFromArray(this.arraySectionKey, keys);
	}

	@Override
	public void removeAllFromArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.flatFile.removeAllFromArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		this.flatFile.removeAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public void removeAllFromArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		this.flatFile.removeAllFromArray(this.getFinalArrayKey(blockKey), keys);
	}

	protected String getFinalKey(final @NotNull String key) {
		return (this.getSectionKey() == null || this.getSectionKey().isEmpty()) ? Objects.notNull(key, "Key  must not be null") : this.getSectionKey() + "." + Objects.notNull(key, "Key  must not be null");
	}

	protected String[] getFinalArrayKey(final @NotNull String... key) {
		String[] tempKey = new String[this.arraySectionKey.length + key.length];
		System.arraycopy(this.arraySectionKey, 0, tempKey, 0, this.arraySectionKey.length);
		System.arraycopy(key, 0, tempKey, this.arraySectionKey.length, key.length);
		return (this.getSectionKey() == null || this.getSectionKey().isEmpty())
			   ? Objects.notNull(tempKey, "Key  must not be null")
			   : tempKey;
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}