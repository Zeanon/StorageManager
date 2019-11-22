package de.zeanon.storage.internal.files.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.zeanon.storage.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.files.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.section.YamlFileSection;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.datafiles.YamlUtils;
import de.zeanon.storage.internal.utility.utils.editor.YamlEditor;
import java.io.*;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Yaml-Type Files
 *
 * @author Zeanon
 * @version 1.3.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlFile extends CommentEnabledFile<StandardFileData> {


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting) {
		super(file, YamlFile.FileType.YAML, new LocalFileData(), reloadSetting, commentSetting);

		if (SMFileUtils.createFile(this.getFile()) && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			//noinspection unchecked
			this.getFileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.getFile())).read());
			this.setLastLoaded(System.currentTimeMillis());
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while loading '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		} catch (YamlException e) {
			throw new FileParseException("Error while parsing '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	public void reload() {
		try {
			//noinspection unchecked
			this.getFileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.getFile())).read());
			this.setLastLoaded(System.currentTimeMillis());
		} catch (YamlException e) {
			throw new FileParseException("Error while parsing '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while loading '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			if (this.getCommentSetting() != Comment.PRESERVE) {
				this.write(this.getFileData().getDataMap());
			} else {
				final List<String> unEdited = YamlEditor.read(this.getFile());
				@NotNull final List<String> header = YamlEditor.readHeader(this.getFile());
				@NotNull final List<String> footer = YamlEditor.readFooter(this.getFile());
				this.write(this.getFileData().getDataMap());
				header.addAll(YamlEditor.read(this.getFile()));
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				YamlEditor.write(this.getFile(), YamlUtils.parseComments(unEdited, header));
				this.write(this.getFileData().getDataMap());
			}
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
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
	public YamlFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	private void write(final @NotNull Map fileData) throws IOException {
		@NotNull @Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.getFile()));
		writer.write(fileData);
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

		YAML("yml");


		@NotNull
		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@NotNull
		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@NotNull
		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends YamlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}
	}

	private static class LocalFileData extends StandardFileData {

		private LocalFileData() {
			super();
		}
	}
}