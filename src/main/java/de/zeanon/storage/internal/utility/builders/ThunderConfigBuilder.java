package de.zeanon.storage.internal.utility.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.basic.interfaces.CommentSetting;
import de.zeanon.storage.internal.basic.interfaces.ReloadSetting;
import de.zeanon.storage.internal.data.files.config.ThunderConfig;
import de.zeanon.storage.internal.utility.setting.Comment;
import de.zeanon.storage.internal.utility.setting.Reload;
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

	@NotNull
	private ReloadSetting reloadSetting = Reload.INTELLIGENT;
	@NotNull
	private CommentSetting commentSetting = Comment.PRESERVE;


	public ThunderConfigBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	@Contract("-> new")
	public final ThunderConfig create() {
		return new LocalThunderConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting);
	}


	private static final class LocalThunderConfig extends ThunderConfig {

		private LocalThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
			super(file, inputStream, reloadSetting, commentSetting);
		}
	}
}
