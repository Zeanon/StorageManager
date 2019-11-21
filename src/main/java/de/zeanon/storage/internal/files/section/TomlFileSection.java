package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.sections.FlatSection;
import de.zeanon.storage.internal.files.raw.TomlFile;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link TomlFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class TomlFileSection extends FlatSection<TomlFile> {

	@NotNull
	private final TomlFile tomlFile;


	protected TomlFileSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
		super(sectionKey, tomlFile);
		this.tomlFile = tomlFile;
	}

	protected TomlFileSection(final @NotNull String[] sectionKey, final @NotNull TomlFile tomlFile) {
		super(sectionKey, tomlFile);
		this.tomlFile = tomlFile;
	}

	@NotNull
	@Override
	public TomlFileSection getSection(final @NotNull String sectionKey) {
		return new TomlFileSection(this.getFinalKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.tomlFile);
	}

	@NotNull
	@Override
	public TomlFileSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new TomlFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.tomlFile);
	}
}