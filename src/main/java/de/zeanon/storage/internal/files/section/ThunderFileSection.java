package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.sections.CommentEnabledSection;
import de.zeanon.storage.internal.files.raw.ThunderFile;
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
@SuppressWarnings("unused")
public class ThunderFileSection extends CommentEnabledSection<ThunderFile, TripletMap, List> {


	@NotNull
	private final ThunderFile baseFile;


	protected ThunderFileSection(final @NotNull String sectionKey, final @NotNull ThunderFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected ThunderFileSection(final @NotNull String[] sectionKey, final @NotNull ThunderFile baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	public void setBigMap(final boolean bigMap) {
		this.baseFile.bigMap(bigMap);
	}


	@NotNull
	@Override
	public ThunderFileSection getSection(final @NotNull String sectionKey) {
		return new ThunderFileSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@NotNull
	@Override
	public ThunderFileSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderFileSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}