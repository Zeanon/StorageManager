package de.zeanon.storage.internal.files.config;

import com.esotericsoftware.yamlbeans.YamlException;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.Config;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.utils.editor.YamlEditor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
@SuppressWarnings("unused")
public class YamlConfig extends YamlFile implements Config {

	private List<String> header;
	private List<String> footer;
	private List<String> comments;


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
		super(file, inputStream, reloadSetting, commentSetting);
	}


	@Override
	public @NotNull List<String> getHeader() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.header;
		} else {
			try {
				this.header = YamlEditor.readHeader(this.getFile());
				return this.header;
			} catch (YamlException e) {
				throw new FileParseException("Error while getting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while getting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		if (header != null) {
			@NotNull List<String> tmp = new ArrayList<>();
			for (final @NotNull String line : header) {
				if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.header = tmp;

			if (this.getFile().length() == 0) {
				try {
					YamlEditor.write(this.getFile(), this.header);
				} catch (YamlException e) {
					throw new FileParseException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			} else {
				try {
					final @NotNull List<String> lines = YamlEditor.read(this.getFile());
					final @NotNull List<String> oldHeader = YamlEditor.readHeader(this.getFile());

					List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					YamlEditor.write(this.getFile(), newLines);
				} catch (YamlException e) {
					throw new FileParseException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			}
		} else {
			this.header = new ArrayList<>();

			try {
				final @NotNull List<String> lines = YamlEditor.read(this.getFile());
				final @NotNull List<String> oldHeader = YamlEditor.readHeader(this.getFile());

				lines.removeAll(oldHeader);

				YamlEditor.write(this.getFile(), lines);
			} catch (YamlException e) {
				throw new FileParseException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public @NotNull List<String> getFooter() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.footer;
		} else {
			try {
				this.footer = YamlEditor.readFooter(this.getFile());
				return this.footer;
			} catch (YamlException e) {
				throw new FileParseException("Error while getting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while getting footer of '" + this.getFile().getAbsolutePath() + "'.", e.getCause());
			}
		}
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		if (footer != null) {
			@NotNull List<String> tmp = new ArrayList<>();
			for (final @NotNull String line : footer) {
				if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.footer = tmp;

			if (this.getFile().length() == 0) {
				try {
					YamlEditor.write(this.getFile(), this.footer);
				} catch (YamlException e) {
					throw new FileParseException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			} else {
				try {
					final @NotNull List<String> lines = YamlEditor.read(this.getFile());
					final @NotNull List<String> oldFooter = YamlEditor.readFooter(this.getFile());

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					YamlEditor.write(this.getFile(), lines);
				} catch (YamlException e) {
					throw new FileParseException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			}
		} else {
			this.footer = new ArrayList<>();

			try {
				final @NotNull List<String> lines = YamlEditor.read(this.getFile());
				final @NotNull List<String> oldFooter = YamlEditor.readFooter(this.getFile());

				lines.removeAll(oldFooter);

				YamlEditor.write(this.getFile(), lines);
			} catch (YamlException e) {
				throw new FileParseException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@Override
	public @NotNull List<String> getComments() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.comments;
		} else {
			try {
				this.comments = YamlEditor.readComments(this.getFile());
				return this.comments;
			} catch (YamlException e) {
				throw new FileParseException("Error while getting comments from '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			} catch (IOException e) {
				throw new RuntimeIOException("Error while getting comments from '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}
}