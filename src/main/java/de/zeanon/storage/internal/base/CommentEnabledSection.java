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
public abstract class CommentEnabledSection<S> extends FlatSection {

	private final CommentEnabledFile commentEnabledFile;

	protected CommentEnabledSection(final @NotNull String sectionKey, final @NotNull CommentEnabledFile commentEnabledFile) {
		super(sectionKey, commentEnabledFile);
		this.commentEnabledFile = commentEnabledFile;
	}


	public S commentSetting(final @NotNull CommentSettingBase commentSetting) {
		this.commentEnabledFile.commentSetting(commentSetting);
		//noinspection unchecked
		return (S) this;
	}

	public S dataType(final @NotNull DataTypeBase dataType) {
		this.commentEnabledFile.dataType(dataType);
		//noinspection unchecked
		return (S) this;
	}


	/**
	 * Set with a specific {@link de.zeanon.storage.internal.base.interfaces.CommentSettingBase}.
	 *
	 * @see FlatSection#set(String, Object) .
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
	 * @see FlatSection#remove(String).
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