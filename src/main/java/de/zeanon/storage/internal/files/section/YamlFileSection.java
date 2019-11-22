package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.sections.CommentEnabledSection;
import de.zeanon.storage.internal.files.config.YamlConfig;
import de.zeanon.storage.internal.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link YamlFile} and {@link YamlConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlFileSection extends CommentEnabledSection<YamlFile> {

	@NotNull
	private final YamlFile yamlFile;


	protected YamlFileSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
		super(sectionKey, yamlFile);
		this.yamlFile = yamlFile;
	}

	protected YamlFileSection(final @NotNull String[] sectionKey, final @NotNull YamlFile yamlFile) {
		super(sectionKey, yamlFile);
		this.yamlFile = yamlFile;
	}

	@NotNull
	@Override
	public YamlFileSection getSection(final @NotNull String sectionKey) {
		return new YamlFileSection(this.getFinalKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.yamlFile);
	}

	@NotNull
	@Override
	public YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new YamlFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.yamlFile);
	}
}