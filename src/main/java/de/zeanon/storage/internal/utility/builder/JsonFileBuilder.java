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
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Accessors(fluent = true)
@ToString(callSuper = true)
@Setter(onMethod_ = {@Override})
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public final class JsonFileBuilder extends StorageManager<JsonFileBuilder, JsonFile, Map, List> {


	private @NotNull Class<? extends Map> map = HashMap.class;
	private @NotNull Class<? extends List> list = GapList.class;


	public JsonFileBuilder(final @NotNull File file) {
		super(file);
	}

	@Override
	@Contract("-> new")
	public final @NotNull JsonFile create() {
		return new LocalJsonFile(super.file, this.inputStream, this.reloadSetting, this.map, this.list);
	}

	@Override
	public void bigList(final boolean bigList) {
		this.list = bigList ? BigList.class : GapList.class;
	}

	private static final class LocalJsonFile extends JsonFile {

		private LocalJsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull Class<? extends Map> map, final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, map, list);
		}
	}
}