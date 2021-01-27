package de.zeanon.storagemanager.internal.utility.builder;

import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.files.raw.TomlFile;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class TomlFileBuilder extends StorageManager<TomlFileBuilder, TomlFile, Map, List> { //NOSONAR


	public TomlFileBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull TomlFile create() {
		return new LocalTomlFile(super.file, this.inputStream, this.reloadSetting, this.synchronizeData, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull TomlFileBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull TomlFileBuilder concurrentData(final boolean concurrentData) {
		return this.mapType(concurrentData ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalTomlFile extends TomlFile {

		private LocalTomlFile(final @NotNull File file,
							  final @Nullable InputStream inputStream,
							  final @NotNull ReloadSetting reloadSetting,
							  final boolean synchronizeData,
							  final @NotNull Class<? extends Map> map,
							  final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, synchronizeData, map, list);
		}
	}
}