package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.FileData;
import de.zeanon.storage.internal.utils.basic.Objects;
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
public abstract class CommentEnabledSection<F extends CommentEnabledFile<? extends FileData<?, ?>>> extends FlatSection<F> {

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

	public void setCommentSetting(final @NotNull CommentSettingBase commentSetting) {
		this.commentEnabledFile.setCommentSetting(commentSetting);
	}

	/**
	 * Set with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatSection#set(String, Object)
	 */
	public void set(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(key, value);
	}

	public void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(dataMap);
	}

	public void setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(key, dataMap);
	}


	/**
	 * Remove with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}
	 *
	 * @see FlatSection#remove(String)
	 */
	public void remove(final @NotNull CommentSettingBase commentSetting, final @NotNull String key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(key);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull List<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}

	public void removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull List<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
	}
}