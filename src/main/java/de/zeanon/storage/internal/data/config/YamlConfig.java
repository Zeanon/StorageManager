package de.zeanon.storage.internal.data.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.data.section.YamlConfigSection;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.YamlUtils;
import de.zeanon.storage.internal.utils.editor.YamlEditor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
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
public class YamlConfig extends CommentEnabledFile<YamlConfig> implements ConfigBase<YamlConfig> {

	private List<String> header;
	private List<String> footer;
	private List<String> comments;


	protected YamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, YamlFile.FileType.YAML, reloadSetting, commentSetting, dataType);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			//noinspection unchecked
			this.setFileData(new LocalFileData((Map<String, Object>) new YamlReader(new FileReader(this.getFile())).read()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (YamlException | FileNotFoundException e) {
			System.err.println("Exception while reloading '" + this.getFile().getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


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
				System.err.println("Couldn't get header of '" + this.getFile().getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	@Override
	public YamlConfig setHeader(final @Nullable List<String> header) {
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
					System.err.println("Error while setting header of '" + this.getFile().getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.getFile());
					final List<String> oldHeader = YamlEditor.readHeader(this.getFile());

					List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					YamlEditor.write(this.getFile(), newLines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying header of '" + this.getFile().getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.header = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.getFile());
				final List<String> oldHeader = YamlEditor.readHeader(this.getFile());

				lines.removeAll(oldHeader);

				YamlEditor.write(this.getFile(), lines);
			} catch (final IOException e) {
				System.err.println("Exception while modifying header of '" + this.getFile().getAbsolutePath() + "'");
				e.printStackTrace();
			}
		}
		return this;
	}

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
				System.err.println("Couldn't get footer of '" + this.getFile().getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	@Override
	public YamlConfig setFooter(@Nullable List<String> footer) {
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
					System.err.println("Error while setting footer of '" + this.getFile().getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.getFile());
					final List<String> oldFooter = YamlEditor.readFooter(this.getFile());

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					YamlEditor.write(this.getFile(), lines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying footer of '" + this.getFile().getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.footer = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.getFile());
				final List<String> oldFooter = YamlEditor.readFooter(this.getFile());

				lines.removeAll(oldFooter);

				YamlEditor.write(this.getFile(), lines);
			} catch (final IOException e) {
				System.err.println("Exception while modifying footer of '" + this.getFile().getAbsolutePath() + "'");
				e.printStackTrace();
			}
		}
		return this;
	}

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
				System.err.println("Couldn't get comments from '" + this.getFile().getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	@Override
	public YamlConfig reload() {
		try {
			//noinspection unchecked
			this.getFileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.getFile())).read());
			this.setLastLoaded(System.currentTimeMillis());
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.getFile().getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public synchronized YamlConfig remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getFileData().containsKey(key)) {
			this.getFileData().remove(key);

			try {
				this.write(this.getFileData().toMap());
			} catch (IOException e) {
				System.err.println("Could not write to '" + this.getFile().getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
		return this;
	}

	@Override
	public synchronized YamlConfig removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String key : keys) {
			this.getFileData().remove(key);
		}

		try {
			this.write(this.getFileData().toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.getFile().getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public synchronized YamlConfig removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String tempKey : keys) {
			this.getFileData().remove(key + "." + tempKey);
		}

		try {
			this.write(this.getFileData().toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.getFile().getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public synchronized YamlConfig set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.writeData();
		}
		return this;
	}

	@Override
	public synchronized YamlConfig setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.writeData();
		}
		return this;
	}

	@Override
	public synchronized YamlConfig setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			this.writeData();
		}
		return this;
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public YamlConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	private void write(final @NotNull Map fileData) throws IOException {
		@Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.getFile()));
		writer.write(fileData);
	}

	@SuppressWarnings("DuplicatedCode")
	private void writeData() {
		try {
			if (this.getCommentSetting() != Comment.PRESERVE) {
				this.write(Objects.notNull(this.getFileData(), "FileData must not be null").toMap());
			} else {
				final List<String> unEdited = YamlEditor.read(this.getFile());
				final List<String> header = YamlEditor.readHeader(this.getFile());
				final List<String> footer = YamlEditor.readFooter(this.getFile());
				this.write(this.getFileData().toMap());
				header.addAll(YamlEditor.read(this.getFile()));
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				YamlEditor.write(this.getFile(), YamlUtils.parseComments(this.getFile(), unEdited, header));
				this.write(Objects.notNull(this.getFileData(), "FileData must not be null").toMap());
			}
		} catch (IOException e) {
			System.err.println("Error while writing to '" + getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	private static class LocalSection extends YamlConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlConfig yamlConfig) {
			super(sectionKey, yamlConfig);
		}
	}
}