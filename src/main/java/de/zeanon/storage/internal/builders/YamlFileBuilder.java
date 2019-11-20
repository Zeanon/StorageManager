package de.zeanon.storage.internal.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.YamlFile;
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
public final class YamlFileBuilder extends StorageManager<YamlFileBuilder, YamlFile> {

	private ReloadSettingBase reloadSetting;
	private CommentSettingBase commentSetting;


	public YamlFileBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	public final YamlFile create() {
		return new LocalYamlFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting);
	}


	private static final class LocalYamlFile extends YamlFile {

		private LocalYamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting) {
			super(file, inputStream, reloadSetting, commentSetting);
		}
	}
}
