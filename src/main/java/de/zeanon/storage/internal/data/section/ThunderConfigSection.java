package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.data.config.ThunderConfig;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderConfigSection extends ThunderFileSection implements ConfigBase {

	@NotNull
	private final ThunderConfig thunderConfig;


	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
		super(sectionKey, thunderConfig);
		this.thunderConfig = thunderConfig;
	}

	protected ThunderConfigSection(final @NotNull String[] sectionKey, final @NotNull ThunderConfig thunderConfig) {
		super(sectionKey, thunderConfig);
		this.thunderConfig = thunderConfig;
	}

	@NotNull
	@Override
	public List<String> getHeader() {
		return this.thunderConfig.getHeader(this.getSectionKey());
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getSectionKey(), header);
	}

	@Nullable
	public List<String> getHeader(final @NotNull String blockKey) {
		return this.thunderConfig.getHeader(this.getFinalKey(blockKey));
	}

	@NotNull
	public ThunderConfigSection setHeader(final @NotNull String blockKey, final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getFinalKey(blockKey), header);
		return this;
	}


	@NotNull
	@Override
	public List<String> getFooter() {
		return this.thunderConfig.getFooter(this.getSectionKey());
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getSectionKey(), footer);
	}

	@Nullable
	public List<String> getFooter(final @NotNull String blockKey) {
		return this.thunderConfig.getFooter(this.getFinalKey(blockKey));
	}

	@NotNull
	public ThunderConfigSection setFooter(final @NotNull String blockKey, final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getFinalKey(blockKey), footer);
		return this;
	}


	@NotNull
	@Override
	public List<String> getComments() {
		return this.thunderConfig.getComments(this.getSectionKey());
	}

	@Nullable
	public List<String> getComments(final @NotNull String blockKey) {
		return this.thunderConfig.getComments(this.getFinalKey(blockKey));
	}

	@Nullable
	public List<String> getBlockComments() {
		return this.thunderConfig.getBlockComments(this.getSectionKey());
	}

	@Nullable
	public List<String> getBlockComments(final @NotNull String blockKey) {
		return this.thunderConfig.getBlockComments(this.getFinalKey(blockKey));
	}

	@NotNull
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.getFinalKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.thunderConfig);
	}

	@NotNull
	@Override
	public FlatSection getSectionUseArray(final @NotNull String[] sectionKey) {
		return new ThunderConfigSection(this.getFinalArrayKey(Objects.notNull(sectionKey, "SectionKey  must not be null")), this.thunderConfig);
	}
}