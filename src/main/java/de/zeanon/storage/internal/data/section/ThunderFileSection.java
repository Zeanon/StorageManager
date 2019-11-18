package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.CommentEnabledSection;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.utils.basic.Objects;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFileSection extends CommentEnabledSection<Pair<Integer, String>> {

	@NotNull
	private final ThunderFile thunderFile;


	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile thunderFile) {
		super(sectionKey, thunderFile);
		this.thunderFile = thunderFile;
	}

	protected ThunderFileSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile thunderFile) {
		super(sectionKey, thunderFile);
		this.thunderFile = thunderFile;
	}

	@NotNull
	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(this.getFinalKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.thunderFile);
	}

	@NotNull
	@Override
	public ThunderFileSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new ThunderFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.thunderFile);
	}
}