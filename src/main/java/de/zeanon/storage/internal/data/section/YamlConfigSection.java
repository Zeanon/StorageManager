package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.CommentEnabledSection;
import de.zeanon.storage.internal.data.config.YamlConfig;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class YamlConfigSection extends CommentEnabledSection<YamlConfigSection, YamlConfig> {

	private final YamlConfig yamlConfig;

	protected YamlConfigSection(final @NotNull String sectionKey, final @NotNull YamlConfig yamlConfig) {
		super(sectionKey, yamlConfig);
		this.yamlConfig = yamlConfig;
	}

	@Override
	public YamlConfigSection getSection(final @NotNull String sectionKey) {
		return new YamlConfigSection(this.getSectionKey() + "." + Objects.notNull(sectionKey, "Key must not be null"), this.yamlConfig);
	}
}