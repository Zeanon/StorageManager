package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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

	protected JsonFileSection getJsonSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonFileSection jsonFileSection = (JsonFileSection) obj;
			return this.jsonFile.equals(jsonFileSection.jsonFile)
				   && this.sectionKey.equals(jsonFileSection.sectionKey);
		}
	}
}