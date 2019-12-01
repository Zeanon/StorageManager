package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.raw.YamlFile;
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
@SuppressWarnings("unused")
public class YamlFileBuilder extends StorageManager<YamlFileBuilder, YamlFile, Map, List> {


	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull CommentSetting commentSetting = Comment.SKIP;


	public YamlFileBuilder(final @NotNull File file) {
		super(file, HashMap.class, GapList.class);
	}

	@Override
	@Contract("-> new")
	public final @NotNull YamlFile create() {
		return new LocalYamlFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.mapType, this.listType);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull YamlFileBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull YamlFileBuilder concurrentData(final boolean synchronize) {
		return this.mapType(synchronize ? ConcurrentHashMap.class : HashMap.class);
	}


	private static final class LocalYamlFile extends YamlFile {

		private LocalYamlFile(final @NotNull File file,
							  final @Nullable InputStream inputStream,
							  final @NotNull ReloadSetting reloadSetting,
							  final @NotNull CommentSetting commentSetting,
							  final @NotNull Class<? extends Map> map,
							  final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, map, list);
		}
	}
}