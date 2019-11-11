package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.settings.DataType;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author Zeanon
 * An exteded FlatFile, to accomodate Config files.
 * @see FlatFile
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuppressWarnings("unused")
public abstract class CommentEnabledFile extends FlatFile {


	/**
	 * Default: {@link Comment#SKIP}
	 */
	private CommentSettingBase commentSetting = Comment.SKIP;
	/**
	 * Default: {@link DataType#AUTOMATIC}
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

	/**
	 * Reload with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatFile#reload().
	 */
	public void reload(final @NotNull CommentSettingBase commentSetting) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}


	/**
	 * Set with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatFile#set(String, Object) .
	 */
	public synchronized void set(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(key, value);
	}

	public synchronized void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(dataMap);
	}

	public synchronized void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(key, dataMap);
	}


	/**
	 * Remove with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatFile#remove(String).
	 */
	public synchronized void remove(final @NotNull CommentSettingBase commentSetting, final @NotNull String key) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(key);
	}

	public synchronized void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String... keys) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public synchronized void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull List<String> keys) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public synchronized void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public synchronized void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull List<String> keys) {
		this.commentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}
}