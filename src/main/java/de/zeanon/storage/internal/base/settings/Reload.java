package de.zeanon.storage.internal.base.settings;

import de.zeanon.storage.internal.base.files.FlatFile;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.utils.basic.Objects;
import de.zeanon.utils.exceptions.ObjectNullException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Enum defining the reload behaviour of the Data classes
 *
 * @author Zeanon
 * @version 2.1.0
 */
@SuppressWarnings({"unused", "EmptyMethod", "SameReturnValue"})
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