package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ConfigBase<C extends ConfigBase> extends StorageBase<C> {

	C setCommentSetting(final @NotNull CommentSettingBase commentSetting);

	C setDataType(final @NotNull DataTypeBase dataType);

	List<String> getHeader();

	default C setHeader(final @Nullable String... header) {
		this.setHeader(header == null ? null : Arrays.asList(header));
		//noinspection unchecked
		return (C) this;
	}

	C setHeader(final @Nullable List<String> header);

	List<String> getFooter();

	default C setFooter(final @Nullable String... footer) {
		this.setFooter(footer == null ? null : Arrays.asList(footer));
		//noinspection unchecked
		return (C) this;
	}

	C setFooter(final @Nullable List<String> footer);

	List<String> getComments();

	default List<String> getHeader(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getHeader();
	}

	default C setHeader(final @NotNull CommentSettingBase commentSetting, final @Nullable String... header) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setHeader(header);
		//noinspection unchecked
		return (C) this;
	}

	default C setHeader(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> header) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setHeader(header);
		//noinspection unchecked
		return (C) this;
	}

	default List<String> getFooter(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getFooter();
	}

	default C setFooter(final @NotNull CommentSettingBase commentSetting, final @Nullable String... footer) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setFooter(footer);
		//noinspection unchecked
		return (C) this;
	}

	default C setFooter(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> footer) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setFooter(footer);
		//noinspection unchecked
		return (C) this;
	}

	default List<String> getComments(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getComments();
	}
}