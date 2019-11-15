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
		return new TomlFileSection(this.getFinalKey(Objects.notNull(sectionKey, "Key  must not be null")), this.tomlFile);
	}

	@NotNull
	@Override
	public FlatSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new TomlFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "Key  must not be null")), this.tomlFile);
	}
}