package de.zeanon.tomlfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.sections.FlatSection;
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


	private final @NotNull TomlFile baseFile;


	protected TomlFileSection(final @NotNull String sectionKey, final @NotNull TomlFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected TomlFileSection(final @NotNull String[] sectionKey, final @NotNull TomlFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public @NotNull TomlFileSection getSection(final @NotNull String sectionKey) {
		return new TomlFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull TomlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}