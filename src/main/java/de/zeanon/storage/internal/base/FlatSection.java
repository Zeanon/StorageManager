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


@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public abstract class FlatSection implements StorageBase, Comparable<FlatSection> {

	@NotNull
	private final FlatFile flatFile;
	@Nullable
	@Getter
	@Setter
	protected String sectionKey;
	@Nullable
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

	@Nullable
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
		this.flatFile.setAll(Objects.notNull(this.getSectionKey()), dataMap);
	}

	@Override
	public void setAll(final @NotNull String blockKey, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getFinalKey(blockKey), dataMap);
	}

	public void set(final @Nullable Object value) {
		this.flatFile.set(Objects.notNull(this.getSectionKey()), value);
	}

	public void remove() {
		this.flatFile.remove(Objects.notNull(this.getSectionKey()));
	}

	@Override
	public void remove(final @NotNull String key) {
		this.flatFile.remove(this.getFinalKey(key));
	}

	@Override
	public void removeAll(final @NotNull String... keys) {
		this.flatFile.removeAll(Objects.notNull(this.getSectionKey()), keys);
	}

	@Override
	public void removeAll(final @NotNull String blockKey, final @NotNull String... keys) {
		this.flatFile.removeAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.getFinalKey(key));
	}

	@Nullable
	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(Objects.notNull(this.getSectionKey()));
	}

	@Nullable
	@Override
	public Set<String> blockKeySet() {
		return this.flatFile.blockKeySet(Objects.notNull(this.getSectionKey()));
	}

	@Nullable
	@Override
	public Set<String> keySet(final @NotNull String key) {
		return this.flatFile.keySet(this.getFinalKey(key));
	}

	@Nullable
	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		return this.flatFile.blockKeySet(this.getFinalKey(key));
	}

	@Nullable
	@Override
	public Object getUseArray(final @NotNull String... key) {
		return this.flatFile.getUseArray(this.getFinalArrayKey(key));
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		//noinspection NullableProblems
		return this.flatFile.getAllUseArray(Objects.notNull(this.arraySectionKey), keys);
	}

	@NotNull
	@Override
	public Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@NotNull
	@Override
	public Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		//noinspection NullableProblems
		return this.flatFile.getAllUseArray(Objects.notNull(this.arraySectionKey), keys);
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
		//noinspection NullableProblems
		this.flatFile.setAllUseArray(Objects.notNull(this.getArraySectionKey()), dataMap);
	}

	@Override
	public void setAllUseArray(final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		this.flatFile.setAllUseArray(this.getFinalArrayKey(blockKey), dataMap);
	}

	@Nullable
	@Override
	public Set<String[]> keySetUseArray() {
		//noinspection NullableProblems
		return this.flatFile.keySetUseArray(Objects.notNull(this.arraySectionKey));
	}

	@Nullable
	@Override
	public Set<String[]> keySetUseArray(final @NotNull String... key) {
		return this.flatFile.keySetUseArray(this.getFinalArrayKey(key));
	}

	@Nullable
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
		//noinspection NullableProblems
		this.flatFile.removeAllUseArray(Objects.notNull(this.arraySectionKey), keys);
	}

	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		this.flatFile.removeAll(Objects.notNull(this.sectionKey), keys);
	}

	@Override
	public void removeAllUseArray(final @NotNull Collection<String[]> keys) {
		//noinspection NullableProblems
		this.flatFile.removeAllUseArray(Objects.notNull(this.arraySectionKey), keys);
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
		return (this.getSectionKey() == null || this.getSectionKey().isEmpty()) ? Objects.notNull(key, "Key  must not be null") : this.getSectionKey() + "." + Objects.notNull(key, "Key  must not be null");
	}

	@NotNull
	protected String[] getFinalArrayKey(final @NotNull String... key) {
		String[] tempKey = new String[Objects.notNull(this.arraySectionKey).length + key.length];
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