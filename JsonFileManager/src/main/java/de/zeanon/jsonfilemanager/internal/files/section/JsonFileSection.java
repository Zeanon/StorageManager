package de.zeanon.jsonfilemanager.internal.files.section;

import de.zeanon.jsonfilemanager.internal.files.raw.JsonFile;
import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.sections.FlatSection;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link JsonFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class JsonFileSection extends FlatSection<JsonFile, Map, List> { //NOSONAR


	protected JsonFileSection(final @NotNull String sectionKey, final @NotNull JsonFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected JsonFileSection(final @NotNull String[] sectionKey, final @NotNull JsonFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	@Override
	public @NotNull JsonFileSection getSection(final @NotNull String sectionKey) {
		return new JsonFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull JsonFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new JsonFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull JsonFileSection getOrCreateSection(final @NotNull String sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull JsonFileSection getOrCreateSectionUseArray(final @NotNull String... sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull JsonFileSection createSection(final @NotNull String sectionKey) {
		this.set(sectionKey, this.collectionsProvider().newMap());
		return new JsonFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull JsonFileSection createSectionUseArray(final @NotNull String... sectionKey) {
		this.setUseArray(sectionKey, this.collectionsProvider().newMap());
		return new JsonFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	protected @NotNull FileData<Map, ?, List> getSectionFileData(final @NotNull String key, final @NotNull FileData<Map, ?, List> fileData) {
		return new LocalFileData(fileData.collectionsProvider(), (Map) Objects.notNull(fileData.get(key)));
	}

	@Override
	protected @NotNull FileData<Map, ?, List> getSectionFileDataUseArray(final @NotNull String[] key, final @NotNull FileData<Map, ?, List> fileData) {
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