package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.TomlFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class TomlFileSection extends FlatSection {

	private final TomlFile tomlFile;

	protected TomlFileSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
		super(sectionKey, tomlFile);
		this.tomlFile = tomlFile;
	}

	@Override
	public TomlFileSection getSection(final @NotNull String sectionKey) {
		return new TomlFileSection(this.getSectionKey() + "." + Objects.notNull(sectionKey, "Key  must not be null"), this.tomlFile);
	}
}