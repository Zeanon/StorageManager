package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.sections.FlatSection;
import de.zeanon.storage.internal.files.raw.TomlFile;
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
@SuppressWarnings("unused")
public class TomlFileSection extends FlatSection<TomlFile, Map, List> {


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
		return new TomlFileSection(this.getFinalKey(sectionKey), this.tomlFile);
	}

	@NotNull
	@Override
	public TomlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlFileSection(this.getFinalArrayKey(sectionKey), this.tomlFile);
	}
}