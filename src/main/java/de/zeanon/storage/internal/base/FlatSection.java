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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@SuppressWarnings("unused")
public abstract class FlatSection<@NotNull K> implements StorageBase, Comparable<FlatSection> {

	@NotNull
	private final FlatFile<K> flatFile;
	@NotNull
	@Setter
	protected String sectionKey = "";
	@NotNull
	@Setter
	protected String[] arraySectionKey = new String[0];


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile<K> flatFile) {
		this.setSectionKey(Objects.notNull(sectionKey, "SectionKey  must not be null"));
		this.setArraySectionKey(Objects.notNull(sectionKey, "SectionKey  must not be null").split("\\."));
		this.flatFile = flatFile;
	}

	protected FlatSection(final @NotNull String[] sectionKey, final @NotNull FlatFile<K> flatFile) {
		@NotNull StringBuilder tempKey = new StringBuilder(sectionKey[0]);
		for (int i = 1; i < sectionKey.length; i++) {
			tempKey.append(".").append(sectionKey[i]);
		}
		this.setSectionKey(tempKey.toString());
		this.setArraySectionKey(Objects.notNull(sectionKey, "SectionKey  must not be null"));
		this.flatFile = flatFile;
	}


	public void setReloadSetting(final @NotNull ReloadSettingBase reloadSetting) {
		this.flatFile.setReloadSetting(reloadSetting);
	}

	@NotNull
	@Override
	public Object get(final @NotNull String key) {
		return this.flatFile.get(this.getFinalKey(key));
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String... keys) {
		return this.flatFile.getAll(keys);
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys) {
		return this.flatFile.getAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public void set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.getFinalKey(key), value);
	}

	@Override
	public void setAll(final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getSectionKey(), dataMap);
	}

	@Override
	public void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getFinalKey(blockKey), dataMap);
	}

	public void set(final @Nullable Object value) {
		this.flatFile.set(this.getSectionKey(), value);
	}

	public void remove() {
		this.flatFile.remove(this.getSectionKey());
	}

	@Override
	public void remove(final @NotNull String key) {
		this.flatFile.remove(this.getFinalKey(key));
	}

	@Override
	public void removeAll(final @NotNull String... keys) {
		this.flatFile.removeAll(this.getSectionKey(), keys);
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull String... keys) {
		this.flatFile.removeAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.getFinalKey(key));
	}

	@NotNull
	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(this.getSectionKey());
	}

	@NotNull
	@Override
	public Set<String> blockKeySet() {
		return this.flatFile.blockKeySet(this.getSectionKey());
	}

	@NotNull
	@Override
	public Set<String> keySet(final @NotNull String key) {
		return this.flatFile.keySet(this.getFinalKey(key));
	}

	@NotNull
	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		return this.flatFile.blockKeySet(this.getFinalKey(key));
	}

	@NotNull
	@Override
	public Object getUseArray(final @NotNull String... key) {
		return this.flatFile.getUseArray(this.getFinalArrayKey(key));
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		return this.flatFile.getAllUseArray(this.getArraySectionKey(), keys);
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		return this.flatFile.getAllUseArray(this.getArraySectionKey(), keys);
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		return this.flatFile.getAllUseArray(this.getFinalArrayKey(blockKey), keys);
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(this.getFinalKey(blockKey), keys);
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		return this.flatFile.getAllUseArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public boolean hasKeyUseArray(final @NotNull String... key) {
		return this.flatFile.hasKeyUseArray(this.getFinalArrayKey(key));
	}

	@Override
	public void setUseArray(final @NotNull String[] key, final @Nullable Object value) {
		this.flatFile.setUseArray(this.getFinalArrayKey(key), value);
	}

	@Override
	public void setAllUseArray(final @NotNull Map<String[], Object> dataMap) {
		this.flatFile.setAllUseArray(this.getArraySectionKey(), dataMap);
	}

	@Override
	public void setAllUseArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		this.flatFile.setAllUseArray(this.getFinalArrayKey(blockKey), dataMap);
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray() {
		return this.flatFile.keySetUseArray(this.getArraySectionKey());
	}

	@NotNull
	@Override
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		return this.flatFile.keySetUseArray(this.getFinalArrayKey(key));
	}

	@NotNull
	@Override
	public Set<String> blockKeySetUseArray(final @NotNull String... key) {
		return this.flatFile.blockKeySetUseArray(this.getFinalArrayKey(key));
	}

	@Override
	public void removeUseArray(final @NotNull String... key) {
		this.flatFile.removeUseArray(this.getFinalArrayKey(key));
	}

	@Override
	public void removeAllUseArray(final @NotNull String[]... keys) {
		this.flatFile.removeAllUseArray(this.getArraySectionKey(), keys);
	}

	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		this.flatFile.removeAll(this.getSectionKey(), keys);
	}

	@Override
	public void removeAllUseArray(final @NotNull Collection<String[]> keys) {
		this.flatFile.removeAllUseArray(this.getArraySectionKey(), keys);
	}

	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.flatFile.removeAllUseArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		this.flatFile.removeAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
		this.flatFile.removeAllUseArray(this.getFinalArrayKey(blockKey), keys);
	}

	@NotNull
	protected String getFinalKey(final @NotNull String key) {
		return this.getSectionKey() + "." + Objects.notNull(key, "Key  must not be null");
	}

	@NotNull
	protected String[] getFinalArrayKey(final @NotNull String... key) {
		@NotNull String[] tempKey = new String[this.getArraySectionKey().length + key.length];
		System.arraycopy(this.getArraySectionKey(), 0, tempKey, 0, this.getArraySectionKey().length);
		System.arraycopy(key, 0, tempKey, this.getArraySectionKey().length, key.length);
		return tempKey;
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}