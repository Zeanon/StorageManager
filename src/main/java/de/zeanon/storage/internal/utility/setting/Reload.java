package de.zeanon.storage.internal.utility.setting;

import de.zeanon.storage.internal.basic.exceptions.ObjectNullException;
import de.zeanon.storage.internal.basic.interfaces.ReloadSetting;
import de.zeanon.storage.internal.data.base.FlatFile;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Enum defining the reload behaviour of the Data classes
 *
 * @author Zeanon
 * @version 2.1.0
 */
@SuppressWarnings("unused")
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
		/**
		 * @throws ObjectNullException if the passed FlatFile is null
		 */
		@Override
		public boolean shouldReload(final @NotNull FlatFile flatFile) {
			return Objects.notNull(flatFile, "FlatFile must not be null").hasChanged();
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
	};

	/**
	 * @return if the File should be reloaded
	 */
	@Override
	public abstract boolean shouldReload(final @NotNull FlatFile flatFile);
}