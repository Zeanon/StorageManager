package de.zeanon.storagemanager.internal.base.files;

import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.FileData;
import de.zeanon.storagemanager.internal.base.interfaces.FileType;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An extended FlatFile, to accommodate Languages supporting Comments
 *
 * @author Zeanon
 * @version 1.5.0
 * @see FlatFile
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class CommentEnabledFile<D extends FileData<M, ?, L>, M extends Map, L extends List> extends FlatFile<D, M, L> {


	/**
	 * Default: {@link Comment#SKIP}
	 * -- Getter --
	 *
	 * @return the assigned CommentSetting
	 * <p>
	 * -- Setter --
	 * Define how comments should be handled
	 */
	private @NotNull CommentSetting commentSetting;


	/**
	 * Initializes a new CommentEnabledFile
	 *
	 * @param file           the backing {@link File}
	 * @param fileType       the {@link FileType} of the backing {@link File}
	 * @param fileData       the {@link FileData} to cache the Data
	 * @param reloadSetting  the {@link ReloadSetting} to be used
	 * @param commentSetting the {@link CommentSetting} to be used
	 */
	protected CommentEnabledFile(final @NotNull File file,
								 final @NotNull FileType fileType,
								 final @NotNull D fileData,
								 final @NotNull ReloadSetting reloadSetting,
								 final @NotNull CommentSetting commentSetting) {
		super(file, fileType, fileData, reloadSetting);
		this.commentSetting = commentSetting;
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
	 * SetUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setUseArray(String[], Object)
	 */
	public void setUseArray(final @NotNull CommentSetting commentSetting,
							final @NotNull String[] key,
							final @Nullable Object value) {
		this.setCommentSetting(commentSetting);
		this.setUseArray(key, value);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAll(Map)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAll(Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull Pair<String, Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAll(dataPairs);
	}

	/**
	 * SetAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAllUseArray(Map)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(dataMap);
	}

	/**
	 * SetAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAllUseArray(Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull Pair<String[], Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(dataPairs);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAll(String, Map)
	 */
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull String key,
					   final @NotNull Map<String, Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataMap);
	}

	/**
	 * SetAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAll(String, Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAll(final @NotNull CommentSetting commentSetting,
					   final @NotNull String key,
					   final @NotNull Pair<String, Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAll(key, dataPairs);
	}

	/**
	 * SetAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAllUseArray(String[], Map)
	 */
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull String[] blockKey,
							   final @NotNull Map<String[], Object> dataMap) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(blockKey, dataMap);
	}

	/**
	 * SetAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#setAllUseArray(String[], Pair[])
	 */
	@SuppressWarnings("unchecked")
	public void setAllUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull String[] blockKey,
							   final @NotNull Pair<String[], Object>... dataPairs) {
		this.setCommentSetting(commentSetting);
		this.setAllUseArray(blockKey, dataPairs);
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
	 * RemoveUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeUseArray(String...)
	 */
	public void removeUseArray(final @NotNull CommentSetting commentSetting,
							   final @NotNull String... key) {
		this.setCommentSetting(commentSetting);
		this.removeUseArray(key);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAll(String...)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAll(Collection)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(keys);
	}

	/**
	 * RemoveAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAllUseArray(String[]...)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * RemoveAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAllUseArray(Collection)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAll(String, String...)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String key,
						  final @NotNull String... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * RemoveAll with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAll(String, Collection)
	 */
	public void removeAll(final @NotNull CommentSetting commentSetting,
						  final @NotNull String key,
						  final @NotNull Collection<String> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAll(key, keys);
	}

	/**
	 * RemoveAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAllUseArray(String[], String[]...)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[] blockKey,
								  final @NotNull String[]... keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(blockKey, keys);
	}

	/**
	 * RemoveAllUseArray with a specific {@link CommentSetting}
	 *
	 * @see FlatFile#removeAllUseArray(String[], Collection)
	 */
	public void removeAllUseArray(final @NotNull CommentSetting commentSetting,
								  final @NotNull String[] key, final @NotNull Collection<String[]> keys) {
		this.setCommentSetting(commentSetting);
		this.removeAllUseArray(key, keys);
	}
}