package de.zeanon.storage.internal.data.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.editor.YamlEditor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended YamlFile with added methods for Config purposes
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlConfig extends YamlFile implements ConfigBase {

	private List<String> header;
	private List<String> footer;
	private List<String> comments;


	protected YamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public void reload() {
		try {
			//noinspection unchecked
			this.getFileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.getFile())).read());
			this.setLastLoaded(System.currentTimeMillis());
		} catch (IOException e) {
			throw new FileParseException("Error while reloading '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}


	@NotNull
	@Override
	public List<String> getHeader() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.header;
		} else {
			try {
				this.header = YamlEditor.readHeader(this.getFile());
				return this.header;
			} catch (IOException e) {
				throw new FileParseException("Error while getting header of '" + this.getFile().getAbsolutePath() + "'.", e.getCause());
			}
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		if (header != null) {
			List<String> tmp = new ArrayList<>();
			for (final String line : header) {
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
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.getFile());
					final List<String> oldHeader = YamlEditor.readHeader(this.getFile());

					List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					YamlEditor.write(this.getFile(), newLines);
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			}
		} else {
			this.header = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.getFile());
				final List<String> oldHeader = YamlEditor.readHeader(this.getFile());

				lines.removeAll(oldHeader);

				YamlEditor.write(this.getFile(), lines);
			} catch (IOException e) {
				throw new RuntimeIOException("Error while setting header of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@NotNull
	@Override
	public List<String> getFooter() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.footer;
		} else {
			try {
				this.footer = YamlEditor.readFooter(this.getFile());
				return this.footer;
			} catch (IOException e) {
				throw new FileParseException("Error while getting footer of '" + this.getFile().getAbsolutePath() + "'.", e.getCause());
			}
		}
	}

	@Override
	public void setFooter(@Nullable List<String> footer) {
		if (footer != null) {
			List<String> tmp = new ArrayList<>();
			for (final String line : footer) {
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
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.getFile());
					final List<String> oldFooter = YamlEditor.readFooter(this.getFile());

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					YamlEditor.write(this.getFile(), lines);
				} catch (IOException e) {
					throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
				}
			}
		} else {
			this.footer = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.getFile());
				final List<String> oldFooter = YamlEditor.readFooter(this.getFile());

				lines.removeAll(oldFooter);

				YamlEditor.write(this.getFile(), lines);
			} catch (IOException e) {
				throw new RuntimeIOException("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'", e.getCause());
			}
		}
	}

	@NotNull
	@Override
	public List<String> getComments() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.comments;
		} else {
			try {
				this.comments = YamlEditor.readComments(this.getFile());
				return this.comments;
			} catch (IOException e) {
				throw new FileParseException("Error while getting comments from '" + this.getFile().getAbsolutePath() + "'.", e.getCause());
			}
		}
	}
}