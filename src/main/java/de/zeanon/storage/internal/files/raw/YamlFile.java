package de.zeanon.storage.internal.files.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.files.CommentEnabledFile;
import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.section.YamlFileSection;
import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.datafiles.YamlUtils;
import de.zeanon.storage.internal.utility.editor.YamlEditor;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
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
public class YamlFile extends CommentEnabledFile<StandardFileData<Map, List>, Map, List> {


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected YamlFile(final @NotNull File file,
					   final @Nullable InputStream inputStream,
					   final @NotNull ReloadSetting reloadSetting,
					   final @NotNull CommentSetting commentSetting,
					   final boolean synchronizedData,
					   final @NotNull Class<? extends Map> map,
					   final @NotNull Class<? extends List> list) {
		super(file, FileType.YAML, new LocalFileData(new Collections(map, list), synchronizedData), reloadSetting, commentSetting);

		if (BaseFileUtils.createFile(this.file()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			//noinspection unchecked
			this.fileData().loadData((Map<String, Object>) new YamlReader(new FileReader(this.file())).read());
			this.lastLoaded(System.currentTimeMillis());
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e);
		} catch (YamlException e) {
			throw new FileParseException("Error while parsing '" + this.file().getAbsolutePath() + "'", e);
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			if (this.getCommentSetting() != Comment.PRESERVE) {
				this.write(this.fileData().dataMap());
			} else {
				final @NotNull List<String> unEdited = YamlEditor.read(this.file());
				final @NotNull List<String> header = YamlEditor.readHeader(this.file(), this.provider());
				final @NotNull List<String> footer = YamlEditor.readFooter(this.file(), this.provider());
				this.write(this.fileData().dataMap());
				header.addAll(YamlEditor.read(this.file()));
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				YamlEditor.write(this.file(), YamlUtils.parseComments(unEdited, header, this.provider()));
				this.write(this.fileData().dataMap());
			}
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.file().getAbsolutePath() + "'", e);
		}
	}

	@Override
	public void bigList(final boolean bigList) {
		this.provider().setListType(bigList ? BigList.class : GapList.class);
	}

	@Override
	public void concurrentData(final boolean synchronize) {
		this.provider().setMapType(synchronize ? ConcurrentHashMap.class : HashMap.class);
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@Override
	public @NotNull YamlFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@Override
	public @NotNull YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	@Override
	protected @NotNull Map readFile() {
		try {
			//noinspection unchecked
			return (Map<String, Object>) new YamlReader(new FileReader(this.file())).read();
		} catch (YamlException e) {
			throw new FileParseException("Error while parsing '" + this.file().getAbsolutePath() + "'", e);
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e);
		}
	}

	private void write(final @NotNull Map fileData) throws IOException {
		final @NotNull @Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.file()));
		writer.write(fileData);
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

		YAML("yml");


		private final @NotNull String extension;

		@Contract(pure = true)
		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Contract(pure = true)
		@Override
		public @NotNull String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Contract(pure = true)
		@Override
		public @NotNull String toString() {
			return this.extension;
		}
	}

	private static class Collections extends Provider<Map, List> {

		private Collections(Class<? extends Map> map, Class<? extends List> list) {
			super(map, list);
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

	private static class LocalFileData extends StandardFileData<Map, List> {

		private LocalFileData(final @NotNull Provider<Map, List> provider, final boolean synchronize) {
			super(provider, synchronize);
		}
	}
}