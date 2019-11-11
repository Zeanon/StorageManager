package de.zeanon.storage.internal.base;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class CommentEnabledSection<C extends CommentEnabledSection, F extends CommentEnabledFile> extends FlatSection<C, F> {

	private final CommentEnabledFile<F> commentEnabledFile;

	protected CommentEnabledSection(final @NotNull String sectionKey, final @NotNull CommentEnabledFile<F> commentEnabledFile) {
		super(sectionKey, commentEnabledFile);
		this.commentEnabledFile = commentEnabledFile;
	}


	public C setCommentSetting(final @NotNull CommentSettingBase commentSetting) {
		this.commentEnabledFile.setCommentSetting(commentSetting);
		//noinspection unchecked
		return (C) this;
	}

	public C setDataType(final @NotNull DataTypeBase dataType) {
		this.commentEnabledFile.setDataType(dataType);
		//noinspection unchecked
		return (C) this;
	}


	/**
	 * Set with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatSection#set(String, Object) .
	 */
	public synchronized C set(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @Nullable Object value) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(key, value);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(dataMap);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C setAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setAll(key, dataMap);
		//noinspection unchecked
		return (C) this;
	}


	/**
	 * Remove with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatSection#remove(String).
	 */
	public synchronized C remove(final @NotNull CommentSettingBase commentSetting, final @NotNull String key) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(key);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull List<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(keys);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull String... keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
		//noinspection unchecked
		return (C) this;
	}

	public synchronized C removeAll(final @NotNull CommentSettingBase commentSetting, final @NotNull String key, final @NotNull List<String> keys) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.removeAll(key, keys);
		//noinspection unchecked
		return (C) this;
	}
}