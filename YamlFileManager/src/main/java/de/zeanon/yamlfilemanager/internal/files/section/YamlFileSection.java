package de.zeanon.yamlfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.sections.CommentEnabledSection;
import de.zeanon.yamlfilemanager.internal.files.raw.YamlFile;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link YamlFile} and {@link YamlConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class YamlFileSection extends CommentEnabledSection<YamlFile, Map, List> { //NOSONAR


	private final @NotNull YamlFile baseFile;


	protected YamlFileSection(final @NotNull String sectionKey, final @NotNull YamlFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected YamlFileSection(final @NotNull String[] sectionKey, final @NotNull YamlFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public @NotNull YamlFileSection getSection(final @NotNull String sectionKey) {
		return new YamlFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull YamlFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new YamlFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}