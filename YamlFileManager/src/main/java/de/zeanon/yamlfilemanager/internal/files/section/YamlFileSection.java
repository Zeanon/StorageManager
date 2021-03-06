package de.zeanon.yamlfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.sections.CommentEnabledSection;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.yamlfilemanager.internal.files.config.YamlConfig;
import de.zeanon.yamlfilemanager.internal.files.raw.YamlFile;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link YamlFile} and {@link YamlConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class YamlFileSection extends CommentEnabledSection<YamlFile, Map, List> { //NOSONAR


	protected YamlFileSection(final @NotNull String sectionKey, final @NotNull YamlFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected YamlFileSection(final @NotNull String[] sectionKey, final @NotNull YamlFile baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}


	@Override
	public @NotNull YamlFileSection getSection(final @NotNull String sectionKey) {
		return new YamlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new YamlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull YamlFileSection getOrCreateSection(final @NotNull String sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull YamlFileSection getOrCreateSectionUseArray(final @NotNull String... sectionKey) {
		try {
			return this.getSectionUseArray(sectionKey);
		} catch (final @NotNull ObjectNullException e) {
			return this.createSectionUseArray(sectionKey);
		}
	}

	@Override
	public @NotNull YamlFileSection createSection(final @NotNull String sectionKey) {
		this.set(sectionKey, this.collectionsProvider().newMap());
		return new YamlFileSection(sectionKey, this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull YamlFileSection createSectionUseArray(final @NotNull String... sectionKey) {
		this.setUseArray(sectionKey, this.collectionsProvider().newMap());
		return new YamlFileSection(sectionKey, this.flatFile(), this.fileData());
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