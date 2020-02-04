package de.zeanon.storagemanager.internal.files.section;

import de.zeanon.storagemanager.internal.base.interfaces.DataMap;
import de.zeanon.storagemanager.internal.base.sections.CommentEnabledSection;
import de.zeanon.storagemanager.internal.files.raw.ThunderFile;
import java.util.List;
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
@SuppressWarnings({"unused", "rawtypes"})
public class ThunderFileSection extends CommentEnabledSection<ThunderFile, DataMap, List> {


	private final @NotNull
	ThunderFile baseFile;


	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected ThunderFileSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public @NotNull
	ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull
	ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}