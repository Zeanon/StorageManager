package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.cache.datamap.BigTripletMap;
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
		return new LocalThunderConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.bufferSize, this.mapType, this.listType);
	}

	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigMap(final boolean bigMap) {
		return this.mapType(bigMap ? BigTripletMap.class : GapTripletMap.class);
	}

	@Override
	@Contract("_ -> this")
	public final @NotNull ThunderConfigBuilder bigList(final boolean bigList) {
		return this.listType(bigList ? BigList.class : GapList.class);
	}


	private static final class LocalThunderConfig extends ThunderConfig {

		private LocalThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final int bufferSize, final @NotNull Class<? extends TripletMap> map, final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, bufferSize, map, list);
		}
	}
}