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
import de.zeanon.storage.internal.files.raw.ThunderFile;
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
public final class ThunderFileBuilder extends StorageManager<ThunderFileBuilder, ThunderFile, TripletMap, List> {


	@Setter
	private @NotNull CommentSetting commentSetting = Comment.SKIP;
	@Setter(onMethod_ = {@Override})
	private @NotNull Class<? extends TripletMap> map = GapTripletMap.class;
	@Setter(onMethod_ = {@Override})
	private @NotNull Class<? extends List> list = GapList.class;


	public ThunderFileBuilder(final @NotNull File file) {
		super(file);
	}

	@Override
	@Contract("-> new")
	public final @NotNull ThunderFile create() {
		return new LocalThunderFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.map, this.list);
	}

	public void bigMap(final boolean bigMap) {
		this.map = bigMap ? BigTripletMap.class : GapTripletMap.class;
	}

	@Override
	public void bigList(final boolean bigList) {
		this.list = bigList ? BigList.class : GapList.class;
	}


	private static final class LocalThunderFile extends ThunderFile {

		private LocalThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final @NotNull Class<? extends TripletMap> map, final @NotNull Class<? extends List> list) {
			super(file, inputStream, reloadSetting, commentSetting, map, list);
		}
	}
}