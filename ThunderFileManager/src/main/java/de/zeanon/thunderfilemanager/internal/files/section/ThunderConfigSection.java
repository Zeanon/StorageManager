package de.zeanon.thunderfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.thunderfilemanager.internal.files.config.ThunderConfig;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link ThunderConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class ThunderConfigSection extends ThunderFileSection { //NOSONAR


	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig baseFile, final @NotNull FileData<DataMap, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected ThunderConfigSection(final @NotNull String[] sectionKey, final @NotNull ThunderConfig baseFile, final @NotNull FileData<DataMap, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	@Override
	public @NotNull ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(sectionKey, (ThunderConfig) this.flatFile(), this.fileData);
	}

	@Override
	public @NotNull ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderConfigSection(sectionKey, (ThunderConfig) this.flatFile(), this.fileData);
	}


	private static class LocalFileData extends ThunderFileData<DataMap, DataMap.Entry, List> { //NOSONAR

		private static final long serialVersionUID = -3736783796296434140L;

		@SuppressWarnings("rawtypes")
		private LocalFileData(final @NotNull CollectionsProvider<DataMap, List> collectionsProvider, final @NotNull DataMap dataMap) { //NOSONAR
			super(collectionsProvider, dataMap);
		}
	}
}