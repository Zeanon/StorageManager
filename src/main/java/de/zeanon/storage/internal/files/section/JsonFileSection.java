package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.sections.FlatSection;
import de.zeanon.storage.internal.files.raw.JsonFile;
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
@SuppressWarnings("unused")
public class JsonFileSection extends FlatSection<JsonFile, Map, List> {


	@NotNull
	private final JsonFile baseFile;


	protected JsonFileSection(final @NotNull String sectionKey, final @NotNull JsonFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected JsonFileSection(final @NotNull String[] sectionKey, final @NotNull JsonFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	@NotNull
	@Override
	public JsonFileSection getSection(final @NotNull String sectionKey) {
		return new JsonFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@NotNull
	@Override
	public JsonFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new JsonFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}