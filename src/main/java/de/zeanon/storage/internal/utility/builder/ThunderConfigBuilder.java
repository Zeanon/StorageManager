package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.BigTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.ConcurrentBigTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.ConcurrentGapTripletMap;
import de.zeanon.storage.internal.base.cache.datamap.GapTripletMap;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.config.ThunderConfig;
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
@SuppressWarnings("unused")
public class ThunderConfigBuilder extends StorageManager<ThunderConfigBuilder, ThunderConfig, TripletMap, List> {

	private boolean bigData;
	private boolean concurrentData;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private @NotNull CommentSetting commentSetting = Comment.PRESERVE;
	@Setter(onMethod_ = {@Contract("_ -> this")})
	private int bufferSize = 8192;

	public ThunderConfigBuilder(final @NotNull File file) {
		super(file, GapTripletMap.class, GapList.class);
	}

	@Override
	@Contract("-> new")
	public final @NotNull ThunderConfig create() {
		return new LocalThunderConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.bufferSize, this.bigData, this.concurrentData, this.mapType, this.listType);
	}

	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigData(final boolean bigData) {
		this.bigData = bigData;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigTripletMap.class : ConcurrentGapTripletMap.class)
												: (this.bigData ? BigTripletMap.class : GapTripletMap.class));
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}

	@Override
	@Contract("_ -> this")
	public @NotNull ThunderConfigBuilder concurrentData(final boolean synchronize) {
		this.concurrentData = synchronize;
		return this.mapType(this.concurrentData ? (this.bigData ? ConcurrentBigTripletMap.class : ConcurrentGapTripletMap.class)
												: (this.bigData ? BigTripletMap.class : GapTripletMap.class));
	}


	private static final class LocalThunderConfig extends ThunderConfig {

		private LocalThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final int bufferSize, final boolean bigData, final boolean concurrentData, final @NotNull Class<? extends TripletMap> map, final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, bufferSize, bigData, concurrentData, map, list);
		}
	}
}