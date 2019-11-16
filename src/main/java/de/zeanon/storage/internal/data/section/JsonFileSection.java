package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class JsonFileSection extends FlatSection {

	@NotNull
	private final JsonFile jsonFile;


	protected JsonFileSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
		super(sectionKey, jsonFile);
		this.jsonFile = jsonFile;
	}

	protected JsonFileSection(final @NotNull String[] sectionKey, final @NotNull JsonFile jsonFile) {
		super(sectionKey, jsonFile);
		this.jsonFile = jsonFile;
	}

	@NotNull
	@Override
	public JsonFileSection getSection(final @NotNull String sectionKey) {
		return new JsonFileSection(this.getFinalKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.jsonFile);
	}

	@NotNull
	@Override
	public JsonFileSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new JsonFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.jsonFile);
	}
}