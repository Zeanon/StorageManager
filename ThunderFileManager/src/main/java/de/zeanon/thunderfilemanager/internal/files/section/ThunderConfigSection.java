package de.zeanon.thunderfilemanager.internal.files.section;

import de.zeanon.storagemanagercore.internal.base.interfaces.Config;
import de.zeanon.thunderfilemanager.internal.files.config.ThunderConfig;
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
public class ThunderConfigSection extends ThunderFileSection implements Config {


	private final @NotNull ThunderConfig baseFile;


	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected ThunderConfigSection(final @NotNull String[] sectionKey, final @NotNull ThunderConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public @Nullable
	List<String> getHeader() {
		return this.baseFile.getHeader(this.getSectionKey());
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		this.baseFile.setHeader(this.getSectionKey(), header);
	}

	public @Nullable
	List<String> getHeader(final @NotNull String blockKey) {
		return this.baseFile.getHeader(this.getFinalKey(blockKey));
	}

	public void setHeader(final @NotNull String blockKey, final @Nullable List<String> header) {
		this.baseFile.setHeader(this.getFinalKey(blockKey), header);
	}


	@Override
	public @NotNull List<String> getFooter() {
		return this.baseFile.getFooter(this.getSectionKey());
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		this.baseFile.setFooter(this.getSectionKey(), footer);
	}

	public @NotNull List<String> getFooter(final @NotNull String blockKey) {
		return this.baseFile.getFooter(this.getFinalKey(blockKey));
	}

	public void setFooter(final @NotNull String blockKey, final @Nullable List<String> footer) {
		this.baseFile.setFooter(this.getFinalKey(blockKey), footer);
	}


	@Override
	public @NotNull List<String> getComments() {
		return this.baseFile.getComments(this.getSectionKey());
	}

	public @NotNull List<String> getComments(final @NotNull String blockKey) {
		return this.baseFile.getComments(this.getFinalKey(blockKey));
	}

	public @NotNull List<String> getBlockComments() {
		return this.baseFile.getBlockComments(this.getSectionKey());
	}

	public @NotNull List<String> getBlockComments(final @NotNull String blockKey) {
		return this.baseFile.getBlockComments(this.getFinalKey(blockKey));
	}


	@Override
	public @NotNull ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new ThunderConfigSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}