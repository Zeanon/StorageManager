package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFileSection extends FlatSection {

	private final ThunderFile thunderFile;

	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
		super(sectionKey, thunderFile);
		this.thunderFile = thunderFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull Comment commentSetting) {
		this.thunderFile.set(this.getSectionKey(key), value, commentSetting);
	}

	public synchronized void remove(final @NotNull String key, final @NotNull Comment commentSetting) {
		String tempKey = this.getSectionKey(key);

		this.thunderFile.remove(tempKey, commentSetting);
	}

	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.thunderFile);
	}
}