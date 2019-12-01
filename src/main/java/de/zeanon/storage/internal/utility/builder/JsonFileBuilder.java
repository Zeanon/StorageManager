package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.raw.JsonFile;
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
@SuppressWarnings("unused")
public class JsonFileBuilder extends StorageManager<JsonFileBuilder, JsonFile, Map, List> {


	public JsonFileBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull JsonFile create() {
		return new LocalJsonFile(super.file, this.inputStream, this.reloadSetting, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull JsonFileBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull JsonFileBuilder concurrentData(final boolean synchronize) {
		return this.mapType(synchronize ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalJsonFile extends JsonFile {

		private LocalJsonFile(final @NotNull File file,
							  final @Nullable InputStream inputStream,
							  final @NotNull ReloadSetting reloadSetting,
							  final @NotNull Class<? extends Map> map,
							  final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, map, list);
		}
	}
}