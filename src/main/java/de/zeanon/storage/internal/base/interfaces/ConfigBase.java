package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ConfigBase extends StorageBase {

	void setCommentSetting(final CommentSettingBase commentSetting);

	void setDataType(final DataTypeBase dataType);

	List<String> getHeader();

	default void setHeader(final @Nullable String... header) {
		this.setHeader(header == null ? null : Arrays.asList(header));
	}

	void setHeader(final @Nullable List<String> header);

	List<String> getFooter();

	default void setFooter(final @Nullable String... footer) {
		this.setFooter(footer == null ? null : Arrays.asList(footer));
	}

	void setFooter(final @Nullable List<String> footer);

	List<String> getComments();

	default List<String> getHeader(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getHeader();
	}

	default void setHeader(final @NotNull CommentSettingBase commentSetting, final @Nullable String... header) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setHeader(header);
	}

	default void setHeader(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> header) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setHeader(header);
	}

	default List<String> getFooter(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getFooter();
	}

	default void setFooter(final @NotNull CommentSettingBase commentSetting, final @Nullable String... footer) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setFooter(footer);
	}

	default void setFooter(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> footer) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.setFooter(footer);
	}

	default List<String> getComments(final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		return this.getComments();
	}
}