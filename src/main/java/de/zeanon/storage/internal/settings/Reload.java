package de.zeanon.storage.internal.settings;

import de.zeanon.storage.internal.base.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * an Enum defining the reload behaviour of the Data classes
 */
@SuppressWarnings("unused")
public enum Reload implements ReloadSettingBase {

	/**
	 * reloads every time you try to get something from the config
	 */
	AUTOMATICALLY {
		@Override
		public boolean shouldReload(final @Nullable FlatFile flatFile) {
			return true;
		}
	},
	/**
	 * reloads only if the File has changed.
	 */
	INTELLIGENT {
		@Override
		public boolean shouldReload(final @NotNull FlatFile flatFile) {
			return Objects.notNull(flatFile).hasChanged();
		}
	},
	/**
	 * only reloads if you manually call the reload.
	 */
	MANUALLY {
		@Override
		public boolean shouldReload(final @Nullable FlatFile flatFile) {
			return false;
		}
	};

	@Override
	public abstract boolean shouldReload(final @NotNull FlatFile flatFile);
}