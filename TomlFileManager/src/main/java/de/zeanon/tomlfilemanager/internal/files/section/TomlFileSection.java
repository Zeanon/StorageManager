package de.zeanon.tomlfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.sections.FlatSection;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.tomlfilemanager.internal.files.raw.TomlFile;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link TomlFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class TomlFileSection extends FlatSection<TomlFile, Map, List> { //NOSONAR


	protected TomlFileSection(final @NotNull String sectionKey, final @NotNull TomlFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected TomlFileSection(final @NotNull String[] sectionKey, final @NotNull TomlFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}


	@Override
	public @NotNull TomlFileSection getSection(final @NotNull String sectionKey) {
		return new TomlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull TomlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull TomlFileSection getOrCreateSection(final @NotNull String sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull TomlFileSection getOrCreateSectionUseArray(final @NotNull String... sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull TomlFileSection createSection(final @NotNull String sectionKey) {
		this.set(sectionKey, this.collectionsProvider().newMap());
		return new TomlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull TomlFileSection createSectionUseArray(final @NotNull String... sectionKey) {
		this.setUseArray(sectionKey, this.collectionsProvider().newMap());
		return new TomlFileSection(sectionKey, this.flatFile(), this.fileData());
	}


	@Override
	protected FileData<Map, ?, List> getSectionFileData(@NotNull String key, @NotNull FileData<Map, ?, List> fileData) {
		return new LocalFileData(fileData.collectionsProvider(), (Map) Objects.notNull(fileData.get(key)));
	}

	@Override
	protected FileData<Map, ?, List> getSectionFileDataUseArray(@NotNull String[] key, @NotNull FileData<Map, ?, List> fileData) {
		return new LocalFileData(fileData.collectionsProvider(), (Map) Objects.notNull(fileData.getUseArray(key)));
	}


	private static class LocalFileData extends StandardFileData<Map, Map.Entry, List> { //NOSONAR

		private static final long serialVersionUID = -3736783796296434140L;

		@SuppressWarnings("rawtypes")
		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider, final @NotNull Map dataMap) { //NOSONAR
			super(collectionsProvider, dataMap);
		}
	}
}