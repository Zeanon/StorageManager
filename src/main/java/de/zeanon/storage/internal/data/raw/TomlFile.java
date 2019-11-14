package de.zeanon.storage.internal.data.raw;

import de.zeanon.storage.internal.base.FlatFile;
import de.zeanon.storage.internal.base.exceptions.FileParseException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.FileTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.section.TomlFileSection;
import de.zeanon.storage.internal.utils.SMFileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Toml-Type Files
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class TomlFile extends FlatFile {

	protected TomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting) {
		super(file, FileType.TOML, reloadSetting);

		if (this.create() && inputStream != null) {
			SMFileUtils.writeToFile(this.getFile(), SMFileUtils.createNewInputStream(inputStream));
		}

		try {
			this.getFileData().loadData(com.electronwill.toml.Toml.read(this.getFile()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (IOException e) {
			throw new FileParseException("Error while loading '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}


	@Override
	public void reload() {
		try {
			this.getFileData().loadData(com.electronwill.toml.Toml.read(this.getFile()));
			this.setLastLoaded(System.currentTimeMillis());
		} catch (IOException e) {
			throw new FileParseException("Error while reloading '" + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	@Override
	public synchronized void save() {
		try {
			com.electronwill.toml.Toml.write(this.getFileData().toMap(), this.getFile());
		} catch (IOException e) {
			throw new RuntimeIOException("Error while writing to " + this.getFile().getAbsolutePath() + "'", e.getCause());
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public TomlFileSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	public enum FileType implements FileTypeBase {

		TOML("toml");


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


	private static class LocalSection extends TomlFileSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
			super(sectionKey, tomlFile);
		}
	}
}