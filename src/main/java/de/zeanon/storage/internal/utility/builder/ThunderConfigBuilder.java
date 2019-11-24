package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.base.settings.Reload;
import de.zeanon.storage.internal.files.config.ThunderConfig;
import java.io.File;
import java.io.InputStream;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Setter
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public final class ThunderConfigBuilder extends StorageManager<ThunderConfigBuilder, ThunderConfig> {

	private @NotNull ReloadSetting reloadSetting = Reload.INTELLIGENT;
	private @NotNull CommentSetting commentSetting = Comment.PRESERVE;
	private boolean bigMap = false;


	public ThunderConfigBuilder(final @NotNull File file) {
		super(file);
	}


	@Override
	@Contract("-> new")
	public final @NotNull ThunderConfig create() {
		return new LocalThunderConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting, this.bigMap);
	}


	private static final class LocalThunderConfig extends ThunderConfig {

		private LocalThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final boolean bigMap) {
			super(file, inputStream, reloadSetting, commentSetting, bigMap);
		}
	}
}
