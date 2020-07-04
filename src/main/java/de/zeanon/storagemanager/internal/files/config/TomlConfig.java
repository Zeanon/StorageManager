package de.zeanon.storagemanager.internal.files.config;

import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.files.FlatFile;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.Config;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.files.raw.TomlFile;
import de.zeanon.storagemanager.internal.files.section.TomlConfigSection;
import de.zeanon.storagemanager.internal.utility.datafiles.TomlUtils;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
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
@SuppressWarnings({"unused", "WeakerAccess", "rawtypes"})
public class TomlConfig extends TomlFile implements Config {


	private CommentSetting commentSetting;


	/**
	 * @param file            the File to be used as a backend
	 * @param inputStream     the FileContent to be set on the creation of the File
	 * @param reloadSetting   the ReloadSetting to be used with this instance
	 * @param commentSetting  the CommentSetting to be used with this instance
	 * @param synchronizeData if the saved data should be synchronized
	 * @param map             the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list            the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	@SuppressWarnings("rawtypes")
	protected TomlConfig(final @NotNull File file,
						 final @Nullable InputStream inputStream,
						 final @NotNull ReloadSetting reloadSetting,
						 final @NotNull CommentSetting commentSetting,
						 final boolean synchronizeData,
						 final @NotNull Class<? extends Map> map,
						 final @NotNull Class<? extends List> list) {
		super(file, inputStream, reloadSetting, synchronizeData, map, list);
		this.commentSetting = commentSetting;
	}


	@Override
	public @NotNull
	List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeader(this.fileData());
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
			TomlUtils.setHeader(this.fileData(), header);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @Nullable
	List<String> getHeader(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeader(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @Nullable
	List<String> getHeaderUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getHeaderUseArray(this.fileData(), key);
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
	public @NotNull
	List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooter(this.fileData());
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
			TomlUtils.setFooter(this.fileData(), footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	public @NotNull
	List<String> getFooter(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooter(this.fileData(), key);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getFooterUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getFooterUseArray(this.fileData(), key);
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
			final @NotNull String tempData = this.fileData().toString();
			TomlUtils.setFooterUseArray(this.fileData(), key, footer);
			if (!this.fileData().toString().equals(tempData)) {
				this.save();
			}
		}
	}

	@Override
	public @NotNull
	List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getCommentsUseArray(this.fileData(), key, true);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getBlockComments(final @NotNull String key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getComments(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	public @NotNull
	List<String> getBlockCommentsUseArray(final @NotNull String... key) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return TomlUtils.getCommentsUseArray(this.fileData(), key, false);
		} else {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		}
	}

	/**
	 * Reload with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#reload()
	 */
	public void reload(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(commentSetting);
		this.reload();
	}

	/**
	 * Save with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#save()
	 */
	public void save(final @NotNull CommentSetting commentSetting) {
		this.setCommentSetting(commentSetting);
		this.save();
	}


	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void set(final @NotNull CommentSetting commentSetting,
					final @NotNull String key,
					final @Nullable Object value) {
		this.setCommentSetting(commentSetting);
		this.set(key, value);
	}

	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void setUseArray(final @NotNull CommentSetting commentSetting,
							final @NotNull String[] key,
							final @Nullable Object value) {
		this.setCommentSetting(commentSetting);
		this.setUseArray(key, value);
	}

	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataMap);
	}

	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(dataMap);
	}

	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull String key,
					   final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataMap);
	}

	/**
	 * Set with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#set(String, Object)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull String[] blockKey,
							   final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(blockKey, dataMap);
	}


	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void remove(final @NotNull CommentSetting commentSetting,
					   final @NotNull String key) {
		this.setCommentSetting(commentSetting);
		this.remove(key);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull String[] key) {
		this.setCommentSetting(commentSetting);
		this.removeUseArray(key);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String key,
						  final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String key,
						  final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[] blockKey,
								  final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(blockKey, keys);
	}

	/**
	 * Remove with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#remove(String)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[] key, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(key, keys);
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull
	TomlConfigSection getSection(final @NotNull String sectionKey) {
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
	public @NotNull
	TomlConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
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