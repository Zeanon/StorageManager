package de.zeanon.storage.internal.base.files;

import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.base.interfaces.FileType;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.utils.basic.Objects;
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
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class CommentEnabledFile<M extends FileData<?, ?>> extends FlatFile<M> {


	/**
	 * Default: {@link Comment#SKIP}
	 */
	@NotNull
	private CommentSetting commentSetting;


	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileType fileType, final @NotNull M fileData, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
		super(file, fileType, fileData, reloadSetting);
		this.commentSetting = commentSetting;
	}

	/**
	 * Reload with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#reload()
	 */
	public void reload(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}


	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void set(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(key, value);
	}

	public void setUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setUseArray(key, value);
	}

	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(dataMap);
	}

	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAllUseArray(dataMap);
	}

	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(key, dataMap);
	}

	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] blockKey, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAllUseArray(blockKey, dataMap);
	}


	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void remove(final @NotNull CommentSetting commentSetting, final @NotNull String key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(key);
	}

	public void removeUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeUseArray(key);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull Collection<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[]... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(keys);
	}

	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Collection<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] blockKey, final @NotNull String[]... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(blockKey, keys);
	}

	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAllUseArray(key, keys);
	}
}