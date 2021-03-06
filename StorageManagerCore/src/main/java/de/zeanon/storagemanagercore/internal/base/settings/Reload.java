package de.zeanon.storagemanagercore.internal.base.settings;

import de.zeanon.storagemanagercore.internal.base.files.FlatFile;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReloadSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Enum defining the reload behaviour of the Data classes
 *
 * @author Zeanon
 * @version 2.1.0
 */
@SuppressWarnings({"unused", "rawtypes"})
public enum Reload implements ReloadSetting {


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
	 * reloads only if the File has changed
	 */
	INTELLIGENT {
		@Override
		public boolean shouldReload(final @NotNull FlatFile flatFile) {
			return flatFile.hasChanged();
		}
	},

	/**
	 * only reloads if you manually call the reload
	 */
	MANUALLY {
		@Override
		public boolean shouldReload(final @Nullable FlatFile flatFile) {
			return false;
		}
	}
}