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
import de.zeanon.storage.internal.settings.DataType;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"unchecked", "unused"})
public class YamlFile extends CommentEnabledFile {

	protected YamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.YAML, reloadSetting, commentSetting, dataType == null ? DataType.STANDARD : dataType);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.file, inputStream);
		}

		try {
			this.fileData = new LocalFileData((Map<String, Object>) new YamlReader(new FileReader(this.file)).read());
			this.lastLoaded = System.currentTimeMillis();
		} catch (YamlException | FileNotFoundException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	@Override
	public void reload() {
		try {
			this.fileData.loadData((Map<String, Object>) new YamlReader(new FileReader(this.file)).read());
			this.lastLoaded = System.currentTimeMillis();
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.fileData.containsKey(key)) {
			this.fileData.remove(key);

			try {
				this.write(this.fileData.toMap());
			} catch (IOException e) {
				System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String key : keys) {
			this.fileData.remove(key);
		}

		try {
			this.write(this.fileData.toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String tempKey : keys) {
			this.fileData.remove(key + "." + tempKey);
		}

		try {
			this.write(this.fileData.toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			this.writeData();
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			this.writeData();
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			this.writeData();
		}
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
		@Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.file));
		writer.write(fileData);
	}

	private void writeData() {
		try {
			if (this.getCommentSetting() != Comment.PRESERVE) {
				this.write(Objects.notNull(this.fileData, "FileData must not be null").toMap());
			} else {
				final List<String> unEdited = YamlEditor.read(this.file);
				final List<String> header = YamlEditor.readHeader(this.file);
				final List<String> footer = YamlEditor.readFooter(this.file);
				this.write(this.fileData.toMap());
				header.addAll(YamlEditor.read(this.file));
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				YamlEditor.write(this.file, YamlUtils.parseComments(this.file, unEdited, header));
				this.write(Objects.notNull(this.fileData, "FileData must not be null").toMap());
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