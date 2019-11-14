package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.CommentEnabledSection;
import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlFileSection extends CommentEnabledSection {

	private final YamlFile yamlFile;

	protected YamlFileSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
		super(sectionKey, yamlFile);
		this.yamlFile = yamlFile;
	}

	protected YamlFileSection(final @NotNull String[] sectionKey, final @NotNull YamlFile yamlFile) {
		super(sectionKey, yamlFile);
		this.yamlFile = yamlFile;
	}

	@Override
	public YamlFileSection getSection(final @NotNull String sectionKey) {
		return new YamlFileSection(this.getFinalKey(Objects.notNull(sectionKey, "Key  must not be null")), this.yamlFile);
	}

	@Override
	public FlatSection getSectionFromArray(final @NotNull String[] sectionKey) {
		return new YamlFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "Key  must not be null")), this.yamlFile);
	}
}