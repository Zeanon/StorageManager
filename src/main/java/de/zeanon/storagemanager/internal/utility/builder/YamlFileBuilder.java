package de.zeanon.storagemanager.internal.utility.builder;

import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.files.raw.YamlFile;
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
public class YamlFileBuilder extends StorageManager<YamlFileBuilder, YamlFile, Map, List> {


	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull CommentSetting commentSetting = Comment.SKIP;


	public YamlFileBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull YamlFile create() {
		return new LocalYamlFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.synchronizedData, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull YamlFileBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull YamlFileBuilder concurrentData(final boolean concurrentData) {
		return this.mapType(concurrentData ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalYamlFile extends YamlFile {

		private LocalYamlFile(final @NotNull File file,
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