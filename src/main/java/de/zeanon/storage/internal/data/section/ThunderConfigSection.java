package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.CommentEnabledSection;
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
public class ThunderConfigSection extends CommentEnabledSection<ThunderConfigSection, ThunderConfig> implements ConfigBase<ThunderConfigSection> {

	private final ThunderConfig thunderConfig;

	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
		super(sectionKey, thunderConfig);
		this.thunderConfig = thunderConfig;
	}

	@Override
	public List<String> getHeader() {
		return this.thunderConfig.getHeader(this.getSectionKey());
	}

	@Override
	public ThunderConfigSection setHeader(final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getSectionKey(), header);
		return this;
	}

	public List<String> getHeader(final @NotNull String key) {
		return this.thunderConfig.getHeader(this.sectionKey(key));
	}

	public ThunderConfigSection setHeader(final @NotNull String key, final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.sectionKey(key), header);
		return this;
	}


	@Override
	public List<String> getFooter() {
		return this.thunderConfig.getFooter(this.getSectionKey());
	}

	@Override
	public ThunderConfigSection setFooter(final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getSectionKey(), footer);
		return this;
	}

	public List<String> getFooter(final @NotNull String key) {
		return this.thunderConfig.getFooter(this.sectionKey(key));
	}

	public ThunderConfigSection setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.sectionKey(key), footer);
		return this;
	}


	@Override
	public List<String> getComments() {
		return this.thunderConfig.getComments(this.getSectionKey());
	}

	public List<String> getComments(final @NotNull String key) {
		return this.thunderConfig.getComments(this.sectionKey(key));
	}

	public List<String> getBlockComments() {
		return this.thunderConfig.getBlockComments(this.getSectionKey());
	}

	public List<String> getBlockComments(final @NotNull String key) {
		return this.thunderConfig.getBlockComments(this.sectionKey(key));
	}

	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.getSectionKey() + "." + Objects.notNull(sectionKey, "Key must not be null"), this.thunderConfig);
	}
}