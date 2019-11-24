package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.sections.CommentEnabledSection;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


/**
 * Section for {@link ThunderFile}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderFileSection extends CommentEnabledSection<ThunderFile> {

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
	public ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderFileSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.thunderFile);
	}
}