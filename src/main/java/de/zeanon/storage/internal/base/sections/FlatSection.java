package de.zeanon.storage.internal.base.sections;

import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.DataStorage;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic foundation for the Sections, providing the necessary fields and basic methods
 *
 * @author Zeanon
 * @version 2.4.0
 */
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@SuppressWarnings("unused")
public abstract class FlatSection<F extends FlatFile<? extends FileData<M, ?, L>, M, L>, M extends Map, L extends List> implements DataStorage<M, L>, Comparable<FlatSection> {

	private final @NotNull F flatFile;
	@Setter
	protected @NotNull String sectionKey = "";
	@Setter
	protected @NotNull String[] arraySectionKey = new String[0];


	protected FlatSection(final @NotNull String sectionKey, final @NotNull F flatFile) {
		this.setSectionKey(sectionKey);
		this.setArraySectionKey(sectionKey.split("\\."));
		this.flatFile = flatFile;
	}

	protected FlatSection(final @NotNull String[] sectionKey, final @NotNull F flatFile) {
		@NotNull StringBuilder tempKey = new StringBuilder(sectionKey[0]);
		for (int i = 1; i < sectionKey.length; i++) {
			tempKey.append(".").append(sectionKey[i]);
		}
		this.setSectionKey(tempKey.toString());
		this.setArraySectionKey(sectionKey);
		this.flatFile = flatFile;
	}


	public void reloadSetting(final @NotNull ReloadSetting reloadSetting) {
		this.flatFile.reloadSetting(reloadSetting);
	}

	@NotNull
	@Override
	public Provider<M, L> provider() {
		return this.flatFile.provider();
	}

	@Override
	public void bigList(final boolean bigList) {
		this.flatFile.bigList(bigList);
	}

	@Override
	public @NotNull Object get(final @NotNull String key) {
		return this.flatFile.get(this.getFinalKey(key));
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String... keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull String... keys) {
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

	@Override
	public @NotNull Object getUseArray(final @NotNull String... key) {
		return this.flatFile.getUseArray(this.getFinalArrayKey(key));
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		return this.flatFile.getAllUseArray(this.getArraySectionKey(), keys);
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		return this.flatFile.getAllUseArray(this.getArraySectionKey(), keys);
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		return this.flatFile.getAllUseArray(this.getFinalArrayKey(blockKey), keys);
	}

	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey, final @NotNull Collection<String> keys) {
		return this.flatFile.getAll(this.getFinalKey(blockKey), keys);
	}

	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey, final @NotNull Collection<String[]> keys) {
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

	protected @NotNull String getFinalKey(final @NotNull String key) {
		return this.getSectionKey() + "." + key;
	}

	protected @NotNull String[] getFinalArrayKey(final @NotNull String... key) {
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