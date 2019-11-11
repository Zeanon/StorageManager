package de.zeanon.storage.internal.data.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.zeanon.storage.internal.base.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.section.YamlFileSection;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.YamlUtils;
import de.zeanon.storage.internal.utils.editor.YamlEditor;
import java.io.*;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Yaml-Type Files
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlFile extends CommentEnabledFile<YamlFile> {

	protected YamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.YAML, reloadSetting, commentSetting, dataType);

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
	public YamlFile reload() {
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
	public synchronized YamlFile remove(final @NotNull String key) {
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
	public synchronized YamlFile removeAll(final @NotNull List<String> keys) {
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
	public synchronized YamlFile removeAll(final @NotNull String key, final @NotNull List<String> keys) {
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
	public synchronized YamlFile set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.writeData();
		}
		return this;
	}

	@Override
	public synchronized YamlFile setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.writeData();
		}
		return this;
	}

	@Override
	public synchronized YamlFile setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
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
	public YamlFileSection getSection(final @NotNull String sectionKey) {
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


	public enum FileType implements FileTypeBase {

		YAML("yml");


		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends YamlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}
	}
}