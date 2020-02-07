package de.zeanon.storagemanager.internal.utility.builder;


import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.files.config.TomlConfig;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Setter(onMethod_ = {@Contract("_ -> this")})
@SuppressWarnings({"unused", "rawtypes"})
public class TomlConfigBuilder extends StorageManager<TomlConfigBuilder, TomlConfig, Map, List> {


	private @NotNull
	CommentSetting commentSetting = Comment.PRESERVE;


	public TomlConfigBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull
	TomlConfig create() {
		return new TomlConfigBuilder.LocalTomlConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.synchronizedData, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull
	TomlConfigBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull
	TomlConfigBuilder concurrentData(final boolean concurrentData) {
		return this.mapType(concurrentData ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalTomlConfig extends TomlConfig {

		private LocalTomlConfig(final @NotNull File file,
								final @Nullable InputStream inputStream,
								final @NotNull ReloadSetting reloadSetting,
								final @NotNull CommentSetting commentSetting,
								final boolean synchronizedData,
								final @NotNull Class<? extends Map> map,
								final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, synchronizedData, map, list);
		}
	}
}