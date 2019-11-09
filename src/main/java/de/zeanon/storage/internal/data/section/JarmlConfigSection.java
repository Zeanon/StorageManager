package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.data.config.JarmlConfig;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class JarmlConfigSection extends JarmlFileSection {

	private final JarmlConfig jarmlConfig;

	protected JarmlConfigSection(final @NotNull String sectionKey, final @NotNull JarmlConfig jarmlConfig) {
		super(sectionKey, jarmlConfig);
		this.jarmlConfig = jarmlConfig;
	}

	public List<String> getHeader() {
		return this.jarmlConfig.getHeader(this.sectionKey);
	}

	public void setHeader(final @Nullable List<String> header) {
		this.jarmlConfig.setHeader(this.sectionKey, header);
	}

	public List<String> getFooter() {
		return this.jarmlConfig.getFooter(this.sectionKey);
	}

	public void setFooter(final @Nullable List<String> footer) {
		this.jarmlConfig.setFooter(this.sectionKey, footer);
	}

	public List<String> getHeader(final @NotNull String key) {
		return this.jarmlConfig.getHeader(this.getSectionKey(key));
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		this.jarmlConfig.setHeader(this.getSectionKey(key), header);
	}

	public List<String> getFooter(final @NotNull String key) {
		return this.jarmlConfig.getFooter(this.getSectionKey(key));
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		this.jarmlConfig.setFooter(this.getSectionKey(key), footer);
	}

	@Override
	public JarmlConfigSection getSection(final @NotNull String sectionKey) {
		return new JarmlConfigSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.jarmlConfig);
	}

	protected JarmlConfigSection getJarmlConfigSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JarmlConfigSection jarmlConfigSection = (JarmlConfigSection) obj;
			return this.jarmlConfig.equals(jarmlConfigSection.jarmlConfig)
				   && this.sectionKey.equals(jarmlConfigSection.sectionKey);
		}
	}
}