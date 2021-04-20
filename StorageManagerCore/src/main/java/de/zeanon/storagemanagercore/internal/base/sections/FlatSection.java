package de.zeanon.storagemanagercore.internal.base.sections;

import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataStorage;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanagercore.internal.utility.basic.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
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
@Accessors(fluent = true)
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public abstract class FlatSection<F extends FlatFile<? extends FileData<M, ?, L>, M, L>, M extends Map, L extends List> implements DataStorage, Comparable<FlatSection> { //NOSONAR

	@Getter(AccessLevel.PROTECTED)
	private final @NotNull F flatFile;
	@Setter
	protected @NotNull String sectionKey;
	@Setter
	protected @NotNull String[] arraySectionKey;
	protected @NotNull FileData<M, ?, L> fileData;


	@Contract(pure = true)
	protected FlatSection(final @NotNull String sectionKey, final @NotNull F flatFile, final @NotNull FileData<M, ?, L> fileData) {
		this.sectionKey = sectionKey;
		this.arraySectionKey = sectionKey.split("\\.");
		this.flatFile = flatFile;
		this.fileData = this.getFileData(sectionKey, fileData);
	}

	@Contract(pure = true)
	protected FlatSection(final @NotNull String[] sectionKey, final @NotNull F flatFile, final @NotNull FileData<M, ?, L> fileData) {
		this.sectionKey = String.join(".", sectionKey);
		this.arraySectionKey = sectionKey;
		this.flatFile = flatFile;
		this.fileData = this.getSectionFileDataUseArray(sectionKey, fileData);
	}


	/**
	 * Checks whether the FileData contains the given key
	 *
	 * @param key key to check
	 *
	 * @return true if key exists, otherwise false
	 */
	@Override
	public boolean hasKey(final @NotNull String key) {
		this.update();
		return this.fileData().containsKey(key);
	}

	/**
	 * Checks whether the FileData contains the given key
	 *
	 * @param key key to check
	 *
	 * @return true if key exists, otherwise false
	 */
	@Override
	public boolean hasKeyUseArray(final @NotNull String... key) {
		this.update();
		return this.fileData().containsKeyUseArray(key);
	}

	@Override
	public @Nullable Object get(final @NotNull String key) {
		this.update();
		return this.fileData().get(key);
	}

	@Override
	public @Nullable Object getUseArray(final @NotNull String... key) {
		this.update();
		return this.fileData().getUseArray(key);
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.flatFile().collectionsProvider().newMap();
		for (final @NotNull String key : keys) {
			tempMap.put(key, this.fileData().get(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.flatFile().collectionsProvider().newMap();
		for (final @NotNull String[] key : keys) {
			tempMap.put(key, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.flatFile().collectionsProvider().newMap();
		for (final @NotNull String key : keys) {
			tempMap.put(key, this.fileData().get(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param keys the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.flatFile().collectionsProvider().newMap();
		for (final @NotNull String[] key : keys) {
			tempMap.put(key, this.fileData().getUseArray(key));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey,
											   final @NotNull String... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.flatFile().collectionsProvider().newMap();
		//noinspection unchecked
		final @NotNull FlatSection<F, M, L> currentSection = this.getSection(blockKey);
		for (final @NotNull String tempKey : keys) {
			tempMap.put(blockKey, currentSection.get(tempKey));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
														 final @NotNull Collection<String[]> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.flatFile().collectionsProvider().newMap();
		//noinspection unchecked
		final @NotNull FlatSection<F, M, L> currentSection = this.getSectionUseArray(blockKey);
		for (final @NotNull String[] tempKey : keys) {
			tempMap.put(blockKey, currentSection.getUseArray(tempKey));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String[], Object> getAllUseArray(final @NotNull String[] blockKey,
														 final @NotNull String[]... keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String[], Object> tempMap = this.flatFile().collectionsProvider().newMap();
		//noinspection unchecked
		final @NotNull FlatSection<F, M, L> currentSection = this.getSectionUseArray(blockKey);
		for (final @NotNull String[] tempKey : keys) {
			tempMap.put(blockKey, currentSection.getUseArray(tempKey));
		}
		return tempMap;
	}

	/**
	 * Get all values mapped to the given keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to get from
	 *
	 * @return the keys and their corresponding values as a Map
	 */
	@Override
	public @NotNull Map<String, Object> getAll(final @NotNull String blockKey,
											   final @NotNull Collection<String> keys) {
		this.update();

		//noinspection unchecked
		final @NotNull Map<String, Object> tempMap = this.flatFile().collectionsProvider().newMap();
		//noinspection unchecked
		final @NotNull FlatSection<F, M, L> currentSection = this.getSection(blockKey);
		for (final @NotNull String tempKey : keys) {
			tempMap.put(blockKey, currentSection.get(tempKey));
		}
		return tempMap;
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void set(final @NotNull String key,
					final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setWithoutCheck(final @NotNull String key,
								final @Nullable Object value) {
		this.insertWithoutCheck(key, value);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setUseArray(final @NotNull String[] key,
							final @Nullable Object value) {
		if (this.insertUseArray(key, value)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign the given value to the given key
	 *
	 * @param key   the key your value should be associated with
	 * @param value the value you want to set in your File
	 */
	@Override
	public void setUseArrayWithoutCheck(final @NotNull String[] key,
										final @Nullable Object value) {
		this.insertUseArrayWithoutCheck(key, value);
		this.lastLoaded(System.currentTimeMillis());
	}


	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull Pair<String, Object>... dataPairs) {
		if (this.insertAll(dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull Pair<String[], Object>... dataPairs) {
		if (this.insertAllUseArray(dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull String blockKey,
					   final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(blockKey, dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull String[] blockKey,
							   final @NotNull Map<String[], Object> dataMap) {
		if (this.insertAllUseArray(blockKey, dataMap)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAll(final @NotNull String blockKey,
					   final @NotNull Pair<String, Object>... dataPairs) {
		if (this.insertAll(blockKey, dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArray(final @NotNull String[] blockKey,
							   final @NotNull Pair<String[], Object>... dataPairs) {
		if (this.insertAllUseArray(blockKey, dataPairs)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}


	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull Map<String, Object> dataMap) {
		this.insertAllWithoutCheck(dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataMap the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull Map<String[], Object> dataMap) {
		this.insertAllUseArrayWithoutCheck(dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull Pair<String, Object>... dataPairs) {
		this.insertAllWithoutCheck(dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull Pair<String[], Object>... dataPairs) {
		this.insertAllUseArrayWithoutCheck(dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull String blockKey,
								   final @NotNull Map<String, Object> dataMap) {
		this.insertAllWithoutCheck(blockKey, dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param dataMap  the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
										   final @NotNull Map<String[], Object> dataMap) {
		this.insertAllUseArrayWithoutCheck(blockKey, dataMap);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllWithoutCheck(final @NotNull String blockKey,
								   final @NotNull Pair<String, Object>... dataPairs) {
		this.insertAllWithoutCheck(blockKey, dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Assign all given values to their corresponding keys
	 *
	 * @param blockKey  key to the subBlock to be looked at
	 * @param dataPairs the pairs to be set
	 */
	@Override
	public void setAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
										   final @NotNull Pair<String[], Object>... dataPairs) {
		this.insertAllUseArrayWithoutCheck(blockKey, dataPairs);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void remove(final @NotNull String key) {
		if (this.internalRemove(key)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeUseArray(final @NotNull String... key) {
		if (this.internalRemoveUseArray(key)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeWithoutCheck(final @NotNull String key) {
		this.internalRemoveWithoutCheck(key);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove a key and the corresponding value from the internal cache
	 *
	 * @param key the key to remove
	 */
	@Override
	public void removeUseArrayWithoutCheck(final @NotNull String... key) {
		this.internalRemoveUseArrayWithoutCheck(key);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String... keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String blockKey,
						  final @NotNull String... keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAll(final @NotNull String blockKey,
						  final @NotNull Collection<String> keys) {
		if (this.internalRemoveAll(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey,
								  final @NotNull String[]... keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArray(final @NotNull String[] blockKey,
								  final @NotNull Collection<String[]> keys) {
		if (this.internalRemoveAllUseArray(blockKey, keys)) {
			this.save();
		}
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String... keys) {
		this.internalRemoveAllWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull Collection<String> keys) {
		this.internalRemoveAllWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[]... keys) {
		this.internalRemoveAllUseArrayWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param keys the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull Collection<String[]> keys) {
		this.internalRemoveAllUseArrayWithoutCheck(keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String blockKey,
									  final @NotNull String... keys) {
		this.internalRemoveAllWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllWithoutCheck(final @NotNull String blockKey,
									  final @NotNull Collection<String> keys) {
		this.internalRemoveAllWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
											  final @NotNull String[]... keys) {
		this.internalRemoveAllUseArrayWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	/**
	 * Remove all keys and their corresponding values
	 *
	 * @param blockKey key to the subBlock to be looked at
	 * @param keys     the keys to remove
	 */
	@Override
	public void removeAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
											  final @NotNull Collection<String[]> keys) {
		this.internalRemoveAllUseArrayWithoutCheck(blockKey, keys);
		this.lastLoaded(System.currentTimeMillis());
	}

	public void update() {
		this.flatFile().update();
	}

	public void reloadSetting(final @NotNull ReloadSetting reloadSetting) {
		this.flatFile.reloadSetting(reloadSetting);
	}

	public void save() {
		this.flatFile().save();
	}


	protected abstract FileData<M, ?, L> getFileData(final @NotNull String key, final @NotNull FileData<M, ?, L> fileData);

	protected abstract FileData<M, ?, L> getSectionFileDataUseArray(final @NotNull String[] key, final @NotNull FileData<M, ?, L> fileData);

	protected void lastLoaded(final long lastLoaded) {
		this.flatFile().lastLoaded(lastLoaded);
	}


	private boolean insert(final @NotNull String key,
						   final @Nullable Object value) {
		this.update();

		final @Nullable Object currentValue = this.fileData().get(key);
		if ((currentValue == null && value != null) || (currentValue != null && !currentValue.equals(value))) {
			this.fileData().insert(key, value);
			return true;
		} else {
			return false;
		}
	}

	private void insertWithoutCheck(final @NotNull String key,
									final @Nullable Object value) {
		this.update();
		this.fileData().insert(key, value);
	}

	private boolean insertUseArray(final @NotNull String[] key,
								   final @Nullable Object value) {
		this.update();

		final @Nullable Object currentValue = this.fileData().getUseArray(key);
		if ((currentValue == null && value != null) || (currentValue != null && !currentValue.equals(value))) {
			this.fileData().insertUseArray(key, value);
			return true;
		} else {
			return false;
		}
	}

	private void insertUseArrayWithoutCheck(final @NotNull String[] key,
											final @Nullable Object value) {
		this.update();
		this.fileData().insertUseArray(key, value);
	}

	private boolean insertAll(final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull Pair<String, Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String[], Object> entry : pairs) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull String key,
							  final @NotNull Map<String, Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull String[] key,
									  final @NotNull Map<String[], Object> map) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAll(final @NotNull String key,
							  final @NotNull Pair<String, Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean insertAllUseArray(final @NotNull String[] key,
									  final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull Pair<String[], Object> entry : pairs) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
		return !this.fileData().toString().equals(tempData);
	}

	private void insertAllWithoutCheck(final @NotNull Map<String, Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull Map<String[], Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull Pair<String, Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String[], Object> entry : pairs) {
			this.fileData().insertUseArray(entry.getKey(), entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull String key,
									   final @NotNull Map<String, Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String, Object> entry : map.entrySet()) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull String[] key,
											   final @NotNull Map<String[], Object> map) {
		this.update();

		for (final @NotNull Map.Entry<String[], Object> entry : map.entrySet()) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
	}

	private void insertAllWithoutCheck(final @NotNull String key,
									   final @NotNull Pair<String, Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String, Object> entry : pairs) {
			this.fileData().insert(key + "." + entry.getKey(), entry.getValue());
		}
	}

	private void insertAllUseArrayWithoutCheck(final @NotNull String[] key,
											   final @NotNull Pair<String[], Object>... pairs) {
		this.update();

		for (final @NotNull Pair<String[], Object> entry : pairs) {
			final @NotNull String[] tempKey = new String[key.length + entry.getKey().length];
			System.arraycopy(key, 0, tempKey, 0, key.length);
			System.arraycopy(entry.getKey(), 0, tempKey, key.length, entry.getKey().length);
			this.fileData().insertUseArray(tempKey, entry.getValue());
		}
	}

	private boolean internalRemove(final @NotNull String key) {
		this.update();

		if (this.fileData().containsKey(key)) {
			this.fileData().remove(key);
			return true;
		} else {
			return false;
		}
	}

	private boolean internalRemoveUseArray(final @NotNull String... key) {
		this.update();

		if (this.fileData().containsKeyUseArray(key)) {
			this.fileData().removeUseArray(key);
			return true;
		} else {
			return false;
		}
	}

	private void internalRemoveWithoutCheck(final @NotNull String key) {
		this.update();

		this.fileData().remove(key);
	}

	private void internalRemoveUseArrayWithoutCheck(final @NotNull String... key) {
		this.update();

		this.fileData().removeUseArray(key);
	}

	private boolean internalRemoveAll(final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey,
									  final @NotNull String... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAll(final @NotNull String blockKey,
									  final @NotNull Collection<String> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey,
											  final @NotNull String[]... keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private boolean internalRemoveAllUseArray(final @NotNull String[] blockKey,
											  final @NotNull Collection<String[]> keys) {
		this.update();

		final @NotNull String tempData = this.fileData().toString();
		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
		return !this.fileData().toString().equals(tempData);
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String... keys) {
		this.update();

		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull Collection<String> keys) {
		this.update();

		for (final @NotNull String tempKey : keys) {
			this.fileData().remove(tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[]... keys) {
		this.update();

		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull Collection<String[]> keys) {
		this.update();

		for (final String[] tempKey : keys) {
			this.fileData().removeUseArray(tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String blockKey,
											   final @NotNull String... keys) {
		this.update();

		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
	}

	private void internalRemoveAllWithoutCheck(final @NotNull String blockKey,
											   final @NotNull Collection<String> keys) {
		this.update();

		for (final String tempKey : keys) {
			this.fileData().remove(blockKey + "." + tempKey);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
													   final @NotNull String[]... keys) {
		this.update();

		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
	}

	private void internalRemoveAllUseArrayWithoutCheck(final @NotNull String[] blockKey,
													   final @NotNull Collection<String[]> keys) {
		this.update();

		for (final @NotNull String[] tempKey : keys) {
			final @NotNull String[] key = new String[blockKey.length + tempKey.length];
			System.arraycopy(blockKey, 0, key, 0, blockKey.length);
			System.arraycopy(tempKey, 0, key, blockKey.length, tempKey.length);
			this.fileData().removeUseArray(key);
		}
	}

	@Override
	public int compareTo(final @NotNull FlatSection flatSection) {
		return this.flatFile.compareTo(flatSection.flatFile);
	}
}