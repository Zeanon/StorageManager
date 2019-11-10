package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public class JsonFileSection extends FlatSection {

	private final JsonFile jsonFile;

	protected JsonFileSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
		super(sectionKey, jsonFile);
		this.jsonFile = jsonFile;
	}

	@Override
	public JsonFileSection getSection(final @NotNull String sectionKey) {
		return new JsonFileSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.jsonFile);
	}
}