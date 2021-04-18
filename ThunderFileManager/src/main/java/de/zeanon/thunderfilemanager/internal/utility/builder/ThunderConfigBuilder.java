package de.zeanon.thunderfilemanager.internal.utility.builder;

import de.zeanon.storagemanagercore.external.browniescollections.BigList;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.cache.datamap.BigDataMap;
import de.zeanon.storagemanagercore.internal.base.cache.datamap.ConcurrentBigDataMap;
import de.zeanon.storagemanagercore.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storagemanagercore.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.thunderfilemanager.ThunderFileManager;
import de.zeanon.thunderfilemanager.internal.files.config.ThunderConfig;
import java.io.File;
import java.io.InputStream;
import java.util.List;
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
public class ThunderConfigBuilder extends ThunderFileManager<ThunderConfigBuilder, ThunderConfig, DataMap, List> { //NOSONAR


	private boolean bigData;
	private boolean concurrentData;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private boolean autoFlush;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull CommentSetting commentSetting = Comment.PRESERVE;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull String indentation = "  ";
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private int bufferSize = 8192;


	public ThunderConfigBuilder(final @NotNull File file) {
		super(file, GapDataMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull ThunderConfig create() {
		return new LocalThunderConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.indentation, this.bufferSize, this.autoFlush, this.bigData, this.concurrentData, this.synchronizeData, this.mapType, this.listType);
	}

	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigData(final boolean bigData) {
		this.bigData = bigData;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
												: (this.bigData ? BigDataMap.class : GapDataMap.class));
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull ThunderConfigBuilder concurrentData(final boolean concurrentData) {
		this.concurrentData = concurrentData;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
												: (this.bigData ? BigDataMap.class : GapDataMap.class));
	}


	private static final class LocalThunderConfig extends ThunderConfig {

		private LocalThunderConfig(final @NotNull File file,
								   final @Nullable InputStream inputStream,
								   final @NotNull ReloadSetting reloadSetting,
								   final @NotNull CommentSetting commentSetting,
								   final @NotNull String indentation,
								   final int bufferSize,
								   final boolean autoFlush,
								   final boolean bigData,
								   final boolean concurrentData,
								   final boolean synchronizeData,
								   final @NotNull Class<? extends DataMap> map,
								   final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, indentation, bufferSize, autoFlush, bigData, concurrentData, synchronizeData, map, list);
		}
	}
}