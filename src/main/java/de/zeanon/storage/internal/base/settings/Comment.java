package de.zeanon.storage.internal.base.settings;

import de.zeanon.storage.internal.base.interfaces.CommentSetting;


/**
 * Enum defining how Comments should be handled in languages supporting it
 *
 * @author Zeanon
 * @version 1.30
 */
public enum Comment implements CommentSetting {

	SKIP,
	PRESERVE
}