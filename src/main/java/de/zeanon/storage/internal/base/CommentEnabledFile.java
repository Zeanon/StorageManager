package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.*;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.settings.DataType;
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
 * @author Zeanon
 * An exteded FlatFile, to accomodate Config files
 * @see FlatFile
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class CommentEnabledFile<@NotNull K> extends FlatFile<K> {


	/**
	 * Default: {@link Comment#SKIP}
	 */
	@NotNull
	@Setter
	private CommentSettingBase commentSetting = Comment.SKIP;
	/**
	 * Default: {@link DataType#AUTOMATIC}
	 */
	@NotNull
	@Setter
	private DataTypeBase dataType = DataType.AUTOMATIC;


	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileTypeBase fileType, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType, final @NotNull FileData<String, K, Object> fileData) {
		super(file, fileType, reloadSetting, fileData);
		if (commentSetting != null) {
			this.setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			this.setDataType(dataType);
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