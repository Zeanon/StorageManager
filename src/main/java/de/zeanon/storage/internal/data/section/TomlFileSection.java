package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.TomlFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class TomlFileSection extends FlatSection {

	private final TomlFile tomlFile;

	protected TomlFileSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
		super(sectionKey, tomlFile);
		this.tomlFile = tomlFile;
	}

	@Override
	public TomlFileSection getSection(final @NotNull String sectionKey) {
		return new TomlFileSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.tomlFile);
	}

	protected TomlFileSection getTomlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlFileSection tomlFileSection = (TomlFileSection) obj;
			return this.tomlFile.equals(tomlFileSection.tomlFile)
				   && this.sectionKey.equals(tomlFileSection.sectionKey);
		}
	}
}