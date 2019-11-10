package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.data.config.ThunderConfig;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public class ThunderConfigSection extends ThunderFileSection {

	private final ThunderConfig thunderConfig;

	protected ThunderConfigSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
		super(sectionKey, thunderConfig);
		this.thunderConfig = thunderConfig;
	}

	public List<String> getHeader() {
		return this.thunderConfig.getHeader(this.sectionKey);
	}

	public void setHeader(final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.sectionKey, header);
	}

	public List<String> getFooter() {
		return this.thunderConfig.getFooter(this.sectionKey);
	}

	public void setFooter(final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.sectionKey, footer);
	}

	public List<String> getHeader(final @NotNull String key) {
		return this.thunderConfig.getHeader(this.getSectionKey(key));
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		this.thunderConfig.setHeader(this.getSectionKey(key), header);
	}

	public List<String> getFooter(final @NotNull String key) {
		return this.thunderConfig.getFooter(this.getSectionKey(key));
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		this.thunderConfig.setFooter(this.getSectionKey(key), footer);
	}

	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new ThunderConfigSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.thunderConfig);
	}
}