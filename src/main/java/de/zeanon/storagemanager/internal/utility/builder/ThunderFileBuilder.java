package de.zeanon.storagemanager.internal.utility.builder;

import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.external.browniescollections.BigList;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.cache.datamap.BigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentBigDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storagemanager.internal.base.cache.datamap.GapDataMap;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.files.raw.ThunderFile;
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
public class ThunderFileBuilder extends StorageManager<ThunderFileBuilder, ThunderFile, DataMap, List> {


	private boolean bigData;
	private boolean concurrentData;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private boolean autoFlush;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull
	CommentSetting commentSetting = Comment.SKIP;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private int bufferSize = 8192;


	public ThunderFileBuilder(final @NotNull File file) {
		super(file, GapDataMap.class, GapList.class);
	}


	@Override
	@Contract("-> new")
	public final @NotNull
	ThunderFile create() {
		return new LocalThunderFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.bufferSize, this.autoFlush, this.bigData, this.concurrentData, this.synchronizedData, this.mapType, this.listType);
	}

	@Contract("_ -> this")
	public final @NotNull
	ThunderFileBuilder bigData(final boolean bigData) {
		this.bigData = bigData;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
												: (this.bigData ? BigDataMap.class : GapDataMap.class));
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull
	ThunderFileBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull
	ThunderFileBuilder concurrentData(final boolean concurrentData) {
		this.concurrentData = concurrentData;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigDataMap.class : ConcurrentGapDataMap.class)
												: (this.bigData ? BigDataMap.class : GapDataMap.class));
	}


	private static final class LocalThunderFile extends ThunderFile {

		private LocalThunderFile(final @NotNull File file,
								 final @Nullable InputStream inputStream,
								 final @NotNull ReloadSetting reloadSetting,
								 final @NotNull CommentSetting commentSetting,
								 final int bufferSize,
								 final boolean autoFlush,
								 final boolean bigData,
								 final boolean concurrentData,
								 final boolean synchronizedData,
								 final @NotNull Class<? extends DataMap> map,
								 final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, bufferSize, autoFlush, bigData, concurrentData, synchronizedData, map, list);
		}
	}
}