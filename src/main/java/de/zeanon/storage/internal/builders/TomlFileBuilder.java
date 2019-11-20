package de.zeanon.storage.internal.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.TomlFile;
import java.io.File;
import java.io.InputStream;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public final class TomlFileBuilder extends StorageManager<TomlFileBuilder, TomlFile> {

	private ReloadSettingBase reloadSetting;


	public TomlFileBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	public final TomlFile create() {
		return new LocalTomlFile(super.file, this.inputStream, this.reloadSetting);
	}


	private static final class LocalTomlFile extends TomlFile {

		private LocalTomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting) {
			super(file, inputStream, reloadSetting);
		}
	}
}
