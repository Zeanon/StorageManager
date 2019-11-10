package de.zeanon.storage.internal.data.section;

import de.zeanon.storage.internal.base.FlatSection;
import de.zeanon.storage.internal.data.raw.JarmlFile;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public class JarmlFileSection extends FlatSection {

	private final JarmlFile jarmlFile;

	protected JarmlFileSection(final @NotNull String sectionKey, final @NotNull JarmlFile jarmlFile) {
		super(sectionKey, jarmlFile);
		this.jarmlFile = jarmlFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull Comment commentSetting) {
		this.jarmlFile.set(this.getSectionKey(key), value, commentSetting);
	}

	public synchronized void remove(final @NotNull String key, final @NotNull Comment commentSetting) {
		String tempKey = this.getSectionKey(key);

		this.jarmlFile.remove(tempKey, commentSetting);
	}

	@Override
	public JarmlFileSection getSection(final @NotNull String sectionKey) {
		return new JarmlFileSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.jarmlFile);
	}
}