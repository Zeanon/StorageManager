package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.CommentEnabledSection;
import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFileSection extends CommentEnabledSection {

	private final ThunderFile thunderFile;

	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
		super(sectionKey, thunderFile);
		this.thunderFile = thunderFile;
	}

	protected ThunderFileSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile thunderFile) {
		super(sectionKey, thunderFile);
		this.thunderFile = thunderFile;
	}

	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(this.getFinalKey(Objects.notNull(sectionKey, "Key  must not be null")), this.thunderFile);
	}

	@Override
	public FlatSection getSectionFromArray(final @NotNull String[] sectionKey) {
		return new ThunderFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "Key  must not be null")), this.thunderFile);
	}
}