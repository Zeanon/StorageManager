package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.settings.DataType;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuppressWarnings("unused")
public abstract class CommentEnabledFile extends FlatFile {


	/**
	 * Default: SKIP
	 */
	private CommentSettingBase commentSetting = Comment.SKIP;
	/**
	 * Default: AUTOMATIC
	 */
	private DataTypeBase dataType = DataType.AUTOMATIC;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileTypeBase fileType, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, fileType, reloadSetting);
		if (commentSetting != null) {
			this.commentSetting(commentSetting);
		}
		if (dataType != null) {
			this.dataType(dataType);
		}
	}

	public void reload(final @NotNull Comment commentSetting) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull CommentSettingBase commentSetting) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(Objects.notNull(key, "Key must not be null"), Objects.notNull(value, "Value must not be null"));
	}

	public synchronized void remove(final @NotNull String key, final @NotNull CommentSettingBase commentSetting) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(Objects.notNull(key, "Key must not be null"));
	}
}