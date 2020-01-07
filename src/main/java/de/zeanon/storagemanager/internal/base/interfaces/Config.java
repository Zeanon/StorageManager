package de.zeanon.storagemanager.internal.base.interfaces;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Base Interface for Config type DataFiles
 *
 * @author Zeanon
 * @version 1.1.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Config extends DataStorage {

	/**
	 * Define how comments should be handled
	 */
	void setCommentSetting(final @NotNull CommentSetting commentSetting);

	@Nullable List<String> getHeader();

	default void setHeader(final @Nullable List<String> header) {
		this.setHeader(header == null ? null : header.toArray(new String[0]));
	}

	void setHeader(final @Nullable String... header);

	@NotNull List<String> getFooter();

	default void setFooter(final @Nullable List<String> footer) {
		this.setFooter(footer == null ? null : footer.toArray(new String[0]));
	}

	void setFooter(final @Nullable String... footer);

	@NotNull List<String> getComments();

	default @Nullable List<String> getHeader(final @NotNull CommentSetting commentSetting) {
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