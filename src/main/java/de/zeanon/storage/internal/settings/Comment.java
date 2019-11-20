package de.zeanon.storage.internal.settings;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;


/**
 * Enum defining how Comments should be handled in languages supporting it
 *
 * @author Zeanon
 * @version 1.30
 */
public enum Comment implements CommentSettingBase {

	SKIP,
	PRESERVE
}