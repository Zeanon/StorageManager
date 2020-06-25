package de.zeanon.storagemanager.internal.files.section;

import de.zeanon.storagemanager.internal.base.sections.FlatSection;
import de.zeanon.storagemanager.internal.files.raw.JsonFile;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link JsonFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "rawtypes"})
public class JsonFileSection extends FlatSection<JsonFile, Map, List> { //NOSONAR


	private final @NotNull
	JsonFile baseFile;


	protected JsonFileSection(final @NotNull String sectionKey, final @NotNull JsonFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected JsonFileSection(final @NotNull String[] sectionKey, final @NotNull JsonFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public @NotNull
	JsonFileSection getSection(final @NotNull String sectionKey) {
		return new JsonFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull
	JsonFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new JsonFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}