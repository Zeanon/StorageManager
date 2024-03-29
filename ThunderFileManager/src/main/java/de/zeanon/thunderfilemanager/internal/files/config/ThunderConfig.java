package de.zeanon.thunderfilemanager.internal.files.config;

import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanagercore.internal.base.interfaces.*;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.thunderfilemanager.internal.files.raw.ThunderFile;
import de.zeanon.thunderfilemanager.internal.files.section.ThunderConfigSection;
import de.zeanon.thunderfilemanager.internal.utility.datafiles.ThunderUtils;
import java.io.File;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
@SuppressWarnings({"unused", "WeakerAccess", "rawtypes"})
public class ThunderConfig extends ThunderFile implements Config {


	/**
	 * @param file            the File to be used as a backend
	 * @param inputStream     the FileContent to be set on the creation of the File
	 * @param reloadSetting   the ReloadSetting to be used with this instance
	 * @param commentSetting  the CommentSetting to be used with this instance
	 * @param bufferSize      the bufferSize to be used with the Reader and Writer
	 * @param autoFlush       autoFlush parameter of the PrintWriter
	 * @param concurrentData  if the saved data should be concurrent
	 * @param synchronizeData if the saved data should be synchronized
	 * @param map             the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list            the List implementation to be used, default ist GapList
	 *
	 * @throws UncheckedIOException if the File can not be accessed properly
	 * @throws FileParseException   if the Content of the File can not be parsed properly
	 */
	protected ThunderConfig(final @NotNull File file,
							final @Nullable InputStream inputStream,
							final @NotNull ReloadSetting reloadSetting,
							final @NotNull CommentSetting commentSetting,
							final @NotNull String indentation,
							final int bufferSize,
							final boolean autoFlush,
							final boolean bigMap,
							final boolean concurrentData,
							final boolean synchronizeData,
							final @NotNull Class<? extends DataMap> map,
							final @NotNull Class<? extends List> list) {
		super(file, inputStream, reloadSetting, commentSetting, indentation, bufferSize, autoFlush, bigMap, concurrentData, synchronizeData, map, list);
	}


	@Override
	public @NotNull List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData());
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			ThunderUtils.setHeader(this.fileData(), header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @Nullable List<String> getHeader(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @Nullable List<String> getHeaderUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeaderUseArray(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
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
			ThunderUtils.setHeader(this.fileData(), key, header);
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
			ThunderUtils.setHeaderUseArray(this.fileData(), key, header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}


	@Override
	public @NotNull List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.fileData());
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			final @NotNull String tempData = this.fileData().toString();
			ThunderUtils.setFooter(this.fileData(), footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @NotNull List<String> getFooter(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getFooterUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooterUseArray(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
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
			final @NotNull String tempData = this.fileData().toString();
			ThunderUtils.setFooterUseArray(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	@Override
	public @NotNull List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getBlockComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull List<String> getBlockCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
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
	public @NotNull ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this, this.fileData());
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this, this.fileData());
	}


	private static class LocalSection extends ThunderConfigSection {

		private LocalSection(final @NotNull String sectionKey,
							 final @NotNull ThunderConfig thunderConfig,
							 final @NotNull FileData<DataMap, ?, List> fileData) {
			super(sectionKey, thunderConfig, fileData);
		}

		private LocalSection(final @NotNull String[] sectionKey,
							 final @NotNull ThunderConfig thunderConfig,
							 final @NotNull FileData<DataMap, ?, List> fileData) {
			super(sectionKey, thunderConfig, fileData);
		}
	}
}