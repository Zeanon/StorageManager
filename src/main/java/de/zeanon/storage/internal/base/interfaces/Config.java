package de.zeanon.storage.internal.base.interfaces;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Base Interface for Config type DataFiles
 *
 * @author Zeanon
 * @version 1.1.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Config<M extends Map, L extends List> extends DataStorage<M, L> {


	void setCommentSetting(final CommentSetting commentSetting);

	@NotNull List<String> getHeader();

	default void setHeader(final @Nullable String... header) {
		this.setHeader(header == null ? null : Arrays.asList(header));
	}

	void setHeader(final @Nullable List<String> header);

	@NotNull List<String> getFooter();

	default void setFooter(final @Nullable String... footer) {
		this.setFooter(footer == null ? null : Arrays.asList(footer));
	}

	void setFooter(final @Nullable List<String> footer);

	@NotNull List<String> getComments();

	default @NotNull List<String> getHeader(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(commentSetting);
		return this.getHeader();
	}

	default void setHeader(final @NotNull CommentSetting commentSetting, final @Nullable String... header) {
		this.setCommentSetting(commentSetting);
		this.setHeader(header);
	}

	default void setHeader(final @NotNull CommentSetting commentSetting, final @Nullable List<String> header) {
		this.setCommentSetting(commentSetting);
		this.setHeader(header);
	}

	default @NotNull List<String> getFooter(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(commentSetting);
		return this.getFooter();
	}

	default void setFooter(final @NotNull CommentSetting commentSetting, final @Nullable String... footer) {
		this.setCommentSetting(commentSetting);
		this.setFooter(footer);
	}

	default void setFooter(final @NotNull CommentSetting commentSetting, final @Nullable List<String> footer) {
		this.setCommentSetting(commentSetting);
		this.setFooter(footer);
	}

	default @NotNull List<String> getComments(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(commentSetting);
		return this.getComments();
	}
}