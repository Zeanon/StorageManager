package de.zeanon.thunderfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.storagemanagercore.internal.base.cache.provider.CollectionsProvider;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.interfaces.FileData;
import de.zeanon.storagemanagercore.internal.base.sections.CommentEnabledSection;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.thunderfilemanager.internal.files.raw.ThunderFile;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link ThunderFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class ThunderFileSection extends CommentEnabledSection<ThunderFile, DataMap, List> { //NOSONAR


	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile baseFile, final @NotNull FileData<DataMap, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	protected ThunderFileSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile baseFile, final @NotNull FileData<DataMap, ?, List> fileData) {
		super(sectionKey, baseFile, fileData);
	}

	@Override
	public @NotNull ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(sectionKey, this.flatFile(), this.fileData);
	}

	@Override
	public @NotNull ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderFileSection(sectionKey, this.flatFile(), this.fileData);
	}

	@Override
	protected FileData<DataMap, ?, List> getSectionFileData(@NotNull String key, @NotNull FileData<DataMap, ?, List> fileData) {
		return new LocalFileData(fileData.collectionsProvider(), fileData.synchronizeData(), (DataMap) Objects.notNull(fileData.get(key)));
	}

	@Override
	protected FileData<DataMap, ?, List> getSectionFileDataUseArray(@NotNull String[] key, @NotNull FileData<DataMap, ?, List> fileData) {
		return new LocalFileData(fileData.collectionsProvider(), fileData.synchronizeData(), (DataMap) Objects.notNull(fileData.getUseArray(key)));
	}

	private static class LocalFileData extends ThunderFileData<DataMap, DataMap.Entry<String, Object>, List> { //NOSONAR

		private static final long serialVersionUID = -3736783796296434140L;

		@SuppressWarnings("rawtypes")
		private LocalFileData(final @NotNull CollectionsProvider<DataMap, List> collectionsProvider, final boolean synchronize, final @NotNull DataMap dataMap) { //NOSONAR
			super(collectionsProvider, synchronize, dataMap);
		}
	}
}