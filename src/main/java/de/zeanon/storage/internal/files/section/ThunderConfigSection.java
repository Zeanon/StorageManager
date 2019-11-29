package de.zeanon.storage.internal.files.section;

import de.zeanon.storage.internal.base.cache.base.TripletMap;
import de.zeanon.storage.internal.base.interfaces.Config;
import de.zeanon.storage.internal.files.config.ThunderConfig;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Section for {@link ThunderConfig}
 *
 * @author Zeanon
 * @version 1.1.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderConfigSection extends ThunderFileSection implements Config<TripletMap, List> {


	@NotNull
	private final ThunderConfig baseFile;


	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected ThunderConfigSection(final @NotNull String[] sectionKey, final @NotNull ThunderConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	@Override
	public @Nullable List<String> getHeader() {
		return this.baseFile.getHeader(this.getSectionKey());
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.baseFile.setHeader(this.getSectionKey(), header);
	}

	public @Nullable List<String> getHeader(final @NotNull String blockKey) {
		return this.baseFile.getHeader(this.getFinalKey(blockKey));
	}

	public void setHeader(final @NotNull String blockKey, final @Nullable List<String> header) {
		this.baseFile.setHeader(this.getFinalKey(blockKey), header);
	}


	@NotNull
	@Override
	public List<String> getFooter() {
		return this.baseFile.getFooter(this.getSectionKey());
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.baseFile.setFooter(this.getSectionKey(), footer);
	}

	@NotNull
	public List<String> getFooter(final @NotNull String blockKey) {
		return this.baseFile.getFooter(this.getFinalKey(blockKey));
	}

	public void setFooter(final @NotNull String blockKey, final @Nullable List<String> footer) {
		this.baseFile.setFooter(this.getFinalKey(blockKey), footer);
	}


	@NotNull
	@Override
	public List<String> getComments() {
		return this.baseFile.getComments(this.getSectionKey());
	}

	@NotNull
	public List<String> getComments(final @NotNull String blockKey) {
		return this.baseFile.getComments(this.getFinalKey(blockKey));
	}

	@NotNull
	public List<String> getBlockComments() {
		return this.baseFile.getBlockComments(this.getSectionKey());
	}

	@NotNull
	public List<String> getBlockComments(final @NotNull String blockKey) {
		return this.baseFile.getBlockComments(this.getFinalKey(blockKey));
	}

	@NotNull
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@NotNull
	@Override
	public ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderConfigSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}