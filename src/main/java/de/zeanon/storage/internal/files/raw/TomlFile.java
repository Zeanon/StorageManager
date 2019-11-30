package de.zeanon.storage.internal.files.raw;

import com.electronwill.toml.TomlException;
import de.zeanon.storage.external.lists.BigList;
import de.zeanon.storage.external.lists.GapList;
import de.zeanon.storage.internal.base.cache.base.Provider;
import de.zeanon.storage.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.files.section.TomlFileSection;
import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Toml-Type Files
 *
 * @author Zeanon
 * @version 1.2.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class TomlFile extends FlatFile<StandardFileData<Map, List>, Map, List> {


	/**
	 * @param file          the File to be used as a backend
	 * @param inputStream   the FileContent to be set on the creation of the File
	 * @param reloadSetting the ReloadSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected TomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull Class<? extends Map> map, final @NotNull Class<? extends List> list) {
		super(file, FileType.TOML, new LocalFileData(new Collections(map, list)), reloadSetting);

		if (BaseFileUtils.createFile(this.file()) && inputStream != null) {
			BaseFileUtils.writeToFile(this.file(), BaseFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.fileData().loadData(com.electronwill.toml.Toml.read(this.file()));
			this.lastLoaded(System.currentTimeMillis());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e.getCause());
		} catch (TomlException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	@Synchronized
	public void save() {
		try {
			//noinspection unchecked
			com.electronwill.toml.Toml.write(this.fileData().getDataMap(), this.file());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.file().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public void bigList(final boolean bigList) {
		this.provider().setListType(bigList ? BigList.class : GapList.class);
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
	public TomlFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public TomlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	@Override
	protected void readFile() {
		try {
			this.fileData().loadData(com.electronwill.toml.Toml.read(this.file()));
		} catch (IOException e) {
			throw new RuntimeIOException("Error while loading '" + this.file().getAbsolutePath() + "'", e.getCause());
		} catch (TomlException e) {
			throw new FileParseException("Error while parsing '" + this.getAbsolutePath() + "'", e.getCause());
		}
	}


	public enum FileType implements de.zeanon.storage.internal.base.interfaces.FileType {

		TOML("toml");


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

	private static class LocalSection extends TomlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
			super(sectionKey, tomlFile);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull TomlFile tomlFile) {
			super(sectionKey, tomlFile);
		}
	}

	private static class LocalFileData extends StandardFileData<Map, List> {

		private LocalFileData(final @NotNull Provider<Map, List> provider) {
			super(provider);
		}
	}
}