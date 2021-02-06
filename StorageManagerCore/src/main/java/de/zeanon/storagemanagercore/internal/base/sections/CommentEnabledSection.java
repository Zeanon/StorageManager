package de.zeanon.storagemanagercore.internal.base.sections;

import de.zeanon.storagemanagercore.internal.base.files.CommentEnabledFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An extended FlatSection, to accommodate Languages supporting Comments
 *
 * @author Zeanon
 * @version 1.5.0
 * @see FlatSection
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public abstract class CommentEnabledSection<F extends CommentEnabledFile<? extends FileData<M, ?, L>, M, L>, M extends Map, L extends List> extends FlatSection<F, M, L> {


	@NotNull private final F commentEnabledFile;


	protected CommentEnabledSection(final @NotNull String sectionKey, final @NotNull F commentEnabledFile) {
		super(sectionKey, commentEnabledFile);
		this.commentEnabledFile = commentEnabledFile;
	}

	protected CommentEnabledSection(final @NotNull String[] sectionKey, final @NotNull F commentEnabledFile) {
		super(sectionKey, commentEnabledFile);
		this.commentEnabledFile = commentEnabledFile;
	}


	public void setCommentSetting(final @NotNull CommentSetting commentSetting) {
		this.commentEnabledFile.setCommentSetting(commentSetting);
	}

	/**
	 * Set with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#set(String, Object)
	 */
	public void set(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.setCommentSetting(commentSetting);
		this.set(key, value);
	}

	/**
	 * Set with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setUseArray(String[], Object)
	 */
	public void setUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @Nullable Object value) {
		this.setCommentSetting(commentSetting);
		this.setUseArray(key, value);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAll(Map)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAllUseArray(Map)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAll(Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull Pair<String, Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataPairs);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAllUseArray(Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull Pair<String[], Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(dataPairs);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAll(String, Map)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAllUseArray(String[], Map)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(key, dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAll(String, Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Pair<String, Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataPairs);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}.
	 *
	 * @see FlatSection#setAllUseArray(String[], Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @NotNull Pair<String[], Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(key, dataPairs);
	}


	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#remove(String)
	 */
	public void remove(final @NotNull CommentSetting commentSetting, final @NotNull String key) {
		this.setCommentSetting(commentSetting);
		this.remove(key);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeUseArray(String...)
	 */
	public void removeUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String... key) {
		this.setCommentSetting(commentSetting);
		this.removeUseArray(key);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAll(String, String...)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAllUseArray(String[]...)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAll(Collection)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAllUseArray(Collection)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAll(String, String...)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAllUseArray(String[], String[]...)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(key, keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAll(String, Collection)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatSection#removeAllUseArray(String[], Collection)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting, final @NotNull String[] key, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(key, keys);
	}
}