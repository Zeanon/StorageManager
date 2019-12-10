package de.zeanon.storage.internal.files.config;

import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.Config;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.raw.TomlFile;
import de.zeanon.storage.internal.files.section.TomlConfigSection;
import de.zeanon.storage.internal.utility.datafiles.TomlUtils;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"WeakerAccess", "unused"})
public class TomlConfig extends TomlFile implements Config {


	private CommentSetting commentSetting;


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected TomlConfig(final @NotNull File file,
						 final @Nullable InputStream inputStream,
						 final @NotNull ReloadSetting reloadSetting,
						 final @NotNull CommentSetting commentSetting,
						 final boolean synchronizedData,
						 final @NotNull Class<? extends Map> map,
						 final @NotNull Class<? extends List> list) {
		super(file, inputStream, reloadSetting, synchronizedData, map, list);
		this.commentSetting = commentSetting;
	}


	@Override
	public @NotNull List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeader(this.fileData());
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setHeader(this.fileData(), header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @Nullable List<String> getHeader(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeader(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @Nullable List<String> getHeaderUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeaderUseArray(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public void setHeader(final @NotNull String key,
						  final @Nullable List<String> header) {
		this.setHeader(key, header == null ? null : header.toArray(new String[0]));
	}

	public void setHeader(final @NotNull String key,
						  final @Nullable String... header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setHeader(this.fileData(), key, header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public void setHeaderUseArray(final @NotNull String[] key,
								  final @Nullable List<String> header) {
		this.setHeaderUseArray(key, header == null ? null : header.toArray(new String[0]));
	}

	public void setHeaderUseArray(final @NotNull String[] key,
								  final @Nullable String... header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setHeaderUseArray(this.fileData(), key, header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}


	@Override
	public @NotNull List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooter(this.fileData());
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setFooter(this.fileData(), footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @NotNull List<String> getFooter(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooter(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getFooterUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooterUseArray(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public void setFooter(final @NotNull String key,
						  final @Nullable List<String> footer) {
		this.setFooter(key, footer == null ? null : footer.toArray(new String[0]));
	}

	public void setFooter(final @NotNull String key,
						  final @Nullable String... footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setFooter(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public void setFooterUseArray(final @NotNull String[] key,
								  final @Nullable List<String> footer) {
		this.setFooterUseArray(key, footer == null ? null : footer.toArray(new String[0]));
	}

	public void setFooterUseArray(final @NotNull String[] key,
								  final @Nullable String... footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.fileData().toString();
			TomlUtils.setFooterUseArray(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	@Override
	public @NotNull List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getCommentsUseArray(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), false);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getBlockComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @NotNull List<String> getBlockCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getCommentsUseArray(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull TomlConfigSection getSection(final @NotNull String sectionKey) {
		return new TomlConfig.LocalSection(sectionKey, this);
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull TomlConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlConfig.LocalSection(sectionKey, this);
	}


	private static class LocalSection extends TomlConfigSection {

		private LocalSection(final @NotNull String sectionKey,
							 final @NotNull TomlConfig tomlConfig) {
			super(sectionKey, tomlConfig);
		}

		private LocalSection(final @NotNull String[] sectionKey,
							 final @NotNull TomlConfig tomlConfig) {
			super(sectionKey, tomlConfig);
		}
	}
}
