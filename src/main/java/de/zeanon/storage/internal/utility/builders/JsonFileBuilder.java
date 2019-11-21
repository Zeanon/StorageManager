package de.zeanon.storage.internal.utility.builders;

import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.basic.interfaces.ReloadSetting;
import de.zeanon.storage.internal.data.files.raw.JsonFile;
import de.zeanon.storage.internal.utility.setting.Reload;
import java.io.File;
import java.io.InputStream;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Setter
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public final class JsonFileBuilder extends StorageManager<JsonFileBuilder, JsonFile> {

	@NotNull
	private ReloadSetting reloadSetting = Reload.INTELLIGENT;


	public JsonFileBuilder(final @NotNull File file) {
		super(file);
	}


	@NotNull
	@Override
	@Contract("-> new")
	public final JsonFile create() {
		return new LocalJsonFile(super.file, this.inputStream, this.reloadSetting);
	}


	private static final class LocalJsonFile extends JsonFile {

		private LocalJsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting
							 ) {
			super(file, inputStream, reloadSetting);
		}
	}
}
