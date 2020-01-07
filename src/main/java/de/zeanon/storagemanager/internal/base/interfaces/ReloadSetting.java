package de.zeanon.storagemanager.internal.base.interfaces;

import de.zeanon.storagemanager.internal.base.files.FlatFile;
import org.jetbrains.annotations.NotNull;


/**
 * Base interface for ReloadSettings, provides a method to check whether to reload with the given setting
 *
 * @author Zeanon
 * @version 1.1.0
 */
@SuppressWarnings("EmptyMethod")
public interface ReloadSetting {

	/**
	 * Check whether the given FlatFile should be reloaded
	 *
	 * @param flatFile the FlatFile to check
	 */
	boolean shouldReload(final @NotNull FlatFile flatFile);
}