package de.zeanon.storage.internal.utility.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.basic.interfaces.CommentSetting;
import de.zeanon.storage.internal.basic.interfaces.ReloadSetting;
import de.zeanon.storage.internal.data.files.raw.YamlFile;
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
public final class YamlFileBuilder extends StorageManager<YamlFileBuilder, YamlFile> {

	@NotNull
	private ReloadSetting reloadSetting = Reload.INTELLIGENT;
	@NotNull
	private CommentSetting commentSetting = Comment.SKIP;


	public YamlFileBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	@Contract("-> new")
	public final YamlFile create() {
		return new LocalYamlFile(super.file, this.inputStream, this.reloadSetting, this.commentSetting);
	}


	private static final class LocalYamlFile extends YamlFile {

		private LocalYamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
			super(file, inputStream, reloadSetting, commentSetting);
		}
	}
}
