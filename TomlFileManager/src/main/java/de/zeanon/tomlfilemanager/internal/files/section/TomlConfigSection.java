package de.zeanon.tomlfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.cache.filedata.StandardFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.tomlfilemanager.internal.files.config.TomlConfig;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link TomlConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class TomlConfigSection extends TomlFileSection { //NOSONAR


	protected TomlConfigSection(final @NotNull String sectionKey, final @NotNull TomlConfig baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected TomlConfigSection(final @NotNull String[] sectionKey, final @NotNull TomlConfig baseFile, final @NotNull FileData<Map, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}


	@Override
	public @NotNull TomlConfigSection getSection(final @NotNull String sectionKey) {
		return new TomlConfigSection(sectionKey, (TomlConfig) this.flatFile(), this.fileData());
	}

	@Override
	public @NotNull TomlConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlConfigSection(sectionKey, (TomlConfig) this.flatFile(), this.fileData());
	}


	private static class LocalFileData extends StandardFileData<Map, Map.Entry<String, Object>, List> { //NOSONAR

		private static final long serialVersionUID = -3736783796296434140L;

		@SuppressWarnings("rawtypes")
		private LocalFileData(final @NotNull CollectionsProvider<Map, List> collectionsProvider, final boolean synchronize, final @NotNull Map dataMap) { //NOSONAR
			super(collectionsProvider, synchronize, dataMap);
		}
	}
}