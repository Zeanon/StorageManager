package de.zeanon.storage.internal.files.config;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.Config;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.storage.internal.files.section.ThunderConfigSection;
import de.zeanon.storage.internal.utility.datafiles.ThunderUtils;
import de.zeanon.storage.internal.utility.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended ThunderFile with added methods for Config purposes
 *
 * @author Zeanon
 * @version 2.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ThunderConfig extends ThunderFile implements Config<TripletMap, List> {


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderConfig(final @NotNull File file,
							final @Nullable InputStream inputStream,
							final @NotNull ReloadSetting reloadSetting,
							final @NotNull CommentSetting commentSetting,
							final int bufferSize,
							final boolean bigMap,
							final boolean synchronizedData,
							final @NotNull Class<? extends TripletMap> map,
							final @NotNull Class<? extends List> list) {
		super(file, inputStream, reloadSetting, commentSetting, bufferSize, bigMap, synchronizedData, map, list);
	}


	@NotNull
	@Override
	public List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData());
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
			ThunderUtils.setHeader(this.fileData(), header);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
			}
		}
	}

	public @Nullable List<String> getHeader(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	public @Nullable List<String> getHeaderUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeaderUseArray(this.fileData(), key);
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
			@NotNull String tempData = this.fileData().toString();
			ThunderUtils.setHeader(this.fileData(), key, header);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
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
			ThunderUtils.setHeaderUseArray(this.fileData(), key, header);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
			}
		}
	}


	@NotNull
	@Override
	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.fileData());
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
			ThunderUtils.setFooter(this.fileData(), footer);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
			}
		}
	}

	@NotNull
	public List<String> getFooter(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getFooterUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooterUseArray(this.fileData(), key);
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
			ThunderUtils.setFooter(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
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
			ThunderUtils.setFooterUseArray(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.file(), this.fileData(), this.getCommentSetting());
			}
		}
	}

	@NotNull
	@Override
	public List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), false);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getBlockComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.provider().newList();
		}
	}

	@NotNull
	public List<String> getBlockCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.fileData(), key, false);
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
	@NotNull
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	private static class LocalSection extends ThunderConfigSection {

		private LocalSection(final @NotNull String sectionKey,
							 final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}

		private LocalSection(final @NotNull String[] sectionKey,
							 final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}
	}
}