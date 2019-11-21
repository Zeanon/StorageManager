package de.zeanon.storage.internal.utility.builder;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.base.settings.Reload;
import de.zeanon.storage.internal.files.config.YamlConfig;
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
public final class YamlConfigBuilder extends StorageManager<YamlConfigBuilder, YamlConfig> {

	@NotNull
	private ReloadSetting reloadSetting = Reload.INTELLIGENT;
	@NotNull
	private CommentSetting commentSetting = Comment.PRESERVE;


	public YamlConfigBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	@Contract("-> new")
	public final YamlConfig create() {
		return new LocalYamlConfig(super.file, this.inputStream, this.reloadSetting, this.commentSetting);
	}


	private static final class LocalYamlConfig extends YamlConfig {

		private LocalYamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
			super(file, inputStream, reloadSetting, commentSetting);
		}
	}
}
