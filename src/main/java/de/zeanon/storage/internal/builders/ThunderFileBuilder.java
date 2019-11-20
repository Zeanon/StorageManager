package de.zeanon.storage.internal.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import java.io.File;
import java.io.InputStream;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Setter
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public final class ThunderFileBuilder extends StorageManager<ThunderFileBuilder, ThunderFile> {

	private ReloadSettingBase reloadSetting;
	private CommentSettingBase commentSetting;


	public ThunderFileBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	public final ThunderFile create() {
		return new LocalThunderFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting);
	}


	private static final class LocalThunderFile extends ThunderFile {

		private LocalThunderFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting) {
			super(file, inputStream, reloadSetting, commentSetting);
		}
	}
}
