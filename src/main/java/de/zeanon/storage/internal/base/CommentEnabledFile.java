package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An extended FlatFile, to accommodate Languages supporting Comments
 *
 * @author Zeanon
 * @version 1.5.0
 * @see FlatFile
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class CommentEnabledFile<M extends FileData<?, ?>> extends FlatFile<M> {


	/**
	 * Default: {@link Comment#SKIP}
	 */
	@NotNull
	@Setter
	private CommentSettingBase commentSetting = Comment.SKIP;


	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileTypeBase fileType, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @NotNull M fileData) {
		super(file, fileType, reloadSetting, fileData);
		if (commentSetting != null) {
			this.setCommentSetting(commentSetting);
		}
	}

	/**
	 * Reload with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}
	 *
	 * @see FlatFile#reload()
	 */
	public void reload(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}


	/**
	 * Set with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void set(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(key, value);
	}

	public void setUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[] key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setUseArray(key, value);
	}

	public void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(dataMap);
	}

	public void setAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAllUseArray(dataMap);
	}

	public void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(key, dataMap);
	}

	public void setAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAllUseArray(blockKey, dataMap);
	}


	/**
	 * Remove with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void remove(final @NotNull CommentSettingBase commentSetting, final @NotNull String key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(key);
	}

	public void removeUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[] key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeUseArray(key);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull Collection<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[]... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(keys);
	}

	public void removeAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull Collection<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public void removeAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(blockKey, keys);
	}

	public void removeAllUseArray(final @NotNull CommentSettingBase commentSetting, final @NotNull String[] key, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(key, keys);
	}
}