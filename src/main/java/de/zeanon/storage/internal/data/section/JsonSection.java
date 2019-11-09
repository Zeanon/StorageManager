package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.JsonFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class JsonSection extends FlatSection {

	private final JsonFile jsonFile;

	protected JsonSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
		super(sectionKey, jsonFile);
		this.jsonFile = jsonFile;
	}

	@Override
	public JsonSection getSection(final @NotNull String sectionKey) {
		return new JsonSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.jsonFile);
	}

	protected JsonSection getJsonSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonSection jsonSection = (JsonSection) obj;
			return this.jsonFile.equals(jsonSection.jsonFile)
				   && this.sectionKey.equals(jsonSection.sectionKey);
		}
	}
}