package de.zeanon.storagemanager.internal.files.config;

import com.esotericsoftware.yamlbeans.YamlException;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.Config;
import de.zeanon.storagemanager.internal.base.interfaces.ReloadSetting;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.files.raw.YamlFile;
import de.zeanon.storagemanager.internal.utility.editor.YamlEditor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended YamlFile with added methods for Config purposes
 *
 * @author Zeanon
 * @version 2.4.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class YamlConfig extends YamlFile implements Config {


	private @NotNull
	List<String> header;
	private @NotNull
	List<String> footer;
	private @NotNull
	List<String> comments;


	/**
	 * @param file             the File to be used as a backend
	 * @param inputStream      the FileContent to be set on the creation of the File
	 * @param reloadSetting    the ReloadSetting to be used with this instance
	 * @param commentSetting   the CommentSetting to be used with this instance
	 * @param synchronizedData if the saved data should be synchronized
	 * @param map              the Map implementation to be used, default is GapDataMap or ConcurrentGapDataMap if concurrent
	 * @param list             the List implementation to be used, default ist GapList
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlConfig(final @NotNull File file,
						 final @Nullable InputStream inputStream,
						 final @NotNull ReloadSetting reloadSetting,
						 final @NotNull CommentSetting commentSetting,
						 final boolean synchronizedData,
						 final @NotNull Class<? extends Map> map,
						 final @NotNull Class<? extends List> list) {
		super(file, inputStream, reloadSetting, commentSetting, synchronizedData, map, list);
		//noinspection unchecked
		this.header = this.collectionsProvider().newList();
		//noinspection unchecked
		this.footer = this.collectionsProvider().newList();
		//noinspection unchecked
		this.comments = this.collectionsProvider().newList();
	}


	@Override
	public @NotNull
	List<String> getHeader() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		} else if (!this.shouldReload()) {
			return this.header;
		} else {
			try {
				this.header = YamlEditor.readHeader(this.file(), this.collectionsProvider());
				return this.header;
			} catch (final @NotNull YamlException e) {
				throw new FileParseException("Error while getting header of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while getting header of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e.getCause());
			}
		}
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		if (header != null && header.length > 0) {
			//noinspection unchecked
			final @NotNull List<String> tmp = this.collectionsProvider().newList();
			for (final @Nullable String line : header) {
				if (line == null) {
					tmp.add("#");
				} else if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.header = tmp;

			if (this.file().length() == 0) {
				try {
					YamlEditor.write(this.file(), this.header);
				} catch (final @NotNull YamlException e) {
					throw new FileParseException("Error while setting header of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e);
				} catch (final @NotNull IOException e) {
					throw new RuntimeIOException("Error while setting header of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e.getCause());
				}
			} else {
				try {
					final @NotNull List<String> lines = YamlEditor.read(this.file());
					final @NotNull List<String> oldHeader = YamlEditor.readHeader(this.file(), this.collectionsProvider());
					final @NotNull List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					YamlEditor.write(this.file(), newLines);
				} catch (final @NotNull YamlException e) {
					throw new FileParseException("Error while setting header of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e);
				} catch (final @NotNull IOException e) {
					throw new RuntimeIOException("Error while setting header of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e.getCause());
				}
			}
		} else {
			//noinspection unchecked
			this.header = this.collectionsProvider().newList();

			try {
				final @NotNull List<String> lines = YamlEditor.read(this.file());
				final @NotNull List<String> oldHeader = YamlEditor.readHeader(this.file(), this.collectionsProvider());

				lines.removeAll(oldHeader);

				YamlEditor.write(this.file(), lines);
			} catch (final @NotNull YamlException e) {
				throw new FileParseException("Error while setting header of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while setting header of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e.getCause());
			}
		}
	}

	@Override
	public @NotNull
	List<String> getFooter() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		} else if (!this.shouldReload()) {
			return this.footer;
		} else {
			try {
				this.footer = YamlEditor.readFooter(this.file(), this.collectionsProvider());
				return this.footer;
			} catch (final @NotNull YamlException e) {
				throw new FileParseException("Error while getting footer of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while getting footer of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e.getCause());
			}
		}
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		if (footer != null && footer.length > 0) {
			//noinspection unchecked
			final @NotNull List<String> tmp = this.collectionsProvider().newList();
			for (final @Nullable String line : footer) {
				if (line == null) {
					tmp.add("#");
				} else if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.footer = tmp;

			if (this.file().length() == 0) {
				try {
					YamlEditor.write(this.file(), this.footer);
				} catch (final @NotNull YamlException e) {
					throw new FileParseException("Error while setting footer of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e);
				} catch (final @NotNull IOException e) {
					throw new RuntimeIOException("Error while setting footer of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e.getCause());
				}
			} else {
				try {
					final @NotNull List<String> lines = YamlEditor.read(this.file());
					final @NotNull List<String> oldFooter = YamlEditor.readFooter(this.file(), this.collectionsProvider());

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					YamlEditor.write(this.file(), lines);
				} catch (final @NotNull YamlException e) {
					throw new FileParseException("Error while setting footer of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e);
				} catch (final @NotNull IOException e) {
					throw new RuntimeIOException("Error while setting footer of '"
												 + this.file().getAbsolutePath()
												 + "'",
												 e.getCause());
				}
			}
		} else {
			//noinspection unchecked
			this.footer = this.collectionsProvider().newList();

			try {
				final @NotNull List<String> lines = YamlEditor.read(this.file());
				final @NotNull List<String> oldFooter = YamlEditor.readFooter(this.file(), this.collectionsProvider());

				lines.removeAll(oldFooter);

				YamlEditor.write(this.file(), lines);
			} catch (final @NotNull YamlException e) {
				throw new FileParseException("Error while setting footer of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while setting footer of '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e.getCause());
			}
		}
	}

	@Override
	public @NotNull
	List<String> getComments() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			//noinspection unchecked
			return this.collectionsProvider().newList();
		} else if (!this.shouldReload()) {
			return this.comments;
		} else {
			try {
				this.comments = YamlEditor.readComments(this.file(), this.collectionsProvider());
				return this.comments;
			} catch (final @NotNull YamlException e) {
				throw new FileParseException("Error while getting comments from '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e);
			} catch (final @NotNull IOException e) {
				throw new RuntimeIOException("Error while getting comments from '"
											 + this.file().getAbsolutePath()
											 + "'",
											 e.getCause());
			}
		}
	}
}