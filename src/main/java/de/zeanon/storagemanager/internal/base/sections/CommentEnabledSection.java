package de.zeanon.storagemanager.internal.base.sections;

import de.zeanon.storagemanager.internal.base.files.CommentEnabledFile;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.FileData;
import java.util.List;
import java.util.Map;
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
@SuppressWarnings("unused")
public abstract class CommentEnabledSection<F extends CommentEnabledFile<? extends FileData<M, ?, L>, M, L>, M extends Map, L extends List> extends FlatSection<F, M, L> {


	@NotNull
	private final F commentEnabledFile;


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

	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataMap);
	}

	public void setAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataMap);
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

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull List<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	public void removeAll(final @NotNull CommentSetting commentSetting, final @NotNull String key, final @NotNull List<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}
}