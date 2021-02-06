package de.zeanon.storagemanagercore;

import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanagercore.internal.base.settings.Reload;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Builder for StorageManager DataFile Classes
 *
 * @author Zeanon
 * @version 2.0.0
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess", "rawtypes"})
public abstract class StorageManager<B extends StorageManager, F extends FlatFile, M extends Map, L extends List> {

	protected final @NotNull File file;
	protected @Nullable BufferedInputStream inputStream;
	protected @NotNull ReloadSetting reloadSetting = Reload.INTELLIGENT;
	protected @NotNull Class<? extends M> mapType;
	protected @NotNull Class<? extends L> listType;
	protected boolean synchronizeData = false;

	@Contract(pure = true)
	protected StorageManager(final @NotNull File file, final @NotNull Class<? extends M> mapType, final @NotNull Class<? extends L> listType) {
		this.file = file;
		this.mapType = mapType;
		this.listType = listType;
	}


	@Contract("_ -> this")
	public final @NotNull B fromInputStream(final @NotNull InputStream inputStream) {
		this.inputStream = BaseFileUtils.createNewInputStream(inputStream);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull File file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull Path file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromFile(final @NotNull String file) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(file);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull String directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull File directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_, _ -> this")
	public final @NotNull B fromFile(final @NotNull Path directory, final @NotNull String name) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromFile(directory, name);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B fromResource(final @NotNull String resource) {
		this.inputStream = BaseFileUtils.createNewInputStreamFromResource(resource);
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B reloadSetting(final @NotNull ReloadSetting reloadSetting) {
		this.reloadSetting = reloadSetting;
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B mapType(final @NotNull Class<? extends M> mapType) {
		this.mapType = mapType;
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B listType(final @NotNull Class<? extends L> listType) {
		this.listType = listType;
		//noinspection unchecked
		return (B) this;
	}

	@Contract("_ -> this")
	public final @NotNull B synchronizeData(final boolean synchronizeData) {
		this.synchronizeData = synchronizeData;
		//noinspection unchecked
		return (B) this;
	}

	public abstract @NotNull B bigList(final boolean bigList);

	public abstract @NotNull B concurrentData(final boolean concurrentData);


	/**
	 * Create the defined File
	 *
	 * @return the Instance of the FlatFile to be created
	 */
	@Contract("-> new")
	public abstract @NotNull F create();
}