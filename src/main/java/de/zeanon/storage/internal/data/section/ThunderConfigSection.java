package de.zeanon.storage.internal.data.section;

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
	public void setHeader(final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getSectionKey(), header);
	}

	public List<String> getHeader(final @NotNull String key) {
		return this.thunderConfig.getHeader(this.getFinalKey(key));
	}

	public ThunderConfigSection setHeader(final @NotNull String key, final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getFinalKey(key), header);
		return this;
	}


	@Override
	public List<String> getFooter() {
		return this.thunderConfig.getFooter(this.getSectionKey());
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getSectionKey(), footer);
	}

	public List<String> getFooter(final @NotNull String key) {
		return this.thunderConfig.getFooter(this.getFinalKey(key));
	}

	public ThunderConfigSection setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getFinalKey(key), footer);
		return this;
	}


	@Override
	public List<String> getComments() {
		return this.thunderConfig.getComments(this.getSectionKey());
	}

	public List<String> getComments(final @NotNull String key) {
		return this.thunderConfig.getComments(this.getFinalKey(key));
	}

	public List<String> getBlockComments() {
		return this.thunderConfig.getBlockComments(this.getSectionKey());
	}

	public List<String> getBlockComments(final @NotNull String key) {
		return this.thunderConfig.getBlockComments(this.getFinalKey(key));
	}

	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.getSectionKey() + "." + Objects.notNull(sectionKey, "Key  must not be null"), this.thunderConfig);
	}
}