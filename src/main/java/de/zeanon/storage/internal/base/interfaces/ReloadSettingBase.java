package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.base.FlatFile;
import org.jetbrains.annotations.NotNull;


/**
 * Base interface for ReloadSettings, provides a method to check whether to reload with the given setting
 *
 * @author Zeanon
 * @version 1.1.0
 */
public interface ReloadSettingBase {

	boolean shouldReload(final @NotNull FlatFile flatFile);
}