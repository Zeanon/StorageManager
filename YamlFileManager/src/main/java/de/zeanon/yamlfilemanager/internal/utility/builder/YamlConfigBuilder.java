package de.zeanon.yamlfilemanager.internal.utility.builder;

import de.zeanon.storagemanagercore.external.browniescollections.BigList;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.yamlfilemanager.YamlFileManager;
import de.zeanon.yamlfilemanager.internal.files.config.YamlConfig;
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
@SuppressWarnings({"unused", "rawtypes"})
public class YamlConfigBuilder extends YamlFileManager<YamlConfigBuilder, YamlConfig, Map, List> { //NOSONAR


	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull CommentSetting commentSetting = Comment.PRESERVE;


	public YamlConfigBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull YamlConfig create() {
		return new LocalYamlConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.synchronizeData, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull YamlConfigBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull YamlConfigBuilder concurrentData(final boolean concurrentData) {
		return this.mapType(concurrentData ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalYamlConfig extends YamlConfig {

		private LocalYamlConfig(final @NotNull File file,
								final @Nullable InputStream inputStream,
								final @NotNull ReloadSetting reloadSetting,
								final @NotNull CommentSetting commentSetting,
								final boolean synchronizeData,
								final @NotNull Class<? extends Map> map,
								final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, synchronizeData, map, list);
		}
	}
}