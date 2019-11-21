package de.zeanon.storage.internal.utility.setting;

import de.zeanon.storage.internal.basic.interfaces.CommentSetting;


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